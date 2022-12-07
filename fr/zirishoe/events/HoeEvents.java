package fr.zirishoe.events;

import fr.zirishoe.Main;
import fr.zirishoe.manager.MoneyManager;
import fr.zirishoe.utils.ActionMessage;
import fr.zirishoe.utils.ColorsUtil;
import fr.zirishoe.utils.CropsState;
import fr.zirishoe.utils.HoeUtils;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
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

        if(e.getClickedBlock() == null || e.getItem() == null) return;

        if(e.getItem().getType().equals(Material.DIAMOND_HOE) && e.getItem().getItemMeta().hasLore()) {
            int level = Integer.parseInt(HoeUtils.
                    getIntegerInString(e.getItem()
                            .getItemMeta().getDisplayName()));

            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && CropsState.harvestable(e.getClickedBlock().getData())) {
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

        if(handItem.hasItemMeta() && HoeUtils.isHoeItem(handItem, HoeUtils.getIntegerInString(handItem.getItemMeta().getDisplayName()))) {
            e.setCancelled(true);
        }
    }




    private void breakRadius(Player player,Block block,int radius){
        for(int x = -radius/2;x<=radius/2;x++){
            for(int z = -radius/2;z<=radius/2;z++){
                Location loc = new Location(player.getWorld(),block.getLocation().getBlockX() +x
                        ,block.getY(),block.getLocation().getBlockZ()+z);
                Block bl = player.getWorld().getBlockAt(loc);

                if(!CropsState.getCrops().contains(bl.getType())) continue;

                    if(!HoeUtils.canPick(player.getInventory(),new ItemStack(bl.getType() == Material.NETHER_WARTS ? Material.NETHER_STALK:
                            bl.getType()))){
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

                    this.addBrokenBlock(player);

                    if((radius+1) == 5) {
                        MoneyManager.addMoneyTo(player.getDisplayName(), 10);
                    }else{
                        this.givePlayerDrops(player,radius,bl.getType() == Material.NETHER_WARTS ? Material.NETHER_STALK:
                                bl.getType());

                    }
                }
            }
    }

    private void addBrokenBlock(Player player){
        String ownerName = HoeUtils.getHoeOwner(player.getItemInHand());
        String level = HoeUtils.getIntegerInString(player.getItemInHand().getItemMeta().getDisplayName());

        List<String> lore = player.getItemInHand().getItemMeta().getLore();

        ItemStack handItem = player.getItemInHand();

        int blocks = instance.getConfig().getInt("hoeBlocks." + HoeUtils.trueOwnerName(ownerName) +"." + HoeUtils
                .getItemStackTag(player.getItemInHand()));


        lore.set(3, ColorsUtil.translate.apply(ColorsUtil.translate.apply("&7Niveau Sup:&f "
                +blocks+"/"+ Integer.parseInt(level)*20000)));

        ItemMeta meta = handItem.getItemMeta();
        meta.setLore(lore);
        player.getItemInHand().setItemMeta(meta);
        player.updateInventory();


        instance.getConfig().set("hoeBlocks." + HoeUtils.trueOwnerName(ownerName) + "." + HoeUtils
                .getItemStackTag(player.getItemInHand()),blocks + 1);
        instance.saveConfig();
    }


    public void givePlayerDrops(Player player,int level,Material mat){
        int amount = this.calcDrops(level);

        for (int i = 0; i <amount ; i++) {
            player.getInventory().addItem(new ItemStack(mat != Material.NETHER_STALK ?
                    Material.matchMaterial(mat.name() + "_ITEM"): Material.NETHER_STALK));
        }
    }


    private int calcDrops(int level){
        Random random = new Random();

        return random.nextInt(3* level+1);

    }

    private int getOwnerHoeSize(String ownerName){
        int count = 0;
        for(String str: instance.getConfig().getConfigurationSection("hoeBlocks." + ownerName).getKeys(false)){
            count++;
        }

        return count;

    }




}
