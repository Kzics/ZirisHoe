package fr.zirishoe.utils;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public enum CropsState {
    NETHER_FULLY_GROWTH((byte) 3),
    NORMAL_FULLY_GROWTH((byte) 7);



    private byte state;
    CropsState(byte state){
        this.state = state;
    }


    public byte getState() {
        return state;
    }

    public static boolean harvestable(byte state){
        return state == NETHER_FULLY_GROWTH.getState() || state == NORMAL_FULLY_GROWTH.getState();
    }

    public static List<Material> getCrops(){
        return Lists.newArrayList(Material.NETHER_WARTS,Material.CARROT,
                Material.POTATO,Material.WHEAT);
    }
}
