package graphics.extras;
import graphics.core.Mesh;
import graphics.geometry.ParticleGeometry;
import graphics.material.ParticleMaterial;

public class ParticleSystem extends Mesh
{
    public ParticleSystem(graphics.core.Texture particleTexture)
    {
        this(100, particleTexture);
    }

    public ParticleSystem(
            int particleCount,
            graphics.core.Texture particleTexture)
    {
        super(
                new ParticleGeometry(particleCount),
                new ParticleMaterial(particleTexture) );
    }
}