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

import com.exorath.buycraftconsumer.commandHandlers.ActionAPIHandler;
import com.exorath.buycraftconsumer.commandHandlers.RankAPIHandler;
import com.exorath.buycraftconsumer.res.ConsumerSettings;
import com.exorath.service.actionapi.api.ActionAPIServiceAPI;
import com.exorath.service.rank.api.RankServiceAPI;

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon on 4/7/2017.
 */
public class Main {
    private BuycraftConsumer buycraftConsumer;

    public Main() {
        this.buycraftConsumer = new BuycraftConsumer(getConsumerSettings());
        buycraftConsumer.addHandler(new ActionAPIHandler(new ActionAPIServiceAPI(getEnv("ACTIONAPI_SERVICE_ADDRESS"))));
        buycraftConsumer.addHandler(new RankAPIHandler(new RankServiceAPI(getEnv("RANK_SERVICE_ADDRESS"))));
    }

    private ConsumerSettings getConsumerSettings() {
        return new ConsumerSettings()
                .setBuycraftPrivateKey(getEnv("BUYCRAFT_PRIVATE_KEY"))
                .setInterval(20)
                .setIntervalUnit(TimeUnit.SECONDS);
    }

    private String getEnv(String field){
        String envValue = System.getenv(field);
        if(envValue != null && envValue != "") {
            return envValue;
        } else {
            throw new IllegalStateException("No " + field + " environment variable was provided while trying to load the tableName");
        }
    }
    public static void main(String[] args) {
        new Main();
    }
}
