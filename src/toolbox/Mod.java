package toolbox;

import java.io.IOException;
import java.util.List;

import coding.Phoskel;
import coding.PhoskelData;
import engineTester.MainGameLoop;

public class Mod {
	
	public static String ModLoader;
	
	public final String ModID;
	public final String ModName;
	public final String ModVersion;
	
	protected List<GameAsset> assets;
	
	public static void setModLoader(final String ModLoader) {
		Mod.ModLoader = ModLoader;
	}
	
	public Mod(final String modId, final String modName, final String modVersion) {
		this.ModID = modId;
		this.ModName = modName;
		this.ModVersion = modVersion;
		
		System.out.println(" [" + ModID + "] " + ModName + " Version: " + ModVersion + " Loaded with " + Mod.ModLoader +".");
	}
	
	protected void decompileAssets() throws IOException {
		MainGameLoop.compileAssets();
		for(GameAsset asset : MainGameLoop.assets) {
			assets.add(getAsset(asset.path));
		}
	}
	
	protected GameAsset getAsset(String path) throws IOException {
		return GameAsset.getFileAsset(path);
	}
	
	protected void addAsset(GameAsset asset) {
		assets.add(asset);
	}
	
	protected GameAsset changeAsset(String path, String param) throws IOException {
		return GameAsset.getFileAsset(path).addParam(param);
	}
	
	protected void recompileAssets() throws IOException {
		for(GameAsset asset : assets) {
			PhoskelData data = new PhoskelData();
			data.setData((String[]) asset.params.toArray());
			Phoskel.pskCodificatePhoskel(asset.path, data);
		}
	}
	
}
