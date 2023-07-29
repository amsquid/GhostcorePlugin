package me.squid;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GhostsFile extends File {

    Plugin plugin;

    public GhostsFile(String pathname) throws IOException {
        super(pathname);

        plugin = Bukkit.getPluginManager().getPlugin(Ghostcore.id);

        if(createNewFile()) {
            plugin.getLogger().info("Created file at " + getAbsolutePath());

            overwriteFile("{}");
        } else {
            plugin.getLogger().info("Couldn't create file at " + getAbsolutePath());
            plugin.getLogger().info("Possibly because the file already exists");
        }
    }

    public JsonObject getJson() {
        StringBuilder lines = new StringBuilder();

        try {
            Scanner reader = new Scanner(this);

            while(reader.hasNextLine()) {
                String line = reader.nextLine();

                lines.append(line);
            }

            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        return gson.fromJson(lines.toString(), JsonObject.class);
    }


    public JsonObject getPlayer(String playerID) throws IOException {
        JsonObject jsonData = getJson();

        plugin.getLogger().info("Getting player with ID " + playerID);

        if(!jsonData.has(playerID)) {
            JsonElement blank = new JsonObject();

            jsonData.add(playerID, blank);

            plugin.getLogger().info("Adding player with ID " + playerID + " to " + getName());

            overwriteFile(jsonData.toString());
        }

        jsonData = jsonData.get(playerID).getAsJsonObject();

        return jsonData;
    }

    public void setPlayer(String playerID, String key, String value) throws IOException {
        JsonObject playerData = getPlayer(playerID);

        playerData.addProperty(key, value);

        try {
            overwriteFile(playerData.getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void overwriteFile(String data) throws IOException {
        FileWriter writer = new FileWriter(getAbsoluteFile());

        writer.write(data);

        writer.close();
    }
}
