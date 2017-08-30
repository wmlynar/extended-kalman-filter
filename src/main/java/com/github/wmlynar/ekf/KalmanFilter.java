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

	public double time = 0;
	public double maximalTimeStep = Double.MAX_VALUE;
	
	public ProcessModel model;

	public KalmanFilter(ProcessModel model) {
		this.model = model;
		
		model.initialState(model.state_estimate.data);
		model.initialStateCovariance(model.estimate_covariance.data);
		
		model.identity_scratch.set_identity_matrix();
	}

	public void setMaximalTimeStep(double maximalTimeStep) {
		this.maximalTimeStep = maximalTimeStep;
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
	public void update(double t, ObservationModel obs) {
		do {
			double dt = t - time;
			if(dt>maximalTimeStep) {
				dt = maximalTimeStep;
			}
			predict(dt);
			time += dt;
		} while(time<t);
		estimate(obs);
	}

	/* Just the prediction phase of update. */
	public void predict(double dt) {
		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.state_estimate.data, model.delta_matrix_scratch.data);
		Matrix.add_scaled_matrix(model.identity_scratch, dt, model.delta_matrix_scratch, model.state_transition);
		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.multiply_matrix(model.state_transition, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.big_square_scratch, model.state_transition, model.predicted_estimate_covariance);
		Matrix.add_matrix(model.predicted_estimate_covariance, model.process_noise_covariance, model.predicted_estimate_covariance);
		Matrix.copy_matrix(model.predicted_estimate_covariance, model.estimate_covariance);
		
		/* Predict the state */
		model.stateFunction(model.state_estimate.data, model.delta_vector_scratch.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.delta_vector_scratch, model.predicted_state);
		Matrix.copy_matrix(model.predicted_state, model.state_estimate);
	}
	
	public void predict_rk2(double dt) {
		// runge-kutta 2 (explicit midpoint method)
		
		/* Predict the state */
		model.stateFunction(model.state_estimate.data, model.delta_vector_scratch.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt/2, model.delta_vector_scratch, model.predicted_state_midpoint);
		model.stateFunction(model.predicted_state_midpoint.data, model.delta_vector_scratch.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.delta_vector_scratch, model.predicted_state);

		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.predicted_state_midpoint.data, model.delta_matrix_scratch.data);
		Matrix.add_scaled_matrix(model.identity_scratch, dt, model.delta_matrix_scratch, model.state_transition);
		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.multiply_matrix(model.state_transition, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.big_square_scratch, model.state_transition, model.predicted_estimate_covariance);
		Matrix.add_matrix(model.predicted_estimate_covariance, model.process_noise_covariance, model.predicted_estimate_covariance);

		Matrix.copy_matrix(model.predicted_state, model.state_estimate);
		Matrix.copy_matrix(model.predicted_estimate_covariance, model.estimate_covariance);
	}

	/* Just the estimation phase of update. */
	void estimate(ObservationModel obs) {
		/* Calculate innovation */
		obs.observationMeasurement(obs.observation.data);
		obs.observationModel(model.predicted_state.data, obs.innovation.data);
		Matrix.subtract_matrix(obs.observation, obs.innovation, obs.innovation);

		/* Calculate innovation covariance */
		obs.observationModelJacobian(obs.observation_model.data);
		obs.observationNoiseCovariance(obs.observation_noise_covariance.data);
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
