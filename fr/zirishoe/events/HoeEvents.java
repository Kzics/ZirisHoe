package fr.zirishoe.events;

import fr.zirishoe.Main;
import fr.zirishoe.utils.ActionMessage;
import fr.zirishoe.utils.ColorsUtil;
import fr.zirishoe.utils.CropsState;
import fr.zirishoe.utils.HoeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class HoeEvents implements Listener {


    public static List<UUID> waitingMessage = new ArrayList<>();
    private final Main instance;


    public HoeEvents(final Main instance){
        this.instance = instance;

    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();

        if(e.getClickedBlock() == null) return;

        if(e.getItem().getType().equals(Material.DIAMOND_HOE) && e.getItem().getItemMeta().hasLore()) {
            int level = Integer.parseInt(HoeUtils.
                    getIntegerInString(e.getItem()
                            .getItemMeta().getDisplayName()));

            if (e.getClickedBlock().getType().equals(Material.NETHER_WARTS)
                    && e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getData() == CropsState.FULLY_GROWTH.getState()) {
                e.getClickedBlock().setData((byte) 0);

                this.breakRadius(player,e.getClickedBlock(), level-1);
                e.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        String[] levelList = {"1","2","3","4","5"};
        Player player = e.getPlayer();

        ItemStack handItem = player.getItemInHand();

        if(handItem.hasItemMeta() && HoeUtils.isHoeItem(handItem, HoeUtils
                .getIntegerInString(handItem.getItemMeta().getDisplayName()))) {
            if (e.getBlock().getData() != CropsState.FULLY_GROWTH.getState()
                    && Arrays.asList(levelList).contains(HoeUtils.
                    getIntegerInString(handItem
                            .getItemMeta().getDisplayName()))) {
                e.setCancelled(true);

            }
        }
    }


    private void breakRadius(Player player,Block block,int radius){
        for(int x = -radius;x<=radius;x++){
            for(int z = -radius;z<=radius;z++){
                Location loc = new Location(player.getWorld(),block.getLocation().getBlockX() +x
                        ,block.getY(),block.getLocation().getBlockZ()+z);
                Block bl = player.getWorld().getBlockAt(loc);
                if(bl.getType().equals(Material.NETHER_WARTS)) {
                    if(!HoeUtils.canPick(player.getInventory(),new ItemStack(Material.NETHER_STALK))){
                        if(!waitingMessage.contains(player.getUniqueId())) {
                            new ActionMessage("Erreur: Inventaire remplis").sendMessage(player);
                            waitingMessage.add(player.getUniqueId());
                            Bukkit.getScheduler().runTaskLater(instance, () -> {
                                waitingMessage.remove(player.getUniqueId());

                            },60);
                        }
                        return;
                    }
                    bl.setData((byte) 0);
                    this.givePlayerDrops(player,radius);
                    this.addBrokenBlock(player);
                }
            }

        }
    }

    private void addBrokenBlock(Player player){
        String ownerName = player.getItemInHand().hasItemMeta() ?
                player.getItemInHand().getItemMeta().getLore().get(1).split(" ")[1]:
                null;
        String level = HoeUtils.getIntegerInString(player.getItemInHand().getItemMeta().getDisplayName());

        List<String> lore = player.getItemInHand().getItemMeta().getLore();

        int blocks = instance.getConfig().get("hoeBlocks." + ownerName + "." + HoeUtils.getTag(player.getItemInHand())) == null ? 0:
                instance.getConfig().getInt("hoeBlocks." + ownerName+ "." + HoeUtils.getTag(player.getItemInHand()));

        lore.set(3, ColorsUtil.translate.apply(ColorsUtil.translate.apply("&7Niveau Sup:&f "
                +blocks+"/"+ Integer.parseInt(level)*20000)));

        ItemMeta meta = player.getItemInHand().getItemMeta();
        meta.setLore(lore);
        player.getItemInHand().setItemMeta(meta);
        player.updateInventory();

        instance.getConfig().set("hoeBlocks." + ownerName+ "." + HoeUtils.getTag(player.getItemInHand()),blocks + 1);
        instance.saveConfig();
    }


    public void givePlayerDrops(Player player,int level){
        int amount = this.calcDrops(level);
        while(amount == 0){
            amount = this.calcDrops(level);
        }

        for (int i = 0; i <amount ; i++) {
            player.getInventory().addItem(new ItemStack(Material.NETHER_STALK));
        }
    }


    private int calcDrops(int level){
        Random random = new Random();

        return random.nextInt(2* level+1);

    }




}
