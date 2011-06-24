package me.wPlachno.FlowerFarm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FlowerFarmConfig {
	//ArrayList<Integer> excludedWorlds = new ArrayList<Integer> ();
	FlowerFarm plugin;
	Integer secondsToUpdate = 15;
	Boolean simpleAlgorithm = true;
	Boolean upToDate = true;
	String pluginMainDir = "./plugins/FlowerFarm";
	String pluginConfigLocation = pluginMainDir + "/FlowerFarm.yml";
	String pluginDBLocation = pluginMainDir + "/FFDB.data";
	private static File DBFile;
	private static File cfgFile;
	
	
	public FlowerFarmConfig(final FlowerFarm instance){
		FlowerFarm.logIt("Config called.");
		plugin = instance;
		cfgFile = new File(pluginConfigLocation);
		cfgFile.getParentFile().mkdirs();
		if (cfgFile.exists()){
			FlowerFarm.logIt("cfgFile found.");
			ArrayList<FlowerFarmFather> fathers = new ArrayList<FlowerFarmFather>();
			boolean loaded = this.loadConfig(cfgFile, fathers);
			if (loaded == false){
				FlowerFarm.logIt("Unable to load the config file");
				buildDefaults();
			}
			else {
				FlowerFarm.fathers = fathers;
				FlowerFarm.logIt("Successfully loaded the config file.");
			}
		}
		else {
			//load default classes
			FlowerFarm.logIt("No cfg found. Loading defaults.");
			buildDefaults();
		}
		DBFile = new File(pluginDBLocation);
		DBFile.getParentFile().mkdirs();
		if (DBFile.exists()){
			ArrayList<FlowerGrowthTimerTask> timers = new ArrayList<FlowerGrowthTimerTask>();
			boolean loaded = this.loadDB(DBFile, timers);
			if (loaded){
				FlowerFarm.logIt("Database loaded");
				FlowerFarm.timers = timers;
			}
			else {
				FlowerFarm.logIt("Unable to load databases.");
			}
		}
	}
	
	/**
	 * Will build the FlowerFarm.fathers array with our default values
	 * The default values implement daffodils and roses
	 */
	private void buildDefaults(){
		//default A: Daffodils
		FlowerFarmFather daffodil = new FlowerFarmFather();
		daffodil.fatherID = 37;
		
		FlowerFarmTypeTag dafSeed = new FlowerFarmTypeTag();
		dafSeed.tag = 100;
		dafSeed.typeID = daffodil.fatherID;
		daffodil.Descendents.add(dafSeed);
		
		FlowerFarmTypeTag dafDirt = new FlowerFarmTypeTag();
		dafDirt.tag = 4;
		dafDirt.typeID = 3;
		daffodil.Soil.add(dafDirt);
		
		FlowerFarmTypeTag dafGrass = new FlowerFarmTypeTag();
		dafGrass.tag = 3;
		dafGrass.typeID = 2;
		daffodil.Soil.add(dafGrass);
		
		FlowerFarmTypeTag dafWaterSrc = new FlowerFarmTypeTag();
		dafWaterSrc.tag = 4;
		dafWaterSrc.typeID = 8;
		daffodil.Nutrients.add(dafWaterSrc);
		
		FlowerFarmTypeTag dafWaterRun = new FlowerFarmTypeTag();
		dafWaterRun.tag = 3;
		dafWaterRun.typeID = 9;
		daffodil.Nutrients.add(dafWaterRun);
		
		daffodil.fatherIdx = FlowerFarm.fathers.size();
		FlowerFarm.fathers.add(daffodil);
		
		//default B: Rose
		FlowerFarmFather rose = new FlowerFarmFather();
		rose.fatherID = 38;
		
		FlowerFarmTypeTag roseSeed = new FlowerFarmTypeTag();
		roseSeed.tag = 100;
		roseSeed.typeID = rose.fatherID;
		rose.Descendents.add(roseSeed);
		
		FlowerFarmTypeTag roseDirt = new FlowerFarmTypeTag();
		roseDirt.tag = 4;
		roseDirt.typeID = 3;
		rose.Soil.add(roseDirt);
		
		FlowerFarmTypeTag roseGrass = new FlowerFarmTypeTag();
		roseGrass.tag = 3;
		roseGrass.typeID = 2;
		rose.Soil.add(roseGrass);
		
		FlowerFarmTypeTag roseWaterSrc = new FlowerFarmTypeTag();
		roseWaterSrc.tag = 4;
		roseWaterSrc.typeID = 8;
		rose.Nutrients.add(roseWaterSrc);
		
		FlowerFarmTypeTag roseWaterRun = new FlowerFarmTypeTag();
		roseWaterRun.tag = 3;
		roseWaterRun.typeID = 9;
		rose.Nutrients.add(roseWaterRun);

		rose.fatherIdx = FlowerFarm.fathers.size();
		FlowerFarm.fathers.add(rose);
		
		FlowerFarmFather pumpkin = new FlowerFarmFather();
		pumpkin.fatherID = 86;
		
		FlowerFarmTypeTag pumpSeed = new FlowerFarmTypeTag();
		pumpSeed.typeID = 86;
		pumpSeed.tag = 90;
		pumpkin.Descendents.add(pumpSeed);
		
		FlowerFarmTypeTag jackSeed = new FlowerFarmTypeTag();
		jackSeed.typeID = 91;
		jackSeed.tag = 10;
		pumpkin.Descendents.add(jackSeed);
		
		FlowerFarmTypeTag pumpGrass = new FlowerFarmTypeTag();
		pumpGrass.typeID = 2;
		pumpGrass.tag = 4;
		pumpkin.Soil.add(pumpGrass);
		
		FlowerFarmTypeTag pumpDirt = new FlowerFarmTypeTag();
		pumpDirt.typeID = 3;
		pumpDirt.tag = 3;
		pumpkin.Soil.add(pumpDirt);
		
		FlowerFarmTypeTag pumpWaterSrc = new FlowerFarmTypeTag();
		pumpWaterSrc.typeID = 8;
		pumpWaterSrc.tag = 4;
		pumpkin.Nutrients.add(pumpWaterSrc);
		
		FlowerFarmTypeTag pumpWaterRun = new FlowerFarmTypeTag();
		pumpWaterRun.tag = 3;
		pumpWaterRun.typeID = 9;
		pumpkin.Nutrients.add(pumpWaterRun);
		
		pumpkin.fatherIdx = FlowerFarm.fathers.size();
		FlowerFarm.fathers.add(pumpkin);
		
		FlowerFarmFather brnMush = new FlowerFarmFather();
		brnMush.fatherID = 39;
		
		FlowerFarmTypeTag brnSeed = new FlowerFarmTypeTag();
		brnSeed.tag = 100;
		brnSeed.typeID = brnMush.fatherID;
		brnMush.Descendents.add(brnSeed);
		
		FlowerFarmTypeTag brnDirt = new FlowerFarmTypeTag();
		brnDirt.tag = 4;
		brnDirt.typeID = 3;
		brnMush.Soil.add(brnDirt);
		
		FlowerFarmTypeTag brnGrass = new FlowerFarmTypeTag();
		brnGrass.tag = 3;
		brnGrass.typeID = 2;
		brnMush.Soil.add(brnGrass);
		
		FlowerFarmTypeTag brnWaterSrc = new FlowerFarmTypeTag();
		brnWaterSrc.tag = 4;
		brnWaterSrc.typeID = 8;
		brnMush.Nutrients.add(brnWaterSrc);
		
		FlowerFarmTypeTag brnWaterRun = new FlowerFarmTypeTag();
		brnWaterRun.tag = 3;
		brnWaterRun.typeID = 9;
		brnMush.Nutrients.add(brnWaterRun);

		brnMush.fatherIdx = FlowerFarm.fathers.size();
		FlowerFarm.fathers.add(brnMush);
		
		FlowerFarmFather rMush = new FlowerFarmFather();
		rMush.fatherID = 40;
		
		FlowerFarmTypeTag rSeed = new FlowerFarmTypeTag();
		rSeed.tag = 100;
		rSeed.typeID = rMush.fatherID;
		rMush.Descendents.add(rSeed);
		
		FlowerFarmTypeTag rDirt = new FlowerFarmTypeTag();
		rDirt.tag = 4;
		rDirt.typeID = 3;
		rMush.Soil.add(rDirt);
		
		FlowerFarmTypeTag rGrass = new FlowerFarmTypeTag();
		rGrass.tag = 3;
		rGrass.typeID = 2;
		rMush.Soil.add(rGrass);
		
		FlowerFarmTypeTag rWaterSrc = new FlowerFarmTypeTag();
		rWaterSrc.tag = 4;
		rWaterSrc.typeID = 8;
		rMush.Nutrients.add(rWaterSrc);
		
		FlowerFarmTypeTag rWaterRun = new FlowerFarmTypeTag();
		rWaterRun.tag = 3;
		rWaterRun.typeID = 9;
		rMush.Nutrients.add(rWaterRun);

		rMush.fatherIdx = FlowerFarm.fathers.size();
		FlowerFarm.fathers.add(rMush);
		
		FlowerFarm.logIt("Default fathers built.");
		this.saveConfig();
	}
	
	/**
	 * This function takes the current fathers array and prints it out to the cfg file
	 */
	public static void dumpToConfig(){
		try{
			BufferedWriter fWriter = new BufferedWriter(new FileWriter(cfgFile));
			fWriter.write("Hallelujah. Write me");
			fWriter.flush();
			fWriter.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Parses the config file and loads it into the fathers array
	 * @param cfgFile: the cfgFile, should be properly arranged 
	 * @return boolean: whether the file was correctly loaded
	 */
	public boolean loadConfig(File cfgFile, ArrayList<FlowerFarmFather> fathers){
		try{
			BufferedReader cfgReader = new BufferedReader(new FileReader (cfgFile));
			String buffer = getNextLine(cfgReader);
			if (!buffer.contains("father:")){
				throw new IOException();
			}
			int amtFathers = this.getNextInt(cfgReader);
			for (int f = 0; f < amtFathers; f++){
				fathers.add(parseFather(cfgReader));
			}
			cfgReader.close();
			FlowerFarm.logIt("cfgReader closed.");
			return true;
		}
		catch (IOException ex){
			FlowerFarm.log.info("Could not load the config file. Using default settings.");
			buildDefaults();
			return false;
		}		
	}
	
	/**
	 * gets the next line from the given reader. Ignores comment lines and trims whats left
	 * @param bRead: the reader we will be reading from.
	 * @return String: The string representing the line we got from the reader
	 * @throws IOException: This function does use the reader.
	 */
	public String getNextLine(BufferedReader bRead) throws IOException{
		String buffer;
		while ((buffer = bRead.readLine()) != null){
			if((!buffer.contains("#"))&&(buffer.contains(":"))){
				return buffer.trim();
			}
		}
		return "END OF FILE";
	}
	
	/**
	 * Gets the String value of the next line from the reader. Checks for the ':' character
	 * @param bRead: the reader to use when reading the value from
	 * @return String: the string representing whatever was after the ':' char
	 * @throws IOException: See getNextLine
	 */
	public String getNextValue(BufferedReader bRead) throws IOException{
		String buffer;
		buffer = getNextLine(bRead);
		if (buffer == "END OF FILE"){
			throw new IOException();
		}
		else{
			int assignIdx = buffer.indexOf(":");
			return buffer.substring(assignIdx+1);
		}
	}
	
	/**
	 * Gets the int value of the nextline from the reader.
	 * @param bRead: the reader to read from.
	 * @return int: the int we found
	 * @throws IOException: See getNextLine
	 */
	public int getNextInt (BufferedReader bRead) throws IOException{
		String buffer = getNextValue(bRead);
		return Integer.parseInt(buffer.trim());
	}
	
	/**
	 * Parses an individual father from the config file
	 * @param bRead: the reader to read from
	 * @return FlowerFarmFather: the father that we just collected
	 * @throws IOException: See getNextLine
	 */
	public FlowerFarmFather parseFather(BufferedReader bRead) throws IOException{
		FlowerFarmFather curFather = new FlowerFarmFather();
		curFather.fatherIdx = this.getNextInt(bRead);
		curFather.fatherID = this.getNextInt(bRead);
		curFather.millisecondsToSeed = this.getNextInt(bRead) * 1000;
		curFather.seedRadius = this.getNextInt(bRead);
		curFather.reqLum = this.getNextInt(bRead);
		curFather.fertMult = this.getNextInt(bRead);
		if (!getNextLine(bRead).contains("descendents:")){
			throw new IOException();
		}
		int amtDesc = getNextInt(bRead);
		for (int d = 0; d < amtDesc; d++){
			curFather.Descendents.add(parseDescendent(bRead));
		}
		if (!getNextLine(bRead).contains("soil:")){
			throw new IOException();
		}
		int amtSoil = getNextInt(bRead);
		for (int s = 0; s < amtSoil; s++){
			curFather.Soil.add(parseSoil(bRead));
		}
		if (!getNextLine(bRead).contains("nutrients:")){
			throw new IOException();
		}
		int amtNutri = getNextInt(bRead);
		for (int n = 0; n < amtNutri; n++){
			curFather.Nutrients.add(parseNutrient(bRead));
		}
		return curFather;		
	}
	
	/**
	 * parses each descendent from the config file
	 * @param bRead: the reader we read from
	 * @return FlowerFarmTypeTag: the typeID of the desc and its prob
	 * @throws IOException: see getNextLine
	 */
	public FlowerFarmTypeTag parseDescendent(BufferedReader bRead) throws IOException{
		FlowerFarmTypeTag curDescendent = new FlowerFarmTypeTag();
		getNextLine(bRead); //gets the Idx
		curDescendent.typeID = getNextInt(bRead);
		getNextLine(bRead); //gets the descendents required luminosity
		curDescendent.tag = getNextInt(bRead);
		return curDescendent;
	}
	
	/**
	 * parses each soil from the config file
	 * @param bRead: the reader we read from
	 * @return FlowerFarmTypeTag: the typeID and soilMult of the soil
	 * @throws IOException: see getNextLine
	 */
	public FlowerFarmTypeTag parseSoil (BufferedReader bRead) throws IOException{
		FlowerFarmTypeTag curSoil = new FlowerFarmTypeTag();
		getNextLine(bRead); //gets the idx
		curSoil.typeID = getNextInt(bRead);
		curSoil.tag = getNextInt(bRead);
		return curSoil;
	}
	
	/**
	 * parses each Nutrient from the config file
	 * @param bRead: the reader we read from
	 * @return FlowerFarmTypeTag: the typeID and NutrientMult of the nutrient
	 * @throws IOException: see getNextLine
	 */
	public FlowerFarmTypeTag parseNutrient(BufferedReader bRead) throws IOException{
		FlowerFarmTypeTag curNutrient = new FlowerFarmTypeTag();
		getNextLine(bRead); //gets the Idx
		curNutrient.typeID = getNextInt(bRead);
		curNutrient.tag = getNextInt(bRead);
		return curNutrient;
	}
	
	/**
	 * Loads the DB file into the server
	 * @param DBFile: The file that has the Database info in it
	 * @param timers: the arrayList of timers that we are populating
	 * @return boolean: did we successfully load information into the timers array
	 */
	public boolean loadDB(File DBFile, ArrayList<FlowerGrowthTimerTask> timers){
		try{
			BufferedReader DBReader = new BufferedReader(new FileReader (DBFile));
			String curLine;
			while ((curLine = this.getNextLine(DBReader))!="END OF FILE"){
				FlowerFarm.logIt("Loading DB: got line - " + curLine);
				FlowerGrowthTimerTask curTask = parseDBEntry(curLine);
				if (curTask.timerIdx > -1){
					timers.add(parseDBEntry(curLine));
				}
			}
			DBReader.close();
			return true;
		} catch (IOException ex) {
			FlowerFarm.log.info("Could not load DB. Starting with an empty DB.);");
			return false;
		}
	}
	
	/**
	 * Converts a line of the Database into a FlowerGrowthTimerTask
	 * @param curLine: the string to parse into a timer task
	 * @return FlowerGrowthTimerTask: an occurance of a father
	 */
	public FlowerGrowthTimerTask parseDBEntry(String curLine){
		String world;
		String x;
		String y;
		String z;
		String[] buffer = curLine.split(":");
		FlowerFarm.logIt(buffer[0]);
		FlowerFarm.logIt(buffer[1]);
		FlowerFarm.logIt(buffer[2]);
		FlowerFarm.logIt(buffer[3]);
		world = buffer[0];
		x = buffer[1];
		y = buffer[2];
		z = buffer[3];
		FlowerFarm.logIt("'" + world + "' '" + x + "' '" + y + "' '" + z + "'");
		int intX = Integer.parseInt(x);
		int intY = Integer.parseInt(y);
		int intZ = Integer.parseInt(z);
		int delay = FlowerFarm.generator.nextInt(10) * 200;
		FlowerFarm.logIt(world + "- " + intX + " " + intY + " " + intZ + " " + delay);
		return new FlowerGrowthTimerTask(world, intX, intY, intZ, delay);
	}
	
	/**
	 * Saves the database to the DBFile
	 * @return boolean: whether the file was saved correctly;
	 */
	public boolean saveDB(){
		try {
			BufferedWriter DBWriter = new BufferedWriter( new FileWriter ( DBFile ));
			String buffer;
			int idx = 0;
			while (idx < FlowerFarm.timers.size()){
				buffer = new String();
				buffer = buffer + FlowerFarm.timers.get(idx).world + ":" + FlowerFarm.timers.get(idx).blkx + ":";
				buffer = buffer + FlowerFarm.timers.get(idx).blky + ":" + FlowerFarm.timers.get(idx).blkz;
				line(buffer, DBWriter);
				idx++;
			}
			DBWriter.flush();
			DBWriter.close();
			FlowerFarm.logIt("Database saved.");
			return true;
		} catch (IOException ex){
			FlowerFarm.pLog("UNABLE TO SAVE DB!!!!");
			return false;
		}
	}
	
	/**
	 * Saves the current father configurations to the config file
	 * @return boolean: whether the save was successful or not
	 */
	public boolean saveConfig(){
		try{
			BufferedWriter cfgWriter = new BufferedWriter ( new FileWriter (cfgFile));
			FlowerFarmFather curFather;
			FlowerFarmTypeTag curTT;
			line("father:", cfgWriter);
			cfgWriter.newLine();
			line(" amtFather: " + FlowerFarm.fathers.size(), cfgWriter);
			int f;
			for (f = 0; f < FlowerFarm.fathers.size(); f++){
				cfgWriter.newLine();
				curFather = FlowerFarm.fathers.get(f);
				line(" fatherIdx: " + f, cfgWriter);
				line("  fatherID: " + curFather.fatherID, cfgWriter);
				line("  secondsToSeed: " + (int)(curFather.millisecondsToSeed/1000), cfgWriter);
				line("  seedRadius: " + curFather.seedRadius, cfgWriter);
				line("  reqLum: " + curFather.reqLum, cfgWriter);
				line("  fertMult: " + curFather.fertMult, cfgWriter);
				cfgWriter.newLine();
				line("  descendents: ", cfgWriter);
				line("   amtDesc: " + curFather.Descendents.size(), cfgWriter);
				int d;
				for (d = 0; d < curFather.Descendents.size(); d++){
					cfgWriter.newLine();
					curTT = curFather.Descendents.get(d);
					line("   descIdx: " + d, cfgWriter);
					line("    descID: " + curTT.typeID, cfgWriter);
					line("    descLum: " + "6", cfgWriter);
					line("    descProb: " + curTT.tag, cfgWriter);
				}
				cfgWriter.newLine();
				line("  soil: ", cfgWriter);
				line("   amtSoil: " + curFather.Soil.size(), cfgWriter);
				int s;
				for (s = 0; s < curFather.Soil.size(); s++){
					cfgWriter.newLine();
					curTT = curFather.Soil.get(s);
					line("   soilIdx: " + s, cfgWriter);
					line("    soilID: " + curTT.typeID, cfgWriter);
					line("    soilMult: " + curTT.tag, cfgWriter);
				}
				cfgWriter.newLine();
				line("  nutrients: ", cfgWriter);
				line("   amtNutrients: " + curFather.Nutrients.size(), cfgWriter);
				int n;
				for (n = 0; n < curFather.Nutrients.size(); n++){
					cfgWriter.newLine();
					curTT = curFather.Nutrients.get(n);
					line("   nutriIdx: " + n, cfgWriter);
					line("    nutriID: " + curTT.typeID, cfgWriter);
					line("    nutriMult: " + curTT.tag, cfgWriter);
				}
			}
			cfgWriter.flush();
			cfgWriter.close();
			FlowerFarm.logIt("Config file written.");
			return true;
		} catch (IOException ex) {
			FlowerFarm.pLog("Could not save the Config file !!!!");
			return false;
		}
	}
	
	/**
	 * Simply writes the string and moves to a new line
	 * @param str: the string to be written
	 * @param bw: the writer to write to
	 * @return boolean: true. period.
	 * @throws IOException: may have IOException if something goes wrong. :/
	 */
	public boolean line(String str, BufferedWriter bw) throws IOException{
		bw.write(str);
		bw.newLine();
		return true;
	}
	@SuppressWarnings("static-access")
	public String ID2String(int typeID){
		return FlowerFarm.mat.getMaterial(typeID).name();
	}
	@SuppressWarnings("static-access")
	public int String2ID(String matName){
		FlowerFarm.logIt(matName);
		return FlowerFarm.mat.getMaterial(matName).getId();
	}
}