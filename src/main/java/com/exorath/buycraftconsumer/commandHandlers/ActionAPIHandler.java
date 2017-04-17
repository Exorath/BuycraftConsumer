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

package com.exorath.buycraftconsumer.commandHandlers;

import com.exorath.buycraftconsumer.CommandHandler;
import com.exorath.buycraftconsumer.res.DueCommand;
import com.exorath.buycraftconsumer.res.DuePlayer;
import com.exorath.service.actionapi.api.ActionAPIServiceAPI;
import com.exorath.service.actionapi.res.Action;
import com.exorath.service.actionapi.res.Success;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by toonsev on 4/14/2017.
 */
public class ActionAPIHandler implements CommandHandler {
    private static final Gson GSON = new Gson();

    private ActionAPIServiceAPI actionAPIServiceAPI;

    public ActionAPIHandler(ActionAPIServiceAPI actionAPIServiceAPI) {
        this.actionAPIServiceAPI = actionAPIServiceAPI;
    }

    @Override
    public String getHandlerId() {
        return "0";
    }

    @Override
    public boolean handle(JsonObject command, DuePlayer duePlayer, DueCommand dueCommand) {
        try {

            String type = command.get("type").getAsString();

            String subject = command.has("subject") ? command.get("subject").getAsString() : duePlayer.getUuid();
            String destination = command.has("destination") ? command.get("destination").getAsString() : null;
            boolean spigot = command.has("spigot") ? command.get("spigot").getAsBoolean() : true;

            JsonObject meta = command.has("meta") ? command.get("meta").getAsJsonObject() : new JsonObject();
            meta = GSON.fromJson(meta.toString().replaceAll("%NAME%", duePlayer.getName()).replaceAll("%UUID%", duePlayer.getUuid()), JsonObject.class);
            if (subject.equals("uuid"))
                subject = duePlayer.getUuid();
            Action action = new Action(subject, destination, type, meta, spigot);
            Success success = actionAPIServiceAPI.publishAction(action);
            if (!success.getSuccess())
                System.out.println(success.getError());
            return success.getSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
