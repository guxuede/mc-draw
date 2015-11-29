package com.guxuede.mc.printer;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.guxuede.mc.loader.PaintPoint;
import com.guxuede.mc.loader.PaintPointBox;

public class CoordinateTransformPrinter extends AsynchronouslyPrinter{

	public CoordinateTransformPrinter(PaintPointBox ppbox,
			JavaPlugin javaPlugin, Player player) {
		super(ppbox, javaPlugin, player);
	}

	public int type = 0;
	public int roate = 0;
	

	
	public void transform(PaintPoint p){
		float cx = (float) getCenterPoint().getX();
		float cy = (float) getCenterPoint().getY();
		float cz = (float) getCenterPoint().getZ();
		p.point.rotate(getPpbox().getCenterPoint(),roate);
		
		float ox = p.point.x;
		float oy = p.point.y;
		float oz = p.point.z;
        if(type == 0){//Vertical
        	p.point.x = ox + cx; 
        	p.point.y = oy + cy; 
        	p.point.z = oz + cz; 
        }
        else if(type == 1){//Horizontal
        	p.point.x = ox + cx; 
        	p.point.y = oz + cy; 
        	p.point.z = oy + cz; 
        }
	}
	

	@Override
	public void drawBatch(List<PaintPoint> batch) {
		for(PaintPoint p:batch){
			transform(p);
			changeBlock(p);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
}
