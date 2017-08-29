package com.github.wmlynar.ctekf.examples;

import com.github.wmlynar.ctekf.CtProcessModel;
import com.github.wmlynar.ekf.Matrix;

public class SpeedAngleCtProcessModel extends CtProcessModel {

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
	public void stateFunction(Matrix state, Matrix derivative) {
		derivative.data[0][0] = state.data[2][0] * Math.sin(state.data[3][0]);
		derivative.data[1][0] = state.data[2][0] * Math.cos(state.data[3][0]);
		derivative.data[2][0] = 0;
		derivative.data[3][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(Matrix state, Matrix derivative_jacobian) {
		derivative_jacobian.data[0][2] = Math.sin(state.data[3][0]);
		derivative_jacobian.data[0][3] = state.data[2][0] * Math.cos(state.data[3][0]);
		derivative_jacobian.data[1][2] = Math.cos(state.data[3][0]);
		derivative_jacobian.data[1][3] = -state.data[2][0] * Math.sin(state.data[3][0]);
	}

	@Override
	public void processNoiseCovariance(Matrix process_noise_covariance) {
		process_noise_covariance.set_identity_matrix();
	}

}
