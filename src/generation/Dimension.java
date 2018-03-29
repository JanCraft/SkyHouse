package generation;

import java.util.ArrayList;
import java.util.List;

import engineTester.MainGameLoop;
import entities.Entity;

public class Dimension {
	
	public static int endID = 0;
	
	public static List<Dimension> dimensions = new ArrayList<Dimension>();
	
	public static Dimension current;
	
	public final int ID;
	
	private List<Entity> entities;
	
	public Dimension(final WorldGenerator generator) {
		this.ID = Dimension.endID;
		Dimension.endID++;
		this.entities = generator.HeightMap2World();
		Dimension.dimensions.add(this);
	}
	
	public Dimension(final List<Entity> entities) {
		this.ID = Dimension.endID;
		Dimension.endID++;
		this.entities = entities;
		Dimension.dimensions.add(this);
	}
	
	public static void changeDimension(final int targetID) {
		if(Dimension.current != null) {
			Dimension.current.entities = MainGameLoop.getEntities();
		}
		
		Dimension.current = Dimension.dimensions.get(targetID);
		
		MainGameLoop.entities = Dimension.dimensions.get(targetID).entities;
	}

}
