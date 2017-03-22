package fr.vinetos;/*
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
import fr.vinetos.command.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyPlugin extends JavaPlugin {

    private static MyPlugin instance;
    private TeamManager teamManager;

    public static MyPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        MyPlugin.instance = this;
    }

    @Override
    public void onEnable() {
        // Create an instance of TeamManager and create the teams
        this.teamManager = new TeamManager();
        this.teamManager.createTeams();

        // Register the listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Register commands
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("quit").setExecutor(new QuitCommand());
    }

    @Override
    public void onDisable() {

    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
}
