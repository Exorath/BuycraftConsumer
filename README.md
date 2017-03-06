# BuycraftConsumer
####*Exorath's NodeJS buycraft purchase API consumer.*

## Features
- All purchases are eventually handled once and exactly once, with a 100% undebatable success rate on data changing purchases (things like a chat message may never be send or be send multiple times, though the chance is low, but this is okay).
- Handling time is tweakable (You can reduce the time between each BuyCraft purchase poll to speed up the execution of the function).
- The buycraft package contains all the information for what should be executed, this means that the API consumer will not have to be updated when a package updates.
- the consumer is never down for an unreasonable amount of time (it is rescheduled).

## Approach
When a purchase is made, buycraft saves an array of commands to it's database, which are fetched when the server polls for the latest due players (latest purchases). We do this poll on commands that should only be "executed when the player is online" (this makes Buycraft not delete the command before we say so). Our system is supposed to not care about whether or not the player is online, so it makes perfect sense to remove the function of this option.

Note that each API call to fetch commands will give the packageId, paymentId and commandId of each command, to make this call you also need to know the uuid of the player.

## Commands
Each package consists of the following commands (buycraft requires commands to be under 200 digits, hence why we can't batch into one command): 
#### #1 The package descriptor command
##### {"price":"{price}", "packagePrice": "{packagePrice}", "currency": "{currency}"}
When this command is handled, all data should be logged to a datastore
The saved document should be formatted as {"uuid": "playerId" "price": double, "packagePrice": double, "currency": string, descriptorId: string, commandIds: [string]};


#### #2-n The action commands
##### {"type": action_type_enum, "meta": {//action meta//}}
These commands are meant to execute an action: the command will only be deletes as the action is explicitely executed (this means important data-changing commands like giving 5 chests to the player, should be ATOMIC, the command execution should check whether or not the command was already executed and simply delete the command when it notices this.

The player uuid will be injected in the meta by the consumer, this means that there are still around 130 characters for the action meta.

type 0: An [ActionAPI](#) execution, with the ActionAPI data in the meta field. The ActionAPI allows you to send actions to a player's server/all servers.

## Notes

- Every x seconds get list of unfinished transactions (get-due-players | get-online-commands | delete-commands).
  - List with players and json data (given as an online command).
  - Data will je JSON.
  - Example command: actionapi{"type":"message", "msg":"Thanks for purchasing this rank!"}
- Send this data with UUID and other needed data
  - Using POST to the action API (HTTP request)
