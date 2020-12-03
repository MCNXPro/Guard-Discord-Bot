package Commands.Chat;

import Commands.Command;
import Core.perms;
import Util.JOKES;
import Util.MSGS;
import Util.STATIC;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static Util.STATIC.GUILD_PERMS;

public class cmdJoke implements Command {

    Guild g;

    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();

        return perms.check2(event, GUILD_PERMS.get(g).USE);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        int randomJoke = (int) ((Math.random()*JOKES.JOKES.length));

        event.getTextChannel().sendMessage(
                MSGS.builder("joke", null, JOKES.JOKES[randomJoke])
        ).queue();
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "cmdJoke' was used!");
    }

    @Override
    public String help() {
        return "**`" + STATIC.PREFIX.get(g) + "joke`** *(Require permission: USE)* - returns a random joke.";
    }
}
