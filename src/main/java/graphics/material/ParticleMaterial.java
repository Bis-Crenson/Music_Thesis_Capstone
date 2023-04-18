package graphics.material;

import graphics.math.Vector;
import graphics.core.Texture;

import static org.lwjgl.opengl.GL40.*;

public class ParticleMaterial extends Material
{
    public ParticleMaterial(Texture texture)
    {
        super(
                "src/main/java/graphics/shaders/particle.vert",
                "src/main/java/graphics/shaders/particle.frag"  );

        drawStyle = GL_POINTS;

        addUniform("vec3", "minPosition", new Vector(0,0,0) );
        addUniform("vec3", "maxPosition", new Vector(1,1,1) );
        addUniform("float", "time", 0);
        addUniform("vec3", "minGravity", new Vector(0, -1, 0));
        addUniform("vec3", "maxGravity", new Vector(0, -1, 0));
        addUniform("float", "minSpeed", 1.0f);
        addUniform("float", "maxSpeed", 1.0f);
        addUniform("float", "minAge", 10.0f);
        addUniform("float", "maxAge", 10.0f);
        addUniform("vec3", "baseColor", new Vector(1,1,1) );
        addUniform("sampler2D", "image", new Vector(texture.textureRef, 1));

        locateUniforms();

        addRenderSetting( "pointSize", 8 );
    }
}
