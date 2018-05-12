package coding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.List;

public class Phoskel {

	public static PhoskelData pskDecodificatePhoskel(String filePath) throws IOException {
		PhoskelData data = new PhoskelData();
		List<String> fileData;

		File file = new File(filePath + ".psk");

		fileData = Files.readAllLines(file.toPath());

		String[] listData = new String[fileData.size()];

		for (int i = 0; i < listData.length; i++) {
			listData[i] = fileData.get(i);
		}

		data.setData(listData);

		return data;
	}

	public static File pskCodificatePhoskel(String filePath, PhoskelData data)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filePath + ".psk", "UTF-8");
		for (int i = 0; i < data.getData().length; i++) {
			writer.println(data.getData()[i]);
		}
		writer.close();
		return new File(filePath + ".psk");
	}

	public static String[] pskDecodificateTag(String tag) throws NullPointerException {
		String[] result;
		result = tag.split(",");
		if (result == null || tag == null) {
			throw new NullPointerException();
		}
		return result;
	}

}
