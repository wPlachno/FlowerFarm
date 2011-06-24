package me.wPlachno.FlowerFarm;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class FlowerFarmBlockListener extends BlockListener {

	public static FlowerFarm plugin; 
	
	public FlowerFarmBlockListener(){
	}
	
	public void onBlockPlace(BlockPlaceEvent curEvent){
		FlowerFarm.pLog("onBlockPlace event triggered.");
		int idx = FlowerFarm.isFather(curEvent.getBlock());
		if (idx > -1){
			FlowerFarm.pLog(FlowerFarm.fathers.get(idx).fatherID + " was placed at " + curEvent.getBlock().getLocation());
			FlowerFarm.timers.add(new FlowerGrowthTimerTask(FlowerFarm.fathers.get(idx).millisecondsToSeed, curEvent.getBlock(), idx, FlowerFarm.timers.size()));
			FlowerFarm.pLog("Added timer:");
			FlowerFarm.pLog(FlowerFarm.fathers.get(idx).millisecondsToSeed + "s timer.");
			FlowerFarm.log.info("Added " + FlowerFarm.fathers.get(idx).millisecondsToSeed + "s timer for " + curEvent.getBlock().getLocation().toString());
		}
		return;
	}
	public void onBlockBreak(BlockBreakEvent curEvent){
		FlowerFarm.affectsTimers(curEvent.getBlock()); 
	}
	public void onBlockCanBuild(BlockCanBuildEvent curEvent){
		if (FlowerFarm.isTimer(curEvent.getBlock())){
			curEvent.setBuildable(true);
		}
	}
}
