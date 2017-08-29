# extended-kalman-filter
Extended Kalman Filter implemented in Java with easy representation of model and observation functions

# One-dimensional example of moving object

Let's start with simple example of object moving in one dimension. Object has position x and velocity v. Object starts at unknown position and unknown velocity. Object is being observed at time intervals i=0,1..10 at positions x0=0, x1=1, ... x10=10. The task is to estimate the position and velocity at time i=10.

State equation is defined by state vector x and function f

![equation](https://latex.codecogs.com/gif.latex?%5Cfrac%7Bd%7D%7Bdt%7D%5Cmathbf%7Bx%3Df%28x%29%7D)  

