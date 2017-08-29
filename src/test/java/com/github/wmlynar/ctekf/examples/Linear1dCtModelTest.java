package com.github.wmlynar.ctekf.examples;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.wmlynar.ctekf.CtKalmanFilter;

public class Linear1dCtModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Linear1dCtProcessModel model = new Linear1dCtProcessModel();
		Linear1dCtObservationModel obs = new Linear1dCtObservationModel();
		CtKalmanFilter filter = new CtKalmanFilter(model);
		
        for (int i = 0; i <= 10; ++i) {
        	obs.setPosition(i);
            filter.update(1,obs);
        }
        
        double x = filter.model.state_estimate.data[0][0];
        double v = filter.model.state_estimate.data[1][0];
        
        assertEquals(10,x,1e-3);
        assertEquals(1,v,1e-3);
	}

}
