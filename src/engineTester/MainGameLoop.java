package engineTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import coding.Phoskel;
import coding.PhoskelData;
import commands.CommonChat;
import commands.IChat;
import entities.Camera;
import entities.Entity;
import entities.GameRegistry;
import entities.Light;
import events.Listener;
import events.ModLoadingEvent;
import generation.Dimension;
import generation.WorldGenerator;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Instance;
//import toolbox.JarLoader;
import toolbox.Maths;
import toolbox.Proxy;
import toolbox.Side;
import toolbox.SideOnly;
import toolbox.SidedProxy;

/**
 * The main class of SkyHouse
 * 
 * @author Jan
 * @since Every version of SkyHouse
 * @version 1.6.2
 * @see org.lwjgl.opengl.Display
 */
public class MainGameLoop {

	public volatile List<Entity> entities = new ArrayList<Entity>();

	public final int DIRT_HEIGHT = 2;
	public final int STONE_HEIGHT = 2;
	public final int RENDER_DISTANCE = 30;

	private int WORLD_WIDTH = 15;
	private int WORLD_HEIGHT = 15;

	public int renderingModels = 0;

	private String worldName = "world";

	public String resourceFolder = "C:/SkyHouse/res";

	public int startDimensionID = -1;
	
	private List<Listener> listeners = new ArrayList<Listener>();

	public List<Entity> registerNewEntities = new ArrayList<Entity>();
	
	@SidedProxy(client = Proxy.class, server = Proxy.class)
	public Proxy proxy;
	
	@SideOnly(Side.SERVER)
	public IChat chat;
	
	@Instance(MainGameLoop.class)
	public static MainGameLoop instance;
	
	public void registerListener(Listener listener) {
		listeners.add(listener);
	}
	
	public List<Listener> getListeners() {
		return listeners;
	}
	
	public void startGameLoop() {
		List<PhoskelData> mods = new ArrayList<PhoskelData>();
		try {
			compileAssets(mods);
		} catch (IOException e) {
			System.err.println("[AssetManager] Loading Assets failed.");
		}

		System.out.println("[AssetManager] Asset Loading finished.");

		DisplayManager.createDisplay();

		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		System.out.println("[Loader] Loading Loader success.");
		System.out.println("[Shaders] Loading Shaders success.");
		System.out.println("[Renderer] Loading Renderer success.");

		float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f

		};

		float[] textureCoords = {

				0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0,
				1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0

		};

