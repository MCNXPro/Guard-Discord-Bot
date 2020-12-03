package Listeners;

import Util.STATIC;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class policeListener extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            String[] msg = event.getMessage().getContentRaw().split(" ");
            for (String m : msg) {
                for (String i : STATIC.INSULT) {
                    if (m.equalsIgnoreCase(i)) {
                        System.out.println(m);

                        event.getMessage().delete().queue();
                        event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(
                                ":warning: " + event.getMember().getAsMention() + " Please don't use insults or you will be kicked!"
                        ).queue());
                        event.getGuild().getTextChannelsByName("warninglog", false).get(0).sendMessage("User " + event.getMember().getAsMention() + " wurde vorgewahnt wegen des Nutzens des Wortes `" + m + "`!").queue();
                        return;
                    }
                }
                if (STATIC.INSULTS.containsKey(event.getGuild())) {
                    STATIC.INSULTS.get(event.getGuild()).forEach(insult -> {
                        if (m.equalsIgnoreCase(insult.toString())) {
                            System.out.println(m);

                            event.getMessage().delete().queue();
                            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(
                                    ":warning: " + event.getMember().getAsMention() + " Please don't use insults or you will be kicked!"
                            ).queue());
                            event.getGuild().getTextChannelsByName("warninglog", false).get(0).sendMessage("User " + event.getMember().getAsMention() + " wurde vorgewahnt wegen des Nutzens des Wortes `" + m + "`!").queue();
                        }
                    });
                }
            }
        }
    }
}