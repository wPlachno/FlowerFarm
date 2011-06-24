package me.wPlachno.FlowerFarm;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * TO DO LIST
 * 
 * 1. Database of placed blocks
 *  - Both saving and reading
 *  
 * 
 */

public class FlowerFarm extends JavaPlugin {
	
	public static Logger log = Logger.getLogger("Minecraft");
	public static Server server;	
	public static String logPrefix = "[FlowerFarm] ";
	String version = "0.0.1";
	public static String pluginMainDir = "./plugins/FlowerFarm";	
	public FlowerFarmConfig setUp;
	public static File pLogF;
	public static BufferedWriter pluginLog;
	public static Random generator;
	public static Material mat;


	public static FlowerFarmBlockListener blockListener;
	public static FlowerFarmWorldListener worldListener;

	//Global array of fathers. Fathers are the germination schemas
	public static ArrayList<FlowerFarmFather> fathers = new ArrayList<FlowerFarmFather>();
	//global array of timer tasks. These timer tasks house the individual germinations
	public static ArrayList<FlowerGrowthTimerTask> timers = new ArrayList<FlowerGrowthTimerTask>();
	
	//When the plugin is enabled..
	public void onEnable(){
		generator = new Random();
		pLogF = new File(pluginMainDir + "/pluginLog.txt");
		pLogF.getParentFile().mkdirs();
		try{
			pluginLog = new BufferedWriter(new FileWriter(pLogF));
		} catch (IOException ex){
			ex.printStackTrace();
		} 
		server = this.getServer();
		setUp = new FlowerFarmConfig(this);
		blockListener = new FlowerFarmBlockListener();
		worldListener = new FlowerFarmWorldListener();
		
		PluginManager pm = server.getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_CANBUILD, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.CHUNK_LOAD, worldListener, Event.Priority.High, this);
		pLog("Plugin enabled.");
		pLog("Event registered.");
		log.info(logPrefix + "FlowerFarm v" + version + " has been enabled!");		
	}
	
	public void onDisable(){
		//save databases
		setUp.saveDB();
		log.info("FlowerFarm has been disabled! Oh no!");
		pLog("Plugin disabled.");
		try {
			pluginLog.flush();
			pluginLog.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static void logIt(String msg){
		pLog(logPrefix + msg);
		log.info(logPrefix + msg);
	}
	public static void pLog(String logMsg){
		try {
			pluginLog.write(logMsg);
			pluginLog.newLine();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Called onBlockBreak, checks if the broken block was a father,
	 * if so, it cancels the timer and removes said timer from the array
	 * @param blk: the blk of the event
	 * @return static: whether the blk broken had a timer.
	 */
	public static boolean affectsTimers(Block blk){
		int idx = isFather(blk);
		if (idx == -1){
			return false;
		}
		else {
			int timerIdx = 0;
			while (timerIdx < timers.size()){
				if (blk.getX() == timers.get(timerIdx).blkx){
					if (blk.getZ() == timers.get(timerIdx).blkz){
						if (blk.getY() == timers.get(timerIdx).blky){
							timers.get(timerIdx).cancel();
							timers.remove(timerIdx);
							return true;
						}
					}
				}
				timerIdx++;
			}
		}
		return false;
	}
	public static int isFather(Block blk){
		int i = 0;
		while (i < fathers.size()){
			if (blk.getTypeId()==fathers.get(i).fatherID){
				return i;
			}
			i++;
		}
		return -1;
	}
	public static boolean isTimer(Block blk){
		int t = 0;
		while (t<timers.size()){
			FlowerGrowthTimerTask buf = timers.get(t);
			if (buf.blkx == blk.getX()){
				if(buf.blky == blk.getY()){
					if(buf.blkz == blk.getZ()){
						return true;
					}
				}
			}
			t++;
		}
		return false;
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		 
		 if(cmd.getName().equalsIgnoreCase("flowerfarm")){ // If the player typed /basic then do the following...
		   logIt(FlowerFarm.logPrefix + this.version);
		   logIt("Certain blocks will spread! Active germinators:");
		   for (int i = 0; i < FlowerFarm.fathers.size(); i++){
			   logIt(FlowerFarm.fathers.get(i).print());
		   }
		   return true;
		 } //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		 return false; 
		}
}
