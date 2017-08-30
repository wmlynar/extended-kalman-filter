package com.github.wmlynar.ekf.examples;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.wmlynar.ekf.KalmanFilter;

public class Linear1dModelTest {

	@Test
	public void test() {
		Linear1dProcessModel model = new Linear1dProcessModel();
		Linear1dObservationModel obs = new Linear1dObservationModel();
		KalmanFilter filter = new KalmanFilter(model);
		
        for (int i = 0; i <= 10; ++i) {
        	double time = i;
        	obs.setPosition(i);
            filter.update(time,obs);
        }
        
        double x = model.getState()[0][0];
        double v = model.getState()[0][1];
        
        assertEquals(10,x,1e-3);
        assertEquals(1,v,1e-3);
	}
}
