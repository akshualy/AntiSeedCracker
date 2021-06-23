package me.gadse.antiseedcracker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiSeedCracker extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs())
            getLogger().severe("Could not create data folders. Config might not get created.");
        saveDefaultConfig();

        PluginCommand ascCommand = getCommand("antiseedcracker");
        if (ascCommand != null) ascCommand.setExecutor(new ASCCommand(this));

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Server.LOGIN) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                try {
                    packet.getLongs().write(0, getConfig().getLong("replacing-seed"));
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
                    packet.getLongs().write(0, getConfig().getLong("replacing-seed"));
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
