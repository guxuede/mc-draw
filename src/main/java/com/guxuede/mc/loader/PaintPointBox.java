package com.guxuede.mc.loader;

import java.util.ArrayList;
import java.util.List;

import com.guxuede.math.Vector3;

public class PaintPointBox {

	public List<PaintPoint> points;
	public Vector3 centerPoint = new Vector3();
	public float maxX;
	public float maxY;
	public float maxZ;
	public float minX;
	public float minY;
	public float minZ;
	
	public PaintPointBox() {
		points = new ArrayList<PaintPoint>();
	}
	
	public void addPoint(PaintPoint p){
		points.add(p);
		if(p.point.x > maxX){
			maxX = p.point.x;
		}
		if(p.point.y > maxY){
			maxY = p.point.y;
		}
		if(p.point.z > maxZ){
			maxZ = p.point.z;
		}
		if(p.point.x < minX){
			minX = p.point.x;
		}
		if(p.point.y < minY){
			minY = p.point.y;
		}
		if(p.point.z < minZ){
			minZ = p.point.z;
		}
		
		centerPoint.x = (maxX - minX) / 2;
		centerPoint.y = (maxY - minY) / 2;
		centerPoint.z = (maxZ - minZ) / 2;
	}
	
	public Vector3 getCenterPoint(){
		return centerPoint;
	}
}
