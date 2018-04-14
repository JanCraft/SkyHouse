package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import generation.Dimension;
import generation.WorldGenerator;
import models.TexturedModel;

public abstract class ModLoader implements Registry {
	
	public static List<Object> entits = new ArrayList<Object>();
	
	public static Exception error = null;
	
	public Exception getThrownException() {
		if(error == null) {
			return null;
		}
		return new SHException(error.getMessage());
	}
	
	public abstract void registerLoader();
	
	public abstract void startMod();
	
	public abstract ModLoader getMasterLoader();
	
	protected WorldGenerator getWorldGenerator(TexturedModel[] models, TexturedModel[] oreModels) {
		return new WorldGenerator(models, oreModels);
	}
	
	protected void generateDimension(WorldGenerator gen, final int minHeight, final int maxHeight, final int width, final int height, final boolean setToDefault) {
		gen.generateHeightMap(minHeight, maxHeight, width, height);
		List<Entity> ents = gen.HeightMap2World();
		
		Dimension dim = new Dimension(ents);
		
		if(setToDefault) {
			MainGameLoop.startDimensionID = dim.ID;
		}
	}
	
	protected void registryAdd(final Entity added) {
		if(added.dead)
			return;
		
		added.setPosition(new Vector3f(0, 0, 0));
		
		MainGameLoop.registerNewEntities.add(added);
	}
	
}
