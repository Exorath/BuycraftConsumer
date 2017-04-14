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

import com.exorath.buycraftconsumer.res.DueCommand;
import com.exorath.buycraftconsumer.res.DuePlayer;
import com.google.gson.JsonObject;

/**
 * Created by toonsev on 4/14/2017.
 */
public interface CommandHandler {
    /**
     * Gets the id of this commandHandler
     * @return the id of this commandHandler
     */
    String getHandlerId();
    /**
     * Handles the command
     * @param command
     * @param duePlayer
     * @param dueCommand
     * @return whether or not the handling was successful
     */
    boolean handle(JsonObject command, DuePlayer duePlayer, DueCommand dueCommand);
}
