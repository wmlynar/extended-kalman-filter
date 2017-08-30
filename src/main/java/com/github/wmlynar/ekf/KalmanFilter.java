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
			if (dt > maximalTimeStep) {
				dt = maximalTimeStep;
			}
			predict(dt);
			time += dt;
		} while (time < t);
		estimate(obs);
	}

	/* Just the prediction phase of update. */
	public void predict(double dt) {
		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.state_estimate.data, model.state_jacobian.data);
		Matrix.add_scaled_matrix(model.identity_scratch, dt, model.state_jacobian, model.state_transition);
		Matrix.multiply_matrix(model.state_transition, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.big_square_scratch, model.state_transition,
				model.estimate_covariance);
		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.add_scaled_matrix(model.estimate_covariance, dt, model.process_noise_covariance,
				model.estimate_covariance);

		/* Predict the state */
		model.stateFunction(model.state_estimate.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.state_function, model.state_estimate);
	}

	// unfortunately there is no observable improvement
	public void predict_rk2(double dt) {
		// runge-kutta 2 (explicit midpoint method)
		model.stateFunction(model.state_estimate.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt / 2, model.state_function, model.predicted_state_midpoint);

		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.predicted_state_midpoint.data, model.state_jacobian.data);
		Matrix.add_scaled_matrix(model.identity_scratch, dt, model.state_jacobian, model.state_transition);
		Matrix.multiply_matrix(model.state_transition, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.big_square_scratch, model.state_transition,
				model.estimate_covariance);
		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.add_scaled_matrix(model.estimate_covariance, dt, model.process_noise_covariance,
				model.estimate_covariance);

		/* Predict the state */
		model.stateFunction(model.predicted_state_midpoint.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.state_function, model.state_estimate);
	}

	// this requires a lot of iterations (step size 0.001)
	public void predict_continuous(double dt) {
		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.state_estimate.data, model.state_jacobian.data);
		Matrix.multiply_matrix(model.state_jacobian, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.estimate_covariance, model.state_jacobian, model.big_square_scratch2);
		Matrix.add_matrix(model.big_square_scratch, model.big_square_scratch2, model.big_square_scratch);

		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.add_matrix(model.big_square_scratch, model.process_noise_covariance, model.big_square_scratch);

		Matrix.add_scaled_matrix(model.estimate_covariance, dt, model.big_square_scratch, model.estimate_covariance);

		/* Predict the state */
		model.stateFunction(model.state_estimate.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.state_function, model.state_estimate);
	}

	// this requires a lot of iterations (step size 0.001)
	public void predict_continuous_rk2(double dt) {
		// runge-kutta 2 (explicit midpoint method)
		model.stateFunction(model.state_estimate.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt / 2, model.state_function, model.predicted_state_midpoint);

		/* Predict the state estimate covariance */
		model.stateFunctionJacobian(model.predicted_state_midpoint.data, model.state_jacobian.data);
		Matrix.multiply_matrix(model.state_jacobian, model.estimate_covariance, model.big_square_scratch);
		Matrix.multiply_by_transpose_matrix(model.estimate_covariance, model.state_jacobian, model.big_square_scratch2);
		Matrix.add_matrix(model.big_square_scratch, model.big_square_scratch2, model.big_square_scratch);

		model.processNoiseCovariance(model.process_noise_covariance.data);
		Matrix.add_matrix(model.big_square_scratch, model.process_noise_covariance, model.big_square_scratch);

		Matrix.add_scaled_matrix(model.estimate_covariance, dt, model.big_square_scratch, model.estimate_covariance);

		/* Predict the state */
		model.stateFunction(model.predicted_state_midpoint.data, model.state_function.data);
		Matrix.add_scaled_matrix(model.state_estimate, dt, model.state_function, model.state_estimate);
	}

	/* Just the estimation phase of update. */
	void estimate(ObservationModel obs) {
		/* Calculate innovation */
		obs.observationMeasurement(obs.observation.data);
		obs.observationModel(model.state_estimate.data, obs.innovation.data);
		Matrix.subtract_matrix(obs.observation, obs.innovation, obs.innovation);

		/* Calculate innovation covariance */
		obs.observationModelJacobian(obs.observation_model.data);
		obs.observationNoiseCovariance(obs.observation_noise_covariance.data);
		Matrix.multiply_by_transpose_matrix(model.estimate_covariance, obs.observation_model, obs.vertical_scratch);
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
		Matrix.multiply_matrix(obs.optimal_gain, obs.innovation, model.state_delta_scratch);
		Matrix.add_matrix(model.state_estimate, model.state_delta_scratch, model.state_estimate);

		/* Estimate the state covariance */
		Matrix.multiply_matrix(obs.optimal_gain, obs.observation_model, model.big_square_scratch);
		Matrix.subtract_from_identity_matrix(model.big_square_scratch);
		Matrix.multiply_matrix(model.big_square_scratch, model.estimate_covariance, model.big_square_scratch2);
		Matrix.copy_matrix(model.big_square_scratch2, model.estimate_covariance);
	}
}
