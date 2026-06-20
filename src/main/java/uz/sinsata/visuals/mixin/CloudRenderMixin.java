package uz.sinsata.visuals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uz.sinsata.visuals.SinsataVisualsClient;

/**
 * Agar "Bulutlarni soddalashtirish" yoqilgan bo'lsa, bulut render rejimini
 * majburan "FAST" holatiga o'tkazadi (bu vanilla Minecraft-ning o'zidagi
 * standart sozlama, biz uni avtomatlashtiramiz).
 *
 * DIQQAT: aniq metod nomi/signature Minecraft versiyasiga qarab farq qilishi
 * mumkin. Loyihani IntelliJ + Minecraft Development plugin bilan ochib,
 * "GameOptions" klassidagi bulut sozlamasi getteriga moslang.
 */
@Mixin(GameOptions.class)
public class CloudRenderMixin {

    @Inject(method = "getCloudRenderMode", at = @At("HEAD"), cancellable = true)
    private void sinsata$forceFastClouds(CallbackInfoReturnable<CloudRenderMode> cir) {
        var config = SinsataVisualsClient.CONFIG;
        if (config != null && config.simplifyClouds) {
            cir.setReturnValue(CloudRenderMode.FAST);
        }
    }
}
