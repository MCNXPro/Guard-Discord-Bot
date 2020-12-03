package Listeners;

import Commands.Voice.Autochannel;
import Util.MSGS;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static Commands.Settings.cmdSetPre.loadPrefix;
import static Util.PERMS.loadPerms;

public class reconnectListener extends ListenerAdapter
{

    Message msg;

    public void onReconnect(ReconnectedEvent event) {
        loadPrefix(event.getJDA());
        loadPerms(event.getJDA());

        String out = "\nThis Bot run on the following Servers: \n";

        for (Guild g : event.getJDA().getGuilds())
        {
            out = out.concat(g.getName().concat(" (".concat(g.getId()).concat(")\n")));

            // Nachricht auf den Servern
            switch (g.getName()) {
                default:
                    msg = g.getSystemChannel().sendMessage(
                            MSGS.builder("bot", null, "Hey, I'm back!!")
                    ).complete();

                    TIMER.deleteMSG("bot", msg);

                    break;
            }
        }
        Autochannel.load(event.getJDA());
    }
}
