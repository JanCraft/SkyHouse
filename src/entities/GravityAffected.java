package entities;

public class GravityAffected {

	public float gravityEffect = 0.05f;
	
	private Camera affected;
	
	public GravityAffected(Camera cam, float effect) {
		this.gravityEffect = effect;
		this.affected = cam;
	}
	
	public GravityAffected(float effect) {
		this.gravityEffect = effect;
	}

	public void updateCam(Camera affected) {
		if((boolean) affected.capabilities.getCapabilityData(0, 1))
			return;
		
		if (Entity.getDistanceInColumn(affected) > 2f) {
			float gravity = Entity.getDistanceInColumn(affected) * gravityEffect;

			affected.setPositionWithDifference(0, -gravity, 0);
		}
	}
	
	public void update() {
		if((boolean) affected.capabilities.getCapabilityData(0, 1))
			return;
		
		if (Entity.getDistanceInColumn(affected) > 2f) {
			float gravity = Entity.getDistanceInColumn(affected) * gravityEffect;

			affected.setPositionWithDifference(0, -gravity, 0);
		}
	}

}
