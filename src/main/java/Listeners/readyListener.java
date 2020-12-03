package Listeners;

import Commands.Voice.Autochannel;
import Util.MSGS;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static Commands.Settings.cmdSetPre.loadPrefix;
import static Util.PERMS.*;
import static Util.STATIC.GUILD_PERMS;

public class readyListener extends ListenerAdapter
{

    Message msg;

    public void onReady(ReadyEvent event) {
        loadPrefix(event.getJDA());
        loadPerms(event.getJDA());

        String out = "\nThis Bot run on the following Servers: \n";

        for (Guild g : event.getJDA().getGuilds()) {
            out = out.concat(g.getName().concat(" (".concat(g.getId()).concat(")\n")));

            if (!STATIC.PREFIX.containsKey(g)) {
                STATIC.PREFIX.put(g, "!");
                System.out.println("set new prefix for the guild: " + g.getName().concat(" (".concat(g.getId()).concat(")\n")));
            }
            if (!GUILD_PERMS.containsKey(g)) {
                createGuildPerms(g);
                savePerms();
            }
            // Nachricht auf den Servern
            switch (g.getName()) {
                default:
                    msg = g.getSystemChannel().sendMessage(
                            MSGS.builder("bot", null, "Hey, I'm back!!\n" + g.getName() + "-Prefix: " + STATIC.PREFIX.get(g))
                    ).complete();

                    TIMER.deleteMSG("bot", msg);

                    break;
            }


        }
        System.out.println(out);
        Autochannel.load(event.getJDA());
    }

}
