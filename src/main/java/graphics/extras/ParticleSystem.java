package graphics.extras;
import graphics.core.*;
import graphics.geometry.ParticleGeometry;
import graphics.material.ParticleMaterial;

public class ParticleSystem extends Mesh
{
    public ParticleSystem(Texture particleTexture)
    {
        this(100, particleTexture);
    }

    public ParticleSystem(
            int particleCount,
            Texture particleTexture)
    {
        super(
                new ParticleGeometry(particleCount),
                new ParticleMaterial(particleTexture) );
    }
}