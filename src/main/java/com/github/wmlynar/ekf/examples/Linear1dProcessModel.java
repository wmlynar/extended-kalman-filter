package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ProcessModel;

public class Linear1dProcessModel extends ProcessModel {

	@Override
	public int stateDimension() {
		return 2;
	}

	@Override
	public void initialState(double[][] x) {
		x[0][0] = 0;
		x[1][0] = 0;
	}

	@Override
	public void initialStateCovariance(double[][] cov) {
		cov[0][0] = 1000;
		cov[1][1] = 1000;
	}

	@Override
	public void stateFunction(double[][] x, double[][] f) {
		f[0][0] = x[1][0];
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
		j[0][1] = 1;
	}

	@Override
	public void processNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
	}
}
