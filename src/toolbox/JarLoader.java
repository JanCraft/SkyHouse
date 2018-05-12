package toolbox;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import engineTester.MainGameLoop;
import engineTester.ModLoader;

public class JarLoader {
	public List<Object> getClassesFromJar(String pathToJar) throws IOException, ClassNotFoundException {
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		List<Object> objects = new ArrayList<Object>();

		while (e.hasMoreElements()) {
		    JarEntry je = e.nextElement();
		    if(je.isDirectory() || !je.getName().endsWith(".class")){
		        continue;
		    }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    Class<?> c = cl.loadClass(className);
		    objects.add((Object) c);
		}
		jarFile.close();
		return objects;
	}
	
	/**
	 * Executes the mod 'obj' if it is a mod
	 * returns true if executed successfully and false if it isn't a mod or is an error
	 * @param obj The mod to execute
	 * @author Jan
	 * @see engineTester.ModLoader
	 * @since SkyHouse 1.3
	 * @return If the mod is valid
	 **/
	public String executeMod(Object obj) {
		String returns = "Isn't a Mod!";
		try {
			if(obj instanceof ModLoader) {
				((ModLoader) obj).sta(MainGameLoop.instance);
				returns = "Is Mod!";
			}
		} catch (NoSuchMethodError e) {
			returns = "No Such Method Error";
		}
		return returns;
	}
}
