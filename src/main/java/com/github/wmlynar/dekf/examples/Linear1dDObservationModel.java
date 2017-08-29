package com.github.wmlynar.dekf.examples;

import com.github.wmlynar.dekf.DObservationModel;
import com.github.wmlynar.ekf.Matrix;

public class Linear1dDObservationModel extends DObservationModel {

	private double x;
	
	public void setPosition(double x) {
		this.x = x;
	}
	
	@Override
	public int stateDimension() {
		return 2;
	}

	@Override
	public int observationDimension() {
		return 1;
	}

	@Override
	public void observationMeasurement(Matrix observation_measured) {
		observation_measured.data[0][0] = x;
	}

	@Override
	public void observationModel(Matrix state, Matrix observation_predicted) {
		observation_predicted.data[0][0] = state.data[0][0];
	}

	@Override
	public void observationModelJacobian(Matrix observation_jacobian) {
		observation_jacobian.data[0][0] = 1;
	}

	@Override
	public void observationNoiseCovariance(Matrix observation_noise_covariance) {
		observation_noise_covariance.set_identity_matrix();
	}

}
