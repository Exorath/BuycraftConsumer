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
import com.exorath.service.rank.api.RankServiceAPI;
import com.exorath.service.rank.res.RankPlayer;
import com.exorath.service.rank.res.Success;
import com.google.gson.JsonObject;

/**
 * Created by toonsev on 4/16/2017.
 */
public class RankAPIHandler implements CommandHandler {
    private RankServiceAPI rankServiceAPI;

    public RankAPIHandler(RankServiceAPI rankServiceAPI) {
        this.rankServiceAPI = rankServiceAPI;
    }

    @Override
    public String getHandlerId() {
        return "1";
    }

    @Override
    public boolean handle(JsonObject command, DuePlayer duePlayer, DueCommand dueCommand) {
        try {
            if (!command.has("rank"))
                return false;
            Success success = rankServiceAPI.setPlayerRank(duePlayer.getUuid(), new RankPlayer(command.get("rank").getAsString()));
            if (!success.isSuccess())
                System.out.println("RankAPI error: " + success.getCode() + ": " + success.getError());
            return success.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
