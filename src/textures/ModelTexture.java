package textures;

import java.io.Serializable;

public class ModelTexture implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int texureID;

	public ModelTexture(final int id) {
		this.texureID = id;
	}

	public int getID() {
		return texureID;
	}

}
