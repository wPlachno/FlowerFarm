package me.wPlachno.FlowerFarm;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;


public class FlowerFarmFather {
	
	public enum Result{
		NO_GERM,
		FATHER_MISSING,
		GERM_1,
		GERM_2,
		GERM_3,
		GERM_4;
	}
	
	public int fatherID;
	public int fatherIdx;
	public long millisecondsToSeed = 65000;
	public int seedRadius = 2;
	public int reqLum = 6;
	public int fertMult = 3;
	public ArrayList<FlowerFarmTypeTag> Descendents;
	public ArrayList<FlowerFarmTypeTag> Soil;
	public ArrayList<FlowerFarmTypeTag> Nutrients;
	
	
	public FlowerFarmFather(){
		Descendents = new ArrayList<FlowerFarmTypeTag> ();
		Soil = new ArrayList<FlowerFarmTypeTag> ();
		Nutrients = new ArrayList<FlowerFarmTypeTag> ();
	}
	
	/**
	 * Called when timer goes off. Determines whether we will germinate.
	 * @param father: The source block that is germinating.
	 * @return A result enumeration of which germ spread.
	 */
	public Result seed(Block father){
		if (father.getTypeId() != fatherID){
			return Result.FATHER_MISSING;
		}
		ArrayList<FlowerFarmAlgoBlock> neighbors = new ArrayList<FlowerFarmAlgoBlock>();
		FlowerFarm.logIt("Populating neighbor array:");
		neighbors = popNeighbors(father);
		FlowerFarm.logIt("Finished populating neighbors... neighbors has " + neighbors.size() + " elements.");
		int i = 0;
		while ( i < neighbors.size()){
			int seedProb = (this.fertMult)*15;
			int curResult = FlowerFarm.generator.nextInt(100);
			FlowerFarm.logIt("Neighbor " + i + "/" + neighbors.size() + ": " + fertMult + "/" + curResult);
			if (curResult<seedProb){
				FlowerFarm.logIt("Neighbor at x: " + neighbors.get(i).x + " y: " + neighbors.get(i).y + " z: " + neighbors.get(i).z);
				FlowerFarm.logIt(curResult + "/" + seedProb + " - going to phase 2.");
				seed(neighbors.get(i));
			}
			i++;
		}
		return Result.NO_GERM;
	}
	
	/**
	 * Calls the blocks fertilization multiplier and may seed.
	 * @param blk: The block where a seed may germinate.
	 * @return: void. 
	 */
	public void seed(FlowerFarmAlgoBlock blk){
		FlowerFarm.logIt("Dist: " + blk.distance + " Lumination: " + blk.lumMult + " Nutrition: " + blk.nutriMult + " Soil: " + blk.soilMult);
		int curResult = FlowerFarm.generator.nextInt(100);
		FlowerFarm.logIt(curResult + "(x)/(y)" + blk.getFert() + "   if x < y, we register.");
		
		if (curResult < blk.getFert()){
			FlowerFarm.logIt("Registering... ");
			FlowerFarm.worldListener.regBlock(blk);
		}
	}
	
