uniform sampler2D blendmap;

// these images appear over the corresponding colored pixels in blendMap
uniform sampler2D redImage;
uniform sampler2D greenImage;
uniform sampler2D blueImage;
uniform sampler2D blackImage;

in vec2 UV;
out vec4 fragColor;

void main()
{
	vec4 blendData = texture(blendmap, UV);
	
	float redImagePercent   = blendData.r;
	float greenImagePercent = blendData.g;
	float blueImagePercent  = blendData.b;
	float blackImagePercent = 1.0 - redImagePercent - greenImagePercent - blueImagePercent;

	// red + green + blue + black = 1
	// black = 1 - red - green - blue

	vec4 redImageData   = texture(redImage, UV);
	vec4 greenImageData = texture(greenImage, 10.0 * UV);
	vec4 blueImageData  = texture(blueImage, 10.0 * UV);
	vec4 blackImageData = texture(blackImage, UV);
	
	fragColor = redImagePercent * redImageData + greenImagePercent * greenImageData 
		    + blueImagePercent * blueImageData + blackImagePercent * blackImageData;







}