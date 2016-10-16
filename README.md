# BuycraftConsumer
####*Exorath's NodeJS buycraft purchase API consumer.*

*This consumer is ran on an [Azure function](https://azure.microsoft.com/en-us/services/functions/) on a timed interval (much like a cron job)*

## Features
- All purchases are eventually handled once and exactly once, with a 100% undebatable success rate.
- Handling time is tweakable (You can reduce the time between each BuyCraft purchase poll to speed up the execution of the function).
- The buycraft package contains all the information for what should be executed, this means that the API consumer will not have to be updated when a package updates.
- the consumer is never down for an unreasonable amount of time: as it is hosted as an azure function, the consumer will be rescheduled on a different node if odd happens.

## Approach
When a purchase is made, buycraft saves an array of commands to it's database, which are fetched when the server polls for the latest due players (latest purchases). We do this poll on commands that should only be "executed when the player is online" (this makes Buycraft not delete the command before we say so). Our system is supposed to not care about whether or not the player is online, so it makes perfect sense to remove the function of this option.

Each package consists of the following commands (buycraft requires commands to be under 200 digits, hence why we can't batch into one command):
Note that each API call to fetch commands will give the packageId, paymentId and commandId of each command, to make this call you also need to know the uuid of the player.
- #1 The package descriptor command: {"price":"{price}", "packagePrice": "{packagePrice}", "currency": "{currency}"}
- #2-n The action commands: {"type": action_type_enum, "meta": {//action meta}}
