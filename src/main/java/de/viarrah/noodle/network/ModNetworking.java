package de.viarrah.noodle.network;

import de.viarrah.noodle.network.fogBlock.OpenFogScreen;
import de.viarrah.noodle.network.fogBlock.SaveFogSettings;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking {

    private ModNetworking() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SaveFogSettings.TYPE,
                SaveFogSettings.STREAM_CODEC,
                SaveFogSettings::handle
        );

        registrar.playToClient(
                OpenFogScreen.TYPE,
                OpenFogScreen.STREAM_CODEC,
                OpenFogScreen::handle
        );

    }
}
