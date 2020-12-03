package Util.AC;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.io.Serializable;

public class VC_TMP implements Serializable {
    public VoiceChannel TMP_VC;
    public AC ROOT_VC;
    public int NUMBER;
    public TextChannel TC;

    public VC_TMP(VoiceChannel tmpVC, AC rootVC, Integer number, TextChannel tmpTC) {
        this.TMP_VC = tmpVC;
        this.ROOT_VC = rootVC;
        this.NUMBER = number;
        this.TC = tmpTC;
    }
}
