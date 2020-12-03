package Core;

import Commands.Chat.cmdClear;
import Commands.Chat.cmdJoke;
import Commands.Chat.cmdSay;
import Commands.Settings.cmdPerms;
import Commands.Settings.cmdPing;
import Commands.Settings.cmdSetPre;
import Commands.Voice.Autochannel;
import Commands.cmdHelp;
import Listeners.*;
import Util.SAVELOAD;
import Util.SECRETS;
import Util.STATIC;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main
{
    public static JDABuilder builder;

    public static void main(String[] Args)
    {
        SECRETS.load("TG");
        builder = JDABuilder.createDefault(SECRETS.TOKEN);

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "[" + STATIC.VERSION + "] " + "DefaultPrefix: !", null));

        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        addListeners();
        addCommands();

        File path = new File("RES/BOT_SETTINGS/");
        if (!path.exists()) {
            new File("RES/").mkdir();
            new File("RES/BOT_SETTINGS/").mkdir();
        }

        try {
            JDA jda = builder.build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void addCommands()
    {
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("AC", new Autochannel());commandHandler.commands.put("ac", new Autochannel());
        commandHandler.commands.put("clear", new cmdClear());commandHandler.commands.put("c",new cmdClear());
        //commandHandler.commands.put("info", new cmdInfo());
        commandHandler.commands.put("joke", new cmdJoke());
        commandHandler.commands.put("perms", new cmdPerms());
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("say", new cmdSay());
        commandHandler.commands.put("setPre", new cmdSetPre());
    }

    public static void addListeners()
    {
        builder.addEventListeners(new readyListener(),
                new reconnectListener(),
                new commandListener(),
                new policeListener(),
                new guildJoinListener(),
                new AutochannelListener());
    }
}
