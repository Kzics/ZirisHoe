package fr.zirishoe.utils;

import fr.zirishoe.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HoeUtils {





    public static boolean isHoeItem(ItemStack it,String level){
        if(it.hasItemMeta() && it.getType().equals(Material.DIAMOND_HOE)){
            String firstLore = Main.getInstance().getConfig().getString("hoe.Name")
                    .replace("{level}",level);
            if(it.getItemMeta().getDisplayName().equals(firstLore)){
                return true;
            }

        }
        return false;
    }

    public static NBTTagCompound getTag(ItemStack item){
        return CraftItemStack.asNMSCopy(item).getTag();
    }
    public static boolean isFull(Inventory inv){
        return inv.firstEmpty() == -1;
    }

    public static boolean canPick(Inventory inv, ItemStack it){
        if(isFull(inv)){
            for(ItemStack item: inv.getContents()){
                if(item != null) {
                    if (item.getType().equals(it.getType())) {
                        if (item.getAmount() < 64) {
                            return true;
                        }
                    }
                }
            }
        }else{
            return true;
        }
        return false;
    }

    public static String getIntegerInString(String str){
        for(char c: str.toCharArray()){
            if(isInteger(String.valueOf(c))){
                return String.valueOf(c);
            }
        }
        return "1";
    }

    public static boolean isInteger(String str){
        try{

            Integer.parseInt(str);

            return true;
        }catch (Exception e){
            return false;
        }

    }
}
