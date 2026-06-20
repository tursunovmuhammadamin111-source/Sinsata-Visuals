package uz.sinsata.visuals.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import uz.sinsata.visuals.SinsataVisualsClient;
import uz.sinsata.visuals.config.VisualsConfig;

/**
 * "Sinsata Visuals" sozlamalar oynasi - to'liq o'zbek tilida.
 */
public class SinsataVisualsScreen extends Screen {

    private final Screen parent;
    private final VisualsConfig config;

    public SinsataVisualsScreen(Screen parent) {
        super(Text.literal("Sinsata Visuals — Sozlamalar"));
        this.parent = parent;
        this.config = SinsataVisualsClient.CONFIG;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = 40;
        int spacing = 24;

        // --- FPS bo'limi ---
        addToggle(centerX, y, "Zarrachalarni kamaytirish", config.reduceParticles,
                v -> config.reduceParticles = v);
        y += spacing;

        addToggle(centerX, y, "Bulutlarni soddalashtirish (FPS uchun)", config.simplifyClouds,
                v -> config.simplifyClouds = v);
        y += spacing;

        addToggle(centerX, y, "Tumanni soddalashtirish", config.simplifyFog,
                v -> config.simplifyFog = v);
        y += spacing;

        addToggle(centerX, y, "Blok sindirish effektini o'chirish", config.disableBlockBreakParticles,
                v -> config.disableBlockBreakParticles = v);
        y += spacing + 10;

        // --- Vizual bo'limi ---
        addToggle(centerX, y, "Rang filtri (kosmetik)", config.enableColorFilter,
                v -> config.enableColorFilter = v);
        y += spacing;

        addToggle(centerX, y, "Vinyetka effekti (ekran chetlari)", config.enableVignette,
                v -> config.enableVignette = v);
        y += spacing;

        addToggle(centerX, y, "Kamera harakatini yumshatish", config.smoothCameraTransitions,
                v -> config.smoothCameraTransitions = v);
        y += spacing + 10;

        // Saqlash va chiqish
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Saqlash va yopish"), btn -> {
            config.save();
            this.client.setScreen(parent);
        }).dimensions(centerX - 100, y, 200, 20).build());
    }

    private void addToggle(int centerX, int y, String label, boolean current, java.util.function.Consumer<Boolean> setter) {
        final boolean[] state = {current};
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(label + ": " + (state[0] ? "Yoqilgan" : "O'chirilgan")),
                btn -> {
                    state[0] = !state[0];
                    setter.accept(state[0]);
                    btn.setMessage(Text.literal(label + ": " + (state[0] ? "Yoqilgan" : "O'chirilgan")));
                }
        ).dimensions(centerX - 150, y, 300, 20).build());
    }

    @Override
    public void close() {
        config.save();
        this.client.setScreen(parent);
    }

    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Sinsata Visuals"), this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
