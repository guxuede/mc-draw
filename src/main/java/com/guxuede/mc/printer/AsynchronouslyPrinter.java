package com.guxuede.mc.printer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.guxuede.mc.loader.PaintPoint;
import com.guxuede.mc.loader.PaintPointBox;

public abstract class AsynchronouslyPrinter implements ObjectPrinter{

	public static final int EVERY_BATCH_PRINT_COUNT = 1000;
	
	private List<PaintPoint> target;
	private PaintPointBox paintPointBox;
	private Location centerPoint;
	private World world;
	private JavaPlugin javaPlugin;
	private Player player;
	
	public AsynchronouslyPrinter(PaintPointBox ppbox,JavaPlugin javaPlugin,Player player) {
		this.centerPoint = player.getLocation();
		this.target = ppbox.points;
		this.paintPointBox = ppbox;
		this.javaPlugin = javaPlugin;
		this.world = player.getWorld();
		this.player = player;
	}

	@Override
	public void print() {
        BukkitRunnable mTask =  new BukkitRunnable(){
            int time=0;
            @Override
            public void run() {
                if(time==0){
                    printLogMessage("Analysis End...Drawing....Total Block:"+target.size());
                }
                final List<PaintPoint> batchTaskPoints = new ArrayList<PaintPoint>();
                for(int i=time*EVERY_BATCH_PRINT_COUNT;(i<target.size() && i< time*EVERY_BATCH_PRINT_COUNT + EVERY_BATCH_PRINT_COUNT);i++){
                    batchTaskPoints.add(target.get(i));
                }
                printLogMessage("Drawing batch in:"+time+"/"+(target.size()/EVERY_BATCH_PRINT_COUNT+1));

                drawBatch(batchTaskPoints);

                if(time*EVERY_BATCH_PRINT_COUNT > target.size()){
                    printLogMessage("Draw finish total block:"+target.size());
                    this.cancel();
                }
                time++;
            }
        };
        mTask.runTaskTimer(javaPlugin, 5, 5);
	}
	
	public abstract void drawBatch(List<PaintPoint> batch);
	
	@Override
	public PaintPoint changeBlock(PaintPoint p) {
		Block block = world.getBlockAt((int)p.point.x, (int)p.point.y, (int)p.point.z);
		block.setType(p.pigment.type);
		block.setData(p.pigment.data);
		return null;
	}
	
	@Override
	public List<PaintPoint> getPrintResult() {
		return target;
	}

	protected void printLogMessage(String msg) {
		System.err.println(msg);
    	player.sendMessage(msg);
	}

	public List<PaintPoint> getTarget() {
		return target;
	}

	public void setTarget(List<PaintPoint> target) {
		this.target = target;
	}

	public Location getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Location centerPoint) {
		this.centerPoint = centerPoint;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public JavaPlugin getJavaPlugin() {
		return javaPlugin;
	}

	public void setJavaPlugin(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PaintPointBox getPaintPointBox() {
		return paintPointBox;
	}

	public void setPaintPointBox(PaintPointBox paintPointBox) {
		this.paintPointBox = paintPointBox;
	}
	
	
	
}
