# extended-kalman-filter
Extended Kalman Filter implemented in Java with easy representation of model and observation functions

# One-dimensional example of moving object

Let's start with simple example of object moving in one dimension. Object has position x and velocity v. Object starts at unknown position and unknown velocity. Object is being observed at time intervals i=0,1..10 at positions x0=0, x1=1, ... x10=10. The task is to estimate the position and velocity at time i=10.

State equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\frac{d}{dt}\mathbf{x=f(x)})  

where state vector x and system function f are defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bf%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bc%7Dv%5C%5C0%5Cend%7Barray%7D%5Cright%5D)

Observation equation is defined as follows

![equation](https://latex.codecogs.com/gif.latex?\mathbf{y=h(x)})

where in our example we are observing position so h is defined as

![equation](https://latex.codecogs.com/gif.latex?%5Cmathbf%7Bh%7D%3D%5Cleft%5Bx%5Cright%5D)

In this simple example functions f and h are linear. The library is written for general non-linear case. That is why we need to provide jacobians of f and h functions

https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%5Cmathbf%7Bf%7D%7D%7B%5Cpartial%20x%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D0%261%5C%5C0%260%5Cend%7Barray%7D%5Cright%5D

https://latex.codecogs.com/gif.latex?%5Cfrac%7B%5Cpartial%5Cmathbf%7Bh%7D%7D%7B%5Cpartial%20x%7D%3D%5Cleft%5B%5Cbegin%7Barray%7D%7Bcc%7D1%260%5Cend%7Barray%7D%5Cright%5D


