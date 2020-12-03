package Listeners;

import Core.commandHandler;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class commandListener extends ListenerAdapter {

    Guild g;

    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX.get(event.getGuild())) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId())
            {
                commandHandler.handleCommand(commandHandler.parser.parser(event.getMessage().getContentRaw(), event));
                try {
                    TIMER.deleteMSG(300000, event.getMessage());
                } catch (Exception ignored) {}
            }
        } catch (NullPointerException | IllegalStateException ignored) {}

    }

}
