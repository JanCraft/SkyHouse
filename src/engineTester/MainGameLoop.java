package engineTester;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import coding.Phoskel;
import coding.PhoskelData;
import entities.Camera;
import entities.Entity;
import entities.Light;
import generation.Dimension;
import generation.WorldGenerator;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.GameAsset;
import toolbox.RunnableAsset;

public class MainGameLoop {

	private volatile static List<Entity> entities = new ArrayList<Entity>();

	public static final int DIRT_HEIGHT = 3;
	public static final int STONE_HEIGHT = 5;
	public static final int RENDER_DISTANCE = 30;

	private static int WORLD_WIDTH = 100;
	private static int WORLD_HEIGHT = 100;

	public static int renderingModels = 0;

	public static final int NORMAL_RENDERING_MODELS = 31329;

	private static String worldName = "world";
	
	public static String resourceFolder = "res";
	
	public static List<GameAsset> assets;

	public static int startDimensionID = -1;

	public synchronized static void main(String[] args) {
		assets = new ArrayList<GameAsset>();
		try {
			compileAssets();
		} catch (IOException e) {
			System.err.println(System.nanoTime()+ " [AssetManager] Loading Assets failed.");
		} finally {
			System.out.println(System.nanoTime()+ " [AssetManager] Asset Loading finished.");
		}
		
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		System.out.println(System.nanoTime()+ " [Loader] Loading Loader success.");
		System.out.println(System.nanoTime()+ " [Shaders] Loading Shaders success.");
		System.out.println(System.nanoTime()+ " [Renderer] Loading Renderer success.");

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
		
		System.out.println(System.nanoTime()+ " [Texture] Texture loading -> All success.");

		/* WORLD GENERATION */

		TexturedModel[] models = new TexturedModel[3];
		models[0] = grass;
		models[1] = dirt;

		TexturedModel[] oreModels = { stone, stone, stone, coal, iron };

		int MOUNTAIN = 3;
		
		Dimension overworld;

		if (!MainGameLoop.deserializeEntities(entities, models, oreModels)) {
			WorldGenerator gen = new WorldGenerator(models, oreModels);
			gen.setHeightmap(gen.generateHeightMap(0, 15, WORLD_WIDTH, WORLD_HEIGHT));
			MOUNTAIN = gen.getMountainTop();
			overworld = new Dimension(gen);
		} else {
			overworld = new Dimension(entities);
		}
		
		if(startDimensionID == -1) {
			startDimensionID = overworld.ID;
		}
		
		Dimension.changeDimension(startDimensionID);
		
		System.out.println(System.nanoTime()+ " [WorldManager] World Managing success.");
		
		/* CAMERA THINGS... */

		Light light = new Light(new Vector3f(0, 20, -20), new Vector3f(1, 1, 1));

		Camera camera = new Camera(0.25f);
		//GravityAffected gravity = new GravityAffected();
		//gravity.gravityEffect = 0.025f;
		camera.setPosition(WORLD_WIDTH / 2, MOUNTAIN + 1, WORLD_HEIGHT / 2);
		camera.setJumpPower(0.5f);
		Mouse.setGrabbed(true);
		
		System.out.println(System.nanoTime()+ " [Camera] Loading Camera success.");

		/* MAIN BUCLE */

		while (!Display.isCloseRequested()) {
			camera.move();
			//gravity.update(camera);

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
								&& camera.getPosition().z - entity.getPosition().z > -RENDER_DISTANCE)
						&& entity.isVisible(camera)) {
					generate = true;
				}

				if (generate) {
					entity.update();
					renderer.render(entity, shader);
					renderingModels++;
				}

			}

			/* STOP ALL */
			shader.stop();
			Display.setTitle("SkyHouse - " + renderingModels);
			DisplayManager.updateDisplay();

			renderingModels = 0;

		}

		/* APPLICATION EXIT */

		MainGameLoop.serializeEntities(entities);

		shader.CleanUp();
		loader.CleanUp();
		DisplayManager.closeDisplay();

	}

	public synchronized static List<Entity> getEntities() {
		return entities;
	}

	private synchronized static void serializeEntities(List<Entity> entities) {
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

	private synchronized static boolean deserializeEntities(List<Entity> entities, TexturedModel[] models,
			TexturedModel[] oreModels) {
		if (!new File("C:/SkyHouse/" + worldName + ".psk").exists()) {
			System.err.println("Error Loading World: Not Exists");
			return false;
		}

		boolean gen = false;

		try {
			PhoskelData data = Phoskel.pskDecodificatePhoskel("C:/SkyHouse/" + worldName);
			for (int i = 0; i < data.getData().length; i++) {
				String[] tag = Phoskel.pskDecodificateTag(data.getData()[i]);
				if (tag[0] == tag[0]) {
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

					Entity entity = null;

					if (modelType == 0) {
						entity = new Entity(models[type], pos, rot, siz);
					} else if (modelType == 1) {
						entity = new Entity(oreModels[type], pos, rot, siz);
					}
					entity.type = type;

					entities.add(entity);
					gen = true;
				}
			}
			return gen;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void compileAssets() throws IOException {
		GameAsset currentAsset;
		
		currentAsset = GameAsset.getFileAsset("C:/SkyHouse/asset").addParam("ASSET_END,0");
		MainGameLoop.assets.add(currentAsset);
		
		currentAsset = GameAsset.getFileAsset("C:/SkyHouse/asmpx").addParam("ASSET_END,0");
		MainGameLoop.assets.add(currentAsset);
		
		currentAsset = GameAsset.getFileAsset("C:/SkyHouse/awympx").addParam("ASSET_END,0");
		MainGameLoop.assets.add(currentAsset);
		
		currentAsset = GameAsset.getFileAsset("C:/SkyHouse/kliof").addParam("ASSET_END,0");
		MainGameLoop.assets.add(currentAsset);
	}

	public static void addEntities(List<Entity> entities2) {
		entities.addAll(entities2);
	}

}
