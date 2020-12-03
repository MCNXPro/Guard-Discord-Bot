package Listeners;

import Commands.Voice.Autochannel;
import Util.AC.AC;
import Util.AC.VC_TMP;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AutochannelListener extends ListenerAdapter {

    private static HashMap<VoiceChannel, VC_TMP> tmpVCs = new HashMap<>();
    private int i;
    private HashMap<VoiceChannel, AC> acs = Autochannel.getAutochannels();

    public static HashMap<VoiceChannel, VC_TMP> getTmpVCs() {return tmpVCs;}

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        HashMap<VoiceChannel, AC> acs = Autochannel.getAutochannels();
        VoiceChannel vc = event.getChannelJoined();
        Guild g = event.getGuild();

        if (acs.containsKey(vc)) {
            i = 0;
            while (acs.get(vc).I.contains(i)) {
                i++;
            }
            acs.get(vc).I.add(i);

            if (acs.get(vc).LT) {
                VoiceChannel nvc = (VoiceChannel) vc.createCopy().setName(vc.getName() + " - " + i).setPosition(vc.getPosition() + 1).complete();
                nvc.createPermissionOverride(event.getMember()).setAllow(Permission.MANAGE_CHANNEL).queue();
                TextChannel ntc = (TextChannel) g.createTextChannel(vc.getName() + " - " + i).complete();

                tmpVCs.put(nvc, new VC_TMP(nvc, acs.get(vc), i, ntc));

                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    if (vc.getParent() != null) {
                                        nvc.getManager().setParent(vc.getParent()).queue();
                                        ntc.getManager().setParent(vc.getParent()).queue();
                                    }

                                    // g.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vc.getPosition() + 1).queue();
                                    g.moveVoiceMember(event.getMember(), nvc).queue();
                                } catch (Exception ignored) {}
                            }
                        }, 750);
            } else if (!acs.get(vc).LT) {
                VoiceChannel nvc = (VoiceChannel) vc.createCopy().setName(vc.getName() + " - " + i).setPosition(vc.getPosition() + 1).complete();
                nvc.createPermissionOverride(event.getMember()).setAllow(Permission.MANAGE_CHANNEL).queue();

                tmpVCs.put(nvc, new VC_TMP(nvc, acs.get(vc), i, null));

                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    if (vc.getParent() != null) {
                                        nvc.getManager().setParent(vc.getParent()).queue();
                                    }

                                    // g.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vc.getPosition() + 1).queue();
                                    g.moveVoiceMember(event.getMember(), nvc).queue();
                                } catch (Exception ignored) {}
                            }
                        }, 750);
            }
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        VoiceChannel vc = event.getChannelLeft();
        if (tmpVCs.containsKey(vc) && vc.getMembers().size() == 0) {
            VC_TMP tmpVC = tmpVCs.get(vc);

            int k = 0;
            while (tmpVC.ROOT_VC.I.contains(tmpVC.NUMBER) && k < 5) {
                try {
                    tmpVC.ROOT_VC.I.remove(tmpVC.NUMBER);
                    System.out.println("Success I: " + tmpVC.NUMBER + " " + tmpVC.ROOT_VC.I.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("I: " + tmpVC.ROOT_VC.I.toString() + tmpVC.TMP_VC);
                    k++;
                }
            }

            if (tmpVC.TC != null) {
                tmpVC.TC.delete().queue();
            }
            tmpVCs.remove(vc);
            vc.delete().queue();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        HashMap<VoiceChannel, AC> acs = Autochannel.getAutochannels();
        Guild g = event.getGuild();

        VoiceChannel vcLeft = event.getChannelLeft();
        VoiceChannel vcJoin = event.getChannelJoined();

        if (tmpVCs.containsKey(vcLeft) && vcLeft.getMembers().size() == 0) {
            VC_TMP tmpVC = tmpVCs.get(vcLeft);

            int k = 0;
            while (tmpVC.ROOT_VC.I.contains(tmpVC.NUMBER) && k < 5) {
                try {
                    tmpVC.ROOT_VC.I.remove(tmpVC.NUMBER);
                    System.out.println("Success I: " + tmpVC.NUMBER + " " + tmpVC.ROOT_VC.I.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("I: " + tmpVC.ROOT_VC.I.toString() + tmpVC.TMP_VC);
                    k++;
                }
            }

            if (tmpVC.TC != null) {
                tmpVC.TC.delete().queue();
            }
            tmpVCs.remove(vcLeft);
            vcLeft.delete().queue();
        }

        if (acs.containsKey(vcJoin)) {
            i = 0;
            while (acs.get(vcJoin).I.contains(i)) {
                i++;
            }
            acs.get(vcJoin).I.add(i);

            if (acs.get(vcJoin).LT) {
                VoiceChannel nvc = (VoiceChannel) vcJoin.createCopy().setName(vcJoin.getName() + " - " + i).setPosition(vcJoin.getPosition() + 1).complete();
                nvc.createPermissionOverride(event.getMember()).setAllow(Permission.MANAGE_CHANNEL).queue();
                TextChannel ntc = (TextChannel) g.createTextChannel(vcJoin.getName() + " -" + i).complete();

                tmpVCs.put(nvc, new VC_TMP(nvc, acs.get(vcJoin), i, ntc));

                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    if (vcJoin.getParent() != null) {
                                        nvc.getManager().setParent(vcJoin.getParent()).queue();
                                        ntc.getManager().setParent(vcJoin.getParent()).queue();
                                    }

                                    // g.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vcJoin.getPosition() + 1).queue();
                                    g.moveVoiceMember(event.getMember(), nvc).queue();
                                } catch (Exception ignored) {}
                            }
                        }, 750);
            } else if (!acs.get(vcJoin).LT) {
                VoiceChannel nvc = (VoiceChannel) vcJoin.createCopy().setName(vcJoin.getName() + " - " + i).setPosition(vcJoin.getPosition() + 1).complete();
                nvc.createPermissionOverride(event.getMember()).setAllow(Permission.MANAGE_CHANNEL).queue();

                tmpVCs.put(nvc, new VC_TMP(nvc, acs.get(vcJoin), i, null));

                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    if (vcJoin.getParent() != null) {
                                        nvc.getManager().setParent(vcJoin.getParent()).queue();
                                    }

                                    // g.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vcJoin.getPosition() + 1).queue();
                                    g.moveVoiceMember(event.getMember(), nvc).queue();
                                } catch (Exception ignored) {}
                            }
                        }, 750);
            }
        }
    }

    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        if (tmpVCs.containsKey(event.getChannel())) {
            VC_TMP tmpVC = tmpVCs.get(event.getChannel());

            while (tmpVCs.get(event.getChannel()).ROOT_VC.I.contains(tmpVCs.get(event.getChannel()).NUMBER)) {
                try {
                    tmpVC.ROOT_VC.I.remove((Object) tmpVC.NUMBER);
                    System.out.println("Success I: " + tmpVC.NUMBER + " " + tmpVC.ROOT_VC.I.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("I: " + tmpVC.ROOT_VC.I.toString());
                }
            }

            if (tmpVC.TC != null) {
                tmpVC.TC.delete().queue();
            }
            tmpVCs.remove(event.getChannel());
        }

        HashMap<VoiceChannel, AC> autochannels = Autochannel.getAutochannels();
        if (autochannels.containsKey(event.getChannel()))
            Autochannel.unsetAutochannel(event.getChannel());
    }
}