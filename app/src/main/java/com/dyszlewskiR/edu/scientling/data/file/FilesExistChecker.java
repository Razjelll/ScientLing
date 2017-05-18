package com.dyszlewskiR.edu.scientling.data.file;

import java.io.File;

public class FilesExistChecker {

    public static boolean checkFilesExist(String catalogPath){
        File file = new File(catalogPath);
        if(file.exists()){
            return file.listFiles().length >0;
        }
        return false;
    }
}
