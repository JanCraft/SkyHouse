package models;

import java.io.Serializable;

public class RawModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int vaoID;
	private int vertexCount;

	public RawModel(final int vaoID, final int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
