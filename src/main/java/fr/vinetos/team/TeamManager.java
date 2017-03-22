package fr.vinetos.team;/*
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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamManager {

    public static final Team[] TEAMS = {
            // Unique name of the team, suffix, her displayed name, her prefix (before the name of the player)
            new Team("adminTeam", "§r", ChatColor.DARK_RED + "AdminTeam", ChatColor.DARK_RED + "[Admin] ")
    };

    /**
     * Creates teams. All viewers can see the team ( {@link Team#getViewers()} ).
     */
    public void createTeams() {
//
//        Stream.of(TEAMS).forEach(Team::create);
//
        for (Team team : TEAMS) {
            team.create();
        }

    }

    /**
     * Sends all the fr.vinetos.team to a player
     *
     * @param player
     */
    public void sendTeams(Player player) {
//
//        Stream.of(TEAMS).forEach(team -> {
//            team.addViewer(player);
//            team.rebuildTeamPacket(player);
//        });
//
        for (Team team : TEAMS) {
            // Make the player can see the team
            team.addViewer(player);
            // Resend all parameters and players into the team
            team.rebuildTeamPacket(player);
        }
    }


}
