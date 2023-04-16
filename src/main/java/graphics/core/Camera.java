package graphics.core;

import graphics.math.Matrix;

public class Camera extends Object3D
{
	public graphics.math.Matrix viewMatrix;
	public graphics.math.Matrix projectionMatrix;

	public Camera()
	{
		viewMatrix = graphics.math.Matrix.makeIdentity();
		projectionMatrix = graphics.math.Matrix.makePerspective();
	}

	public Camera(double angleOfView, double aspectRatio, double near, double far)
	{
		viewMatrix = graphics.math.Matrix.makeIdentity();
		projectionMatrix = Matrix.makePerspective(angleOfView, aspectRatio, near, far);
	}

	public void updateViewMatrix()
	{
		viewMatrix = getWorldMatrix().inverse();
	}

}
