package inventory;

import entities.Entity;

public class Item {

	private Entity entity;

	private int x;
	private int y;

	private final boolean deleteOnUse;

	public Item(final int x, final int y, final boolean deleteOnUse) {
		this.x = x;
		this.y = y;
		this.deleteOnUse = deleteOnUse;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(final Entity entity) {
		this.entity = entity;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public boolean use() {
		return deleteOnUse;
	}

}
