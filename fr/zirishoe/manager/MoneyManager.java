package fr.zirishoe.manager;

import fr.zirishoe.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MoneyManager {




    public static double getMoneyOf(OfflinePlayer player){
        return Main.getEcon().getBalance(player);
    }
    public static double getMoneyOf(String player){
        return Main.getEcon().getBalance(player);
    }
    public static void addMoneyTo(OfflinePlayer player, int amount){
        Main.getEcon().depositPlayer(player,amount);
    }
    public static void addMoneyTo(String player, int amount){
        Main.getEcon().depositPlayer(player,amount);
    }
    public static void removeMoneyTo(OfflinePlayer player,int amount){
        Main.getEcon().withdrawPlayer(player,amount);
    }
    public static void removeMoneyTo(Player player, int amount){
        Main.getEcon().withdrawPlayer(player,amount);
    }
}
