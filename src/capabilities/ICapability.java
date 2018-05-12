package capabilities;

public interface ICapability {
	public Object[] data = new Object[10];
	
	public Object getObject(int index);
	
	public Object setObject(int index, Object object);
}
