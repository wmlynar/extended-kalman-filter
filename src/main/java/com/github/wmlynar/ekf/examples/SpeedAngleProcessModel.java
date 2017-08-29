package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ProcessModel;

public class SpeedAngleProcessModel extends ProcessModel {

	@Override
	public int stateDimension() {
		return 4;
	}

	@Override
	public void initialState(double[][] x) {
		x[0][0] = 0;
		x[1][0] = 0;
		x[2][0] = 0;
		x[3][0] = 0;
	}

	@Override
	public void initialStateCovariance(double[][] cov) {
		cov[0][0] = 1000;
		cov[1][1] = 1000;
		cov[2][2] = 1000;
		cov[3][3] = 1000;
	}

	@Override
	public void stateFunction(double[][] x, double[][] f) {
		f[0][0] = x[2][0] * Math.sin(x[3][0]);
		f[1][0] = x[2][0] * Math.cos(x[3][0]);
		f[2][0] = 0;
		f[3][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
		j[0][2] = Math.sin(x[3][0]);
		j[0][3] = x[2][0] * Math.cos(x[3][0]);
		j[1][2] = Math.cos(x[3][0]);
		j[1][3] = -x[2][0] * Math.sin(x[3][0]);
	}

	@Override
	public void processNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
		cov[2][2] = 1;
		cov[3][3] = 1;
	}

}
