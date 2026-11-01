#include veil:fog
#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform float NoodleFogInfluence;
uniform float NoodleFogStart;
uniform float NoodleFogEnd;
uniform vec4 NoodleFogColor;
uniform int NoodleFogShape;

in vec2 texCoord;
out vec4 fragColor;

void main() {

    // Original scene color.
    vec4 baseColor = texture(DiffuseSampler0, texCoord);

    // Scene depth.
    float depth = texture(DiffuseDepthSampler, texCoord).r;

    // Reconstruct camera-relative position.
    vec3 pos = screenToLocalSpace(texCoord, depth).xyz;

    // Calculate distance for the fog equation.
    float distance = fog_distance(pos, NoodleFogShape);

    // Apply fog.
    vec4 foggedColor = linear_fog(
            baseColor,
            distance,
            NoodleFogStart,
            NoodleFogEnd,
            NoodleFogColor
    );

    // Blend between the original scene and the fogged scene.
    fragColor = mix(
            baseColor,
            foggedColor,
            clamp(NoodleFogInfluence, 0.0, 1.0)
    );
}