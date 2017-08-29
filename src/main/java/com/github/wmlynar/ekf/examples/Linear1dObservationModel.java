package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ObservationModel;

public class Linear1dObservationModel extends ObservationModel {

	private double mx;
	
	public void setPosition(double x) {
		this.mx = x;
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
	public void observationMeasurement(double[][] y) {
		y[0][0] = mx;
	}

	@Override
	public void observationModel(double[][] x, double[][] h) {
		h[0][0] = x[0][0];
	}

	@Override
	public void observationModelJacobian(double[][] j) {
		j[0][0] = 1;
		j[0][1] = 0;
	}

	@Override
	public void observationNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
	}
}
