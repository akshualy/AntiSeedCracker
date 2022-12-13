package me.gadse.antiseedcracker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public final class AntiSeedCracker extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Server.LOGIN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                try {
                    packet.getLongs().write(
                            0, packet.getLongs().read(0) + ThreadLocalRandom.current().nextInt(-9999, 10000)
                    );
                } catch (FieldAccessException ex) {
                    ex.printStackTrace();
                }
                event.setPacket(packet);
            }
        });
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Server.RESPAWN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                try {
                    packet.getLongs().write(
                            0, packet.getLongs().read(0) + ThreadLocalRandom.current().nextInt(-9999, 10000)
                    );
                } catch (FieldAccessException ex) {
                    ex.printStackTrace();
                }
                event.setPacket(packet);
            }
        });
    }

    @Override
    public void onDisable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.removePacketListeners(this);
    }
}
