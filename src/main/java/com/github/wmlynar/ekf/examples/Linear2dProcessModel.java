package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.ProcessModel;
import com.github.wmlynar.ekf.Matrix;

public class Linear2dProcessModel extends ProcessModel {

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
		f[0][0] = x[1][0];
		f[1][0] = 0;
		f[2][0] = x[3][0];
		f[3][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
		j[0][1] = 1;
		j[2][3] = 1;
	}

	@Override
	public void processNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[1][1] = 1;
		cov[2][2] = 1;
		cov[3][3] = 1;
	}
}
