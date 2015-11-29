package com.guxuede.mc.printer;

import java.util.List;

import com.guxuede.mc.loader.PaintPoint;

public interface ObjectPrinter {

	public void print();
	
	public List<PaintPoint> getPrintResult();
	
	public PaintPoint changeBlock(PaintPoint p);
}
