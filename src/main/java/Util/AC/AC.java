package Util.AC;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.io.Serializable;
import java.util.ArrayList;

public class AC implements Serializable {
    public VoiceChannel VC;
    public Guild G;
    public boolean LT;
    public ArrayList<Integer> I;

    public AC(VoiceChannel vc, Guild g, boolean lt) {
        this.VC = vc;
        this.G = g;
        this.LT = lt;
        this.I = new ArrayList<>();
    }
}