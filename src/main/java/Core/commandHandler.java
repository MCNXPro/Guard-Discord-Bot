package Core;

import Commands.Command;
import Util.MSGS;
import Util.TIMER;

import java.util.HashMap;

public class commandHandler
{
    public static final commandParser parser = new commandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) {
        if (commands.containsKey(cmd.invoke))   // Abfrage, ob der cmd existiert: true -> versuch ihn auszuführen
        {
            boolean safe = commands.get(cmd.invoke).called(cmd.invoke, cmd.args, cmd.event);    // Speichern, ob der cmd aufgerufen werden kann über die Methode called

            if (safe)   // Speichern, ob der cmd aufgerufen werden kann über die Methode called: true -> führe den cmd aus
            {
                commands.get(cmd.invoke).action(cmd.invoke, cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else    // false -> gebe die Nachricht, dass der cmd nicht ausgeführt werden konnte
            {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
        else    // false -> Gebe aus, das der cmd nicht existiert
        {
            System.out.println("Command don't exist!!!");
            TIMER.deleteMSG("error",
                    cmd.event.getTextChannel().sendMessage(
                            MSGS.builder("error", null, "This command `" + cmd.raw + "` does not exist!")
                    ).complete());
        }
    }
}