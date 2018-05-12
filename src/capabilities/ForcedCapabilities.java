package capabilities;

public class ForcedCapabilities implements ICapabilities {
	
	/**
	 * The player's game mode.
	 * 
	 * @author Jan
	 */
	public static final int GAME_MODE = 0;
	
	/**
	 * If the player can fly.
	 * 
	 * @author Jan
	 */
	public static final int ALLOW_FLIGHT = 1;

	@Override
	public ICapability changeCapability(int capability, int index, Object value) {
		capabilities[capability].setObject(index, value);
		return capabilities[capability];
	}
	
	public Object getCapabilityData(int capability, int index) {
		return capabilities[capability].getObject(index);
	}
	
	public ForcedCapabilities() {
		for(int i = 0; i < capabilities.length; i++) {
			capabilities[i] = new ForcedCapability();
		}
		
		changeCapability(0, 0, 1);
		changeCapability(0, 1, true);
	}

}
