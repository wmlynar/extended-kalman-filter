package com.github.wmlynar.ekf;

public abstract class ObservationModel {

	/* This group of matrices must be specified by the user. */
	/* H_k */
	public Matrix observation_model = new Matrix(observationDimension(), stateDimension());
	/* R_k */
	public Matrix observation_noise_covariance = new Matrix(observationDimension(), observationDimension());
	
	/* The observation is modified by the user every time step. */
	/* z_k */
	public Matrix observation = new Matrix(observationDimension(), 1);
	
	/* This group of matrices are updated every time step by the filter. */
	/* y-tilde_k */
	public Matrix innovation = new Matrix(observationDimension(), 1);
	/* S_k */
	public Matrix innovation_covariance = new Matrix(observationDimension(), observationDimension());
	/* S_k^-1 */
	public Matrix inverse_innovation_covariance = new Matrix(observationDimension(), observationDimension());
	/* K_k */
	public Matrix optimal_gain = new Matrix(stateDimension(), observationDimension());

	/* This group is used for meaningless intermediate calculations */
	public Matrix vertical_scratch = new Matrix(stateDimension(), observationDimension());
	
	public abstract int stateDimension();
	public abstract int observationDimension();
	public abstract void observationMeasurement(Matrix observation_measured);
	public abstract void observationModel(Matrix state, Matrix observation_predicted);
	public abstract void observationModelJacobian(Matrix observation_jacobian);
	public abstract void observationNoiseCovariance(Matrix observation_noise_covariance);
}
