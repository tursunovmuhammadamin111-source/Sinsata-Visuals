package uz.sinsata.visuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.event.client.ClientTickEvents;
import org.lwjgl.glfw.GLFW;
import uz.sinsata.visuals.config.VisualsConfig;
import uz.sinsata.visuals.gui.SinsataVisualsScreen;

/**
 * Sinsata Visuals — Minecraft uchun estetik vizual yaxshilanishlar
 * va past quvvatli kompyuterlar uchun FPS optimizatsiyasi to'plami.
 *
 * Diqqat: bu mod hech qanday cheat (ESP, aimbot, x-ray va h.k.) funksiyasini
 * o'z ichiga olmaydi. Faqat kosmetik effektlar va sof optimizatsiya bor.
 */
public class SinsataVisualsClient implements ClientModInitializer {

    public static final String MOD_ID = "sinsatavisuals";
    public static VisualsConfig CONFIG;

    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        // Konfiguratsiyani diskdan o'qish (yoki standart qiymatlar bilan yaratish)
        CONFIG = VisualsConfig.load();

        // Sozlamalar oynasini ochish uchun tugma: standart - "RIGHT SHIFT"
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sinsatavisuals.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.sinsatavisuals.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new SinsataVisualsScreen(null));
                }
            }
        });
    }
}
