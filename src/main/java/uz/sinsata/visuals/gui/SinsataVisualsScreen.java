package uz.sinsata.visuals.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import uz.sinsata.visuals.SinsataVisualsClient;
import uz.sinsata.visuals.config.VisualsConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * "Sinsata Visuals" — ClickGUI uslubidagi yon panel + modul ro'yxati ko'rinishi.
 * Faqat TASHQI KO'RINISH stilizatsiyasi; funksiyalar 100% halol (FPS/vizual sozlamalar).
 */
public class SinsataVisualsScreen extends Screen {

    private enum Category { FPS, VIZUAL }

    // ----- Mavzu ranglari -----
    private static final int ACCENT = 0xFFB873FF;
    private static final int ACCENT_DIM = 0xFF5C3D88;
    private static final int BG_PANEL = 0xF0121016;
    private static final int BG_SIDEBAR = 0xF01A1620;
    private static final int BG_ROW = 0xFF211D2B;
    private static final int BG_ROW_HOVER = 0xFF2C2640;
    private static final int BORDER = 0xFF3A2A55;
    private static final int ON_COLOR = 0xFF4ADE80;
    private static final int OFF_COLOR = 0xFF8A8A99;
    private static final int TEXT_DIM = 0xFF9A94AC;

    private final Screen parent;
    private final VisualsConfig config;

    private int panelX, panelY, panelW, panelH;
    private static final int SIDEBAR_W = 96;
    private static final int ROW_H = 24;

    private Category selectedCategory = Category.FPS;
    private TextFieldWidget searchField;
    private String searchQuery = "";

    private final List<ModuleRow> activeRows = new ArrayList<>();

    public SinsataVisualsScreen(Screen parent) {
        super(Text.literal("Sinsata Visuals"));
        this.parent = parent;
        this.config = SinsataVisualsClient.CONFIG;
    }

