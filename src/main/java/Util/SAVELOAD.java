package Util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class SAVELOAD {
    public static void save(Object object, String file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(file)));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void save(Object object, File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void save(String link, File file) {
        try {
            FileUtils.copyURLToFile(
                    new URL(link),
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object load(String src) {
        Object object = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(src)));
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (object != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    public static Object load(File src) {
        Object object = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(src));
            object = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (object != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}
