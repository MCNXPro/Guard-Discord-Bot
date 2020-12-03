package Commands.Chat;

import Commands.Command;
import Core.perms;
import Util.MSGS;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static Util.STATIC.GUILD_PERMS;

public class cmdClear implements Command {

    Guild g;

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();

        if (args.length < 1)
        {
            Message[] msg = new Message[2];
            msg[0] = event.getTextChannel().sendMessage(
                    MSGS.builder("error", null, "Please enter a number of messages you want to delete!")
            ).complete();
            msg[1] = event.getTextChannel().sendMessage(
                    MSGS.builder("error", null, STATIC.CMDHELP + help())
            ).complete();

            TIMER.deleteMSG("error", msg[0]);
            TIMER.deleteMSG("error", msg[1]);

            return false;
        }

        return perms.check2(event, GUILD_PERMS.get(g).CMD_LV2);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {

        MessageHistory history = new MessageHistory(event.getTextChannel());
        List<Message> msgs;

        switch (args[0].toLowerCase()) {

            // Command "all" und "a"
            case "all":
            case "a":

                try {
                    while (true) {
                        msgs = history.retrievePast(1).complete();

                        if (msgs.get(0).isPinned())
                            msgs.remove(0);
                        else
                            msgs.get(0).delete().queue();
                    }
                } catch (Exception ex) {
                    //Nichts tun
                }

                Message msg = event.getChannel().sendMessage(
                        MSGS.builder("info", null, "Alle Nachricheten wurden gelöscht.")
                ).complete();

                TIMER.deleteMSG("info", msg);

                break;

            default:
                // Command "[Zahl]"
                int numb = getInt(args[0]);

                if (numb > 1 && numb <= 100) {

                    try {

                        event.getMessage().delete().queue();

                        msgs = history.retrievePast(numb).complete();

                        // Entfernt alle angepinnten Nachrichten von der Delete-List
                        ArrayList<Message> pinned = new ArrayList();
                        int i = 0;
                        for (Message m : msgs) {
                            if (m.isPinned()) {
                                pinned.add(msgs.get(i));
                            }
                            i++;
                        }
                        for (Message m : pinned) {
                                msgs.remove(m);
                        }

                        if (msgs.size() > 1) {
                            event.getTextChannel().deleteMessages(msgs).queue();
                        } else {
                            msg = event.getTextChannel().sendMessage(
                                    MSGS.builder("error", null, "Please enter a higher number, because the messages in this range cannot be deleted!")
                            ).complete();

                            TIMER.deleteMSG("error", msg);
                        }

                        // Bestätigung
                        msg = event.getTextChannel().sendMessage(
                                MSGS.builder("info", null, "Die letzten " + numb + " Nachrichten wurden gelöscht.")
                        ).complete();

                        TIMER.deleteMSG("info", msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    msg = event.getTextChannel().sendMessage(
                            MSGS.builder("error", null, "Please enter a number between 2 and 100!")
                    ).complete();

                    TIMER.deleteMSG("error", msg);
                }
                break;
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "clear' was used!");
    }

    @Override
    public String help() {
        return "**`" + STATIC.PREFIX.get(g) + "clear/c`** *(Require permission: CMD_LV2)*\n" +
                "`- [n]`\nDelete n messages.\n" +
                "`- all/a`\nDelete all messages in this channel.";
    }
}
