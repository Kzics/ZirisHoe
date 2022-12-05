package fr.zirishoe;

import fr.zirishoe.utils.ColorsUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;

public enum HoeMessages {

    MULTIPLIER("{multiple}"),
    RADIUS("{radius}"),
    MAX_MINE("{levelmax}"),
    AUTOSELL("{autosell}"),
    OWNER("{owner}");


    private String str;
    HoeMessages(String str){
        this.str = str;
    }


    public String toString() {
        return str;
    }

    public static HashMap<String,String> getValues(String level, Player player){
        HashMap<String,String> val = new HashMap<>();
        val.put(MULTIPLIER.toString(),"x" + level);
        val.put(RADIUS.toString(), level + "x" + level);
        val.put(MAX_MINE.toString(), String.valueOf(20000*Integer.parseInt(level)));
        val.put(AUTOSELL.toString(), Integer.parseInt(level) == 5 ? ColorsUtil.translate.apply("&a✓"):
                ColorsUtil.translate.apply("&cx"));
        val.put(OWNER.toString(), player.getDisplayName());

        return val;

    }
}
