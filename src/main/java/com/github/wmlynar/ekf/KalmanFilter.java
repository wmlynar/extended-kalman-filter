package com.github.wmlynar.ekf;

/* Refer to http://en.wikipedia.org/wiki/Kalman_filter for
 mathematical details. The naming scheme is that variables get names
 that make sense, and are commented with their analog in
 the Wikipedia mathematical notation.
 This Kalman filter implementation does not support controlled
 input.
 (Like knowing which way the steering wheel in a car is turned and
 using that to inform the model.)
 Vectors are handled as n-by-1 matrices.
 TODO: comment on the dimension of the matrices */

public class KalmanFilter {

	public ProcessModel model;

	public KalmanFilter(ProcessModel model) {
		this.model = model;
		
		model.initialState(model.state_estimate);
		model.initialStateCovariance(model.estimate_covariance);
	}

	/*
	 * Runs one timestep of prediction + estimation.
	 *
	 * Before each time step of running this, set f.observation to be the next
	 * time step's observation.
	 *
	 * Before the first step, define the model by setting: f.state_transition
	 * f.observation_model f.process_noise_covariance
	 * f.observation_noise_covariance
	 *
	 * It is also advisable to initialize with reasonable guesses for
	 * f.state_estimate f.estimate_covariance
	 */
	public void update(double dt, ObservationModel obs) {
		predict(dt);
		estimate(dt, obs);
	}

	/* Just the prediction phase of update. */
	public void predict(double dt) {
		/* Predict the state */
		model.predictionModel(model.state_estimate, dt, model.predicted_state);
		
		/* Predict the state estimate covariance */
		model.predictionModelJacobian(model.state_estimate, dt, model.state_transition);
		model.processNoiseCovariance(dt, model.process_noise_covariance);
		Matrix.multiply_matrix(model.state_transition, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.big_square_scratch, model.state_transition, model.predicted_estimate_covariance);
		Matrix.add_matrix(model.predicted_estimate_covariance, model.process_noise_covariance, model.predicted_estimate_covariance);
	}

	/* Just the estimation phase of update. */
	void estimate(double dt, ObservationModel obs) {
		/* Calculate innovation */
		obs.observationMeasurement(obs.observation);
		obs.observationModel(model.predicted_state, obs.innovation);
		Matrix.subtract_matrix(obs.observation, obs.innovation, obs.innovation);

		/* Calculate innovation covariance */
		obs.observationModelJacobian(obs.observation_model);
		obs.observationNoiseCovariance(obs.observation_noise_covariance);
		Matrix.multiply_by_transpose_matrix(model.predicted_estimate_covariance, obs.observation_model, obs.vertical_scratch);
		Matrix.multiply_matrix(obs.observation_model, obs.vertical_scratch, obs.innovation_covariance);
		Matrix.add_matrix(obs.innovation_covariance, obs.observation_noise_covariance, obs.innovation_covariance);

		/*
		 * Invert the innovation covariance. Note: this destroys the innovation
		 * covariance. TODO: handle inversion failure intelligently.
		 */
		Matrix.destructive_invert_matrix(obs.innovation_covariance, obs.inverse_innovation_covariance);

		/*
		 * Calculate the optimal Kalman gain. Note we still have a useful
		 * partial product in vertical scratch from the innovation covariance.
		 */
		Matrix.multiply_matrix(obs.vertical_scratch, obs.inverse_innovation_covariance, obs.optimal_gain);

		/* Estimate the state */
		Matrix.multiply_matrix(obs.optimal_gain, obs.innovation, model.state_estimate);
		Matrix.add_matrix(model.state_estimate, model.predicted_state, model.state_estimate);

		/* Estimate the state covariance */
		Matrix.multiply_matrix(obs.optimal_gain, obs.observation_model, model.big_square_scratch);
		Matrix.subtract_from_identity_matrix(model.big_square_scratch);
		Matrix.multiply_matrix(model.big_square_scratch, model.predicted_estimate_covariance, model.estimate_covariance);
	}
}
