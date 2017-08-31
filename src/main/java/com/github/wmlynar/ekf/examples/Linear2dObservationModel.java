package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ObservationModel;

public class Linear2dObservationModel extends ObservationModel {

	private double mx;
	private double my;

	public void setPosition(double x, double y) {
		this.mx = x;
		this.my = y;
	}

	@Override
	public int observationDimension() {
		return 2;
	}

	@Override
	public int stateDimension() {
		return 4;
	}

	@Override
	public void observationMeasurement(double[][] y) {
		y[0][0] = mx;
		y[1][0] = my;
	}

	@Override
	public void observationModel(double[][] x, double[][] h) {
		h[0][0] = x[0][0];
		h[1][0] = x[2][0];
	}

	@Override
	public void observationModelJacobian(double[][] x, double[][] j) {
		j[0][0] = 1;
		j[1][2] = 1;
	}

	@Override
	public void observationNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
	}

}
