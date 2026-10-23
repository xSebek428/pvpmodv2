package pl.pvpmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Entrypoint "client": wczytuje config, rejestruje HUD i klawisz menu.
 */
public class PvpModClient implements ClientModInitializer {
    // Klawisz otwierający menu - domyślnie Prawy Shift (można zmienić w Sterowaniu).
    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        PvpConfig.load();

        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvpclient.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.pvpclient"));

        HudRenderCallback.EVENT.register(PvpHud::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new PvpConfigScreen(null));
            }
        });
    }
}
