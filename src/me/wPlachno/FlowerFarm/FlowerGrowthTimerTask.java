package me.wPlachno.FlowerFarm;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.block.Block;


public class FlowerGrowthTimerTask extends TimerTask{
	Timer FlowerGrowth;
	long delay;
	Block src;
	int blkx;
	int blky;
	int blkz;
	int fatherIdx;
	int timerIdx;
	String world;
	
	public FlowerGrowthTimerTask(long delay, Block src, int fatherIdx, int timerIdx){
		this.delay = delay;
		this.fatherIdx = fatherIdx;
		this.timerIdx = timerIdx;
		this.src = src;
		this.blkx = src.getX();
		this.blky = src.getY();
		this.blkz = src.getZ();
		this.world = src.getWorld().getName();
		FlowerGrowth = new Timer(true);
		FlowerGrowth.schedule(this, delay, delay);
	}
	public FlowerGrowthTimerTask(String world, int x, int y, int z, long wait){
		this.blkx = x;
		this.blky = y;
		this.blkz = z;
		this.world = world;
		this.src = FlowerFarm.server.getWorld(world).getBlockAt(x,y,z);
		this.fatherIdx = FlowerFarm.isFather(src);
		if(fatherIdx == -1){
			timerIdx = -1;
		} else {
			this.delay = FlowerFarm.fathers.get(fatherIdx).millisecondsToSeed;
			this.timerIdx = FlowerFarm.timers.size();
			FlowerGrowth = new Timer (true);
			FlowerGrowth.schedule(this, wait, delay);
		}
	}
	
	public void run(){
		this.src = FlowerFarm.server.getWorld(src.getWorld().getName()).getBlockAt(blkx, blky, blkz);
		if (!FlowerFarm.fathers.get(fatherIdx).isFather(src)){
			FlowerFarm.pLog("Block dissappeared. Huh...");
			FlowerFarm.log.info("Block at " + src.getLocation().toString() + " is missing.");
			this.cancel();
			FlowerFarm.timers.remove(this);
		}
		else {
			FlowerFarm.log.info("Seeding block at " + src.getLocation().toString());
			FlowerFarm.pLog("seeding block");
			FlowerFarm.fathers.get(fatherIdx).seed(src);
		}
	}
	
	

}
