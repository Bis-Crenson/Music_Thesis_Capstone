package graphics.extras;

import graphics.core.Mesh;
import graphics.material.LineMaterial;
import graphics.math.Vector;

public class GridHelper extends Mesh
{
	public GridHelper(double size, int divisions,
					  graphics.math.Vector gridColor, graphics.math.Vector centerColor, int lineWidth )
	{
		super( new GridGeometry(size, divisions, gridColor, centerColor), 
			   new LineMaterial() );
		this.material.uniforms.get("useVertexColors").data = 1;
		this.material.renderSettings.get("lineWidth").data = lineWidth;
	}

	public GridHelper()
	{
		this(10, 10, 
			new graphics.math.Vector(0.5,0.5,0.5),
			new Vector(0.8,0.8,0.8), 1);
	}
}