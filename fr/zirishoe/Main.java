package fr.zirishoe;

import fr.zirishoe.commands.HoeCommand;
import fr.zirishoe.events.HoeEvents;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {


    private static Main instance;


    @Override
    public void onEnable() {
        instance = this;
        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdir();
        }

        PluginManager pm = Bukkit.getPluginManager();



        if(!configExists()){
            createFile();
        }
        pm.registerEvents(new HoeEvents(this), this);

        getCommand("hoegive").setExecutor(new HoeCommand(this));
    }


    private void createFile() {
        File file = new File(this.getDataFolder(), "config.yml");
        try {
            file.createNewFile();
            FileUtils.copyInputStreamToFile(this.getResource("config.yml"), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean configExists() {
        return new File(this.getDataFolder(), "config.yml").exists();
    }

    public static Main getInstance(){
        return instance;
    }
}
