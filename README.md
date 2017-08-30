# extended-kalman-filter
Extended Kalman Filter implemented in Java with easy representation of model and observation functions

# One-dimensional example of moving object

Let's start with a simple example of object that is moving in one dimension. Object has position x and velocity v. Object starts at unknown position and unknown velocity. Object is being observed at time points i=0,1..10 at positions y0=0, y1=1, ... y10=10. The task is to estimate the position and velocity at time i=10.

State equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\frac{d}{dt}\mathbf{x=f(x)})  

where state vector x and system function f are defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bx%3D%7D%5Cleft%5B%20%5Cbegin%7Barray%7D%7Bc%7D%20x%20%5C%5C%20v%20%5Cend%7Barray%7D%20%5Cright%5D)

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bf%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bc%7Dv%5C%5C0%5Cend%7Barray%7D%5Cright%5D)

In this simple example function f is linear. Nevertheless the library is written for general non-linear Extended Kalman Filter case. That is why we need to provide jacobian of f

![equation](https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%20%5Cmathbf%7Bf%7D%7D%7B%5Cpartial%20%5Cmathbf%7Bx%7D%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D0%261%5C%5C0%260%5Cend%7Barray%7D%5Cright%5D)

In Java the above formulas can be provided by subclassing the ProcessModel class:

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

Observation equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\mathbf{y=h(x)})

where in our example we are observing position so h is defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bh%7D%3D%5Cleft%5Bx%5Cright%5D)

Jacobian of function h is computed as

![equation](https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%20%5Cmathbf%7Bh%7D%7D%7B%5Cpartial%20%5Cmathbf%7Bx%7D%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D1%260%5Cend%7Barray%7D%5Cright%5D)

Above formulas are implemented in Java by subclassing the ObservationModel:

```
public class Linear1dObservationModel extends ObservationModel {

	private double mx;
	
	public void setPosition(double x) {
		this.mx = x;
	}
	
	@Override
	public int observationDimension() {
		return 1;
	}

	@Override
	public int stateDimension() {
		return 2;
	}

	@Override
	public void observationMeasurement(double[][] y) {
		y[0][0] = mx;
	}

	@Override
	public void observationModel(double[][] x, double[][] h) {
		h[0][0] = x[0][0];
	}

	@Override
	public void observationModelJacobian(double[][] j) {
		j[0][0] = 1;
		j[0][1] = 0;
	}

	@Override
	public void observationNoiseCovariance(double[][] cov) {
		cov[0][0] = 1;
	}
}

```

