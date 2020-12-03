package Commands.Settings;

import Commands.Command;
import Core.perms;
import Util.MSGS;
import Util.STATIC;
import Util.TIMER;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static Util.PERMS.savePerms;
import static Util.STATIC.GUILD_PERMS;

public class cmdPerms implements Command {

    Guild g;

    private HashMap<String, ArrayList<String>> getPermCat(String cat) { // gibt die zubearbeitende Permission zurück oder null
        HashMap<String, ArrayList<String>> pCat = null;
        switch (cat) {
            case "use":
                pCat = GUILD_PERMS.get(g).USE;
                break;

            case "cmd1":
                pCat = GUILD_PERMS.get(g).CMD_LV1;
                break;

            case "cmd2":
                pCat = GUILD_PERMS.get(g).CMD_LV2;
                break;

            case "settings":
                pCat = GUILD_PERMS.get(g).SETTINGS;
                break;
        }
        return pCat;
    }



    @Override
    public boolean called(String invoke, String[] args, MessageReceivedEvent event) {
        g = event.getGuild();
        return perms.check2(event, GUILD_PERMS.get(g).SETTINGS);
    }

    @Override
    public void action(String invoke, String[] args, MessageReceivedEvent event) {
        HashMap<String, ArrayList<String>> pCat = null;

        switch (args[0]) {
            case "add": // Command um Permissions einem User oder einer Rolle zu geben
                System.out.println(args[1]);
                pCat = getPermCat(args[1]); // Abspeichern, der zu verändernden Permission
                System.out.println(pCat);
                if (pCat == null) { // Wenn keine oder eine nicht vorhandene Permission angegeben wurde
                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", null, "Please check your input:\n" +
                                            "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                            "CMD:\t" + help())
                            ).complete()
                    );
                    return;
                }

