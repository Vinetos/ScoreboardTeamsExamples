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
package fr.vinetos.listener;

import fr.vinetos.MyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Sends all registered teams to the new player
        MyPlugin.getInstance().getTeamManager().sendTeams(e.getPlayer());
    }

}
