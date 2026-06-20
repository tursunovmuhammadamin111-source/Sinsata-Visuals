package uz.sinsata.visuals.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Sinsata Visuals sozlamalari.
 * Barcha maydonlar - sof kosmetik/optimizatsiya tugmalari, cheat emas.
 */
public class VisualsConfig {

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("sinsatavisuals.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // ----- FPS OPTIMIZATSIYASI -----
    public boolean reduceParticles = true;      // zarrachalar sonini kamaytirish
    public int particleReductionPercent = 60;   // necha foizga kamaytirish (0-100)
    public boolean simplifyClouds = true;       // bulutlarni "fast" rejimga o'tkazish
    public boolean simplifyFog = true;          // tumanni soddalashtirish
    public boolean disableBlockBreakParticles = false; // blok sindirish effektlarini o'chirish

    // ----- VIZUAL (KOSMETIK) -----
    public boolean enableColorFilter = false;   // yengil rang filtri (post-process)
    public float colorFilterWarmth = 0.0f;      // -1.0 (sovuq) ... 1.0 (issiq)
    public boolean enableVignette = false;      // ekran chetlarini sal qoraytirish
    public boolean smoothCameraTransitions = true; // kamera harakatini yumshatish

    public static VisualsConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                VisualsConfig cfg = GSON.fromJson(reader, VisualsConfig.class);
                if (cfg != null) {
                    return cfg;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        VisualsConfig fresh = new VisualsConfig();
        fresh.save();
        return fresh;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
