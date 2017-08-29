package com.github.wmlynar.dekf.examples;

import com.github.wmlynar.dekf.DProcessModel;
import com.github.wmlynar.ekf.Matrix;

public class Linear1dDProcessModel extends DProcessModel {

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
	public void predictionModel(Matrix state, Matrix state_delta) {
		state_delta.data[0][0] = state.data[1][0];
	}

	@Override
	public void predictionModelJacobian(Matrix state, Matrix state_delta_jacobian) {
		state_delta_jacobian.data[0][1] = 1;
	}

	@Override
	public void processNoiseCovariance(Matrix process_noise_covariance) {
		process_noise_covariance.data[0][0] = 1;
		process_noise_covariance.data[1][1] = 1;
	}
}
