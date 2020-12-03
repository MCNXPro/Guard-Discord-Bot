package Util.AC;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.io.Serializable;
import java.util.ArrayList;

public class ACS implements Serializable {
    public String VC;
    public String G;
    public boolean LT;
    public ArrayList<Integer> I;

    public ACS(VoiceChannel vc, Guild g, boolean lt) {
        this.VC = vc.getId();
        this.G = g.getId();
        this.LT = lt;
        this.I = new ArrayList<>();
    }
}