package com.lowdragmc.shimmer.core.mixins;

import com.lowdragmc.shimmer.client.auxiliaryScreen.HsbColorWidget;
import com.lowdragmc.shimmer.client.light.LightManager;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.lowdragmc.shimmer.client.ShimmerRenderTypes;
import com.lowdragmc.shimmer.platform.Services;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author KilaBash
 * @date 2022/05/02
 * @implNote GameRendererMixin, used to refresh shader and fbo size.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private Map<String, ShaderInstance> shaders;

    @Inject(method = "resize", at = @At(value = "RETURN"))
    private void injectResize(int width, int height, CallbackInfo ci) {
        PostProcessing.resize(width, height);
    }

    @Inject(method = "reloadShaders", at = @At(value = "RETURN"))
    private void injectReloadShaders(ResourceManager pResourceManager, CallbackInfo ci) {
        if (Services.PLATFORM.isLoadingStateValid()) {
            LightManager.INSTANCE.reloadShaders();
        }
    }

    /* Replacement for RegisterShadersEvent, as fabric has no equivalent event  */
    @Inject(method = "reloadShaders", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;shutdownShaders()V", shift = At.Shift.AFTER))
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) {
        if (Services.PLATFORM.getPlatformName().equalsIgnoreCase("fabric")) {

	        {
		        Pair<ShaderInstance, Consumer<ShaderInstance>> blitShader = RenderUtils.registerShaders(resourceManager);
		        this.shaders.put(blitShader.getFirst().getName(), blitShader.getFirst());
		        blitShader.getSecond().accept(blitShader.getFirst());
	        }

	        {
		        Pair<ShaderInstance, Consumer<ShaderInstance>> armorShader = ShimmerRenderTypes.registerShaders(resourceManager);
		        this.shaders.put(armorShader.getFirst().getName(), armorShader.getFirst());
		        armorShader.getSecond().accept(armorShader.getFirst());
	        }

	        {
		        Pair<ShaderInstance, Consumer<ShaderInstance>> hsbShader = HsbColorWidget.registerShaders(resourceManager);
		        this.shaders.put(hsbShader.getFirst().getName(), hsbShader.getFirst());
		        hsbShader.getSecond().accept(hsbShader.getFirst());
	        }

        }
    }


}
