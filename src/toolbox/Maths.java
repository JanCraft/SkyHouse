package toolbox;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {

	public static final int intNumberHigher = 999999999;
	public static final long longNumberHigher = 999999999999999999L;

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {

		Matrix4f matrix = new Matrix4f();

		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);

		return matrix;

	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	public static int randomInt(final int minValue, final int maxValue) {
		Random r = new Random();
		int rnd = 0;
		rnd = r.nextInt(maxValue - minValue + 1) + minValue;
		return rnd;
	}

	public static float IntToFloat(final int x) {
		return (float) x;
	}

	public static int FloatToInt(final float x) {
		return (int) x;
	}
	
	public static boolean posEqualsInt(Vector3f a, Vector3f b) {
		if(FloatToInt(a.x) == FloatToInt(b.x)) {
			if(FloatToInt(a.y) == FloatToInt(b.y)) {
				if(FloatToInt(a.z) == FloatToInt(b.z)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Vector3f vec3ToInt(Vector3f position) {
		return new Vector3f(FloatToInt(position.x), FloatToInt(position.y), FloatToInt(position.z));
	}
	
	public static Vector3f vectorZero() {
		return new Vector3f(0, 0, 0);
	}
	
	public static Vector3f vectorOne() {
		return new Vector3f(1, 1, 1);
	}

}
