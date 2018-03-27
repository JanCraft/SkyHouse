package entities;

import org.lwjgl.util.vector.Vector3f;

public class BBox {
	
	public float limitX1;
	public float limitX2;
	
	public float limitY1;
	public float limitY2;
	
	public float limitZ1;
	public float limitZ2;
	
	public BBox(final Vector3f size, final Vector3f position) {
		limitX1 = position.x + size.x;
		limitX2 = position.x - size.x;
		limitY1 = position.y + size.y;
		limitY2 = position.y - size.y;
		limitZ1 = position.z + size.z;
		limitZ2 = position.z - size.z;
	}
	
	public static boolean intersects(BBox a, BBox b) {
		if(a.limitX2 >= b.limitX1 && a.limitX1 <= b.limitX2) {
			if(a.limitY2 >= b.limitY1 && a.limitY1 <= b.limitY2) {
				if(a.limitZ2 >= b.limitZ1 && a.limitZ1 <= b.limitZ2) {
					return true;
				}
			}
		}
		
		return false;
	}

}
