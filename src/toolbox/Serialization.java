package toolbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Serialization {

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static void byteArrayToFile(byte[] bytes, String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] fileToByteArray(String path) {
		try {
			Path pth = Paths.get(path);
			byte[] data = Files.readAllBytes(pth);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] fileToByteArray(File file) {
		try {
			byte[] data = Files.readAllBytes(file.toPath());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