	/**
	 * Checks whether the passed block is this fathers ID
	 * @param maybe: the block we are checking as the father
	 * @return bool: whether this is the same ID as this father.
	 */
	public boolean isFather(Block maybe){
		if (maybe.getTypeId() == fatherID){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Checks whether the luminosity of a block is high enough to let this seed germinate
	 * Will return either 0 or the original luminosity. 0 means it wasnt high enough.
	 * @param lum: the unaltered luminosity of the block
	 * @return byte: the luminosity of the block, changed to 0 if it was too dark.
	 */
	public byte setLum(byte lum){
		if (lum < reqLum){
			lum = 0;
		}
		return lum;
	}
	
	/**
	 * Retrieves the seed that germinated.
	 * @return int: the typeID of the seed.
	 */
	public int getChild(){
		if (Descendents.size() == 1){
			return Descendents.get(0).typeID;
		} else {
			if (FlowerFarm.generator.nextInt(100)>this.Descendents.get(0).tag){
				return Descendents.get(0).typeID;
			}
			return Descendents.get(0).typeID;
		}
	}
	
	/**
	 * Populates an ArrayList of FFABlocks that represent germable airblocks.
	 * @param neighbors: the ArrayList to be populated
	 * @param src: the source block to find the neighbors for.
	 */
	public ArrayList<FlowerFarmAlgoBlock> popNeighbors(Block src){
		ArrayList<FlowerFarmAlgoBlock> neighbors = new ArrayList<FlowerFarmAlgoBlock>();
		Block srcNew = FlowerFarm.server.getWorld(src.getWorld().getName()).getBlockAt(src.getLocation());
		int destx= -seedRadius;
		while (destx <= seedRadius){
			int destz= -seedRadius;
			while (destz <= seedRadius){
				FlowerFarm.logIt("Checking " + destx + "/" + destz);
				if(!((destx==0)&&(destz==0))){
					Block dest = srcNew.getWorld().getBlockAt(srcNew.getX() + destx, srcNew.getY(), srcNew.getZ() + destz);
					FlowerFarm.logIt("Checking neighbor xdiff/zdiff: " + destx + "/" + destz);
					int mult = findAir(dest, 0);
					FlowerFarm.logIt("FindAir returned: " + mult);
					if (mult>-1){
						FlowerFarmAlgoBlock curBlock = new FlowerFarmAlgoBlock(dest, srcNew, fatherIdx);
						curBlock.soilMult = mult;
						neighbors.add(curBlock);
					}
				}destz++;
			} destx++;
		}
		return neighbors;
		
	}
	
	/**
	 * Given a block, will find a valid air block with valid soil below it
	 * returning the soil multiplier or -1 if it couldnt find a valid slot
	 * @param blk: a block with the x and z values to search through. Will
	 * 	be changed to the correct altitude at the end
	 * @param yDist: the distance from the altitude of the source block.
	 * 	Required to end the recursive function.
	 * @return int: -1 if a valid space couldn't be found, otherwise the
	 * 	integer returned is the soil Multiplier of the soil beneath the slot.
	 */
	public int findAir(Block blk, int yDist){
		if ((yDist>seedRadius) || (yDist<-seedRadius)){
			return-1;
		}
		int type = blk.getTypeId();
		if (type == 0){
			int belowType = blk.getWorld().getBlockTypeIdAt(blk.getX(), blk.getY()-1, blk.getZ());
			if (belowType == 0){
				return findAir(blk.getWorld().getBlockAt(blk.getX(), blk.getY()-1, blk.getZ()), yDist-1);
			}
			else{
				return validSoil(belowType);
			} 
		} else {
			return findAir(blk.getWorld().getBlockAt(blk.getX(), blk.getY()+1, blk.getZ()), yDist+1);
		}
	}
	
	/**
	 * Checks whether the typeID passed is the ID of a valid soil block
	 * returns the soil multiplier for that block.
	 * @param typeID: the typeID to be checked
	 * @return int: -1 either if there are no valid soils or just none found
	 * 	Otherwise returns the soil multiplier of the soil with the same ID
	 */
	public int validSoil(int typeID){
		if (this.Soil.isEmpty()) {
			return -1;
		} else {
			int i = 0;
			while (i < Soil.size()){
				if (typeID == Soil.get(i).typeID){
					return Soil.get(i).tag;
				} else {
					i++;
				}
			}
		}
		return -1;
	}

	/**
	 * Finds the nutrient multiplier for this fathers nutrients for the given block
	 * @param src: the block to find the multiplier
	 * @return int: the nutrient multiplier of the block. 0 if theres none.
	 */
	public int setNutrient (Block src){
		if (!Nutrients.isEmpty()){
			int total = 0;
			for (int x = -seedRadius; x <= seedRadius; x++){
				for (int y = -seedRadius; y <= seedRadius; y++){
					for (int z = -seedRadius; z <= seedRadius; z++){
						int distanceMult = 3*seedRadius - (Math.abs(x) + Math.abs(y) + Math.abs(z));
						total = total + isNutrient(src.getRelative(x, y, z), distanceMult);					
					}
				}
			}
			return total;
		}
		return 0;
	}
	
	/**
	 * Checks this block for nutrients type ID
	 * @param blk: the block to be checked.
	 * @param distMult: the distance multiplier of the nutrients
	 * @return int: the nutrient multiplier for this block.
	 */
	public int isNutrient(Block blk, int distMult){
		int idx = 0;
		while (idx < Nutrients.size()){
			if (blk.getTypeId() == Nutrients.get(idx).typeID){
				return distMult * Nutrients.get(idx).tag;
			}
			idx++;
		}
		return 0;
	}
	public String print(){
		String fatherStr = Material.getMaterial(fatherID).name();
		String descStr = print(this.Descendents);
		String soilStr = print(this.Soil);
		String nutrStr = print(this.Nutrients);
		return fatherStr + " grows " + descStr + " above " + soilStr + " with nutrients " + nutrStr;		
	}
	private String print(ArrayList<FlowerFarmTypeTag> al){
		String buffer = new String();
		for (int i = 0; i < al.size();i++){
			buffer += Material.getMaterial(al.get(i).typeID).name();
			if (i+1 < al.size()){
				buffer += " or ";
			}
		}
		return buffer;
	}
}
