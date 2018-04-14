package events;

import engineTester.MainGameLoop;

public abstract class Listener {
	
	@SuppressWarnings("unused")
	private MainGameLoop loop;
	
	public Listener(MainGameLoop loop) {
		this.loop = loop;
	}
	
	public void onBlockBreakEvent(BlockChangeEvent event) {
		
	}
	
	public void onBlockPlaceEvent(BlockChangeEvent event) {
		
	}
	
	public void onEntityDeathEvent(EntityDeathEvent event) {
		
	}
	
	public void onPskModLoadsEvent(ModLoadingEvent event) {
		
	}
	
}
