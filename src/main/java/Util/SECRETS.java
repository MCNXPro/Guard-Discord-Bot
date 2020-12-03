package Util;

import java.io.File;
import java.io.Serializable;

public class SECRETS implements Serializable
{
    public static String TOKEN;

    public static String DEVID;

    public static void load(String fileToken) {
        File loadT = new File("RES/BOT_RES/" + fileToken + ".dat");
        File loadDEVID = new File("RES/BOT_RES/DEVID.dat");

        TOKEN = (String) SAVELOAD.load(loadT);
        DEVID = (String) SAVELOAD.load(loadDEVID);
    }
}