		int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19,
				19, 17, 18, 20, 21, 23, 23, 21, 22

		};

		RawModel cubeModel = loader.loadToVAO(vertices, textureCoords, indices);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("spr_grass"));
		ModelTexture texture2 = new ModelTexture(loader.loadTexture("spr_dirt"));
		ModelTexture texture3 = new ModelTexture(loader.loadTexture("spr_stone"));
		ModelTexture texture4 = new ModelTexture(loader.loadTexture("spr_coal"));
		ModelTexture texture5 = new ModelTexture(loader.loadTexture("spr_iron"));
		
		TexturedModel grass = new TexturedModel(cubeModel, texture);
		TexturedModel dirt = new TexturedModel(cubeModel, texture2);
		TexturedModel stone = new TexturedModel(cubeModel, texture3);
		TexturedModel coal = new TexturedModel(cubeModel, texture4);
		TexturedModel iron = new TexturedModel(cubeModel, texture5);

		System.out.println("[Texture] Texture loading -> All success.");

		/* WORLD GENERATION */

		TexturedModel[] models = new TexturedModel[3];
		models[0] = grass;
		models[1] = dirt;

		TexturedModel[] oreModels = { stone, stone, stone, coal, iron };

		GameRegistry.registerBlock(new Entity(grass, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(), 0));
		GameRegistry.registerBlock(new Entity(dirt, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(), 1));
		GameRegistry.registerBlock(new Entity(stone, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(), 2));
		GameRegistry.registerBlock(new Entity(coal, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(), 3));
		GameRegistry.registerBlock(new Entity(iron, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(), 4));

		try {
			loadMods(mods, GameRegistry.class, loader, cubeModel);
			for(Listener listener : listeners) {
				listener.onPskModLoadsEvent(new ModLoadingEvent(mods, "mods"));
			}
		} catch (SHException e) {
			e.printStackTrace();
		}
		
		GameRegistry.registerAll(registerNewEntities);
		
		int MOUNTAIN = 3;

		Dimension overworld;

		if (!instance.deserializeEntities(entities, mods, models, oreModels)) {
			WorldGenerator gen = new WorldGenerator(models, oreModels);
			gen.setHeightmap(gen.generateHeightMap(0, 15, WORLD_WIDTH, WORLD_HEIGHT));
			MOUNTAIN = gen.getMountainTop();
			overworld = new Dimension(gen);
		} else {
			overworld = new Dimension(entities);
		}

		if (startDimensionID == -1) {
			startDimensionID = overworld.ID;
		}

		Dimension.changeDimension(startDimensionID);

		System.out.println("[WorldManager] World Managing success.");

		/* CAMERA THINGS... */

		Light light = new Light(new Vector3f(0, 20, -20), new Vector3f(1, 1, 1));
		
		chat = new CommonChat('/', Keyboard.KEY_TAB);
		
		Camera camera = new Camera(0.25f, chat);
		
		camera.setPosition(WORLD_WIDTH / 2, MOUNTAIN + 1, WORLD_HEIGHT / 2);
		camera.setJumpPower(0.5f);
		Mouse.setGrabbed(true);
		
		camera.respawn = new Vector3f(WORLD_WIDTH / 2, MOUNTAIN + 1, WORLD_HEIGHT / 2);

		System.out.println("[Camera] Loading Camera success.");

		/* MAIN BUCLE */

		while (!Display.isCloseRequested()) {
			camera.move();
			// gravity.update(camera);

			renderer.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			for (Entity entity : entities) {
				boolean generate = false;
				if ((camera.getPosition().x - entity.getPosition().x < RENDER_DISTANCE
						&& camera.getPosition().x - entity.getPosition().x > -RENDER_DISTANCE)
						&& (camera.getPosition().y - entity.getPosition().y < RENDER_DISTANCE
								&& camera.getPosition().y - entity.getPosition().y > -RENDER_DISTANCE)
						&& (camera.getPosition().z - entity.getPosition().z < RENDER_DISTANCE
								&& camera.getPosition().z - entity.getPosition().z > -RENDER_DISTANCE)) {
					generate = true;
				}

				if (entity.dead) {
					entities.remove(entity);
					break;
				}

				if (generate) {
					entity.update();
					
					if(entity.isVisible(camera)) {
						renderer.render(entity, shader);
						renderingModels++;
					}
				}

			}

			/* STOP ALL */
			shader.stop();
			Display.setTitle("SkyHouse - " + renderingModels + " - " + camera.getHealth());
			DisplayManager.updateDisplay();

			renderingModels = 0;
		}

		/* APPLICATION EXIT */

		instance.serializeEntities(entities);

		shader.CleanUp();
		loader.CleanUp();
		DisplayManager.closeDisplay();
	}

	public synchronized static void main(String[] args) {
		instance = new MainGameLoop();
		
		System.setProperty("org.lwjgl.librarypath", new File("C:/SkyHouse/libs/natives/").getAbsolutePath());
		
		/*String pathToJar = JOptionPane.showInputDialog(null, "Mods to load:", "Modding Support", JOptionPane.QUESTION_MESSAGE);
		JarLoader jloader = new JarLoader();
		try {
			List<Object> objs = jloader.getClassesFromJar(pathToJar);
			for(Object obj : objs) {
				System.out.println("Mod Loading...");
				String loaded = jloader.executeMod(obj);
				System.out.println("Mod Loaded: " + loaded);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}*/
		
		//START GAME
		instance.startGameLoop();
	}
	
	@SuppressWarnings({ "resource", "unchecked" })
	public List<Class<ModLoader>> getModClasses(String path) throws IOException, ClassNotFoundException {
		JarFile jarFile = new JarFile(path);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + path+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		List<Class<ModLoader>> classes = new ArrayList<Class<ModLoader>>();

		while (e.hasMoreElements()) {
		    JarEntry je = e.nextElement();
		    if(je.isDirectory() || !je.getName().endsWith(".class")){
		        continue;
		    }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    Class<ModLoader> c = (Class<ModLoader>) cl.loadClass(className);
		    if(c != null) {
		    	classes.add(c);
		    }
		}
		
		return classes;
	}

	public synchronized List<Entity> getEntities() {
		return entities;
	}

	private synchronized void serializeEntities(List<Entity> entities) {
		String[] content = new String[entities.size()];
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			content[i] = "BLOCK," + entity.type + "," + entity.getPosition().x + "," + entity.getPosition().y + ","
					+ entity.getPosition().z;
		}
		PhoskelData data = new PhoskelData();
		data.setData(content);
		try {
			Phoskel.pskCodificatePhoskel("C:/SkyHouse/" + worldName, data);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private synchronized boolean deserializeEntities(List<Entity> entities, List<PhoskelData> mods, TexturedModel[] models,
			TexturedModel[] oreModels) {
		if (!new File("C:/SkyHouse/" + worldName + ".psk").exists()) {
			System.err.println("Error Loading World: Not Exists");
			return false;
		}

		boolean gen = false;
		
		int btax = 0;

		try {
			PhoskelData data = Phoskel.pskDecodificatePhoskel("C:/SkyHouse/" + worldName);
			for (String dta : data.getData()) {
				String[] tag = Phoskel.pskDecodificateTag(dta);
				int type = Integer.parseInt(tag[1]);
				float x = Float.parseFloat(tag[2]);
				float y = Float.parseFloat(tag[3]);
				float z = Float.parseFloat(tag[4]);
				Vector3f pos = new Vector3f(x, y, z);
				Vector3f rot = new Vector3f(0, 0, 0);
				Vector3f siz = new Vector3f(1, 1, 1);

				int modelType = 0;
				if (type >= 2 && type <= 4) {
					modelType = 1;
				}

				if ((modelType == 0 && type >= models.length)) {
					modelType = 2;
				}

				Entity entity = null;

				if (modelType == 0) {
					entity = new Entity(models[type], pos, rot, siz);
				} else if (modelType == 1) {
					entity = new Entity(oreModels[type], pos, rot, siz);
				} else if (modelType == 2) {
					if(mods.isEmpty())
						break;
					
					Entity template = GameRegistry.entits.get(type);
					entity = new Entity(template.getModel(), pos, rot, siz);
				}
				entity.type = type;
				
				entities.add(entity);
				btax++;
				gen = true;
			}
			
			System.out.println(btax);
			
			return gen;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void compileAssets(List<PhoskelData> mods) throws IOException {
		String mds = JOptionPane.showInputDialog(null, "Modeling filenames (Separe with comma)");
		
		if(mds == null || mds == "")
			return;
		
		String[] mdsSpt = mds.split(",");

		for (String file : mdsSpt) {
			mods.add(Phoskel.pskDecodificatePhoskel("C:/SkyHouse/mods/" + file));
		}
	}

	public void loadMods(List<PhoskelData> mods, Class<GameRegistry> gamereg,
			Loader loader, RawModel bmodel) throws SHException {
		
		if(gamereg == null)
			throw new SHException("GameRegistry is null!");
		
		if (mods.isEmpty())
			throw new SHException("Mod List is empty!");

		for (PhoskelData mod : mods) {
			for (String line : mod.getData()) {
				String[] tag = Phoskel.pskDecodificateTag(line);

				if (tag.length < 4 && tag.length != 3)
					throw new SHException("The tag Arguments are incorrect!");
				
				TexturedModel mdtextured;
				
				if(tag.length == 3) {
					RawModel model = bmodel;
					ModelTexture texture = new ModelTexture(loader.loadModTexture(tag[2]));
					mdtextured = new TexturedModel(model, texture);
				} else {
					RawModel model = OBJLoader.loadModObjModel(tag[2], loader);
					ModelTexture texture = new ModelTexture(loader.loadModTexture(tag[3]));
					mdtextured = new TexturedModel(model, texture);
				}
				
				int type = GameRegistry.entits.get(GameRegistry.entits.size() - 1).type + 1;
				
				Entity ent = new Entity(mdtextured, Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne(),
						type);
				GameRegistry.registerOther(ent);
			}
		}
	}
	
	public static RawModel getBlockModel() {
		Loader loader = new Loader();
		float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f

		};

		float[] textureCoords = {

				0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0,
				1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0

		};

		int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19,
				19, 17, 18, 20, 21, 23, 23, 21, 22

		};

		RawModel cubeModel = loader.loadToVAO(vertices, textureCoords, indices);
		
		return cubeModel;
	}

	public void addEntities(List<Entity> entities2) {
		entities.addAll(entities2);
	}

}
