package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.Matrix;
import com.github.wmlynar.ekf.ProcessModel;

public class Linear2dProcessModel extends ProcessModel {

	@Override
	public int stateDimension() {
		return 4;
	}

	@Override
	public void initialState(Matrix initial_state) {
		initial_state.data[0][0] = 0;
		initial_state.data[1][0] = 0;
		initial_state.data[2][0] = 0;
		initial_state.data[3][0] = 0;
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
		predicted_state.data[2][0] = state.data[2][0] + state.data[3][0] * dt;
		predicted_state.data[3][0] = state.data[3][0];
	}

	@Override
	public void predictionModelJacobian(Matrix state, double dt, Matrix predicted_state_jacobian) {
		predicted_state_jacobian.set_identity_matrix();
		predicted_state_jacobian.data[0][1] = dt;
		predicted_state_jacobian.data[2][3] = dt;
	}

	@Override
	public void processNoiseCovariance(double dt, Matrix process_noise_covariance) {
		estimate_covariance.set_identity_matrix();
		estimate_covariance.scale_matrix(dt);
	}
}
