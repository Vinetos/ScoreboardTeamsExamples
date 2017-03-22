package fr.vinetos.team;
/*
 * ==============================================================================
 *            _    _______   __________________  _____
 *            | |  / /  _/ | / / ____/_  __/ __ \/ ___/
 *            | | / // //  |/ / __/   / / / / / /\__ \
 *            | |/ // // /|  / /___  / / / /_/ /___/ /
 *            |___/___/_/ |_/_____/ /_/  \____//____/
 *
 * ==============================================================================
 *
 * ScoreboardTeamsExamples game
 * Copyright (c) Vinetos Software 2017,
 * By Vinetos, mars 2017
 * 
 * ==============================================================================
 * 
 * This file is part of ScoreboardTeamsExamples game.
 * 
 * ScoreboardTeamsExamples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScoreboardTeamsExamples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ScoreboardTeamsExamples.  If not, see <http://www.gnu.org/licenses/>.
 *
 *==============================================================================
*/

import fr.vinetos.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Api for use teams with packet (version 1.8.X)
 * All field are verified and they works for 1.8, 1.8.8.
 * This class use fr.vinetos.util.Reflection from https://gist.github.com/Vinetos/6f497519f1b6465e833ac52006e9205b
 * <p>
 * To create a Team.Team just call {@link #create()}.
 * To delete a Team.Team just call {@link #remove()}.
 * N.B: You need to send the Team of all new player. In PlayerJoinEvent for example, just add {@link #rebuildTeamPacket(Player)}.
 *
 * @author Vinetos
 */
public class Team {
    // Factories
    private static final Class PACKET_CLASS = Reflection.getClass("{nms}.PacketPlayOutScoreboardTeam");
    // This fields haves to be updated for other version that 1.8.8 (1_8_R3)
    private static final Field TEAM_NAME = Reflection.getField(PACKET_CLASS, "a");
    private static final Field DISPLAY_NAME = Reflection.getField(PACKET_CLASS, "b");
    private static final Field PREFIX = Reflection.getField(PACKET_CLASS, "c");
    private static final Field SUFFIX = Reflection.getField(PACKET_CLASS, "d");
    private static final Field NAME_TAG_VISIBILITY = Reflection.getField(PACKET_CLASS, "e");
    private static final Field MEMBERS = Reflection.getField(PACKET_CLASS, "g");
    private static final Field TEAM_MODE = Reflection.getField(PACKET_CLASS, "h");
    private static final Field OPTIONS = Reflection.getField(PACKET_CLASS, "i");

    // Defines attributes
    private static Set<Team> teams = new HashSet<>();
    private final String name;
    private String displayName = "";
    private String suffix = "§r";// Remove the color for the health scoreboard for example
    private String prefix = "";
    private Set<String> players = new HashSet<>();
    private List<Player> viewers = null;
    private DyeColor teamColor;
    private boolean created = false;

    // Options of the Team
    private NameTagVisibility nameTagVisibility = NameTagVisibility.ALWAYS;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = false;

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name The name of the Team (can't be changed after)
     */
    public Team(String name) {
        this(name, (List<Player>) null);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name      The name of the Team (can't be changed after)
     * @param teamColor The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, DyeColor teamColor) {
        this(name, null, teamColor);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name    The name of the Team (can't be changed after)
     * @param viewers The players who can see the Team . Set to <code>null</code> for all connected players
     */
    public Team(String name, List<Player> viewers) {
        this(name, "", name, "", new HashSet<>(), viewers);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name      The name of the Team (can't be changed after)
     * @param viewers   The players who can see the Team . Set to <code>null</code> for all connected players
     * @param teamColor The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, List<Player> viewers, DyeColor teamColor) {
        this(name, "", name, "", new HashSet<>(), viewers, teamColor);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     */
    public Team(String name, String displayName) {
        this(name, "", displayName, "", new HashSet<>(), (List<Player>) null);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     */
    public Team(String name, String suffix, String displayName, String prefix) {
        this(name, suffix, displayName, prefix, new HashSet<>(), (List<Player>) null);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name      The name of the Team (can't be changed after)
     * @param suffix    The suffix of the Team (after the player's name)
     * @param prefix    The prefix of the Team (before the player's name)
     * @param teamColor The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, String suffix, String prefix, DyeColor teamColor) {
        this(name, suffix, name, prefix, new HashSet<>(), null, teamColor);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param teamColor   The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, String suffix, String displayName, String prefix, DyeColor teamColor) {
        this(name, suffix, displayName, prefix, new HashSet<>(), null, teamColor);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param players     The players into the Team
     */
    public Team(String name, String suffix, String displayName, String prefix, Set<String> players) {
        this(name, suffix, displayName, prefix, players, (List<Player>) null);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param players     The players into the Team
     * @param teamColor   The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, String suffix, String displayName, String prefix, Set<String> players, DyeColor teamColor) {
        this(name, suffix, displayName, prefix, players, null, teamColor);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param viewers     The players who can see the Team . Set to <code>null</code> for all connected players
     */
    public Team(String name, String suffix, String displayName, String prefix, List<Player> viewers) {
        this(name, suffix, displayName, prefix, new HashSet<>(), viewers);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param players     The players into the Team
     * @param viewers     The players who can see the Team . Set to <code>null</code> for all connected players
     */
    public Team(String name, String suffix, String displayName, String prefix, Set<String> players, List<Player> viewers) {
        this(name, suffix, displayName, prefix, players, viewers, null);
    }

    /**
     * Create a Team with packets. Call {@link #create()} to send the to viewers players
     *
     * @param name        The name of the Team (can't be changed after)
     * @param suffix      The suffix of the Team (after the player's name)
     * @param displayName The display name of the Team (on the right scoreboard for example)
     * @param prefix      The prefix of the Team (before the player's name)
     * @param players     The players into the Team
     * @param viewers     The players who can see the Team . Set to <code>null</code> for all connected players
     * @param teamColor   The {@link DyeColor} of the Team (to make a GUI for example)
     */
    public Team(String name, String suffix, String displayName, String prefix, Set<String> players, List<Player> viewers, DyeColor teamColor) {
        this.name = name;
        this.suffix = suffix;
        this.displayName = displayName;
        this.prefix = prefix;
        this.players = players;
        this.viewers = viewers;
        this.teamColor = teamColor;
    }

    /**
     * Get all created teams
     *
     * @return A {@link Set} of {@link Team}
     */
    public static Set<Team> getRegisterTeams() {
        return Collections.unmodifiableSet(teams);
    }

    /**
     * Get the Team for a player
     *
     * @param player who is in a Team
     * @return The {@link Team} of the player
     */
    public static Team getTeamOfPlayer(Player player) {
        for (Team team : getRegisterTeams()) {
            if (team.hasPlayer(player)) {
                return team;
            }
        }
        return null;
    }

    /**
     * Send the Team packet for the targeted players
     */
    public void create() {
        if (created) {
            update();
            return;
        }

        // Sends the Team packet
        Reflection.sendPacket(getViewers(), constructDefaultPacket(TeamMode.CREATE));

        // Send players (have to be one by one)
        getPlayers().forEach(playerName -> Reflection.sendPacket(getViewers(), constructPlayerTeamPacket(TeamMode.ADD_PLAYER, playerName)));
        created = true;
    }

    /**
     * Send the Team packet for the targeted players
     *
     * @param player The player who will have the Team created
     */
    public void create(Player player) {
        if (isViewer(player))
            return;
        // Sends the Team packet
        Reflection.sendPacket(player, constructDefaultPacket(TeamMode.CREATE));

        // Send players (have to be one by one)
        getPlayers().forEach(playerName -> Reflection.sendPacket(getViewers(), constructPlayerTeamPacket(TeamMode.ADD_PLAYER, playerName)));
    }

    /**
     * Update the parameters of the Team
     */
    public void update() {
        if (!created)
            return;

        // Send the Team packet
        Reflection.sendPacket(getViewers(), constructDefaultPacket(TeamMode.UPDATE));
    }

    /**
     * Update the parameters of the Team for a player
     *
     * @param player The player who will have the Team updated
     */
    public void update(Player player) {
        if (!isViewer(player))
            return;
        // Send the Team packet
        Reflection.sendPacket(player, constructDefaultPacket(TeamMode.UPDATE));
    }

    /**
     * Add or remove a player
     */
    public void updatePlayer(TeamMode mode, String playerName) {
        if (!created)
            return;

        // Send the new player
        Reflection.sendPacket(getViewers(), constructPlayerTeamPacket(mode, playerName));
    }

    /**
     * Remove the Team for the all players. You can call {@link #create()} to re-make the Team.
     */
    public void remove() {
        if (!created)
            return;

        // Remove players (have to be one by one)
        getPlayers().forEach(playerName -> constructPlayerTeamPacket(TeamMode.REMOVE_PLAYER, playerName));

        // Delete the Team
        Reflection.sendPacket(getViewers(), constructDefaultPacket(TeamMode.REMOVE));

        created = false;
    }

    /**
     * /**
     * Remove the Team for a player. You can call {@link #create(Player)} to re-make the Team.
     *
     * @param player The player who will have the Team removed
     */
    public void remove(Player player) {
        if (!isViewer(player))
            return;
        // Remove players (have to be one by one)
        getPlayers().forEach(playerName -> constructPlayerTeamPacket(TeamMode.REMOVE_PLAYER, playerName));

        // Delete the Team
        Reflection.sendPacket(player, constructDefaultPacket(TeamMode.REMOVE));
    }

    /**
     * Delete the Team for ever ! After, you can't all {@link #create()}. Be safe !
     */
    public void delete() {
        if (created)
            remove();

        teams.remove(this);
    }

    /**
     * Construct the generic packet
     *
     * @param mode Mode of the Team
     * @return the packet built
     */
    private Object constructDefaultPacket(TeamMode mode) {
        if (mode != TeamMode.CREATE && mode != TeamMode.REMOVE && mode != TeamMode.UPDATE)
            return null;
        try {
            Object teamPacket = PACKET_CLASS.newInstance();
            Reflection.setFieldValue(teamPacket, TEAM_NAME, this.name);
            if (mode == TeamMode.REMOVE)
                return teamPacket;

            Reflection.setFieldValue(teamPacket, DISPLAY_NAME, this.displayName);
            Reflection.setFieldValue(teamPacket, PREFIX, this.prefix);
            Reflection.setFieldValue(teamPacket, SUFFIX, this.suffix);
            Reflection.setFieldValue(teamPacket, NAME_TAG_VISIBILITY, this.nameTagVisibility.getVisibility());
            Reflection.setFieldValue(teamPacket, MEMBERS, this.players);
            Reflection.setFieldValue(teamPacket, TEAM_MODE, mode.getMode());
            Reflection.setFieldValue(teamPacket, OPTIONS, packOptionData());
            return teamPacket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a packet for adding/removing a player
     *
     * @param mode       mode of packet (Adding or removing)
     * @param playerName The player who be updated
     * @return the packet built
     */
    private Object constructPlayerTeamPacket(TeamMode mode, String playerName) {
        if (mode != TeamMode.ADD_PLAYER && mode != TeamMode.REMOVE_PLAYER)
            return null;
        try {
            Object teamPacket = PACKET_CLASS.newInstance();
            Reflection.setFieldValue(teamPacket, TEAM_NAME, this.name);
            Reflection.setFieldValue(teamPacket, MEMBERS, Collections.singleton(playerName));
            Reflection.setFieldValue(teamPacket, TEAM_MODE, mode.getMode());
            return teamPacket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate the int for options
     *
     * @return the value of options
     */
    private int packOptionData() {
        int option = 0;

        if (this.allowFriendlyFire())
            option |= 1;
        if (this.canSeeFriendlyInvisibles())
            option |= 2;
        return option;
    }

    /**
     * Rebuild and resend the Team for a specific player
     *
     * @param p the player who need to get the Team
     */
    public void rebuildTeamPacket(Player p) {
        Reflection.sendPacket(p, constructDefaultPacket(TeamMode.CREATE));

        // Send players (have to be one by one)
        getPlayers().forEach(playerName -> Reflection.sendPacket(p, constructPlayerTeamPacket(TeamMode.ADD_PLAYER, playerName)));

    }

    /**
     * Check if the Team is created
     *
     * @return <code>true</code> if the Team is created
     */
    public boolean isCreated() {
        return this.created;
    }

    /**
     * Get the name of the Team
     *
     * @return The name of the Team
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the display name of the Team
     *
     * @return the display name of the Team
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the display name
     *
     * @param displayName The new display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        update();
    }

    /**
     * Get the prefix of a Team
     *
     * @return the prefix of the Team
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Set the prefix of a Team
     *
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        update();
    }

    /**
     * Get the suffix of a Team
     *
     * @return the suffix of the Team
     */
    public String getSuffix() {
        return this.suffix;
    }

    /**
     * Set the suffix of a Team
     *
     * @param suffix the new suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        update();
    }

    /**
     * Check if the Team allow the friendly fire
     *
     * @return <code>true</code> if the Team allow the friendly fire
     */
    public boolean allowFriendlyFire() {
        return this.friendlyFire;
    }

    /**
     * Set if the Team allow the friendly fire
     *
     * @param friendlyFire the new value of friendly fire
     */
    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        update();
    }

    /**
     * Check if the Team can see her members invisibles
     *
     * @return <code>true</code> if can see her members invisibles
     */
    public boolean canSeeFriendlyInvisibles() {
        return this.seeFriendlyInvisibles;
    }

    /**
     * Set if the Team can see her members invisibles
     *
     * @param seeFriendlyInvisibles the new value of see members invisibles
     */
    public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        update();
    }

    /**
     * Add a player to the Team
     *
     * @param player the player who be added
     */
    public void addPlayer(Player player) {
        addPlayer(player.getName());
    }

    /**
     * Add a player to the Team
     *
     * @param playerName the name of player who be added
     */
    public void addPlayer(String playerName) {
        this.players.add(playerName);

        // Send update
        updatePlayer(TeamMode.ADD_PLAYER, playerName);
    }

    /**
     * Check if a player is in a Team
     *
     * @param player the player who be checked
     * @return <code>true</code> if the player is in the Team
     */
    public boolean hasPlayer(Player player) {
        return this.players.contains(player.getName());
    }

    /**
     * Check if a player is in a Team
     *
     * @param playerName the name of player who be checked
     * @return <code>true</code> if the player is in the Team
     */
    public boolean hasPlayer(String playerName) {
        return this.players.contains(playerName);
    }

    /**
     * Remove a player from a Team
     *
     * @param player the player who be removed
     */
    public void removePlayer(Player player) {
        removePlayer(player.getName());
    }

    /**
     * Remove a player from a Team
     *
     * @param playerName the name player who be removed
     */
    public void removePlayer(String playerName) {
        this.players.remove(playerName);

        // Send update
        updatePlayer(TeamMode.REMOVE_PLAYER, playerName);
    }

    /**
     * Get the color of a Team
     *
     * @return THe {@link DyeColor} of the Team
     */
    public DyeColor getTeamColor() {
        return this.teamColor;
    }

    /**
     * Set the color for a Team
     *
     * @param teamColor the new color of the Team
     */
    public void setTeamColor(DyeColor teamColor) {
        this.teamColor = teamColor;
    }

    /**
     * Add a player who can see the Team
     *
     * @param player who can see the Team
     */
    public void addViewer(Player player) {
        if (getViewers().contains(player))
            return;
        this.viewers.add(player);
        // Send the Team to the new viewers
        create(player);
    }

    /**
     * Check if a player see the Team
     *
     * @param player the player who be tested
     * @return <code>true</code> if the player see the Team
     */
    public boolean isViewer(Player player) {
        return getViewers().contains(player);
    }

    /**
     * Remove a player who can see the Team
     *
     * @param player who can see the Team
     */
    public void removeViewer(Player player) {
        if (!getViewers().contains(player))
            return;
        this.viewers.remove(player);
        // Send the Team to the new viewers
        remove(player);
    }

    /**
     * Get players who can see the Team
     * If the viewers aren't set, return all the onlinePlayers
     *
     * @return A {@link List} of <code>? extends </code>{@link Player}
     */
    public List<Player> getViewers() {
        if (viewers == null) {
            this.viewers = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> this.viewers.add(player));
        }
        return Collections.unmodifiableList(viewers);
    }

    /**
     * Get player in the Team
     *
     * @return A {@link Collection} of {@link String}
     */
    public Set<String> getPlayers() {
        if (this.players == null)
            this.players = new HashSet<>();
        return Collections.unmodifiableSet(this.players);
    }

    /**
     * The mode of the Team (when the client receive the packet, what action it have to do)
     */
    public enum TeamMode {

        CREATE(0),
        REMOVE(1),
        UPDATE(2),
        ADD_PLAYER(3),
        REMOVE_PLAYER(4);

        private final int mode;

        TeamMode(final int mode) {
            this.mode = mode;
        }

        public final int getMode() {
            return this.mode;
        }

    }

    /**
     * The NameTagVisility Parameter for the Team
     */
    public enum NameTagVisibility {
        ALWAYS("always"),
        NEVER("never"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam");

        private final String visibility;

        NameTagVisibility(String visibility) {
            this.visibility = visibility;
        }

        public final String getVisibility() {
            return this.visibility;
        }
    }

}
