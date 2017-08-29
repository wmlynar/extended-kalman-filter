package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ObservationModel;

public class SpeedAngleObservationModel extends ObservationModel {

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
	public void observationMeasurement(double[][] z) {
		z[0][0] = x;
		z[1][0] = y;
	}

	@Override
	public void observationModel(double[][] x, double[][] h) {
		h[0][0] = x[0][0];
		h[1][0] = x[1][0];
	}

	@Override
	public void observationModelJacobian(double[][] j) {
		j[0][0] = 1;
		j[1][1] = 1;
	}

	@Override
	public void observationNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
	}

}
