package graphics.geometry;

import graphics.math.Vector;

public class WeirdAssignment15Shape extends SurfaceGeometry
{
    static double aa = 0.05;

    public WeirdAssignment15Shape(
            double width, double height, double depth,
            int radiusSegments, int heightSegments )
    {
        super( (u,v) -> { return new Vector(
                        r(u, v) * Math.cos(phi(u)), //x
                        r(u, v) * Math.sin(phi(u)), //y
                        (Math.log(Math.tan(v/2)) + (aa + 1) * a(u, v) * Math.cos(v)) / Math.sqrt(aa)); }, //z
                0, 2*Math.PI, radiusSegments,
                -Math.PI/2, Math.PI/2, heightSegments  );
    }

    public WeirdAssignment15Shape()
    {
        this(1,1,1, 32,16);
    }

    public static double phi(double u){
        return -u / (Math.sqrt(aa + 1) + Math.atan(Math.sqrt(aa + 1) * Math.tan(u)));
    }

    public static double a(double u, double v){
        return 2 / (Math.sqrt(aa + 1) - aa * Math.pow(Math.sin(v), 2) * Math.pow(Math.cos(u), 2));
    }

    public static double r(double u, double v){
        return a(u, v) * (Math.sqrt((1 + 1/aa) * (1 + aa * Math.pow(Math.sin(u), 2))) * Math.sin(v));
    }


}