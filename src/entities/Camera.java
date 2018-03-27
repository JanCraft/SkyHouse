package entities;

import javax.swing.JOptionPane;

import org.joml.Intersectionf;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import inventory.Inventory;
import inventory.Item;

public class Camera {

	private Vector3f position;
	
	private float pitch;
	private float yaw;
	private float roll;

	public Vector3f previous;

	private Inventory inv;

	private float speed = 0.02f;
	private float jumpPower = 1.5f;

	//public boolean isGrounded = false;
	
	public Matrix4f viewMatrix;

	public Camera(final float speed) {
		position = new Vector3f(0, 0, 0);
		this.speed = speed;
		inv = new Inventory();
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

		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX();
			float mouseDY = -Mouse.getDY();
			yaw += mouseDX * 0.5f;
			pitch += mouseDY * 0.5f;
		}

		 if (Mouse.isButtonDown(0)) {
			int block = Entity.getEntityInColumn(this, MainGameLoop.getEntities());
		 	if (MainGameLoop.getEntities().get(block) != null) {
		 		Item item = new Item(0, 0, false);
		 		item.setEntity(MainGameLoop.getEntities().get(block));
		 		inv.items[0] = item;
		 		MainGameLoop.getEntities().get(block).active = false;
		 	}
		 }

		if (pitch > 90) {
			pitch = 90;
		}
		if (pitch < -90) {
			pitch = -90;
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

	public Inventory getInv() {
		return inv;
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

}
