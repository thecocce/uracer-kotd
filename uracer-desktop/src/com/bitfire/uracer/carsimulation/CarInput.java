package com.bitfire.uracer.carsimulation;

/** Represents the input forces for the simulator.
 *
 * @author manuel */

public final class CarInput {
	public float throttle, steerAngle;
	public boolean updated;

	public CarInput() {
		reset();
	}

	public CarInput( CarInput other ) {
		set( other );
	}

	public void reset() {
		updated = false;
		throttle = 0;
		steerAngle = 0;
	};

	public void set( CarInput other ) {
		this.throttle = other.throttle;
		this.steerAngle = other.steerAngle;
		this.updated = other.updated;
	}
}
