package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engineTester.Registry;

/**
 * This is the registry of the elements of the game
 * 
 * @author Jan
 * @since SkyHouse 1.1
 * @version 1.2
 * @see engineTester.Registry
 *
 */
public class GameRegistry implements Registry {
	
	public static List<Entity> entits = new ArrayList<Entity>();
	
	public static void registerBlock(final Entity block) {
		if(block.dead)
			return;
		
		block.setPosition(new Vector3f(0, 0, 0));
		
		entits.add(block);
	}
	
	public static void registerItem(final Entity item) {
		if(item.dead)
			return;
		
		item.setPosition(new Vector3f(0, 0, 0));
		
		entits.add(item);
	}
	
	public static void registerOther(final Entity other) {
		if(other.dead)
			return;
		
		other.setPosition(new Vector3f(0, 0, 0));
		
		entits.add(other);
	}

	@Override
	public Exception getThrownException() {
		return null;
	}

	public static void registerAll(List<Entity> entities) {
		for(Entity entity : entities) {
			registerOther(entity);
		}
		
	}

	public static int getIndexFromType(int type) {
		int index = -1;
		for(Entity ent : entits) {
			if(ent.type == type) {
				index = entits.indexOf(ent);
				return index;
			}
		}
		return index;
	}
	
}
