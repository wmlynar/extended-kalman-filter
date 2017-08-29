package com.github.wmlynar.dekf.examples;

import com.github.wmlynar.dekf.DProcessModel;
import com.github.wmlynar.ekf.Matrix;

public class Linear2dDProcessModel extends DProcessModel {

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
	public void stateFunction(Matrix state, Matrix function) {
		function.data[0][0] = state.data[1][0];
		function.data[1][0] = 0;
		function.data[2][0] = state.data[3][0];
		function.data[3][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(Matrix state, Matrix function_jacobian) {
		function_jacobian.data[0][1] = 1;
		function_jacobian.data[2][3] = 1;
	}

	@Override
	public void processNoiseCovariance(Matrix process_noise_covariance) {
		estimate_covariance.set_identity_matrix();
	}
}
