package com.github.wmlynar.ekf.examples;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.wmlynar.ekf.KalmanFilter;

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
			double time = i;
			obs.setPosition(i, i);
			filter.update(time, obs);
		}

		double x = filter.model.state_estimate.data[0][0];
		double vx = filter.model.state_estimate.data[1][0];
		double y = filter.model.state_estimate.data[2][0];
		double vy = filter.model.state_estimate.data[3][0];

		Assert.assertEquals(10, x, 1e-1);
		Assert.assertEquals(1, vx, 1e-5);
		Assert.assertEquals(10, y, 1e-1);
		Assert.assertEquals(1, vy, 1e-5);
	}

}
