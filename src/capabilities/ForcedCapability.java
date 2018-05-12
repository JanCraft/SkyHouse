package capabilities;

public class ForcedCapability implements ICapability {

	@Override
	public Object getObject(int index) {
		if(index >= data.length || index < 0)
			return null;
		
		return data[index];
	}

	@Override
	public Object setObject(int index, Object object) {
		if(index >= data.length || index < 0)
			return null;
		
		data[index] = object;
		return getObject(index);
	}

}
