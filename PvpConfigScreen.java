package pl.pvpmod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Menu ustawień (otwierane klawiszem Prawy Shift).
 * Każdy przycisk przełącza jedną funkcję. Aby dodać kolejną opcję,
 * dodaj wywołanie addToggle(...) w init().
 */
public class PvpConfigScreen extends Screen {
    private final Screen parent;

    public PvpConfigScreen(Screen parent) {
        super(Text.literal("PvP Client - ustawienia"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 200, h = 20, x = this.width / 2 - w / 2;
        int y = this.height / 2 - 55;

        addToggle(x, y,       w, h, "Armor HUD",   () -> PvpConfig.ARMOR_HUD,        v -> PvpConfig.ARMOR_HUD = v);
        addToggle(x, y + 25,  w, h, "Potion HUD",  () -> PvpConfig.POTION_HUD,       v -> PvpConfig.POTION_HUD = v);
        addToggle(x, y + 50,  w, h, "Własny celownik", () -> PvpConfig.CUSTOM_CROSSHAIR, v -> PvpConfig.CUSTOM_CROSSHAIR = v);
        addToggle(x, y + 75,  w, h, "No Hurt Cam", () -> PvpConfig.NO_HURT_CAM,      v -> PvpConfig.NO_HURT_CAM = v);

        addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> close())
                .dimensions(x, y + 110, w, h).build());
    }

    private void addToggle(int x, int y, int w, int h, String name, BooleanSupplier getter, Consumer<Boolean> setter) {
        addDrawableChild(ButtonWidget.builder(label(name, getter.getAsBoolean()), b -> {
            boolean now = !getter.getAsBoolean();
            setter.accept(now);
            b.setMessage(label(name, now));
        }).dimensions(x, y, w, h).build());
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "§aWŁĄCZONY" : "§cWYŁĄCZONY"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        PvpConfig.save(); // zapis przy zamknięciu menu
        this.client.setScreen(parent);
    }
}
