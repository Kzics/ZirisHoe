package fr.zirishoe.events;

import fr.zirishoe.utils.ColorsUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class AnvilEvents implements Listener {


    @EventHandler
    public void onAnvil(InventoryClickEvent e){
        Player player =(Player) e.getWhoClicked();
        if(e.getInventory() instanceof AnvilInventory){
            String item = e.getCurrentItem().hasItemMeta() ? e.getCurrentItem().getItemMeta().getDisplayName()
                    : null;

            InventoryView view = e.getView();
            int slots = e.getRawSlot();

            if (slots == view.convertSlot(slots) && slots == 2){
                if(e.getInventory().getItem(slots).getItemMeta().hasLore()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(item)) {
                        e.getInventory().remove(e.getCurrentItem());
                        ItemStack itemStack = e.getInventory().getItem(0);
                        player.getInventory().addItem(itemStack);
                        e.getInventory().remove(itemStack);

                        e.setCancelled(true);

                        player.sendMessage(ColorsUtil.translate.apply("&cImpossible faire cela ! "));
                    }
                }

            }
        }
    }
}
