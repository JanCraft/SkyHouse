package inventory;

public class Inventory {

	public Item[] items;

	private int[][] guiPositions;
	private int[][] count;

	// items[0][0] -> Steak
	// count[0][0] -> 5
	//
	// total[0][0] -> 5 of Steak

	public Inventory() {
		items = new Item[1];
		guiPositions = new int[1][1];
		count = new int[1][1];
	}

	private void useItem(int index) {
		Item item = items[index];
		if (item.use()) {
			count[item.getX()][item.getY()] -= 1;
		}
	}

	private Item getItem(final int x, final int y) {
		for (Item item : items) {
			if (item.getX() == x) {
				if (item.getY() == y) {
					return item;
				}
			}
		}
		return null;
	}

}
