package com.dyszlewskiR.edu.scientling.data.file;

import java.io.File;

public class FileSizeCalculator {

    public final static int KB = 1;
    public final static int MB = 2;
    public final static int GB = 3;

    public static long calculate(String path) {
        File file = new File(path);
        long size = 0;
        for (File fileEntry : file.listFiles()) {
            size += fileEntry.length();
        }
        return size;
    }

    public static float calculate(String path, int unit)
    {
        long sizeInBytes = calculate(path);
        return calculate(sizeInBytes, unit);
    }

    private static float calculate(long longInBytes, int unit){
        float size = longInBytes;
        //nie wstawiamy breaków i wyliczamy w odwrotnej kolejności, ponieważ nie jeżeli chemy wartość w GB
        // będzędziemy musieli podzielić 3 razy przez 1024, i tak też się stanie;
        switch (unit){
            case GB:
                size = size /1024.f;
            case MB:
                size = size / 1024.f;
            case KB:
                size = size / 1024.f;
        }
        return  size;
    }
}