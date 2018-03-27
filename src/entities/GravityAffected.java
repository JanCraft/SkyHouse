package entities;

import org.lwjgl.util.vector.Vector3f;

public class GravityAffected {

	public float gravityEffect = 0.05f;

	public void update(Entity affected) {
		if (Entity.getDistanceInColumn(affected) > 2f) {
			float gravity = Entity.getDistanceInColumn(affected) * gravityEffect;

			affected.increasePosition(new Vector3f(0, -gravity, 0));
		}
	}

	public void update(Camera affected) {
		if (Entity.getDistanceInColumn(affected) > 2f) {
			float gravity = Entity.getDistanceInColumn(affected) * gravityEffect;

			affected.setPositionWithDifference(0, -gravity, 0);
		} else {
			if(Entity.getDistanceInColumn(affected) < 0.9f) {
				float gravity = Entity.getDistanceInColumn(affected) * gravityEffect;
				
				affected.setPositionWithDifference(0, gravity, 0);
			}
		}
	}

}
