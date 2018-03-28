package engineTester;

import java.util.ArrayList;
import java.util.List;

public interface Registry {
	
	public static List<Object> entits = new ArrayList<Object>();
	
	public static Exception error = null;
	
	public abstract Exception getThrownException();
	
}
