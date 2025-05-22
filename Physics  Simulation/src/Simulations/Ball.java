package Simulations;

import java.awt.Color;

public class Ball {
	float x;
	float y;
	float sx;
	float sy;
	float m;
	int r;
	Color col;
	public Ball(float x, float y, int r) {
		this.x=x;
		this.y=y;
		this.r=r;
	}
	public Ball(float x, float y, int r, float sx, float sy, Color col) {
		this.x=x;
		this.y=y;
		this.r=r;
		this.sx=sx;
		this.sy=sy;
		this.m=r*r;
		this.col = col;
	}
}
