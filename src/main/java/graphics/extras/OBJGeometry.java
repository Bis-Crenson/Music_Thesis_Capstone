package graphics.extras;

import graphics.geometry.Geometry;
import graphics.math.Vector;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;


public class OBJGeometry extends Geometry
{
    public OBJGeometry(String fileName)
    {

        List<graphics.math.Vector> points             = new ArrayList<graphics.math.Vector>();
        List<graphics.math.Vector> uvs                = new ArrayList<graphics.math.Vector>();
        List<graphics.math.Vector> normals            = new ArrayList<graphics.math.Vector>();

        List<graphics.math.Vector> vertexPositionList   = new ArrayList<graphics.math.Vector>();
        List<graphics.math.Vector> vertexUVList    	    = new ArrayList<graphics.math.Vector>();
        List<graphics.math.Vector> vertexNormalList     = new ArrayList<graphics.math.Vector>();

        List<String> dataArray  		      = new ArrayList<String>();

        File file = new File(fileName);
        Scanner scan = null;

        try
        {
            scan = new Scanner(file);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        while (scan.hasNextLine())
        {
            dataArray.add(scan.nextLine());
        }
        scan.close();

        for (String line : dataArray)
        {

            String[] d = line.split(" ");

            if (d[0].equals("v"))

                points.add(  new graphics.math.Vector( Float.parseFloat(d[1]),
                        Float.parseFloat(d[2]),
                        Float.parseFloat(d[3]) ) );

            if (d[0].equals("vt"))

                uvs.add(     new graphics.math.Vector( Float.parseFloat(d[1]),
                        Float.parseFloat(d[2]) ) );

            if (d[0].equals("vn"))

                normals.add( new graphics.math.Vector( Float.parseFloat(d[1]),
                        Float.parseFloat(d[2]),
                        Float.parseFloat(d[3]) ) );

            if (d[0].equals("f"))
            {
                for (int i = 1; i < 4; i++)
                {
                    String triangles = d[i];
                    String[] indices = triangles.split("/");

                    graphics.math.Vector P1 =  points.get( Integer.parseInt(indices[0]) - 1);
                    graphics.math.Vector P2 =     uvs.get( Integer.parseInt(indices[1]) - 1);
//                    Vector P3 = normals.get( Integer.parseInt(indices[2]) - 1);

                    vertexPositionList.add( new graphics.math.Vector(  P1.values[0], P1.values[1], P1.values[2] ) );
                    vertexUVList.add(       new graphics.math.Vector(  P2.values[0], P2.values[1]               ) );
     //               vertexNormalList.add(   new Vector(  P3.values[0], P3.values[1], P3.values[2] ) );
                }
            }
        }


        float[] vertexPositionData 		 = graphics.math.Vector.flattenList(vertexPositionList);
        float[] vertexUVData 			     = Vector.flattenList(vertexUVList);
        //float[] vertexNormalData 		   = Vector.flattenList(vertexNormalList);

        addAttribute("vec3", "vertexPosition", vertexPositionData);
        addAttribute("vec2", "vertexUV", vertexUVData);
        //addAttribute("vec3", "vertexNormal", vertexNormalData);

        vertexCount = vertexPositionList.size();
    }
}