package graphics.geometry;

public class ParticleGeometry extends Geometry
{
    public ParticleGeometry()
    {
        this(100);
    }

    public ParticleGeometry(int particleCount)
    {
        float[] indexData = new float[particleCount];
        for (int i = 0; i < particleCount; i++)
            indexData[i] = i;

        addAttribute("float", "particleIndex", indexData);

        vertexCount = particleCount;
    }

}