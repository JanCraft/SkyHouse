package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import models.TexturedModel;
import toolbox.Maths;

public class Entity implements Serializable {

	private static final long serialVersionUID = -4508268748903400661L;

	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	public boolean dead;

	public int type;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.dead = false;
	}
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale, int type) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.dead = false;
		this.type = type;
	}

	public void increasePosition(final Vector3f pos) {
		position.x += pos.x;
		position.y += pos.y;
		position.z += pos.z;
	}

	public void increaseRotation(final Vector3f rot) {
		rotation.x += rot.x;
		rotation.y += rot.y;
		rotation.z += rot.z;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getSize() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void update() {
	}

	public boolean isVisible(Camera camera) {
		boolean visible = true;

		if (camera.getPosition().y - position.y > (float) Maths.FloatToInt(MainGameLoop.instance.RENDER_DISTANCE / 4)) {
			visible = false;
		}

		return visible;
	}

	public static float getDistanceInColumn(Camera affected) {
		float distance = 0;

		List<Entity> row = Maths.getEntitiesFromXZ(affected);
		
		if (row.size() > 0) {
			float biggerY = row.get(0).position.y;
			for (Entity entity : row) {
				if (entity.getPosition().y > biggerY && entity.getPosition().y < affected.getPosition().y) {
					biggerY = entity.getPosition().y;
				}
			}
			distance = affected.getPosition().y - biggerY;
		}

		return distance;
	}

	public static boolean isColliding(Vector3f movePos) {
		for(Entity entity : MainGameLoop.instance.getEntities()) {
			Vector3f blockPos = entity.position;
			
			BBox camBBox = new BBox(movePos, new Vector3f(1, 1, 1));
			BBox blockBBox = new BBox(entity.scale, blockPos);
			
			if(BBox.intersects(camBBox, blockBBox)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static int getEntityInColumn(Camera affected, List<Entity> ents) {
		List<Entity> row = new ArrayList<Entity>();
		for (int i = 0; i < ents.size(); i++) {
			if (((int) ents.get(i).position.x == (int) affected.getPosition().x)
					&& ((int) ents.get(i).position.z == (int) affected.getPosition().z)) {
				row.add(ents.get(i));
			}
		}
		int biggerY = 0;
		if (row.size() > 0) {
			for (Entity entity : row) {
				if (entity.getPosition().y > row.get(biggerY).position.y && entity.getPosition().y < affected.getPosition().y) {
					biggerY = row.indexOf(entity);
				}
			}
		}

		return biggerY;
	}

	public void setDead() {
		dead = true;
	}
	
}
