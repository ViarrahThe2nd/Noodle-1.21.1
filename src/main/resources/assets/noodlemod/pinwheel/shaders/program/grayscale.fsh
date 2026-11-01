uniform sampler2D DiffuseSampler0;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 baseColor = texture(DiffuseSampler0, texCoord);

    float lum = dot(baseColor.rgb, vec3(0.299, 0.587, 0.114));

    fragColor = vec4(lum, lum, lum, 1.0);
}