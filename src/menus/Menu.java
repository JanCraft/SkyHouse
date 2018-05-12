package menus;

import entities.Camera;

public abstract class Menu {
	
	private boolean open = false;
	
	@SuppressWarnings("unused")
	private Camera cam;
	
	public static void OpenClose(Camera cam, Menu menu) {
		menu.open = !menu.open;
		menu.cam = cam;
	}
	
	public static void open(Camera cam, Menu menu) {
		menu.open = true;
		menu.cam = cam;
	}
	
	public static void close(Camera cam, Menu menu) {
		menu.open = false;
		menu.cam = cam;
	}
	
	public boolean show() {
		if(open) {
			
			//CODE...
			
			return true;
		}
		return false;
	}
	
	public boolean isOpen() {
		return open;
	}
}
