package com.github.wmlynar.ekf.examples;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.wmlynar.ekf.DKalmanFilter;
import com.github.wmlynar.ekf.examples.Linear2dDObservationModel;
import com.github.wmlynar.ekf.examples.Linear2dDProcessModel;

public class Linear2dDModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Linear2dDProcessModel model = new Linear2dDProcessModel();
		Linear2dDObservationModel obs = new Linear2dDObservationModel();
		DKalmanFilter filter = new DKalmanFilter(model);
		
        for (int i = 0; i <= 10; ++i) {
        	obs.setPosition(i,i);
            filter.update(1,obs);
        }
        
        double x = filter.model.state_estimate.data[0][0];
        double vx = filter.model.state_estimate.data[1][0];
        double y = filter.model.state_estimate.data[2][0];
        double vy = filter.model.state_estimate.data[3][0];
        
        assertEquals(10,x,1e-1);
        assertEquals(1,vx,1e-6);
        assertEquals(10,y,1e-1);
        assertEquals(1,vy,1e-6);
	}

}
