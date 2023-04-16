uniform sampler2D heightmap;
uniform sampler2D snow;
uniform sampler2D grass;
uniform sampler2D rock;
uniform sampler2D sand;
uniform sampler2D dirt;



in vec2 UV;
out vec4 fragColor;

void main()
{
	vec4 data       = texture(heightmap, UV);
	float height    = data.r;
	vec4 grassColor = texture(grass, 20.0 * UV);
	vec4 rockColor  = texture(rock, 20.0 * UV);
	vec4 sandColor  = texture(sand, 20.0 * UV);
	vec4 dirtColor = texture(dirt, 20.0 * UV);
	vec4 snowColor = texture(snow, 20.0 * UV);

	if (height < 0.3)
	    fragColor = sandColor;
	else if(height >= 0.3 && height < 0.4)
		fragColor = dirtColor;
	else if(height >= 0.4 && height < 0.5)
		fragColor = grassColor;
	else if(height >= 0.5 && height < 0.55)
		fragColor = rockColor;
	else
	    fragColor = snowColor;
}