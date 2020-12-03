package Commands;

import Core.commandHandler;
import Core.perms;
import Util.STATIC;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

import static Util.STATIC.GUILD_PERMS;

public class cmdHelp implements Command {

    Guild g;

    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();

        return perms.check2(event, GUILD_PERMS.get(g).USE);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        ArrayList<Command> cmds = new ArrayList<>();
        cmds.add(commandHandler.commands.get("help"));

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(0x1AA9D3))
                        .setTitle("**Help:**")
                        .addField("***AutoChannel*** *(Require permission: CMD_LV2)*:",
                                "**`" + STATIC.PREFIX.get(g) + "AC/ac`**\n" +
                                        "`- set/add/+ [VoiceChannel ID] [Linked TextChannel on/off]`\nRegisters a new VoiceChannel as AutoChannel with/without a linked TextChannel.\n" +
                                        "`- unset/remove/- [VoiceChannel ID]`\nDeletes a VoiceChannel as AutoChannel.\n" +
                                        "`- reset [AutoChannel ID]`\nResets the counter the AC.\n" +
                                        "`- list`\nReturns a list of all AutoChannel.", false)
                        .addField("***Clear*** *(Require permission: CMD_LV2)*:",
                                "**`" + STATIC.PREFIX.get(g) + "clear/c`**\n" +
                                        "`- [n]`\nDelete n messages.\n" +
                                        "`- all/a`\nDelete all messages in this channel.", false)
                        .addField("***Joke*** *(Require permission: USE)*:",
                                "**`" + STATIC.PREFIX.get(g) + "joke`** - returns a random joke.", false)
                        .addField("***Permissions*** *(Require permission: SETTINGS)*:",
                                "**`" + STATIC.PREFIX.get(g) + "perms`**\n" +
                                        "`- add use/cmd1/cmd2/settings [@user/@role]`\nAdds the and lower permissions to a user or a role.\n" +
                                        "`- remove use/cmd1/cmd2/settings/all [@user/@role]`\nRevokes the permission(s) from a user or a role.\n" +
                                        "`- list`\nLists all permission-categories and shows who has them.", false)
                        .addField("***Ping*** *(Require permission: USE)*:",
                                "**`" + STATIC.PREFIX.get(g) + "ping`** - Returns the ping.", false)
                        .addField("***Prefix*** *(Require permission: SETTINGS)*:",
                                "**`" + STATIC.PREFIX.get(g) + "setPre [new]`** - Changes the guild specific prefix.", false)
                        .addField("***Say*** *(Require permission: CMD_LV1)*:",
                                "**`" + STATIC.PREFIX.get(g) + "say`**\n" +
                                        "`- p [@user] [\"text\"]`\nSends the mentioned user a private message in your name.\n" +
                                        "`- pa [@user] [\"text\"]`\nSends the mentioned user a private message in the name of the Server.\n" +
                                        "`- [\"text\"]`\nThe Bot sends this as his message.", false)
                        .build()
        ).complete();
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "help' was used!");
    }

    @Override
    public String help() {
        return null;
    }
}
