package generation;

public class HeightMap {

	private int[][] heights;
	private final int width;
	private final int height;

	public HeightMap(final int width, final int height) {
		heights = new int[width][height];
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[][] getHeights() {
		return heights;
	}

	public void loadFromFile(String fileName) {

	}

	public void loadFromArray(int[][] array) {
		heights = array;
	}

	public void setHeight(final int x, final int y, final int value) {
		if ((x < 0 || y < 0) || (x >= width || y >= height)) {
			return;
		}
		if (heights[x][y] == 0) {
			heights[x][y] = value;
		}
	}

	public int fillHeights(final int x, final int y, final int dx, final int dy, final int value) {
		int usedSpaces = 0;
		for (int i = x; i <= dx; i++) {
			for (int ix = y; ix <= dy; ix++) {
				if ((i < 0 || ix < 0) || (i >= width || ix >= height)) {
					break;
				}
				if (heights[i][ix] == 0) {
					usedSpaces++;
					heights[i][ix] = value;
				}
			}
		}
		return usedSpaces;
	}

}
