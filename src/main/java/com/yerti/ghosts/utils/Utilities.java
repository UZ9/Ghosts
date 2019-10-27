package com.yerti.ghosts.utils;

import com.yerti.ghosts.Ghosts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilities {

    private static Plugin plugin;
    private String prefix;
    private String noPermissionMessage;
    private String nonPlayerMessage;
    private List<String> helpMessage;
    private String incorrectUsage;
    private String addedLocation;
    private String locationAlreadyExists;
    private String removedLocation;
    private String listLocationFormat;
    private String rewardsGuiMessage;
    private String tierNotFound;
    private String rewardsGuiTitle;
    private String noLocation;
    private String reloadSuccess;
    private String savedTierContents;
    private String eventDeathMessage;
    private String eventMobSpawn;
    private String noLocations;

    private EntityType entityType;
    private int eventInterval;




    public Utilities(Plugin plugin) {
        this.plugin = plugin;

    }

    public Utilities() {
        this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix")).replaceAll("\n", "\n");
        this.noPermissionMessage = formattedConfigValue("no-permission");
        this.nonPlayerMessage = formattedConfigValue("non-player");
        this.helpMessage = getFormattedList("help-message");
        this.incorrectUsage = formattedConfigValue("incorrect-usage");
        this.addedLocation = formattedConfigValue("added-location");
        this.locationAlreadyExists = formattedConfigValue("location-already-exists");
        this.removedLocation = formattedConfigValue("removed-location");
        this.listLocationFormat = formattedConfigValue("list-locations-format");
        this.rewardsGuiMessage = formattedConfigValue("rewards-gui-message");
        this.tierNotFound = formattedConfigValue("tier-not-found");
        this.rewardsGuiTitle = formattedConfigValue("rewards-gui-title");
        this.noLocation = formattedConfigValue("no-location");
        this.reloadSuccess = formattedConfigValue("reload-success");
        this.savedTierContents = formattedConfigValue("closed-rewards-editor");
        this.eventDeathMessage = formattedConfigValue("event-death-message");
        this.eventMobSpawn = formattedConfigValue("event-mob-spawn");
        this.noLocations = formattedConfigValue("list-no-locations");

        this.entityType = EntityType.valueOf(plugin.getConfig().getString("mob-type"));
        this.eventInterval = plugin.getConfig().getInt("event-interval") * 60;
    }

    public String getPrefix() {
        return prefix;
    }

    public String noLocationsMessage() {
        return noLocations;
    }

    public String savedTierContentsMessage() {
        return savedTierContents;
    }

    public String eventMobSpawnMessage() {
        return eventMobSpawn;
    }

    public int getEventInterval() {
        return eventInterval;
    }

    public int getMaxHealth(int tier) {
        return plugin.getConfig().getInt("tiers." + tier + ".health");
    }

    public String eventDeathMessage(int tier) {
        return eventDeathMessage.replace("[tier]", String.valueOf(tier));
    }

    public EntityType getEntityType() {
        return entityType;
    }

    private List<String> getFormattedList(String value) {
        List<String> result = new ArrayList<>();

        for (String line : plugin.getConfig().getStringList(value)) {
            result.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return result;
    }

    public void setRemovedLocation(String key) {
        plugin.getConfig().set("locations." + key, null);
        plugin.saveConfig();
    }

    public void reloadPlugin() {
        plugin.reloadConfig();
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
    }

    public boolean locationExists(String locationName) {
        return plugin.getConfig().get("locations." + locationName) != null;
    }

    //TODO: Replace with static final
    public String reloadSuccessMessage() {
        return reloadSuccess;
    }

    public String noLocationMessage(String name) {
        return noLocation.replace("[name]", name);
    }

    public String addedLocationMessage(String name) {
        return addedLocation.replace("[name]", name);
    }

    public String locationAlreadyExistsMessage(String name) {
        return locationAlreadyExists.replace("[name]", name);
    }

    public String removedLocationMessage(String name) {
        return removedLocation.replace("[name]", name);
    }

    public String getListLocationFormat() {
        return listLocationFormat;
    }

    public String rewardsGuiMessage(int tier) {
        return rewardsGuiMessage.replace("[tier]", String.valueOf(tier));
    }

    public String tierNotFoundMessage(int tier) {
        return tierNotFound.replace("[tier]", String.valueOf(tier));
    }

    public String getRewardsGuiTitle() {
        return rewardsGuiTitle;
    }

    private String formattedConfigValue(String value) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(value)).replace("[prefix]", prefix);
    }

    public String noPermMessage() {
        return noPermissionMessage;
    }

    public String nonPlayerMessage() {
        return nonPlayerMessage;
    }

    public List<String> helpMessage() {
        return helpMessage;
    }

    public String incorrectUsageMessage() {
        return incorrectUsage;
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



}
