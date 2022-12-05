package fr.zirishoe.utils;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionMessage {

    private final String message;

    public ActionMessage(final String message){
        this.message = message;
    }


    public void sendMessage(Player player){
        IChatBaseComponent messageComp = IChatBaseComponent.ChatSerializer.a("{\n" +
                "  \"text\":\n" +
                "    \""+this.message + "\"\n" +
                "}");

        PacketPlayOutChat playOutChat = new PacketPlayOutChat(messageComp,(byte) 2);

        PlayerConnection playerConnection = ((CraftPlayer)player).getHandle().playerConnection;

        playerConnection.sendPacket(playOutChat);
    }
}
