package Commands.Voice;

import Commands.Command;
import Core.perms;
import Listeners.AutochannelListener;
import Util.AC.AC;
import Util.AC.ACS;
import Util.AC.VC_TMP;
import Util.MSGS;
import Util.SAVELOAD;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static Util.STATIC.GUILD_PERMS;

public class Autochannel implements Command {
    
    Guild g;

    private static HashMap<VoiceChannel, AC> autochannels = new HashMap<>();

    public static HashMap<VoiceChannel, AC> getAutochannels() {
        return autochannels;
    }

    public static VoiceChannel getVoiceChannel(String id, Guild g) {
        return g.getVoiceChannelById(id);
    }

    public static Guild getGuild(String id, JDA jda) {
        return jda.getGuildById(id);
    }

    private void errorMSG(TextChannel tc, String content) {
        TIMER.deleteMSG("error",
                tc.sendMessage(
                        MSGS.builder("error", null, content)
                ).complete()
        );
    }

    private void infoMSG(TextChannel tc, String content) {
        TIMER.deleteMSG("info",
                tc.sendMessage(
                        MSGS.builder("info", null, content)
                ).complete()
        );
    }

    private void setAutochannel(String id, Guild g, TextChannel tc, boolean lt) {
        VoiceChannel vc = getVoiceChannel(id, g);
        
        if (vc == null)
            errorMSG(tc, "Please enter a valid VoiceChannel ID!");
        else if (autochannels.containsKey(vc))
            errorMSG(tc, "This VoiceChannel is still registered as autochannel!");
        else {
            autochannels.put(vc, new AC(vc, g, lt));

            infoMSG(tc, String.format("Successfully set VoiceChannel `%s` as an autochannel!", vc.getName()));
        }
        save();
    }

    private void unsetAutochannel(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVoiceChannel(id, g);

        if (vc == null)
            errorMSG(tc, "Please enter a valid VoiceChannel ID!");
        else if (!autochannels.containsKey(vc))
            errorMSG(tc, "This VoiceChannel is not registered as autochannel!");
        else {
            autochannels.remove(vc);

            infoMSG(tc, String.format("Successfully unset VoiceChannel `%s` as an autochannel!", vc.getName()));
        }
        save();
    }
    
    public static void unsetAutochannel(VoiceChannel vc) {
        autochannels.remove(vc);
        save();
    }

    private void resetAutochannel(String id, Guild g, TextChannel tc) {
        VoiceChannel vc = getVoiceChannel(id, g);

        if (vc == null)
            errorMSG(tc, "Please enter a valid VoiceChannel ID!");
        else if (!autochannels.containsKey(vc))
            errorMSG(tc, "This VoiceChannel is not registered as autochannel!");
        else {
            HashMap<VoiceChannel, VC_TMP> tmpVCs = AutochannelListener.getTmpVCs();
            ArrayList<Integer> tmpI = new ArrayList<Integer>();

            for (VoiceChannel tmpVC : tmpVCs.keySet()) {
                if (tmpVCs.get(tmpVC).ROOT_VC.equals(autochannels.get(vc))) {
                    tmpI.add(tmpVCs.get(tmpVC).NUMBER);
                }
            }

            autochannels.get(vc).I.clear();
            autochannels.get(vc).I.addAll(tmpI);

            infoMSG(tc, String.format("Successfully reset VoiceChannel `%s`!", vc.getName()));
        }
        save();
    }
    
    private void listAutochannels(Guild g, TextChannel tc) {
        StringBuilder builder = new StringBuilder().append("**AUTOCHANNELS:**\n\n");
        autochannels.keySet().stream()
                .filter(vc -> autochannels.get(vc).G.equals(g))
                .forEach(vc -> builder.append(String.format(":white_small_square: `%s` *(%s)*\n", vc.getName(), vc.getId())));

        TIMER.deleteMSG("info",
                tc.sendMessage(
                        MSGS.builder("info", null, builder.toString())
                ).complete()
        );
    }

    private static void save() {
        HashMap<String, ACS> out = new HashMap<String, ACS>();

        autochannels.forEach((vc, ac) -> {
            out.put(vc.getId(), new ACS(ac.VC, ac.G, ac.LT));
        });
        SAVELOAD.save(out, new File("RES/BOT_SETTINGS/autochannels.dat"));
        /*ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("SERVER_SETTINGS/autochannels.dat"));
            oos.writeObject(out);
            oos.close();
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

    public static void load(JDA jda) {
        File file =  new File("RES/BOT_SETTINGS/autochannels.dat");
        if (file.exists()) {
            HashMap<String, ACS> out = (HashMap<String, ACS>) SAVELOAD.load(file);
            out.forEach((vID, ac) -> {
                if (jda.getGuildById(ac.G) != null) {
                    Guild g = jda.getGuildById(ac.G);
                    VoiceChannel vc = getVoiceChannel(vID, g);
                    autochannels.put(vc, new AC(vc, g, ac.LT));
                }
            });
            /*ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                HashMap<String, String> out = (HashMap<String, String>) ois.readObject();
                ois.close();
                out.forEach((vID, gID) -> {
                    Guild g = getGuild(gID, jda);
                    autochannels.put(getVoiceChannel(vID, g), g);
                });
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }  finally {
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
            errorMSG(event.getTextChannel(), help());
            return false;
        }

        return perms.check2(event, GUILD_PERMS.get(g).CMD_LV2);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        TextChannel tc = event.getTextChannel();

        switch (args[0]) {

            case "list":
                listAutochannels(g, tc);
                break;

            case "set":
            case "add":
            case "+":
                if (args.length < 3)
                    errorMSG(tc, help());
                else
                    if (args[2].equals("on")) {
                        setAutochannel(args[1], g, tc, true);
                    } else if (args[2].equals("off")) {
                        setAutochannel(args[1], g, tc, false);
                    } else {
                        errorMSG(tc, help());
                    }
                break;

            case "remove":
            case "unset":
            case "-":
                if (args.length < 2)
                    errorMSG(tc, help());
                else
                    unsetAutochannel(args[1], g, tc);
                break;

            case "reset":
                resetAutochannel(args[1], g, tc);
                break;

            default:
                errorMSG(tc, help());
                break;
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] der Command " + STATIC.PREFIX.get(g) + "'auto' was used!");
    }

    @Override
    public String help() {
        return  "**`" + STATIC.PREFIX.get(g) + "AC/ac`** *(Require permission: CMD_LV2)*\n" +
                "`- set/add/+ [VoiceChannel ID] [Linked TextChannel on/off]`\nRegisters a new VoiceChannel as AutoChannel with/without a linked TextChannel.\n" +
                "`- unset/remove/- [VoiceChannel ID]`\nDeletes a VoiceChannel as AutoChannel.\n" +
                "`- reset [AutoChannel ID]`\nResets the counter the AC.\n" +
                "`- list`\nReturns a list of all AutoChannel.";
    }
}
