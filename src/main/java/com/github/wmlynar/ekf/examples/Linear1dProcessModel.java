package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.Matrix;
import com.github.wmlynar.ekf.ProcessModel;

public class Linear1dProcessModel extends ProcessModel {

	@Override
	public int stateDimension() {
		return 2;
	}

	@Override
	public void initialState(Matrix initial_state) {
		initial_state.data[0][0] = 0;
		initial_state.data[1][0] = 0;
	}

	@Override
	public void initialStateCovariance(Matrix initial_covariance) {
		initial_covariance.set_identity_matrix();
		initial_covariance.scale_matrix(1000);
	}

	@Override
	public void predictionModel(Matrix state, double dt, Matrix predicted_state) {
		predicted_state.data[0][0] = state.data[0][0] + state.data[1][0] * dt;
		predicted_state.data[1][0] = state.data[1][0];
	}

	@Override
	public void predictionModelJacobian(Matrix state, double dt, Matrix predicted_state_jacobian) {
		predicted_state_jacobian.data[0][0] = 1;
		predicted_state_jacobian.data[0][1] = dt;
		predicted_state_jacobian.data[1][0] = 0;
		predicted_state_jacobian.data[1][1] = 1;
	}

	@Override
	public void processNoiseCovariance(double dt, Matrix process_noise_covariance) {
		process_noise_covariance.data[0][0] = dt;
		process_noise_covariance.data[1][1] = dt;
	}
}
