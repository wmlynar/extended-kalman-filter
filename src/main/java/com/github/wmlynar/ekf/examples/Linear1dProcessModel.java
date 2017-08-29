package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ProcessModel;
import com.github.wmlynar.ekf.Matrix;

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
	public void stateFunction(Matrix state, Matrix function) {
		function.data[0][0] = state.data[1][0];
	}

	@Override
	public void stateFunctionJacobian(Matrix state, Matrix function_jacobian) {
		function_jacobian.data[0][1] = 1;
	}

	@Override
	public void processNoiseCovariance(Matrix process_noise_covariance) {
		process_noise_covariance.data[0][0] = 1;
		process_noise_covariance.data[1][1] = 1;
	}
}
