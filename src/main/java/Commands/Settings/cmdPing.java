package Commands.Settings;

import Commands.Command;
import Core.perms;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Date;

import static Util.STATIC.GUILD_PERMS;

public class cmdPing implements Command {

    Guild g;

    private final String HELP = "USAGE: ~ping";

    private static long inputTime;

    public static void setInputTime(long inputTimeLong) {
        inputTime = inputTimeLong;
    }

    private Color getColorByPing(long ping) {
        if (ping < 100)
            return Color.cyan;
        if (ping < 400)
            return Color.green;
        if (ping < 700)
            return Color.yellow;
        if (ping < 1000)
            return Color.orange;
        return Color.red;
    }

    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();

        return perms.check2(event, GUILD_PERMS.get(g).USE);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        long processing = new Date().getTime() - inputTime;
        long ping = event.getJDA().getGatewayPing();
        TIMER.deleteMSG("bot",
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(getColorByPing(ping))
                                .setDescription(
                                        String.format("**Pong!**\n:stopwatch: `%s` ms",
                                                ping))
                                .build()
                ).complete()
        );
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "ping' was used!");
    }

    @Override
    public String help() {
        return "**`" + STATIC.PREFIX.get(g) + "ping`** *(Require permission: USE)* - Returns the ping.";
    }


}