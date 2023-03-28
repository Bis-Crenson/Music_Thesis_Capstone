package graphics.geometry;

import graphics.math.Vector;

import java.util.ArrayList;

public class StarGeometry extends Geometry
{


	ArrayList<Vector> positionList = new ArrayList();
	ArrayList<Vector> colorList = new ArrayList();

	ArrayList<Vector> uvList = new ArrayList();
	public StarGeometry(int points, Vector color1, Vector color2, Vector color3)
	{
		double baseAngle = 360.0/(points * 2);
		Vector uvCenter = new Vector(0.5, 0.5);
		baseAngle = Math.toRadians(baseAngle);
		for(int i = 0; i < 2 * points; i += 2){
			Vector origin = new Vector(0, 0, 0);
			Vector P = new Vector(Math.cos(i * baseAngle),
									Math.sin(i * baseAngle),
									0.0);
			Vector Q = new Vector ( (0.5 * Math.cos((i+1) * baseAngle)),  (0.5 * Math.sin((i+1) *baseAngle)), 0.0);
			Vector R = new Vector((Math.cos((i+2) * baseAngle)),  (Math.sin((i+2) *baseAngle)), 0.0);
			positionList.add(origin);
			positionList.add(P);
			positionList.add(Q);
			positionList.add(origin);
			positionList.add(Q);
			positionList.add(R);

			Vector blue = new Vector(color1.values[0], color1.values[1], color1.values[2]);
			Vector pink = new Vector(color2.values[0], color2.values[1], color2.values[2]);
			Vector lime = new Vector(color3.values[0], color3.values[1], color3.values[2]);

			colorList.add(blue);
			colorList.add(pink);
			colorList.add(lime);
			colorList.add(blue);
			colorList.add(pink);
			colorList.add(lime);
			colorList.add(lime);
			colorList.add(blue);
			colorList.add(pink);
			colorList.add(lime);

			//Add the UV center list
			uvList.add(uvCenter);
			uvList.add(new Vector(0.5 * Math.cos(i * baseAngle) + 0.5, 0.5 * Math.sin(i * baseAngle) + 0.5));
			uvList.add(new Vector(0.5 * (0.5 * Math.cos((i+1) * baseAngle)) + 0.5, 0.5 * (0.5 * Math.sin((i+1) *baseAngle)) + 0.5));
			uvList.add(uvCenter);
			uvList.add(new Vector(0.5 * (0.5 * Math.cos((i+1) * baseAngle)) + 0.5, 0.5 * (0.5 * Math.sin((i+1) *baseAngle)) + 0.5));
			uvList.add(new Vector(0.5 * (Math.cos((i+2) * baseAngle)) + 0.5, 0.5 * (Math.sin((i+2) *baseAngle)) + 0.5));


		}
		float[] positionData = Vector.flattenList(positionList);
		float[] colorData = Vector.flattenList(colorList);
		float[] uvData = Vector.flattenList(uvList);

		addAttribute("vec3", "vertexPosition", positionData);
		addAttribute("vec3", "vertexColor", colorData);
		addAttribute("vec2", "vertexUV", uvData);

		vertexCount = 6 * points;
	}
}