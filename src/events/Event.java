package events;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {
	
	private List<Object> data = new ArrayList<Object>();
	private List<String> keys = new ArrayList<String>();
	
	public Event(Object firstData, String key) {
		data.add(firstData);
		keys.add(key);
	}
	
	public Object getData(String key) {
		Object ret = null;
		int index = keys.indexOf(key);
		
		if(index != -1) {
			Object rt = data.get(index);
			if(rt != null) {
				ret = rt;
			}
		}
		
		return ret;
	}
	
}
