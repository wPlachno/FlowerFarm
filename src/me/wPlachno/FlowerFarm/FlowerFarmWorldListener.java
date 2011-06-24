package me.wPlachno.FlowerFarm;

import java.util.ArrayList;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.WorldListener;

public class FlowerFarmWorldListener extends WorldListener{

	ArrayList <FlowerFarmAlgoBlock> changes;
	FlowerFarm plugin;
	
	public FlowerFarmWorldListener(){
		changes = new ArrayList <FlowerFarmAlgoBlock> ();
	}
	
	/**
	 * The chunk load listener. Will check the chunk for any blocks to set.
	 * @param chunkE: the chunk event
	 */
	public void onChunkLoad(ChunkEvent chunkE){
		if (!changes.isEmpty()){
			int i = 0;
			Chunk chunk = chunkE.getChunk();
			while (i<changes.size()){
				if(changes.get(i).inChunk(chunk)){
					FlowerFarm.logIt("onChunkLoad: setting block in loaded chunk.");
					setBlock(chunk, changes.remove(i));
				}
				else {
					i++;
				}
			}
		}
	}
	
	/**
	 * Will either set the block change right away or add it to the changes list
	 * @param blk: the block to change the typeID of.
	 */
	public void regBlock(FlowerFarmAlgoBlock blk){
		if (FlowerFarm.server.getWorld(blk.world).isChunkLoaded(blk.chunkx, blk.chunkz)){
			FlowerFarm.logIt("Surpassing changes array.");
			setBlock(FlowerFarm.server.getWorld(blk.world).getChunkAt(blk.chunkx, blk.chunkz), blk);
		}
		FlowerFarm.logIt("Adding block to changes array.");
		changes.add(blk);
	}
	
	/**
	 * Sets the block in this chunk to the right typeID.
	 * Will get this ID from the respective father object.
	 * @param chunk: the chunk we are going to alter.
	 * @param blk: information for the block we are going to change.
	 */
	private void setBlock(Chunk chunk, FlowerFarmAlgoBlock blk){
		//get chunk block
		Block block = chunk.getBlock(blk.x, blk.y, blk.z);
		//get typeId for child
		int childType = FlowerFarm.fathers.get(blk.fatherIdx).getChild();
		//set typeId
		block.setTypeId(childType);
		int idx = FlowerFarm.isFather(block);
		if ((idx > -1)&& !(FlowerFarm.isTimer(block))){
			FlowerFarm.timers.add(new FlowerGrowthTimerTask(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), 10500));
		}
	}
}
