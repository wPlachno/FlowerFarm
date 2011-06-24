package me.wPlachno.FlowerFarm;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class FlowerFarmAlgoBlock{
	int x;
	int y;
	int z;
	int fatherIdx;
	int distance;
	byte lumMult;
	int soilMult;
	int nutriMult;
	int chunkx;
	int chunkz;
	String world;
	
	FlowerFarmAlgoBlock(Block myBlock, Block src, int fathIdx){
		this.x = myBlock.getX();
		this.y = myBlock.getY();
		this.z = myBlock.getZ();
		this.world = src.getWorld().getName();
		this.fatherIdx = fathIdx;
		this.distance = (3*FlowerFarm.fathers.get(fatherIdx).seedRadius) - (Math.abs(this.x-src.getX()) + Math.abs(this.y - src.getY()) + Math.abs(this.z - src.getZ()));
		this.lumMult = FlowerFarm.fathers.get(fatherIdx).setLum(myBlock.getLightLevel());
		this.nutriMult = FlowerFarm.fathers.get(fatherIdx).setNutrient(myBlock);
		chunkx = myBlock.getChunk().getX();
		chunkz = myBlock.getChunk().getZ();
	}
	
	/**
	 * Checks if this block is in the given chunk.
	 * @param c: The chunk to test with.
	 * @return bool: Whether the block was included in this chunk.
	 */
	public boolean inChunk(Chunk c){
		if (!((this.x > c.getX()) && (this.x < (c.getX()+15)))){
			return false;
		}
		if (!((this.z > c.getZ()) && (this.z < (c.getZ()+15)))){
			return false;
		}
		return true;
	}
	
	/** 
	 * Gets the fertilization probability.
	 * @return int: the fertilization property. 
	 * 	Should be below 100.
	 */
	public int getFert(){
		FlowerFarm.logIt("getFert called:");
		float distProb = (float)distance/7;
		float lumProb = (float)lumMult/15;
		float soilProb = (float)soilMult/5;
		float nutriProb = (float)nutriMult/50;
		FlowerFarm.logIt("Dist: " + distProb + " Lum: " + lumProb + " soil: " + soilProb + " Nutrients: " + nutriProb);
		float stepTwo = (3*distProb)+(2*soilProb)+(lumProb)+(nutriProb);
		float stepThree = stepTwo/7;
		FlowerFarm.logIt(stepTwo + "/7 = " + stepThree);
		return (int) (80*(stepThree));
		
	}
}
