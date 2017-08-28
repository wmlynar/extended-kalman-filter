package com.github.wmlynar.ekf.examples;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.wmlynar.ekf.KalmanFilter;
import com.github.wmlynar.ekf.examples.Linear1dObservationModel;
import com.github.wmlynar.ekf.examples.Linear1dProcessModel;

public class Linear1dModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Linear1dProcessModel model = new Linear1dProcessModel();
		Linear1dObservationModel obs = new Linear1dObservationModel();
		KalmanFilter filter = new KalmanFilter(model);
		
        for (int i = 0; i <= 10; ++i) {
        	obs.setPosition(i);
            filter.update(1,obs);
        }
        
        double x = filter.model.state_estimate.data[0][0];
        double v = filter.model.state_estimate.data[1][0];
        
        assertEquals(10,x,1e-4);
        assertEquals(1,v,1e-4);
	}

}
