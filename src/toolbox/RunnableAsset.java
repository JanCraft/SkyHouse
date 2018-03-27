package toolbox;

public class RunnableAsset extends GameAsset {

	public RunnableAsset(String path, int type) {
		super(path, type);
	}

	public static String hasParam(RunnableAsset asset, String param) {
		for(String pram : asset.params) {
			if(pram.contains(param)) {
				return pram.substring(param.length() + 1);
			}
		}
		return "";
	}

}
