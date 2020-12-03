package Commands.Chat;

import Commands.Command;
import Core.perms;
import Util.MSGS;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static Util.STATIC.GUILD_PERMS;

public class cmdSay implements Command {

    Guild g;
    
    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();
        if (args.length < 1) {

            TIMER.deleteMSG("error", 
                    event.getTextChannel().sendMessage(
                            MSGS.builder("error", null, STATIC.CMDHELP + help())
                    ).complete()
            );
            return true;
        }
        return perms.check2(event, GUILD_PERMS.get(g).CMD_LV1);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        PrivateChannel pChannel;
        String content;

        switch (args[0]){

            /*case "T":
                for (String joke : JOKES.JOKES) {
                    event.getTextChannel().sendMessage(joke + "\n\n-----------------------\n\n").queue();
                }
                break;*/

            case "p":
                if (event.getMessage().getMentionedMembers().isEmpty()) {

                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", null, STATIC.CMDHELP + help())
                            ).complete()
                    );

                    return;
                }
                pChannel = event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete();
                content = event.getAuthor().getName() + ": " + String.join(" ",new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
                pChannel.sendMessage(MSGS.builder("private", null, content)).queue();

                event.getMessage().delete().queue();
                break;

            case "pa":
                if (event.getMessage().getMentionedMembers().isEmpty()) {

                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", null, STATIC.CMDHELP + help())
                            ).complete()
                    );

                    return;
                }
                pChannel = event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete();
                content = event.getGuild().getName() + ": " + String.join(" ",new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
                pChannel.sendMessage(MSGS.builder("private", null, content)).queue();

                event.getMessage().delete().queue();
                break;

            default:
                event.getMessage().delete().queue();
                String out = ":loudspeaker: ";
                for (String arg : args) {
                    out += arg.concat(" ");
                }

                event.getTextChannel().sendMessage(out).queue();
                break;
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "cmdSay' was used!");
    }

    @Override
    public String help() {
        return "**`" + STATIC.PREFIX.get(g) + "say`** *(Require permission: CMD_LV1)*\n" +
                "`- p [@user] [\"text\"]`\nSends the mentioned user a private message in your name.\n" +
                "`- pa [@user] [\"text\"]`\nSends the mentioned user a private message in the name of the Server.\n" +
                "`- [\"text\"]`\nThe Bot sends this as his message.";
    }
}
