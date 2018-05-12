package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import generation.Dimension;
import generation.WorldGenerator;
import models.TexturedModel;

/**
 * ModLoader is a class used to create mods and load it's
 * 
 * @author Jan
 * @since SkyHouse 1.2
 * @version 1.2
 * @see engineTester.Registry
 */
public abstract class ModLoader implements Registry {
	
	public static List<Object> entits = new ArrayList<Object>();
	
	public static Exception error = null;
	
	protected static MainGameLoop main;
	
	@Override
	public String toString() {
		return "SkyHouseModJG8";
	}
	
	public void sta(MainGameLoop mn) {
		main = mn;
		ModLoader master;
		try {
			master = getMasterLoader();
			
			if(master == this) {
				registerLoader(mn).startMod();
			} else {
				master.registerLoader(mn).startMod();
				registerLoader(mn).startMod();
			}
		} catch (ModException | SHException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Exception getThrownException() {
		if(error == null) {
			return null;
		}
		return new SHException(error.getMessage());
	}
	
	/*
	 * Here you need to register the OTHER MODS needed and return this.
	 */
	public abstract ModLoader registerLoader(MainGameLoop main) throws ModException;
	
	/*
	 * Here you need to start and register all your things and return this.
	 */
	public abstract ModLoader startMod() throws SHException;
	
	/*
	 * Here you need to return the mod instance (this) or the mod instance of your master mod (other mod that you use).
	 */
	public abstract ModLoader getMasterLoader() throws ModException;
	
	protected WorldGenerator getWorldGenerator(TexturedModel[] models, TexturedModel[] oreModels) {
		try {
			return new WorldGenerator(models, oreModels);
		} catch (Exception e) {
			error = e;
		}
		return null;
	}
	
	protected void generateDimension(WorldGenerator gen, final int minHeight, final int maxHeight, final int width, final int height, final boolean setToDefault) {
		try {
			gen.generateHeightMap(minHeight, maxHeight, width, height);
			List<Entity> ents = gen.HeightMap2World();
			
			Dimension dim = new Dimension(ents);
			
			if(setToDefault) {
				main.startDimensionID = dim.ID;
			}
		} catch (Exception e) {
			error = e;
		}
	}
	
	protected void registryAdd(final Entity added) {
		try {
			if(added.dead)
				return;
			
			added.setPosition(new Vector3f(0, 0, 0));
			
			main.registerNewEntities.add(added);
		} catch (Exception e) {
			error = e;
		}
	}
	
}
