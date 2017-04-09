/*
 *    Copyright 2017 Exorath
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

import java.util.concurrent.TimeUnit;

/**
 * Created by Toon on 4/7/2017.
 */
public class ConsumerSettings {
    private int interval = 20;
    private TimeUnit intervalUnit = TimeUnit.SECONDS;
    private String actionAPIAddress;
    private String buycraftPrivateKey;

    public int getInterval() {
        return interval;
    }

    public ConsumerSettings setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public TimeUnit getIntervalUnit() {
        return intervalUnit;
    }

    public ConsumerSettings setIntervalUnit(TimeUnit intervalUnit) {
        this.intervalUnit = intervalUnit;
        return this;
    }

    public String getActionAPIAddress() {
        return actionAPIAddress;
    }

    public ConsumerSettings setActionAPIAddress(String actionAPIAddress) {
        this.actionAPIAddress = actionAPIAddress;
        return this;
    }

    public String getBuycraftPrivateKey() {
        return buycraftPrivateKey;
    }

    public ConsumerSettings setBuycraftPrivateKey(String buycraftPrivateKey) {
        this.buycraftPrivateKey = buycraftPrivateKey;
        return this;
    }
}