                if (!event.getMessage().getMentions(Message.MentionType.ROLE).isEmpty()) {  // Wenn die Permission einer Rolle gegeben werden soll
                    String id = event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getId();    // Abspeichern der ID der Rolle

                    if (!pCat.get("role").contains(id)) {   // Wenn die Rolle die Permission noch nicht hat
                        if (pCat == (GUILD_PERMS.get(g).SETTINGS)) { // Wenn die Permission "SETTINGS" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).SETTINGS.get("role").add(id);
                            if (!GUILD_PERMS.get(g).CMD_LV2.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).CMD_LV2.get("role").add(id);
                            }
                            if (!GUILD_PERMS.get(g).CMD_LV1.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).CMD_LV1.get("role").add(id);
                            }
                            if (!GUILD_PERMS.get(g).USE.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).USE.get("role").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).CMD_LV2)) {   // Wenn die Permission "CMD" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).CMD_LV2.get("role").add(id);
                            if (!GUILD_PERMS.get(g).CMD_LV1.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).CMD_LV1.get("role").add(id);
                            }
                            if (!GUILD_PERMS.get(g).USE.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).USE.get("role").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).CMD_LV1)) {   // Wenn die Permission "CMD" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).CMD_LV1.get("role").add(id);
                            if (!GUILD_PERMS.get(g).USE.get("role").contains(id)) { // Wenn die Rolle diese Permission noch nicht hat, dann gib sie ihr
                                GUILD_PERMS.get(g).USE.get("role").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).USE)) {   // Wenn die Permission "USE" vergeben werden soll
                            GUILD_PERMS.get(g).USE.get("role").add(id);
                        }
                        //savePerms();

                        event.getTextChannel().sendMessage(
                                MSGS.builder("info", null, event.getMember().getAsMention() + " add the " + args[1] + "-permissions to " + event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getAsMention() + "!")
                        ).complete();
                    } else {    // Wenn die Rolle die Permission schon hat
                        TIMER.deleteMSG("error",
                                event.getTextChannel().sendMessage(
                                        MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getAsMention() + " has already " + args[1] + "-permissions!")
                                ).complete()
                        );
                        return;
                    }
                } else if (!event.getMessage().getMentions(Message.MentionType.USER).isEmpty()) {   // Wenn die Permission einem User gegeben werden soll
                    String id = event.getMessage().getMentions(Message.MentionType.USER).get(0).getId();    // Abspeichern der ID des Users

                    if (!pCat.get("user").contains(id)) {   // Wenn der User die Permission noch nicht hat
                        if (pCat == (GUILD_PERMS.get(g).SETTINGS)) { // Wenn die Permission "SETTINGS" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).SETTINGS.get("user").add(id);
                            if (!GUILD_PERMS.get(g).CMD_LV2.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).CMD_LV2.get("user").add(id);
                            }
                            if (!GUILD_PERMS.get(g).CMD_LV1.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).CMD_LV1.get("user").add(id);
                            }
                            if (!GUILD_PERMS.get(g).USE.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).USE.get("user").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).CMD_LV2)) {   // Wenn die Permission "CMD" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).CMD_LV2.get("user").add(id);
                            if (!GUILD_PERMS.get(g).CMD_LV1.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).CMD_LV1.get("user").add(id);
                            }
                            if (!GUILD_PERMS.get(g).USE.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).USE.get("user").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).CMD_LV1)) {   // Wenn die Permission "CMD" vergeben werden soll, dann vergebe auch die unteren
                            GUILD_PERMS.get(g).CMD_LV1.get("user").add(id);
                            if (!GUILD_PERMS.get(g).USE.get("user").contains(id)) { // Wenn der User diese Permission noch nicht hat, dann gib sie ihm
                                GUILD_PERMS.get(g).USE.get("user").add(id);
                            }
                        } else if (pCat == (GUILD_PERMS.get(g).USE)) {   // Wenn die Permission "USE" vergeben werden soll
                            GUILD_PERMS.get(g).USE.get("user").add(id);
                        }
                        //savePerms();

                        event.getTextChannel().sendMessage(
                                MSGS.builder("info", null, event.getMember().getAsMention() + " add the " + args[1] + "-permissions to " + event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + "!")
                        ).complete();
                    } else {    // Wenn der User die Permission schon hat
                        TIMER.deleteMSG("error",
                                event.getTextChannel().sendMessage(
                                        MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + " has already " + args[1] + "-permissions!")
                                ).complete()
                        );
                        return;
                    }
                } else {    // Error -> falsche Markierung
                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", "Something went wrong!", "Please check your input:\n" +
                                            "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                            "CMD:\t" + help())
                            ).complete()
                    );
                    return;
                }
                break;

            case "remove":  // Command um Permissions vom User oder einer Rolle zu entziehen

                if (args[1].equals("all")) {    // Alle Permissions entziehen | getrennt vom Rest aus "remove", da es ein Untercommand ist und die Einbindung zu schwer wäre
                    if (!event.getMessage().getMentions(Message.MentionType.ROLE).isEmpty()) {  // Wenn einer Rolle die Permissions entzogen werden soll
                        String id = event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getId();
                        try {   // try, falls diese Rolle nicht alle Permissions besitzt
                            GUILD_PERMS.get(g).USE.get("role").remove(id);
                            GUILD_PERMS.get(g).CMD_LV1.get("role").remove(id);
                            GUILD_PERMS.get(g).CMD_LV2.get("role").remove(id);
                            GUILD_PERMS.get(g).SETTINGS.get("role").remove(id);
                        } catch (NullPointerException ignored) {
                        }
                    } else if (!event.getMessage().getMentions(Message.MentionType.USER).isEmpty()) {   // Wenn einem User die Permissions entzogen werden soll
                        String id = event.getMessage().getMentions(Message.MentionType.USER).get(0).getId();

                        if (!id.equals(event.getGuild().getOwnerId())) {    // Solange nicht dem Server-Owner die Permissions entzogen werden sollen
                            try {   // try, falls dieser User nicht alle Permissions besitzt
                                GUILD_PERMS.get(g).USE.get("user").remove(id);
                                GUILD_PERMS.get(g).CMD_LV1.get("user").remove(id);
                                GUILD_PERMS.get(g).CMD_LV2.get("user").remove(id);
                                GUILD_PERMS.get(g).SETTINGS.get("user").remove(id);
                            } catch (NullPointerException ignored) {
                            }
                        } else {    // Error -> Guild-Owner darf nicht gelöscht werden
                            TIMER.deleteMSG("error",
                                    event.getTextChannel().sendMessage(
                                            MSGS.builder("error", null, "You can not revoke the permissions of the guild-owner!")
                                    ).complete()
                            );
                            return;
                        }
                    } else {    // Wenn keine Rolle bzw. kein User erwähnt wurde
                        TIMER.deleteMSG("error",
                                event.getTextChannel().sendMessage(
                                        MSGS.builder("error", "You did not mention a role or a user!", "Please check your input:\n" +
                                                "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                                "CMD:\t" + help())
                                ).complete()
                        );
                        return;
                    }
                    event.getTextChannel().sendMessage(
                            MSGS.builder("info", null, event.getMember().getAsMention() + " revoked the all permissions from " + event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + "!")
                    ).complete();
                    return;
                }

                pCat = getPermCat(args[1]); // Abspeichern, der zu verändernden Permission

                if (pCat == null) { // Wenn keine oder eine nicht vorhandene Permission angegeben wurde
                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", null, "Please check your input:\n" +
                                            "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                            "CMD:\t" + help())
                            ).complete()
                    );
                    return;
                }

                if (!event.getMessage().getMentions(Message.MentionType.ROLE).isEmpty()) {  // Wenn die Permission einer Rolle entzogen werden soll
                    String id = event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getId();    // Abspeichern der ID der Rolle

                    if (pCat.get("role").contains(id)) {    // Wenn die Rolle die Permission hat
                        if (pCat == GUILD_PERMS.get(g).SETTINGS) {  // Wenn die Permission "SETTINGS" entzogen werden soll
                            pCat.get("role").remove(id);
                        } else if (pCat == GUILD_PERMS.get(g).CMD_LV2 && !GUILD_PERMS.get(g).SETTINGS.get("role").contains(id)) {   // Wenn die Permission "CMD_LV2" entzogen werden soll und die Rolle keine höhere Permission besitzt
                            pCat.get("role").remove(id);
                        } else if (pCat == GUILD_PERMS.get(g).CMD_LV1 && !GUILD_PERMS.get(g).CMD_LV2.get("role").contains(id) && !GUILD_PERMS.get(g).SETTINGS.get("role").contains(id)) {   // Wenn die Permission "CMD_LV1" entzogen werden soll und die Rolle keine höhere Permission besitzt
                            pCat.get("role").remove(id);
                        } else if (pCat == GUILD_PERMS.get(g).USE && !GUILD_PERMS.get(g).CMD_LV1.get("role").contains(id) && !GUILD_PERMS.get(g).CMD_LV2.get("role").contains(id) && !GUILD_PERMS.get(g).SETTINGS.get("role").contains(id)) {    // Wenn die Permission "USE" entzogen werden soll und die Rolle keine höhere Permission besitzt
                            pCat.get("role").remove(id);
                        } else {    // Wenn die Rolle höhere Permissions als die zu enziehende hat -> Error
                            TIMER.deleteMSG("error",
                                    event.getTextChannel().sendMessage(
                                            MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getAsMention() + " has higher permissions, than " + args[1] + "!")
                                    ).complete()
                            );
                            return;
                        }
                        //savePerms();    // Speichern, der veränderten Permissions

                        event.getTextChannel().sendMessage(
                                MSGS.builder("info", null, event.getMember().getAsMention() + " revoked the " + args[1] + "-permissions from " + event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getAsMention() + "!")
                        ).complete();
                    } else {    // Wenn die Rolle die Permission nicht hat
                        TIMER.deleteMSG("error",
                                event.getTextChannel().sendMessage(
                                        MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.ROLE).get(0).getAsMention() + " has no " + args[1] + "-permissions!")
                                ).complete()
                        );
                        return;
                    }
                } else if (!event.getMessage().getMentions(Message.MentionType.USER).isEmpty()) {   // Wenn die Permission einem User entzogen werden soll
                    String id = event.getMessage().getMentions(Message.MentionType.USER).get(0).getId();    // Abspeichern der ID des Users

                    if (!id.equals(event.getGuild().getOwnerId())) {    // Solange nicht dem Server-Owner die Permissions entzogen werden sollen
                        if (pCat.get("user").contains(id)) {    // Wenn der User die Permission hat
                            if (pCat == GUILD_PERMS.get(g).SETTINGS) {  // Wenn die Permission "SETTINGS" entzogen werden soll
                                pCat.get("user").remove(id);
                            } else if (pCat == GUILD_PERMS.get(g).CMD_LV2 && !GUILD_PERMS.get(g).SETTINGS.get("user").contains(id)) {   // Wenn die Permission "CMD_LV2" entzogen werden soll und der User keine höhere Permission besitzt
                                pCat.get("user").remove(id);
                            } else if (pCat == GUILD_PERMS.get(g).CMD_LV1 && !GUILD_PERMS.get(g).CMD_LV2.get("user").contains(id) && !GUILD_PERMS.get(g).SETTINGS.get("user").contains(id)) {   // Wenn die Permission "CMD_LV1" entzogen werden soll und der User keine höhere Permission besitzt
                                pCat.get("user").remove(id);
                            } else if (pCat == GUILD_PERMS.get(g).USE && !GUILD_PERMS.get(g).CMD_LV1.get("user").contains(id) && !GUILD_PERMS.get(g).CMD_LV2.get("user").contains(id) && !GUILD_PERMS.get(g).SETTINGS.get("user").contains(id)) {   // Wenn die Permission "USE" entzogen werden soll und der User keine höhere Permission besitzt
                                pCat.get("user").remove(id);
                            } else {    // Wenn die Rolle höhere Permissions als die zu enziehende hat -> Error
                                TIMER.deleteMSG("error",
                                        event.getTextChannel().sendMessage(
                                                MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + " has higher permissions, than " + args[1] + "!")
                                        ).complete()
                                );
                                return;
                            }
                            //savePerms();    // Speichern, der veränderten Permissions

                            event.getTextChannel().sendMessage(
                                    MSGS.builder("info", null, event.getMember().getAsMention() + " revoked the " + args[1] + "-permissions from " + event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + "!")
                            ).complete();
                        } else {    // Wenn der User die Permission nicht hat
                            TIMER.deleteMSG("error",
                                    event.getTextChannel().sendMessage(
                                            MSGS.builder("error", null, event.getMessage().getMentions(Message.MentionType.USER).get(0).getAsMention() + " has no " + args[1] + "-permissions!")
                                    ).complete()
                            );
                            return;
                        }
                    } else {    // Error -> Guild-Owner darf gelöscht werden
                        TIMER.deleteMSG("error",
                                event.getTextChannel().sendMessage(
                                        MSGS.builder("error", null, "You can not revoke the permissions of the guild-owner!")
                                ).complete()
                        );
                        return;
                    }
                } else {    // Error -> falsche Markierung
                    TIMER.deleteMSG("error",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("error", "Something went wrong!", "Please check your input:\n" +
                                            "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                            "CMD:\t" + help())
                            ).complete()
                    );
                    return;
                }
                break;

            case "list":    // Listet alle Permissions auf
                g = event.getGuild();
                String use_role = "", use_user = "", cmd1_role = "", cmd1_user = "", cmd2_role = "", cmd2_user = "", settings_role = "", settings_user = "";

                try {
                    if (!GUILD_PERMS.get(event.getGuild()).USE.get("role").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).USE.get("role")) {
                            use_role = use_role.concat("\t\t" + event.getGuild().getRoleById(id).getAsMention() + "\n");
                        }
                    } else {
                        use_role = "\t\t-\n";
                    }
                    if (!GUILD_PERMS.get(event.getGuild()).USE.get("user").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).USE.get("user")) {
                            use_user = use_user.concat("\t\t" + event.getGuild().getMemberById(id).getAsMention() + "\n");
                        }
                    } else {
                        use_user = "\t\t-\n";
                    }

                    if (!GUILD_PERMS.get(event.getGuild()).CMD_LV1.get("role").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).CMD_LV1.get("role")) {
                            cmd1_role = cmd1_role.concat("\t\t" + event.getGuild().getRoleById(id).getAsMention() + "\n");
                        }
                    } else {
                        cmd1_role = "\t\t-\n";
                    }
                    if (!GUILD_PERMS.get(event.getGuild()).CMD_LV1.get("user").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).CMD_LV1.get("user")) {
                            cmd1_user = cmd1_user.concat("\t\t" + event.getGuild().getMemberById(id).getAsMention() + "\n");
                        }
                    } else {
                        cmd1_user = "\t\t-\n";
                    }

                    if (!GUILD_PERMS.get(event.getGuild()).CMD_LV2.get("role").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).CMD_LV2.get("role")) {
                            cmd2_role = cmd2_role.concat("\t\t" + event.getGuild().getRoleById(id).getAsMention() + "\n");
                        }
                    } else {
                        cmd2_role = "\t\t-\n";
                    }
                    if (!GUILD_PERMS.get(event.getGuild()).CMD_LV2.get("user").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).CMD_LV2.get("user")) {
                            cmd2_user = cmd2_user.concat("\t\t" + event.getGuild().getMemberById(id).getAsMention() + "\n");
                        }
                    } else {
                        cmd2_user = "\t\t-\n";
                    }

                    if (!GUILD_PERMS.get(event.getGuild()).SETTINGS.get("role").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).SETTINGS.get("role")) {
                            settings_role = settings_role.concat("\t\t" + event.getGuild().getRoleById(id).getAsMention() + "\n");
                        }
                    } else {
                        settings_role = "\t\t-\n";
                    }
                    if (!GUILD_PERMS.get(event.getGuild()).SETTINGS.get("user").isEmpty()) {
                        for (String id : GUILD_PERMS.get(event.getGuild()).SETTINGS.get("user")) {
                            settings_user = settings_user.concat("\t\t" + event.getGuild().getMemberById(id).getAsMention() + "\n");
                        }
                    } else {
                        settings_user = "\t\t-\n";
                    }

                    TIMER.deleteMSG("bot",
                            event.getTextChannel().sendMessage(
                                    MSGS.builder("bot", "Bot Permissions:", "USE:\n" +
                                            "\tRole:\n" + use_role +
                                            "\tUser:\n" + use_user +
                                            "CMD_LV1:\n" +
                                            "\tRole:\n" + cmd1_role +
                                            "\tUser:\n" + cmd1_user +
                                            "CMD_LV2:\n" +
                                            "\tRole:\n" + cmd2_role +
                                            "\tUser:\n" + cmd2_user +
                                            "SETTINGS:\n" +
                                            "\tRole:\n" + settings_role +
                                            "\tUser:\n" + settings_user)
                            ).complete()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    TIMER.deleteMSG("error",
                            Objects.requireNonNull(event.getGuild().getSystemChannel()).sendMessage(
                                    MSGS.builder("error", "Internel-Error:", "Something went wrong!")
                            ).complete()
                    );
                }
                break;

            default:
                TIMER.deleteMSG("error",
                        event.getTextChannel().sendMessage(
                                MSGS.builder("error", "Something went wrong!", "Please check your input:\n" +
                                        "You:\t`" + event.getMessage().getContentRaw() + "`\n" +
                                        "CMD:\t" + help())
                        ).complete()
                );
                break;
        }
        savePerms();    // Abspeichern der geänderten Permissions
    }


    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.PREFIX.get(g) + "perms' was used!");
    }

    @Override
    public String help() {
        // return "`!perms add/remove use/cmd/settings [@user/@role]`";
        return "**`" + STATIC.PREFIX.get(g) + "perms`** *(Require permission: SETTINGS)*\n" +
                "`- add use/cmd1/cmd2/settings [@user/@role]`\nAdds the and lower permissions to a user or a role.\n" +
                "`- remove use/cmd1/cmd2/settings/all [@user/@role]`\nRevokes the permission(s) from a user or a role.\n" +
                "`- list`\nLists all permission-categories and shows who has them.";
    }
}
