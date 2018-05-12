package capabilities;

public interface ICapabilities {
	public ICapability[] capabilities = new ICapability[10];
	
	public ICapability changeCapability(int capability, int index, Object value);
}
