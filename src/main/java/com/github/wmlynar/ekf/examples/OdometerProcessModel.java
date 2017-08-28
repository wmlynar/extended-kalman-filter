package com.github.wmlynar.ekf.examples;

import com.github.wmlynar.ekf.Matrix;
import com.github.wmlynar.ekf.ProcessModel;

public class OdometerProcessModel extends ProcessModel {

	@Override
	public int stateDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initialState(Matrix initial_state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialStateCovariance(Matrix initial_covariance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void predictionModel(Matrix state, double dt, Matrix predicted_state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void predictionModelJacobian(Matrix state, double dt, Matrix predicted_state_jacobian) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processNoiseCovariance(double dt, Matrix process_noise_covariance) {
		// TODO Auto-generated method stub
		
	}

}
