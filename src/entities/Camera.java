package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import toolbox.Maths;

public class Camera {

	private Vector3f position;
	
	private float pitch;
	private float yaw;
	private float roll;

	public Vector3f previous;

	private Entity selected;
	
	private int selectInt = 0;

	private float speed = 0.02f;
	private float jumpPower = 1.5f;
	
	private float maxAttribs = 20f;
	
	private float health = 20f;
	private float hunger = 20f;
	
	public Vector3f respawn;

	public Camera(final float speed) {
		position = new Vector3f(0, 0, 0);
		respawn = position;
		this.speed = speed;
	}

	public void move() {
		previous = position;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float xdif = (float) (Math.sin(Math.toRadians(yaw)) * speed);
			float zdif = (float) Math.cos(Math.toRadians(yaw)) * speed;
			
			if(!Entity.isColliding(vec3(xdif, -1f, zdif))) {
				position.x += xdif;
				position.z -= zdif;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float xdif = (float) (Math.sin(Math.toRadians(yaw)) * speed);
			float zdif = (float) Math.cos(Math.toRadians(yaw)) * speed;
			
			if(!Entity.isColliding(vec3(xdif, -1f, zdif))) {
				position.x -= xdif;
				position.z += zdif;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float xdif = (float) Math.sin(Math.toRadians(yaw - 90)) * speed;
			float zdif = (float) Math.cos(Math.toRadians(yaw - 90)) * speed;
			
			if(!Entity.isColliding(vec3(xdif, -1f, zdif))) {
				position.x += xdif;
				position.z -= zdif;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float xdif = (float) Math.sin(Math.toRadians(yaw + 90)) * speed;
			float zdif = (float) Math.cos(Math.toRadians(yaw + 90)) * speed;

			if(!Entity.isColliding(vec3(xdif, -1f, zdif))) {
				position.x += xdif;
				position.z -= zdif;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (true) {
				position.y += getJumpPower();
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (true) {
				position.y -= speed;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			health--;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			health++;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			hunger--;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			hunger++;
		}

		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX();
			float mouseDY = -Mouse.getDY();
			yaw += mouseDX * 0.5f;
			pitch += mouseDY * 0.5f;
		}

		 if (Mouse.isButtonDown(0)) {
			 for(Entity ent : MainGameLoop.entities) {
				 if(Maths.posEqualsInt(ent.getPosition(), position)) {
					 ent.setDead();
				 }
			 }
		 }
		 
		 if(Mouse.isButtonDown(1)) {
			 if(selected != null) {
				 boolean valid = true;
				 for(Entity entit : MainGameLoop.getEntities()) {
					 if(Maths.posEqualsInt(entit.getPosition(), position)) {
						 valid = false;
					 }
				 }
				 if(valid) {
					 Entity ent = new Entity(selected.getModel(), Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne());
					 ent.setPosition(Maths.vec3ToInt(position));
					 ent.type = selected.type;
					 MainGameLoop.entities.add(ent);
				 }
			 }
		 }
		 
		 if(Mouse.getDWheel() < 0) {
			 selectInt--;
		 } else if(Mouse.getDWheel() > 0) {
			 selectInt++;
		 }
		 
		 if(selectInt < 0) {
			 selectInt = GameRegistry.entits.size() - 1;
		 }
		 
		 if(selectInt >= GameRegistry.entits.size()) {
			 selectInt = 0;
		 }
		 
		 selected = GameRegistry.entits.get(selectInt);

		 if (pitch > 90) {
			 pitch = 90;
		 }
		 if (pitch < -90) {
			 pitch = -90;
		 }
		 
		 if(health > maxAttribs) {
			 health = maxAttribs;
		 }
		 
		 if(health <= 0) {
			 setPosition(respawn.x, respawn.y, respawn.z);
			 health = maxAttribs;
			 hunger = maxAttribs;
		 }
		 
		 if(hunger > maxAttribs) {
			 hunger = maxAttribs;
		 }
		 
		 if(hunger <= 0) {
			 health -= 0.01f;
			 hunger = 0;
		 }
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3f(x, y, z);
	}

	public void setPositionWithDifference(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
	
	private Vector3f vec3(float x, float y, float z) {
		return new Vector3f(position.x + x, position.y + y, position.z + z);
	}

	public float getJumpPower() {
		return jumpPower;
	}

	public void setJumpPower(float jumpPower) {
		this.jumpPower = jumpPower;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getHunger() {
		return hunger;
	}

	public void setHunger(float hunger) {
		this.hunger = hunger;
	}

}
