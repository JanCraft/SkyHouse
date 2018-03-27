package toolbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coding.Phoskel;
import coding.PhoskelData;

public abstract class GameAsset {
	
	public final String path;
	public final int type;
	
	public List<String> params = new ArrayList<String>();
	
	public GameAsset(final String path, final int type) {
		this.path = path;
		this.type = type;
	}
	
	public GameAsset addParam(String param) {
		if(params.contains(param)) {
			return this;
		}
		params.add(param);
		return this;
	}
	
	public static GameAsset getFileAsset(String path) throws IOException {
		GameAsset asset = new RunnableAsset(path, 0);
		PhoskelData dta = Phoskel.pskDecodificatePhoskel(path);
		for(String str : dta.getData()) {
			String[] tag = Phoskel.pskDecodificateTag(str);
			for(String tg : tag) {
				asset.addParam(tg);
			}
		}
		return asset;
	}
	
}
