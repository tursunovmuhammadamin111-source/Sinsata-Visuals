package uz.sinsata.visuals.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uz.sinsata.visuals.SinsataVisualsClient;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Sof FPS-optimizatsiya: agar foydalanuvchi "Zarrachalarni kamaytirish"
 * sozlamasini yoqsa, zarrachalarning bir qismi shunchaki qo'shilmaydi.
 * Bu hech qanday o'yin ma'lumotini (boshqa o'yinchilar, bloklar va h.k.)
 * oshkor qilmaydi — faqat vizual yukni kamaytiradi.
 */
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void sinsata$reduceParticles(ParticleEffect parameters, double x, double y, double z,
                                          double velocityX, double velocityY, double velocityZ,
                                          CallbackInfo ci) {
        var config = SinsataVisualsClient.CONFIG;
        if (config != null && config.reduceParticles) {
            int chance = MathHelperClamp(config.particleReductionPercent);
            if (ThreadLocalRandom.current().nextInt(100) < chance) {
                ci.cancel();
            }
        }
    }

    private static int MathHelperClamp(int value) {
        if (value < 0) return 0;
        if (value > 100) return 100;
        return value;
    }
}
