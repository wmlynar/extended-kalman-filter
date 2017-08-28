package com.github.wmlynar.ekf.examples;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.wmlynar.ekf.KalmanFilter;
import com.github.wmlynar.ekf.examples.Linear1dObservationModel;
import com.github.wmlynar.ekf.examples.Linear1dProcessModel;
import com.github.wmlynar.ekf.examples.Linear2dObservationModel;
import com.github.wmlynar.ekf.examples.Linear2dProcessModel;

public class Linear2dModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Linear2dProcessModel model = new Linear2dProcessModel();
		Linear2dObservationModel obs = new Linear2dObservationModel();
		KalmanFilter filter = new KalmanFilter(model);
		
        for (int i = 0; i <= 10; ++i) {
        	obs.setPosition(i,i);
            filter.update(1,obs);
        }
        
        double x = filter.model.state_estimate.data[0][0];
        double vx = filter.model.state_estimate.data[1][0];
        double y = filter.model.state_estimate.data[2][0];
        double vy = filter.model.state_estimate.data[3][0];
        
        assertEquals(10,x,1e-2);
        assertEquals(1,vx,1e-6);
        assertEquals(10,y,1e-2);
        assertEquals(1,vy,1e-6);
	}

}
