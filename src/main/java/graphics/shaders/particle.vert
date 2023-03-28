// return a random value in [0, 1]
float random(float x)
{
    return fract(23579.12345 * sin(0.33707734 * x));
}

in float particleIndex;        

uniform vec3 minPosition;
uniform vec3 maxPosition;

uniform float time;
uniform vec3 minGravity;
uniform vec3 maxGravity;

uniform float minSpeed;
uniform float maxSpeed;

uniform float minAge;
uniform float maxAge;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main()
{
	float r1 = random(particleIndex + 0.1);
	float r2 = random(particleIndex + 0.2);
	float r3 = random(particleIndex + 0.3);
	float r4 = random(particleIndex + 0.4);
	float r5 = random(particleIndex + 0.5);
	float r6 = random(particleIndex + 0.6);

    	vec3 startPosition = (maxPosition - minPosition) * vec3(r1,r2,r3) + minPosition;
	float speed = (maxSpeed - minSpeed) * r4 + minSpeed;
	float age = (maxAge - minAge) * r5 + minAge;
	vec3 gravity = (maxGravity - minGravity) * r6 + minGravity;
	float t = mod(time, age);
    	startPosition = startPosition + speed * t * gravity;

	vec4 eyePosition = viewMatrix * modelMatrix * vec4(startPosition, 1.0);
	// gl_PointSize = 500.0; 
	gl_PointSize = 50.0 * 1.0 / length(eyePosition);
	gl_Position = projectionMatrix * eyePosition;
}
