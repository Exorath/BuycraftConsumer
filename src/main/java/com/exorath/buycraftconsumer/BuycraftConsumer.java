/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.buycraftconsumer;

import com.exorath.buycraftconsumer.res.ConsumerSettings;
import com.exorath.buycraftconsumer.res.DueCommand;
import com.exorath.buycraftconsumer.res.DueCommands;
import com.exorath.buycraftconsumer.res.DuePlayer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Toon on 4/7/2017.
 */
public class BuycraftConsumer {
    private static final Gson GSON = new Gson();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ConsumerSettings consumerSettings;

    private HashMap<String, CommandHandler> handlers = new HashMap<>();

    public BuycraftConsumer(ConsumerSettings consumerSettings) {
        this.consumerSettings = consumerSettings;
        scheduler.scheduleAtFixedRate(() -> handleTransactions(), 0, consumerSettings.getInterval(), consumerSettings.getIntervalUnit());
    }

    public void addHandler(CommandHandler commandHandler) {
        handlers.put(commandHandler.getHandlerId(), commandHandler);
    }

    private void handleTransactions() {
        Collection<DuePlayer> duePlayers = getDuePlayers();
        for (DuePlayer duePlayer : duePlayers) {
            if (duePlayer.getId() == null) {
                System.out.println("No duePlayer id for " + duePlayer.getName());
                continue;
            }
            DueCommands dueCommands = getDueCommands(duePlayer.getId());
            handleDueCommands(duePlayer, dueCommands);
        }
    }

    private void handleDueCommands(DuePlayer duePlayer, DueCommands dueCommands) {
        for (DueCommand dueCommand : dueCommands.getCommands())
            try {
                handleDueCommand(duePlayer, dueCommand);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void handleDueCommand(DuePlayer duePlayer, DueCommand dueCommand) {
        String command = dueCommand.getCommand();
        String[] split = command.split("\\{", 2);
        String handlerId = split[0];
        String handlerJson = "{" + split[1];
        if (!handlers.containsKey(handlerId)) {
            System.out.println("Did not find handler " + handlerId);
            return;
        }
        boolean handled = handlers.get(handlerId).handle(GSON.fromJson(handlerJson, JsonObject.class), duePlayer, dueCommand);
        if (handled)
            removeDueCommand(duePlayer, dueCommand);
    }

    private void removeDueCommand(DuePlayer duePlayer, DueCommand dueCommand) {
        try {
            int status = deleteAuthed("/queue").queryString("ids[]", dueCommand.getId()).asString().getStatus();
            System.out.println("Deleted " + duePlayer.getUuid() + "'s command (" + dueCommand.getId() + ") with status " + status);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private DueCommands getDueCommands(int playerId) {
        try {
            return GSON.fromJson(getAuthed("/queue/online-commands/{id}")
                    .routeParam("id", playerId + "").asString().getBody(), DueCommands.class);
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Collection<DuePlayer> getDuePlayers() {
        Set<DuePlayer> players = new HashSet<>();
        try {
            for (JsonElement element : GSON.fromJson(getAuthed("/queue").asString().getBody(), JsonObject.class).getAsJsonArray("players"))
                players.add(GSON.fromJson(element, DuePlayer.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return players;
    }


    private static String url(String endpoint) {
        return BUYCRAFT_URL + endpoint;
    }

    private GetRequest getAuthed(String endpoint) {
        return Unirest.get(endpoint).header("X-Buycraft-Secret", consumerSettings.getBuycraftPrivateKey());
    }


    private HttpRequestWithBody deleteAuthed(String endpoint) {
        return Unirest.delete(endpoint).header("X-Buycraft-Secret", consumerSettings.getBuycraftPrivateKey());
    }

    private static final String BUYCRAFT_URL = "https://plugin.buycraft.net";

}
