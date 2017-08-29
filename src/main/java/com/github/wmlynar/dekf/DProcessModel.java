package com.github.wmlynar.dekf;

import com.github.wmlynar.ekf.Matrix;

public abstract class DProcessModel {
	
	/* This group of matrices must be specified by the user. */
	/* F_k */
	public Matrix state_transition = new Matrix(stateDimension(), stateDimension());
	/* Q_k */
	public Matrix process_noise_covariance = new Matrix(stateDimension(), stateDimension());
	
	/* This group of matrices are updated every time step by the filter. */
	/* x-hat_k|k-1 */
	public Matrix predicted_state = new Matrix(stateDimension(), 1);
	/* P_k|k-1 */
	public Matrix predicted_estimate_covariance = new Matrix(stateDimension(), stateDimension());
	/* x-hat_k|k */
	public Matrix state_estimate = new Matrix(stateDimension(), 1);
	/* P_k|k */
	public Matrix estimate_covariance = new Matrix(stateDimension(), stateDimension());
	
	/* This group is used for meaningless intermediate calculations */
	public Matrix big_square_scratch = new Matrix(stateDimension(), stateDimension());
	public Matrix identity_scratch = new Matrix(stateDimension(), stateDimension());
	public Matrix delta_vector_scratch = new Matrix(stateDimension(), 1);
	public Matrix delta_matrix_scratch = new Matrix(stateDimension(), stateDimension());
	
	public abstract int stateDimension();
	public abstract void initialState(Matrix initial_state);
	public abstract void initialStateCovariance(Matrix initial_covariance);
	public abstract void predictionModel(Matrix state, Matrix state_delta);
	public abstract void predictionModelJacobian(Matrix state, Matrix state_delta_jacobian);
	public abstract void processNoiseCovariance(Matrix process_noise_covariance);
}

