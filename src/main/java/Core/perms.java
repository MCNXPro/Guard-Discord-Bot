package Core;

import Util.TIMER;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static Util.SECRETS.DEVID;

public class perms {

    public static boolean check (MessageReceivedEvent event, String[] perm) {
        Message msg;
        for (Role r : event.getMember().getRoles()) {
            System.out.println(r);
            if (Arrays.stream(perm).anyMatch(r.getName()::contains))
                return false;
            else {
                msg = event.getTextChannel().sendMessage(":warning:   Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use this command!").complete();

                TIMER.deleteMSG("error", msg);

                return true;
            }
        }
        msg = event.getTextChannel().sendMessage(":warning:   Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use any command!").complete();

        TIMER.deleteMSG("error", msg);
        return true;
    }

    public static boolean check2 (MessageReceivedEvent event, HashMap<String, ArrayList<String>> perm) {
        Message msg;
        if (event.getMember().getId().equals(DEVID) || event.getMember().equals(event.getGuild().getOwner()) || perm.get("user").contains(event.getMember().getId())){
            return true;
        } else {
            for (Role r : event.getMember().getRoles()) {
                if (perm.get("role").contains(r.getId()))
                    return true;
            }
        }
        msg = event.getTextChannel().sendMessage(":warning:   Sorry, " + event.getAuthor().getAsMention() + ", you don't have the permissions to use any command!").complete();

        TIMER.deleteMSG("error", msg);
        return false;
    }
}
