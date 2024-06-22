package me.gadse.antiseedcracker.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.FieldAccessException;
import me.gadse.antiseedcracker.AntiSeedCracker;

public class ServerRespawn extends PacketAdapter {

    private final AntiSeedCracker plugin;

    public ServerRespawn(AntiSeedCracker plugin) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.RESPAWN);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        try {
            int structureSize = packet.getStructures().size();
            if (structureSize == 0) {
                plugin.getLogger().warning(
                        "Can not write hashed seed at respawn for player " + event.getPlayer().getName() + "."
                );
                return;
            }
            InternalStructure structureModifier = packet.getStructures().read(structureSize - 1);
            structureModifier.getLongs().write(
                    0, plugin.randomizeHashedSeed(structureModifier.getLongs().read(0))
            );
        } catch (FieldAccessException | NullPointerException ex) {
            // FieldAccessException is caused by old versions of Minecraft
            // NPE is caused by old versions of ProtocolLib
            packet.getLongs().write(0, plugin.randomizeHashedSeed(packet.getLongs().read(0)));
        }
        event.setPacket(packet);
    }
}
