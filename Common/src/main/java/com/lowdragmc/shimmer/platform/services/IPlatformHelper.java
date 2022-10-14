package com.lowdragmc.shimmer.platform.services;

import com.lowdragmc.shimmer.event.ShimmerLoadConfigEvent;
import com.lowdragmc.shimmer.event.ShimmerReloadEvent;
import com.lowdragmc.shimmer.client.postprocessing.PostParticle;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.particle.Particle;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.nio.file.Path;

/**
 * @author HypherionSA
 * @date 2022/06/09
 */
public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * This is specific to forge, apparently.
     * @return - True or False on Forge, always false on Fabric
     */
    boolean isStencilEnabled(RenderTarget target);

    /**
     * This is specific to forge, apparently.
     * @return - True or False on Forge, always false on Fabric
     */
    boolean useCombinedDepthStencilAttachment();

    void enableStencil(RenderTarget renderTarget);

    int getUniformBufferObjectOffset();

    boolean useBlockBloom();

    boolean useLightMap();

    default PostParticle createPostParticle(Particle parent, PostProcessing postProcessing) {
        return new PostParticle(parent, postProcessing);
    }

    /**
     * when stepping into broken state , stop doing some operations to prevent crashing from shimmer
     */
    default boolean isLoadingStateValid() {
        return true;
    }

    boolean isColoredLightEnable();

    boolean isBloomEnable();

    boolean isAdditiveBlend();

    ShimmerLoadConfigEvent postLoadConfigurationEvent(ShimmerLoadConfigEvent event);

	ShimmerReloadEvent postReloadEvent(ShimmerReloadEvent event);

    int getBloomColorAttachmentNumber();

    boolean isEnableInsetShaderInfo();

	ResourceLocation getFluidTextureLocation(Fluid fluid, boolean isStill);

	int getFluidColor(Fluid fluid);

	Path getConfigDir();
}
