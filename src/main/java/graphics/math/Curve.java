package graphics.math;

import graphics.geometry.CurveGeometry;

public class Curve extends CurveGeometry
{
    public interface Function
    {
        Vector apply(double t);
    }

    public Function function;

    public Curve(Function function)
    {
        super();
        this.function = function;
    }

    // get a 2D array of points on the surface
    //   represented by this function
    public Vector[] getPoints(
            double tMin, double tMax, int tResolution)
    {

        Vector[] points = new Vector[tResolution+1];
        double deltaT = (tMax - tMin) / tResolution;

        for (int tIndex = 0; tIndex < tResolution+1; tIndex++)
        {
            double t = tMin + tIndex * deltaT;
            points[tIndex] = this.function.apply(t);
        }

        return points;
    }
}
