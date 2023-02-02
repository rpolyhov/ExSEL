package org.exsel.helpers;

import java.io.File;

public class FilesHelper {

    public static String getFilePath(String name) {
        File file;
        if ((file = new File(new File("").getAbsolutePath() + "/" + name)).exists())
            return file.getAbsolutePath();
        else
            return new File(FilesHelper.class.getClassLoader().getResource("./").getPath() + name).getAbsolutePath();
    }

    public static File getFile(String name) {
        File file;
        if ((file = new File(new File("").getAbsolutePath() + "/" + name)).exists())
            return file;
        else
            return new File(FilesHelper.class.getClassLoader().getResource("./").getPath() + name);
    }
}