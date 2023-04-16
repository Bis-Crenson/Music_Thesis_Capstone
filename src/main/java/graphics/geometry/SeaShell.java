package graphics.geometry;

import graphics.math.Vector;

public class SeaShell extends SurfaceGeometry
{
    static double aa = 0.999;
    static double test, psi, sinpsi, cospsi, g, s, r, t;
    public SeaShell(
            double width, double height, double depth,
            int radiusSegments, int heightSegments )
    {
        super( (u,v) -> { doMeth(u, v);return new Vector(
                        (u - t), //x
                        (r * Math.cos(v)), //y
                        (r * Math.sin(v))); }, //z
                0, 2*Math.PI, radiusSegments,
                -Math.PI/2, Math.PI/2, heightSegments);
    }

    public SeaShell()
    {
        this(1,1,1, 32,16);
    }

    public static double phi(double u){
        return -u / (Math.sqrt(aa + 1) + Math.atan(Math.sqrt(aa + 1) * Math.tan(u)));
    }


    public static void doMeth(double u, double v){
        psi = aa; //Equal to aa
        //If beyond these bounds, set it to the max/min
        if(psi < 0.001)
            psi = 0.001;
        if(psi > 0.999)
            psi = 0.999;
        psi *= 2; //Double the value
        sinpsi = Math.sin(psi);
        cospsi = Math.cos(psi);
        g = (u - cospsi * v) / sinpsi;
        s = Math.exp(g);
        r = (2 * sinpsi) / (s + 1 / s);
        t = r * (s - 1 / s) * 0.5;
    }

}