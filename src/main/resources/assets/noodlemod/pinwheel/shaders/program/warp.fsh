uniform sampler2D DiffuseSampler0;

uniform float VeilRenderTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord;

    float time = VeilRenderTime * 6.2831853;

    // Main horizontal sine wave
    float distortionX =
        sin(uv.y * 15.0 + time * 1.5) * 0.004;

    // Secondary, smaller distortion
    distortionX +=
        sin(uv.y * 35.0 - time * 2.0) * 0.0015;

    // Slight vertical distortion
    float distortionY =
        sin(uv.x * 20.0 + time) * 0.002;

    uv += vec2(distortionX, distortionY);

    fragColor = texture(DiffuseSampler0, uv);
}