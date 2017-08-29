# extended-kalman-filter
Extended Kalman Filter implemented in Java with easy representation of model and observation functions

# One-dimensional example of moving object

Let's start with simple example of object moving in one dimension. Object has position x and velocity v. Object starts at unknown position and unknown velocity. Object is being observed at time intervals i=0,1..10 at positions x0=0, x1=1, ... x10=10. The task is to estimate the position and velocity at time i=10.

State equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\frac{d}{dt}\mathbf{x=f(x)})  

where state vector x and system function f are defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bx%3D%7D%5Cleft%5B%20%5Cbegin%7Barray%7D%7Bc%7D%20x%20%5C%5C%20v%20%5Cend%7Barray%7D%20%5Cright%5D)

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bf%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bc%7Dv%5C%5C0%5Cend%7Barray%7D%5Cright%5D)


Observation equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\mathbf{y=h(x)})

where in our example we are observing position so h is defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bh%7D%3D%5Cleft%5Bx%5Cright%5D)

In this simple example functions f and h are linear. Nevertheless the library is written for general non-linear case. That is why we need to provide jacobians of f and h functions

![equation](https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%5Cmathbf%7Bf%7D%7D%7B%5Cpartial%20x%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D0%261%5C%5C0%260%5Cend%7Barray%7D%5Cright%5D)

![equation](https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%5Cmathbf%7Bh%7D%7D%7B%5Cpartial%20x%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D1%260%5Cend%7Barray%7D%5Cright%5D)

In code this is achieved by subclassing StateModel and ObservationModel. They are beaing separated in order to provide ability to fuse different kind of observations.

Below is Java implementation of above formulas of the state equation. As you can see one only needs to provide non-zero values:

```
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
		cov[0][1] = 0;
		cov[1][0] = 0;
		cov[1][1] = 1000;
	}

	@Override
	public void stateFunction(double[][] x, double[][] f) {
		f[0][0] = x[1][0];
		f[1][0] = 0;
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
		j[0][0] = 0;
		j[0][1] = 1;
		j[1][0] = 0;
		j[1][1] = 0;
	}

	@Override
	public void processNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
		cov[0][1] = 0;
		cov[1][0] = 0;
		cov[1][1] = 1;
	}
}

```

