package Util;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;

public class STATIC {
    public static final String VERSION = "v.1.0.2";

    public static HashMap<Guild, String> PREFIX = new HashMap<>();

    public static final String CMDHELP = ":warning: This command isn't complete! Please use the following example:\n\n";

    public static  String[] INSULT = {};

    public static HashMap<Guild, ArrayList> INSULTS = new HashMap<>();

    public static String INVOKE;

    public static HashMap<Guild, PERMS> GUILD_PERMS = new HashMap<>();
}