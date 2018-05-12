package engineTester;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Registry interface used to register objects
 * 
 * @author Jan
 * @since SkyHouse 1.2
 * @see engineTester.MainGameLoop
 *
 */
public interface Registry {
	
	public static List<Object> entits = new ArrayList<Object>();
	
	public static Exception error = null;
	
	public abstract Exception getThrownException();
	
}
