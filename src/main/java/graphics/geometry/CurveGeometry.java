package graphics.geometry;

import graphics.math.Curve;
import graphics.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurveGeometry extends Geometry
{
    public CurveGeometry( Curve.Function function,
                          double tMin, double tMax, int tResolution  )
    {
        Curve curve = new Curve(function);

        Vector[] positions = curve.getPoints(
                tMin, tMax, tResolution);

        List<Vector> quadColors = Arrays.asList(
                new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1) );

        ArrayList<Vector> positionList = new ArrayList<Vector>();
        ArrayList<Vector> colorList    = new ArrayList<Vector>();

        for (int tIndex=0; tIndex<tResolution; tIndex++)
        {
            // position coordinates
            Vector pA = positions[tIndex+0];
            Vector pB = positions[tIndex+1];
            positionList.addAll( Arrays.asList(pA,pB) );

            colorList.addAll(quadColors);
        }

        float[] positionData = Vector.flattenList(positionList);
        float[] colorData = Vector.flattenList(colorList);

        addAttribute("vec3", "vertexPosition", positionData);
        addAttribute("vec3", "vertexColor", colorData);
        vertexCount = tResolution * 2;
    }

    public CurveGeometry() {

    }
}