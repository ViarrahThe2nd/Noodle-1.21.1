package de.viarrah.noodle.fog;


import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.blockentity.FogBlockEntity;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.shader.uniform.ShaderUniformAccess;
import foundry.veil.platform.VeilEventPlatform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(
        modid = NoodleMod.MOD_ID,
        value = Dist.CLIENT
)
public class FogClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            VeilEventPlatform.INSTANCE.preVeilPostProcessing(
                    (pipelineName, pipeline, context) -> {

                        if (!FogClientEvents.FOG_PIPELINE.equals(pipelineName)) {
                            return;
                        }

                        FogManager.FogState fog = FogManager.getCurrentState();

                        setFloat(pipeline, "NoodleFogInfluence", fog.influence());
                        setFloat(pipeline, "NoodleFogStart", fog.fogStart());
                        setFloat(pipeline, "NoodleFogEnd", fog.fogEnd());

                        ShaderUniformAccess color =
                                pipeline.getUniform("NoodleFogColor");

                        if (color != null) {
                            color.setVector(
                                    fog.r(),
                                    fog.g(),
                                    fog.b(),
                                    1.0f
                            );
                        }

                        setInt(pipeline, "NoodleFogShape", 0);
                    }
            );
        });
    }

    @SubscribeEvent
    public static void registerFogBlockColor(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return 0xFFFFFFFF;
            }

            if (level != null
                    && pos != null
                    && level.getBlockEntity(pos) instanceof FogBlockEntity fog) {
                return 0xFF000000 | (fog.getFogColor() & 0xFFFFFF);
            }

            return 0xFFFFFFFF;
        }, ModBlocks.FOG_BLOCK.get());
    }

    private static void setFloat(
            PostPipeline pipeline,
            String name,
            float value
    ) {
        ShaderUniformAccess uniform = pipeline.getUniform(name);

        if (uniform != null) {
            uniform.setFloat(value);
        }
    }

    private static void setInt(
            PostPipeline pipeline,
            String name,
            int value
    ) {
        ShaderUniformAccess uniform = pipeline.getUniform(name);

        if (uniform != null) {
            uniform.setInt(value);
        }
    }
}
