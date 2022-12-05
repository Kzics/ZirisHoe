package fr.zirishoe.commands;

import fr.zirishoe.HoeMessages;
import fr.zirishoe.Main;
import fr.zirishoe.utils.ColorsUtil;
import fr.zirishoe.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.xml.transform.Result;
import java.util.*;
import java.util.stream.IntStream;

public class HoeCommand implements CommandExecutor {



    private final Main instance;
    public HoeCommand(final Main instance){
        this.instance = instance;

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;


        if(args.length == 2) {
            if (label.equalsIgnoreCase("hoegive")) {
                if(!this.isInteger(args[0])){
                    player.sendMessage(ColorsUtil.translate.apply("&cVeuillez entrer un niveau correct !"));
                    return false;
                }
                String level = (args[0]);
                List<String> loreList = instance.getConfig().getStringList("hoe.Lore");

                this.setCustomLore(loreList,level,player);

                ItemStack hoeItem = new ItemUtils(Material.DIAMOND_HOE,1)
                        .setDisplayName(instance.getConfig().getString("hoe.Name")
                                .replace("{level}",level))
                        .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .addLores(loreList)
                        .build();

                System.out.println(hoeItem.hashCode());

                player.getInventory().addItem(hoeItem);

            }
        }

        return false;
    }


    public boolean isInteger(String num){
        try{

            Integer.parseInt(num);

            return true;

        }catch (Exception e){
            return false;
        }

    }

    private void setCustomLore(List<String> loreList,String level,Player player){


        HashMap<String,String> messageKeys = HoeMessages.getValues(level,player);


        for(Map.Entry<String,String> entry: messageKeys.entrySet()){
            if(this.hasKeyMessage(loreList,entry.getKey())){
                int index = this.getKeyMessageIndex(loreList,entry.getKey());
                String newLine = loreList.get(index)
                        .replace(entry.getKey(),entry.getValue());
                loreList.set(index,ColorsUtil.translate.apply(newLine));
            }

        }


    }

    private boolean hasKeyMessage(List<String> lore,String key){
        return IntStream.range(0,lore.size())
                .anyMatch(i-> lore.get(i).contains(key));
    }

    private int getKeyMessageIndex(List<String> lore,String key){
        return IntStream.range(0, lore.size())
                .filter(i -> lore.get(i).contains(key))
                .findFirst()
                .orElse(-1); //Empeche OptionalInt
    }
}
