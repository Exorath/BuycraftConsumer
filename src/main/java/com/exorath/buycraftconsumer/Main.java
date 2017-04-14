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
import com.exorath.service.commons.tableNameProvider.TableNameProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon on 4/7/2017.
 */
public class Main {
    private BuycraftConsumer buycraftConsumer;

    public Main() {
        this.buycraftConsumer = new BuycraftConsumer(getConsumerSettings());
    }

    private ConsumerSettings getConsumerSettings() {
        return new ConsumerSettings()
                .setBuycraftPrivateKey(TableNameProvider.getEnvironmentTableNameProvider("BUYCRAFT_PRIVATE_KEY").getTableName())
                .setActionAPIAddress(TableNameProvider.getEnvironmentTableNameProvider("ACTIONAPI_SERVICE_ADDRESS").getTableName())
                .setInterval(20)
                .setIntervalUnit(TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new Main();
    }
}
