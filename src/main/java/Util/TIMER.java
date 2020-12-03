package Util;

import net.dv8tion.jda.api.entities.Message;

import java.util.Timer;
import java.util.TimerTask;

public class TIMER {
    public static void deleteMSG(String typ, Message msg) {
        try {
            switch (typ) {
                case "error":
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 20000);
                    break;

                case "info":
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 5000);
                    break;

                case "music":
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 180000);
                    break;

                case "vote":
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 60000);
                    break;

                case "bot":
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            msg.delete().queue();
                        }
                    }, 60000);
                    break;
            }
        } catch (Exception ignored) {}
    }

    public static void deleteMSG(long delay, Message msg) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            msg.delete().queue();
                        } catch (Exception ignored) {}
                    }
                }, delay);

    }

    //public static void Timer(long delay, task )
}
