uniform vec3 baseColor;

uniform sampler2D image;

out vec4 FragColor;

void main()
{             
	vec4 imageColor = texture(image, gl_PointCoord);
	
	if (imageColor.a < 0.50)
	    discard;

	FragColor = vec4(baseColor, 1.0) * imageColor;
}