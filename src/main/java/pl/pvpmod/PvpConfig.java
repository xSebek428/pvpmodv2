package pl.pvpmod;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * USTAWIENIA MODA.
 * - Włączniki funkcji (ARMOR_HUD itd.) zmieniasz w grze: klawisz Prawy Shift -> menu.
 *   Zapisują się w pliku config/pvpclient.properties.
 * - Pozycje i kolory edytujesz tutaj w kodzie (kolory w formacie 0xAARRGGBB).
 */
public final class PvpConfig {
    private PvpConfig() {}

    // ---- Włączniki funkcji (zmieniane z menu w grze) ----
    public static boolean ARMOR_HUD = true;
    public static boolean POTION_HUD = true;
    public static boolean CUSTOM_CROSSHAIR = true;
    public static boolean NO_HURT_CAM = true; // true = brak trzęsienia ekranu po dostaniu obrażeń

    // ---- Armor HUD ----
    // X: odstęp od lewej krawędzi. Y_OFFSET: przesunięcie względem środka ekranu w pionie
    // (ujemne = wyżej, dodatnie = niżej).
    public static final int ARMOR_HUD_X = 4;
    public static final int ARMOR_HUD_Y_OFFSET = -50;
    public static final boolean ARMOR_SHOW_EMPTY_SLOTS = false;

    // ---- Potion HUD ----
    public static final int POTION_HUD_X = 4;
    public static final int POTION_HUD_Y = 4;
    public static final int POTION_LOW_TIME_SECONDS = 10;
    public static final int POTION_LOW_TIME_COLOR = 0xFFFF5555;
    public static final int POTION_TIME_COLOR = 0xFFFFFFFF;

    // ---- Celownik (plusik) ----
    public static final int CROSSHAIR_COLOR = 0xFFFFFFFF;   // np. 0xFF00FF00 = zielony
    public static final int CROSSHAIR_ARM = 4;
    public static final int CROSSHAIR_THICKNESS = 1;
    public static final boolean CROSSHAIR_OUTLINE = true;
    public static final int CROSSHAIR_OUTLINE_COLOR = 0xFF000000;

    // ---- Zapis / odczyt włączników ----
    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("pvpclient.properties");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) return;
        try (Reader r = Files.newBufferedReader(f)) {
            Properties p = new Properties();
            p.load(r);
            ARMOR_HUD = Boolean.parseBoolean(p.getProperty("armorHud", "true"));
            POTION_HUD = Boolean.parseBoolean(p.getProperty("potionHud", "true"));
            CUSTOM_CROSSHAIR = Boolean.parseBoolean(p.getProperty("customCrosshair", "true"));
            NO_HURT_CAM = Boolean.parseBoolean(p.getProperty("noHurtCam", "true"));
        } catch (IOException e) {
            PvpMod.LOGGER.warn("Nie udało się wczytać configu", e);
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("armorHud", String.valueOf(ARMOR_HUD));
        p.setProperty("potionHud", String.valueOf(POTION_HUD));
        p.setProperty("customCrosshair", String.valueOf(CUSTOM_CROSSHAIR));
        p.setProperty("noHurtCam", String.valueOf(NO_HURT_CAM));
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "PvP Client");
        } catch (IOException e) {
            PvpMod.LOGGER.warn("Nie udało się zapisać configu", e);
        }
    }
}
