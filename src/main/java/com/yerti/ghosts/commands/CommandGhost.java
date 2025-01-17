package com.yerti.ghosts.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.yerti.ghosts.Ghosts;
import com.yerti.ghosts.GhostsAPI;
import com.yerti.ghosts.core.commands.CustomCommand;
import com.yerti.ghosts.core.utils.CenterFontUtil;
import com.yerti.ghosts.event.EventTimer;
import com.yerti.ghosts.gui.ItemInputInventory;
import com.yerti.ghosts.utils.Utilities;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CommandGhost extends CustomCommand {

    private Ghosts plugin;
    private Utilities utilities;
    private Map<String, Location> locations = new HashMap<>();

    public CommandGhost(Ghosts plugin, String command, String permission) {
        super(command, permission);
        this.plugin = plugin;
        this.utilities = new Utilities();
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (GhostsAPI.getGhostEntity() != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', utilities.getPrefix() + " &cThere is already a ghost spawned!"));
                } else if (EventTimer.currentTime >= 60) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', utilities.getPrefix() + " &7The next ghost will be in &c" + new DecimalFormat("#.##").format(EventTimer.currentTime / 60.) + " &7minutes."));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', utilities.getPrefix() + " &7The next ghost will be in &c" + EventTimer.currentTime + " &7seconds."));
                    ;
                }
                return true;
            } else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("top")) {

                    MongoCollection<Document> collection = plugin.getMongoDatabase().getCollection("leaderboards");

                    FindIterable<Document> iterDoc = collection.find().sort(new BasicDBObject("kills", -1)).limit(10);

                    int index = 1;
                    Iterator<Document> iterator = iterDoc.iterator();


                    player.sendMessage(CenterFontUtil.centerString(ChatColor.GRAY + "" + ChatColor.BOLD + "TOP GHOST KILLS"));
                    String format = "";

                    if (!iterator.hasNext()) {
                        player.sendMessage(utilities.getPrefix() + ChatColor.RED + " There currently aren't any ghost kills. Come back later!");
                        return true;
                    }

                    while (iterator.hasNext()) {
                        Document document = iterator.next();

                        format += CenterFontUtil.centerString(ChatColor.DARK_GRAY + "" + index + ". " + ChatColor.RED + plugin.getFetcher().fetchNameBlocking(UUID.fromString(document.getString("uuid"))) + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.RED + document.getInteger("kills")) + "\n";

                        index++;
                    }


                    player.sendMessage(format);
                    return true;
                }

            }

            if (player.hasPermission("ghosts.admin") || player.isOp()) {



                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        for (String string : utilities.helpMessage()) {
                            player.sendMessage(string);
                        }
                    } else if (args[0].equalsIgnoreCase("listlocations")) {
                        if (locations.size() == 0) {
                            player.sendMessage(utilities.noLocationsMessage());
                        }

                        //'&8[name] > &7[location]'
                        for (String name : locations.keySet()) {

                            Location location = locations.get(name);
                            player.sendMessage(utilities.getListLocationFormat().replace("[name]", name).replace("[location]", location.getWorld().getName() + ", " + location.getX() + ", " + location.getY() + ", " + location.getZ()));
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        utilities.reloadPlugin();
                        player.sendMessage(utilities.reloadSuccessMessage());
                    } else if (args[0].equalsIgnoreCase("forcespawn")) {
                        //TODO: Change from static
                        EventTimer.spawnGhost();

                    } else {
                        player.sendMessage(utilities.incorrectUsageMessage().replace("[usage]", "/ghost top|forcespawn|addlocation|removelocation|rewards|listlocations|reload"));
                    }
                    return true;

                }


                if (args.length == 2) {

                    Location loc = player.getLocation();

                    switch (args[0].toLowerCase()) {
                        case "addlocation":
                            if (locations.keySet().contains(args[1])) {
                                player.sendMessage(utilities.locationAlreadyExistsMessage(args[1]));
                            } else {
                                locations.put(args[1], loc);
                                player.sendMessage(utilities.addedLocationMessage(args[1]));
                            }
                            break;

                        case "removelocation":
                            if (locations.keySet().contains(args[1])) {
                                locations.remove(args[1]);
                                if (utilities.locationExists(args[1])) {
                                    utilities.setRemovedLocation(args[1]);
                                }


                                player.sendMessage(utilities.removedLocationMessage(args[1]));
                            } else {
                                player.sendMessage(utilities.noLocationMessage(args[1]));
                            }
                            break;
                        case "rewards":
                            if (!args[1].matches("[0-9]+")) {
                                player.sendMessage(utilities.incorrectUsageMessage().replace("[usage]", "/ghost addlocation|removelocation|rewards|listlocations|reload"));
                                return true;
                            }

                            if (Integer.parseInt(args[1]) > 0 || Integer.parseInt(args[1]) > 3) {
                                ItemInputInventory inventory = new ItemInputInventory();
                                player.openInventory(inventory.getInventory(Integer.parseInt(args[1])));
                                player.sendMessage(utilities.rewardsGuiMessage(Integer.parseInt(args[1])));
                            } else {
                                player.sendMessage(utilities.tierNotFoundMessage(Integer.parseInt(args[1])));
                            }
                            break;
                        case "reload":

                        default:
                            player.sendMessage(utilities.incorrectUsageMessage().replace("[usage]", "/ghost addlocation|removelocation|rewards|listlocations|reload"));
                            return true;
                    }

                } else {
                    player.sendMessage(utilities.incorrectUsageMessage().replace("[usage]", "/ghost addlocation|removelocation|rewards|listlocations|reload"));
                    return true;

                }


            }

        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("forcespawn")) {
                    EventTimer.spawnGhost();
                    System.out.println("Spawned a ghost.");
                }
            }
        }

        return false;
    }

    //TODO: Use reflection and move to utilities
    private void noAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tagCompound = nmsEntity.getNBTTag();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        nmsEntity.c(tagCompound);
        tagCompound.setInt("NoAI", 1);
        nmsEntity.f(tagCompound);
    }

    public void updateHealthName(LivingEntity entity) {
        entity.setCustomNameVisible(true);
        entity.setCustomName("" + ChatColor.RED + ((int) entity.getHealth()) + " \u2764");
    }

}
