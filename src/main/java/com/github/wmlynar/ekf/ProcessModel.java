package com.github.wmlynar.ekf;

public abstract class ProcessModel {

	/* This group of matrices must be specified by the user. */
	public Matrix state_function = new Matrix(stateDimension(), 1);
	public Matrix state_jacobian = new Matrix(stateDimension(), stateDimension());
	public Matrix process_noise_covariance = new Matrix(stateDimension(), stateDimension());

	/* This group of matrices are updated every time step by the filter. */
	public Matrix state_estimate = new Matrix(stateDimension(), 1);
	public Matrix estimate_covariance = new Matrix(stateDimension(), stateDimension());

	/* This group is used for meaningless intermediate calculations */
	public Matrix big_square_scratch = new Matrix(stateDimension(), stateDimension());
	public Matrix big_square_scratch2 = new Matrix(stateDimension(), stateDimension());
	public Matrix identity_scratch = new Matrix(stateDimension(), stateDimension());
	public Matrix state_transition = new Matrix(stateDimension(), stateDimension());
	public Matrix predicted_state_midpoint = new Matrix(stateDimension(), 1);
	public Matrix state_delta_scratch = new Matrix(stateDimension(), 1);

	public ProcessModel() {
		identity_scratch.set_identity_matrix();
	}

	public double[][] getState() {
		return state_estimate.data;
	}

	public abstract int stateDimension();

	public abstract void initialState(double[][] x);

	public abstract void initialStateCovariance(double[][] cov);

	public abstract void stateFunction(double[][] x, double[][] f);

	public abstract void stateFunctionJacobian(double[][] x, double[][] j);

	public abstract void processNoiseCovariance(double[][] cov);
}
