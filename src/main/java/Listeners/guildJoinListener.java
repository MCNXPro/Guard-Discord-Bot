package Listeners;

import Util.MSGS;
import Util.STATIC;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

import static Util.PERMS.createGuildPerms;
import static Util.PERMS.savePerms;
import static Util.STATIC.GUILD_PERMS;

public class guildJoinListener extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event) {

        createGuildPerms(event.getGuild());
        savePerms();
        if (!STATIC.PREFIX.containsKey(event.getGuild())) {
            STATIC.PREFIX.put(event.getGuild(), "!");
            System.out.println("set new prefix for the guild: " + event.getGuild().getName().concat(" (".concat(event.getGuild().getId()).concat(")\n")));
        }

        Message msg = event.getGuild().getSystemChannel().sendMessage(
                MSGS.builder(new Color(0x1AA9D3), "Hey, my name is *Guard* and i want to help you with your server!\n",
                        "My Default-Prefix is `!` you can change it with the cmd: `!setPre [new]`.\n\n" +
                                "My permissions are split in  three categories:\n" +
                                "\t\"USE\": can use simple commands like `!ping` or `!joke`\n" +
                                "\t\"CMD\": can use normal commands like `!clear` or `!AC`\n" +
                                "\t\"SETTINGS\": can change my settings and server-settings\n\n" +
                                "You can add/remove roles or members to these categories with the commands:\n" +
                                "\t`!perms add/remove use/cmd/settings [@user/@role]`\n\n" +
                                "You also can create autochannels(AC), which automatically duplicates or delete themself with the commands:\n" +
                                "\t`!AC set/remove [voicechannelID]`\n\n" +
                                "Currently that's it.")
        ).complete();
        System.out.println(event.getGuild() + "/" + GUILD_PERMS.get(event.getGuild()).SETTINGS.get("user").get(0));
    }
}
