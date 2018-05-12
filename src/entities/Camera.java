package entities;

import javax.swing.JOptionPane;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import capabilities.ForcedCapabilities;
import commands.IChat;
import engineTester.MainGameLoop;
import events.BlockChangeEvent;
import events.EntityDeathEvent;
import events.Listener;
import menus.Inventory;
import menus.Menu;
import toolbox.Maths;
import toolbox.Side;
import toolbox.SideOnly;

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
	
	private Inventory inv = new Inventory();
	
	public ForcedCapabilities capabilities;
	
	private int[] itemCount;
	
	private GravityAffected gravity;
	
	@SideOnly(Side.SERVER)
	private IChat chat;
	
	public Camera(final float speed, final IChat chat) {
		position = new Vector3f(0, 0, 0);
		respawn = position;
		this.speed = speed;
		//this.gravity = new GravityAffected(this, 0.6f);
		this.gravity = new GravityAffected(this, 0f);
		
		capabilities = new ForcedCapabilities();
		
		this.chat = chat;
		
		itemCount = new int[GameRegistry.entits.size()];
		
		for(int i = 0; i < itemCount.length; i++) {
			itemCount[i] = 0;
		}
	}

	public void move() {
		gravity.update();

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
			if((boolean) this.capabilities.getCapabilityData(0, 1)) {
				position.y -= speed;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
			Menu.close(this, inv);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			Menu.OpenClose(this, inv);
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
			 for(Entity ent : MainGameLoop.instance.entities) {
				 if(Maths.posEqualsInt(ent.getPosition(), position)) {
					 if((int) this.capabilities.getCapabilityData(0, 0) == 0) {
						 this.setItemTypeCount(selected.type, this.getItemTypeCount(selected.type) + 1);
					 }
					 
					 ent.setDead();
					 for(Listener listener : MainGameLoop.instance.getListeners()) {
						 listener.onBlockBreakEvent(new BlockChangeEvent(ent, "breaked"));
					 }
				 }
			 }
		 }
		 
		 if(Mouse.isButtonDown(1)) {
			 if(selected != null) {
				 boolean valid = true;
				 for(Entity entit : MainGameLoop.instance.getEntities()) {
					 if(Maths.posEqualsInt(entit.getPosition(), position)) {
						 valid = false;
					 }
				 }
				 
				 if((int) this.capabilities.getCapabilityData(0, 0) == 0 && valid) {
					 if(this.getItemTypeCount(selected.type) <= 0) {
						 valid = false;
					 }
				 }
				 
				 if(valid) {
					 Entity ent = new Entity(selected.getModel(), Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne());
					 ent.setPosition(Maths.vec3ToInt(position));
					 ent.type = selected.type;
					 MainGameLoop.instance.entities.add(ent);
					 this.setItemTypeCount(selected.type, this.getItemTypeCount(selected.type) - 1);
					 for(Listener listener : MainGameLoop.instance.getListeners()) {
						 listener.onBlockPlaceEvent(new BlockChangeEvent(ent, "placed"));
					 }
				 }
			 }
		 }
		 
		 previous = position;
		 
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
			 for(Listener listener : MainGameLoop.instance.getListeners()) {
				 listener.onEntityDeathEvent(new EntityDeathEvent(this, "entity"));
			 }
		 }
		 
		 if(hunger > maxAttribs) {
			 hunger = maxAttribs;
		 }
		 
		 if(hunger <= 0) {
			 health -= 0.01f;
			 hunger = 0;
		 }
		 
		 if(Keyboard.isKeyDown(chat.getOpeningKey()) && !chat.pressed) {
			 chat.pressed = true;
			 String command = JOptionPane.showInputDialog(null, "Command:", "Debug Console", JOptionPane.QUESTION_MESSAGE);
			 if(command != null && command != "" && !command.isEmpty()) {
				 boolean correct = chat.executeCommand(command, this);
				 if(!correct) {
					 JOptionPane.showMessageDialog(null, chat.getErrorMessage(), "Debug Console", JOptionPane.ERROR_MESSAGE);
				 }
			 }
			 return;
		}
		
		chat.pressed = Keyboard.isKeyDown(chat.getOpeningKey());
	}

	private int getItemTypeCount(int type) {
		return itemCount[GameRegistry.getIndexFromType(type)];
	}
	
	private void setItemTypeCount(int type, int count) {
		itemCount[GameRegistry.getIndexFromType(type)] = count;
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
