package Util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static Util.STATIC.GUILD_PERMS;

public class PERMS implements Serializable {
    public HashMap<String, ArrayList<String>> USE;
    public HashMap<String, ArrayList<String>> CMD_LV1;
    public HashMap<String, ArrayList<String>> CMD_LV2;
    public HashMap<String, ArrayList<String>> SETTINGS;

    public PERMS(HashMap<String, ArrayList<String>> USE, HashMap<String, ArrayList<String>> CMD1, HashMap<String, ArrayList<String>> CMD2, HashMap<String, ArrayList<String>> SETTINGS) {
        this.USE = USE;
        this.CMD_LV1 = CMD1;
        this.CMD_LV2 = CMD2;
        this.SETTINGS = SETTINGS;
    }
    public static void createGuildPerms(Guild g) {
        HashMap<String, ArrayList<String>> use = new HashMap<>();
        use.put("user", new ArrayList<>());
        use.put("role", new ArrayList<>());
        HashMap<String, ArrayList<String>> cmd1 = new HashMap<>();
        cmd1.put("user", new ArrayList<>());
        cmd1.put("role", new ArrayList<>());
        HashMap<String, ArrayList<String>> cmd2 = new HashMap<>();
        cmd2.put("user", new ArrayList<>());
        cmd2.put("role", new ArrayList<>());
        HashMap<String, ArrayList<String>> settings = new HashMap<>();
        settings.put("user", new ArrayList<>());
        settings.put("role", new ArrayList<>());
        PERMS perms = new PERMS(use, cmd1, cmd2, settings);

        /*perms.USE.get("user").add(g.getOwnerId());
        perms.CMD_LV1.get("user").add(g.getOwnerId());
        perms.CMD_LV2.get("user").add(g.getOwnerId());
        perms.SETTINGS.get("user").add(g.getOwnerId());*/
        GUILD_PERMS.put(g, perms);

        //System.out.println(GUILD_PERMS.get(g).SETTINGS);
    }

    public static void savePerms() {
        File path = new File("RES/BOT_SETTINGS/");
        if (!path.exists())
            path.mkdir();

        File saveFile = new File("RES/BOT_SETTINGS/perms.dat");
        HashMap<String, PERMS> out = new HashMap<>();


        // GUILD_PERMS.forEach((g, perms) -> out.put(g.getId(), perms));
        for (Guild g : GUILD_PERMS.keySet()) {
            // System.out.println(g.getId() + "|" + GUILD_PERMS.get(g).USE + "|" + GUILD_PERMS.get(g).CMD_LV1 + "|" + GUILD_PERMS.get(g).CMD_LV2 + "|" + GUILD_PERMS.get(g).SETTINGS);
            out.put(g.getId(), GUILD_PERMS.get(g));
        }
        SAVELOAD.save(out, saveFile);
    }

    public static void loadPerms(JDA jda) {
        GUILD_PERMS.clear();
        File f = new File("RES/BOT_SETTINGS/perms.dat");

        if (f.exists()) {
            HashMap<String, PERMS> in = (HashMap<String, PERMS>) SAVELOAD.load(f);
            in.forEach((id, perms) -> {
                if (jda.getGuildById(id) != null)
                    GUILD_PERMS.put(jda.getGuildById(id), perms);
            });
        }
    }

    private static HashMap<String, PERMS> getPerms() {
        File saveFile = new File("RES/BOT_SETTINGS/perms.dat");
        return (HashMap<String, PERMS>) SAVELOAD.load(saveFile);
    }
}
