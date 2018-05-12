package generation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import entities.Entity;
import models.TexturedModel;
import toolbox.Maths;

public class WorldGenerator {

	private HeightMap heightmap;

	private TexturedModel grass;
	private TexturedModel dirt;
	private TexturedModel[] stones;

	private int CENTER_MOUNTAIN_TOP = 0;
	private int MOUNTAIN_TOP = 0;

	public WorldGenerator(final HeightMap heightmap, final TexturedModel[] models, final TexturedModel[] oreModels) {
		this.heightmap = heightmap;
		this.grass = models[0];
		this.dirt = models[1];
		this.stones = oreModels;
	}

	public WorldGenerator(final TexturedModel[] models, final TexturedModel[] oreModels) {
		this.grass = models[0];
		this.dirt = models[1];
		this.stones = oreModels;
	}

	public HeightMap generateHeightMap(final int minHeight, final int maxHeight, final int width, final int height) {
		if (heightmap == null) {
			heightmap = new HeightMap(width, height);
		}
		HeightMap hg = heightmap;
		HeightMap heightmap = new HeightMap(width, height);
		for (int i = 0; i < hg.getWidth(); i++) {
			for (int ix = 0; ix < hg.getHeight(); ix++) {
				heightmap.setHeight(i, ix, hg.getHeights()[i][ix]);
			}
		}

		int xcenter = width / 2 - 1;
		int zcenter = height / 2 - 1;
		int blankSpaces = width * height;
		int x, z;
		while (blankSpaces > (width * height) / 16) {
			int currentHeight = Maths.randomInt(minHeight,
					maxHeight / (maxHeight / 2) + Maths.randomInt(minHeight, maxHeight / 2) / 128);

			if (CENTER_MOUNTAIN_TOP == 0) {
				x = xcenter;
				z = zcenter;
				CENTER_MOUNTAIN_TOP = currentHeight;
			} else {
				x = Maths.randomInt(0, width - 1);
				z = Maths.randomInt(0, height - 1);
			}
			if (currentHeight > MOUNTAIN_TOP) {
				MOUNTAIN_TOP = currentHeight;
			}

			int bucle = 1;

			heightmap.setHeight(x, z, currentHeight);
			// TOP: currentHeight--;

			while (currentHeight > 0) {
				int minBlankSpaces = 0;
				minBlankSpaces += heightmap.fillHeights(x - bucle, z - bucle, x + bucle, z + bucle, currentHeight);
				minBlankSpaces += heightmap.fillHeights(x + bucle, z + bucle, x - bucle, z - bucle, currentHeight);
				minBlankSpaces += heightmap.fillHeights(x - bucle, z + bucle, x + bucle, z - bucle, currentHeight);
				minBlankSpaces += heightmap.fillHeights(x + bucle, z - bucle, x - bucle, z + bucle, currentHeight);

				bucle++;
				currentHeight--;
				blankSpaces -= minBlankSpaces;
			}
		}

		return heightmap;
	}

	public void printHeightMap() {
		for (int i = 0; i < heightmap.getWidth(); i++) {
			String print = "";
			for (int ix = 0; ix < heightmap.getHeight(); ix++) {
				print += "" + heightmap.getHeights()[i][ix] + " ";
			}
			System.out.println(print);
		}
	}

	public void setHeightmap(HeightMap heightmap) {
		this.heightmap = heightmap;
	}

	public List<Entity> HeightMap2World() {
		List<Entity> entities = new ArrayList<Entity>();

		for (int i = 0; i < heightmap.getWidth(); i++) {
			for (int ix = 0; ix < heightmap.getHeight(); ix++) {
				entities.add(this.generateCorrectBlock(i, ix, 0, 0));
				for (int iz = 1; iz <= MainGameLoop.instance.DIRT_HEIGHT; iz++) {
					entities.add(this.generateCorrectBlock(i, ix, 1, iz));
				}
				for (int izx = 1; izx <= MainGameLoop.instance.STONE_HEIGHT; izx++) {
					entities.add(this.generateCorrectBlock(i, ix, 2, MainGameLoop.instance.DIRT_HEIGHT + izx));
				}
			}
		}

		return entities;
	}

	public Entity generateCorrectBlock(final int x, final int z, final int type, final int modY) {
		float realX = Maths.IntToFloat(x);
		float realZ = Maths.IntToFloat(z);
		int height = heightmap.getHeights()[x][z];

		switch (type) {
		case 0:
			Entity entity = new Entity(grass, new Vector3f(realX, height, realZ), new Vector3f(0, 0, 0),
					new Vector3f(1, 1, 1));
			entity.type = 0;
			return entity;
		case 1:
			Entity entity2 = new Entity(dirt, new Vector3f(realX, height - modY, realZ), new Vector3f(0, 0, 0),
					new Vector3f(1, 1, 1));
			entity2.type = 1;

			return entity2;
		case 2:
			int index = Maths.randomInt(0, stones.length - 1);
			Entity entity3 = new Entity(stones[index], new Vector3f(realX, height - modY, realZ), new Vector3f(0, 0, 0),
					new Vector3f(1, 1, 1));
			int ttype = 2;
			if (index >= 0 && index <= 2) {
				ttype = 2;
			} else if (index == 3) {
				ttype = 3;
			} else if (index == 4) {
				ttype = 4;
			}
			entity3.type = ttype;
			return entity3;
		default:
			return null;
		}

	}

	public int getMountainTop() {
		return CENTER_MOUNTAIN_TOP;
	}

	public HeightMap getHeightmap() {
		return heightmap;
	}
}