    @Override
    protected void init() {
        panelW = 380;
        panelH = 280;
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;

        this.clearChildren();
        activeRows.clear();

        // --- Sidebar: kategoriya tugmalari ---
        int sideY = panelY + 34;
        addCategoryButton(Category.FPS, "FPS", sideY);
        addCategoryButton(Category.VIZUAL, "VIZUAL", sideY + 26);

        // --- Qidiruv maydoni ---
        int contentX = panelX + SIDEBAR_W + 12;
        int contentW = panelW - SIDEBAR_W - 24;
        searchField = new TextFieldWidget(this.textRenderer, contentX, panelY + 34, contentW, 18,
                Text.literal("Qidiruv"));
        searchField.setPlaceholder(Text.literal("Qidiruv...").formatted(Formatting.DARK_GRAY));
        searchField.setChangedListener(s -> {
            this.searchQuery = s.toLowerCase();
            rebuildModuleRows();
        });
        this.addDrawableChild(searchField);

        // --- Yopish tugmasi (yuqori o'ng) ---
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), btn -> {
            config.save();
            this.client.setScreen(parent);
        }).dimensions(panelX + panelW - 24, panelY + 6, 18, 18).build());

        rebuildModuleRows();
    }

    private void addCategoryButton(Category cat, String label, int y) {
        boolean selected = selectedCategory == cat;
        Text text = selected
                ? Text.literal("\u25B8 " + label).formatted(Formatting.BOLD).withColor(ACCENT)
                : Text.literal("  " + label).withColor(TEXT_DIM);
        this.addDrawableChild(ButtonWidget.builder(text, btn -> {
            selectedCategory = cat;
            this.init();
        }).dimensions(panelX + 8, y, SIDEBAR_W - 16, 20).build());
    }

    private void rebuildModuleRows() {
        for (ModuleRow row : activeRows) {
            this.remove(row.toggleButton);
        }
        activeRows.clear();

        int contentX = panelX + SIDEBAR_W + 12;
        int contentW = panelW - SIDEBAR_W - 24;
        int y = panelY + 60;

        List<ModuleDef> modules = selectedCategory == Category.FPS ? fpsModules() : visualModules();

        for (ModuleDef m : modules) {
            if (!searchQuery.isEmpty() && !m.name.toLowerCase().contains(searchQuery)) continue;

            ModuleRow row = new ModuleRow();
            row.def = m;
            row.y = y;
            final boolean[] state = {m.getter.get()};
            row.toggleButton = ButtonWidget.builder(
                    toggleLabel(state[0]),
                    btn -> {
                        state[0] = !state[0];
                        m.setter.accept(state[0]);
                        btn.setMessage(toggleLabel(state[0]));
                    }
            ).dimensions(contentX + contentW - 46, y + 3, 46 - 4, 18).build();
            this.addDrawableChild(row.toggleButton);
            activeRows.add(row);
            y += ROW_H;
        }
    }

    private static Text toggleLabel(boolean on) {
        return on
                ? Text.literal("YOQ").withColor(ON_COLOR)
                : Text.literal("O'CH").withColor(OFF_COLOR);
    }

    private List<ModuleDef> fpsModules() {
        List<ModuleDef> list = new ArrayList<>();
        list.add(new ModuleDef("Zarrachalarni kamaytirish", () -> config.reduceParticles, v -> config.reduceParticles = v));
        list.add(new ModuleDef("Bulutlarni soddalashtirish", () -> config.simplifyClouds, v -> config.simplifyClouds = v));
        list.add(new ModuleDef("Tumanni soddalashtirish", () -> config.simplifyFog, v -> config.simplifyFog = v));
        list.add(new ModuleDef("Blok-sindirish effekti", () -> config.disableBlockBreakParticles, v -> config.disableBlockBreakParticles = v));
        return list;
    }

    private List<ModuleDef> visualModules() {
        List<ModuleDef> list = new ArrayList<>();
        list.add(new ModuleDef("Rang filtri", () -> config.enableColorFilter, v -> config.enableColorFilter = v));
        list.add(new ModuleDef("Vinyetka effekti", () -> config.enableVignette, v -> config.enableVignette = v));
        list.add(new ModuleDef("Kamera yumshatish", () -> config.smoothCameraTransitions, v -> config.smoothCameraTransitions = v));
        return list;
    }

    @Override
    public void close() {
        config.save();
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xB3000000);

        context.fill(panelX - 2, panelY - 2, panelX + panelW + 2, panelY + panelH + 2, BORDER);
        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, BG_PANEL);
        context.fill(panelX, panelY, panelX + panelW, panelY + 3, ACCENT);

        context.fill(panelX, panelY + 3, panelX + SIDEBAR_W, panelY + panelH, BG_SIDEBAR);
        context.fill(panelX + SIDEBAR_W, panelY + 3, panelX + SIDEBAR_W + 1, panelY + panelH, BORDER);

        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, Text.literal("SINSATA").formatted(Formatting.BOLD).withColor(ACCENT),
                panelX + 10, panelY + 10, ACCENT, true);
        context.drawText(this.textRenderer, Text.literal("Visuals"), panelX + 64, panelY + 11, TEXT_DIM, false);

        int contentX = panelX + SIDEBAR_W + 12;
        int contentW = panelW - SIDEBAR_W - 24;
        for (ModuleRow row : activeRows) {
            boolean hovered = mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= row.y && mouseY <= row.y + 22;
            int rowColor = hovered ? BG_ROW_HOVER : BG_ROW;
            context.fill(contentX, row.y, contentX + contentW, row.y + 22, rowColor);
            context.fill(contentX, row.y, contentX + 2, row.y + 22, ACCENT_DIM);
            context.drawText(this.textRenderer, Text.literal(row.def.name),
                    contentX + 8, row.y + 7, 0xFFE8E6F0, false);
        }

        if (activeRows.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Hech narsa topilmadi").formatted(Formatting.GRAY),
                    contentX + contentW / 2, panelY + 90, 0xFF6B6B7A);
        }

        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("v1.0.0").formatted(Formatting.GRAY),
                panelX + panelW / 2, panelY + panelH - 12, 0xFF55505F);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class ModuleDef {
        final String name;
        final java.util.function.Supplier<Boolean> getter;
        final Consumer<Boolean> setter;

        ModuleDef(String name, java.util.function.Supplier<Boolean> getter, Consumer<Boolean> setter) {
            this.name = name;
            this.getter = getter;
            this.setter = setter;
        }
    }

    private static class ModuleRow {
        ModuleDef def;
        int y;
        ButtonWidget toggleButton;
    }
}
