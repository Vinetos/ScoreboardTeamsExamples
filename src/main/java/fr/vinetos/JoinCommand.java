package fr.vinetos;
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

import fr.vinetos.listener.PlayerJoinListener;
import fr.vinetos.team.TeamManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class JoinCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Add the player into the admin team
        if (sender instanceof Player) {
            TeamManager.TEAMS[0].addPlayer((Player) sender);
            sender.sendMessage("You joined the admin team !");
        }
        return false;
    }
}
