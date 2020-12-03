package Commands.Settings;

import Commands.Command;
import Core.perms;
import Util.MSGS;
import Util.SAVELOAD;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.HashMap;

import static Util.STATIC.GUILD_PERMS;

public class cmdSetPre implements Command {

    Guild g;

    public static Guild getGuild(String id, JDA jda) {
        return jda.getGuildById(id);
    }

    public static void savePrefix() {
        File path = new File("RES/BOT_SETTINGS/");
        if (!path.exists())
            path.mkdir();

        HashMap<String, String> out = new HashMap<>();

        STATIC.PREFIX.forEach((g, pre) -> out.put(g.getId(), pre));
        SAVELOAD.save(out, new File("RES/BOT_SETTINGS/prefix.dat"));
        /*ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File("SERVER_SETTINGS/prefix.dat")));
            oos.writeObject(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch(IOException ignored) {}
        }*/
    }

    public static void loadPrefix(JDA jda) {
        File file =  new File("RES/BOT_SETTINGS/prefix.dat");
        if (file.exists()) {
            HashMap<String, String> out = (HashMap<String, String>) SAVELOAD.load(file);
            out.forEach((gID, pre) -> {
                Guild g = getGuild(gID, jda);
                STATIC.PREFIX.put(g, pre);
            });
            /*try {
                ois = new ObjectInputStream(new FileInputStream(file));
                out = (HashMap<String, String>) ois.readObject();
                out.forEach((gID, pre) -> {
                    Guild g = getGuild(gID, jda);
                    STATIC.PREFIX.put(g, pre);
                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(ois != null) ois.close();
                } catch(IOException ignored) {}
            }*/
        }
    }

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

        return perms.check2(event, GUILD_PERMS.get(g).SETTINGS);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {


        STATIC.PREFIX.replace(g, STATIC.PREFIX.get(g), args[0]);
        // builder.setGame(Game.of(Game.GameType.DEFAULT, "[" + STATIC.VERSION + "] " + "Prefix: " + STATIC.PREFIX, null));

        TIMER.deleteMSG("info",
                event.getTextChannel().sendMessage(
                        MSGS.builder("info", null, "Change sucess! New Prefix: " + STATIC.PREFIX.get(g))
                ).complete()
        );
        savePrefix();
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "newPre' was used!");
        System.out.println("[INFO] Prefix: " + STATIC.PREFIX.get(g));
    }

    @Override
    public String help() {
        return "**`" + STATIC.PREFIX.get(g) + "setPre [new]`** *(Require permission: SETTINGS)* - Changes the guild specific prefix.";
    }
}
