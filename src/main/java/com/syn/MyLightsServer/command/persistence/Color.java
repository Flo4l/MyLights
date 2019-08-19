package com.syn.MyLightsServer.command.persistence;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

public class Color implements Serializable {

	@Min(0)
	@Max(255)
	private int red;

	@Min(0)
	@Max(255)
	private int green;

	@Min(0)
	@Max(255)
	private int blue;

	public Color() {
		this(0, 0, 0);
	}

	public Color(@Min(0) @Max(255) int red, @Min(0) @Max(255) int green, @Min(0) @Max(255) int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
}
