package Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MSGS {
    public static MessageEmbed builder(String typ, String title, String content) {
        MessageEmbed msg;
        switch (typ) {

            case "error":
                msg = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(title)
                        .setDescription(content)
                        .build();
                break;
            case "info":
                msg = new EmbedBuilder()
                        .setColor(Color.YELLOW)
                        .setTitle(title)
                        .setDescription(content)
                        .build();
                break;
            case "private":
                msg = new EmbedBuilder()
                        .setColor(new Color(0x2FFFA8))
                        .setTitle(title)
                        .setDescription(content)
                        .build();
                break;
            case "bot":
                msg = new EmbedBuilder()
                        .setColor(new Color(0x1AA9D3))
                        .setTitle(title)
                        .setDescription("**" + content + "**")
                        .build();
                break;
            case "joke":
                msg = new EmbedBuilder()
                        .setColor(new Color(0xFFF98F))
                        .setTitle(title)
                        .setDescription(content)
                        .build();
                break;
            default:
                msg = null;
        }
        return msg;
    }

    public static MessageEmbed builder(Color color, String content) {
        return new EmbedBuilder()
                .setColor(color)
                .setDescription(content)
                .build();
    }

    public static MessageEmbed builder(Color color, String title, String content) {
        return new EmbedBuilder()
                .setTitle(title)
                .setColor(color)
                .setDescription(content)
                .build();
    }
}
