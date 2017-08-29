package com.github.wmlynar.ctekf.examples;

import com.github.wmlynar.ctekf.CtObservationModel;
import com.github.wmlynar.ekf.Matrix;

public class Linear2dCtObservationModel extends CtObservationModel {

	private double x;
	private double y;
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int stateDimension() {
		return 4;
	}

	@Override
	public int observationDimension() {
		return 2;
	}

	@Override
	public void observationMeasurement(Matrix observation_measured) {
		observation_measured.data[0][0] = x;
		observation_measured.data[1][0] = y;
	}

	@Override
	public void observationModel(Matrix state, Matrix observation_predicted) {
		observation_predicted.data[0][0] = state.data[0][0];
		observation_predicted.data[1][0] = state.data[2][0];
	}

	@Override
	public void observationModelJacobian(Matrix observation_jacobian) {
		observation_jacobian.data[0][0] = 1;
		observation_jacobian.data[1][2] = 1;
	}

	@Override
	public void observationNoiseCovariance(Matrix observation_noise_covariance) {
		observation_noise_covariance.set_identity_matrix();
	}

}