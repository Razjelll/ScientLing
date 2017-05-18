package com.dyszlewskiR.edu.scientling.data.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.dyszlewskiR.edu.scientling.utils.BitmapUtils;
import com.dyszlewskiR.edu.scientling.utils.FileUtils;
import com.dyszlewskiR.edu.scientling.utils.UriUtils;

import java.io.File;
import java.io.IOException;


/**
 * Klasa odpowiedzialna za zapisywanie obrazków i nagrań.
 * TODO Klasa powinna odczytywaj w jakiej pamięci ma zostać zapisany plik
 * Klasa stanowi dodatkową warstwę zapisu pliku, na której można wykonywać dodatkowe działania,
 * takie jak przeskalowanie obrazków
 */
public class FileSystem {

    private static final int MAX_IMAGE_SIZE = 300;
    public static final String IMAGES = "images";
    public static final String RECORDS = "records";

    private static String getImagesCatalog(String catalogName){
        return catalogName + "/" + IMAGES;
    }

    private static String getRecordsCatalog(String catalogName){
        return catalogName + "/" + RECORDS;
    }

    public static String getMediaCatalog(String catalogName, String mediaType){
        if (mediaType.equals(IMAGES)) {
            return getImagesCatalog(catalogName);
        } else {
            return getRecordsCatalog(catalogName);
        }
    }

    public static void saveImage(String fileName, String setCatalog, byte[] data, boolean resize, Context context) throws IOException {
        if (resize) {
            data = getResizedData(data, MAX_IMAGE_SIZE);
        }
        //TODO zrobić wykrywanie z jakiej pamięci korzystamy w tej chwili i zapisanie w odpowiednim miejsu
        FileUtils.saveFileInternalStorage(getImagesCatalog(setCatalog), fileName, data, context);
    }

    /**
     * Metoda zwracająca dane zmniejszonego obrazka
     *
     * @param data    dane orginalnego obrazka
     * @param maxSize najdłuższy możliwy wymiar obrazka wyrażony w pikselach.
     * @return dane pomniejszonego obrazka
     */
    private static byte[] getResizedData(byte[] data, int maxSize) {
        Bitmap bitmap = BitmapUtils.getBitmap(data);
        //w przypadku obrazka mniejszego od rozmiaru do którego chcieliśmy konwertować zwracamy orginalne dane
        if (bitmap.getWidth() < maxSize && bitmap.getHeight() < maxSize) {
            return data;
        }
        bitmap = BitmapUtils.resize(bitmap, maxSize, false);
        return BitmapUtils.toByteArray(bitmap);
    }

    public static void saveImage(String fileName, String setCatalog, Uri uri, Context context, boolean resize) throws IOException {
        byte[] data = UriUtils.toByteArray(uri, context);
        saveImage(fileName, setCatalog, data, resize, context);
    }


    public static void saveRecord(String fileName, String setCatalog, byte[] data, Context context) throws IOException {
        //TODO sprawdzenie w jakiej pamięci powinno zostać zapisane nagranie
        FileUtils.saveFileInternalStorage(getRecordsCatalog(setCatalog), fileName, data, context);
    }

    public static void saveRecord(String fileName, String setCatalog, Uri uri, Context context) throws IOException {
        byte[] data = UriUtils.toByteArray(uri, context);
        saveRecord(fileName, setCatalog, data, context);
    }

    public static Uri getImageUri(String fileName, String setCatalog, Context context) {
        //TODO pobierać z odpowiedniej pamięcia
        if (fileName == null || setCatalog == null) {
            return null;
        }
        return FileUtils.getInternalStorageUri(fileName, getImagesCatalog(setCatalog), context);
    }

    public static Uri getRecordUri(String fileName, String setCatalog, Context context) {
        if (fileName == null || setCatalog == null) {
            return null;
        }
        return FileUtils.getInternalStorageUri(fileName, getRecordsCatalog(setCatalog), context);
    }

    public static File getCatalog(String catalog, Context context){
        return FileUtils.getInternalCatalog(catalog, context);
    }

    public static File getFile(String name, String catalog, Context context){
        return FileUtils.getFile(name, catalog, context);
    }

    // TODO można zrobić jedną metodę dla obrazków i nagrań
    public static void deleteImage(String fileName, String setCatalog, Context context) {
        if (fileName != null && setCatalog != null) {
            FileUtils.deleteFileInternalStorage(fileName, getImagesCatalog(setCatalog), context);
        }
    }

    public static void deleteFile(Uri uri){
        if(uri != null){
            FileUtils.deleteFileInternalStorage(uri);
        }
    }

    public static void deleteRecord(String fileName, String setCatalog, Context context) {
        if (fileName != null && setCatalog != null) {
            FileUtils.deleteFileInternalStorage(fileName,getRecordsCatalog(setCatalog), context);
        }
    }

    public static void deleteCatalog(String catalogName, Context context) {
        FileUtils.deleteDirectory(catalogName, context);
    }

    public static void deleteImageCatalog(String catalogName, Context context){
        deleteCatalog(getImagesCatalog(catalogName), context);
    }

    public static void deleteRecordsCatalog(String catalogName, Context context){
        deleteCatalog(getRecordsCatalog(catalogName), context);
    }

    public static boolean checkFileExist(String fileName, String catalog, Context context) {
        if (fileName == null || catalog == null) {
            return false;
        }
        //TODO to przerobić, ponieważ context.getFilesDir nie powinno być tutaj
        boolean found = FileUtils.checkFileExist(fileName, catalog, context);
        return found;
    }

    public static boolean hasImages(String catalogName, Context context){
        return isCatalogEmpty(getImagesCatalog(catalogName), context);
    }

    public static boolean hasRecords(String catalogName, Context context){
        return isCatalogEmpty(getRecordsCatalog(catalogName), context);
    }

    private static boolean isCatalogEmpty(String catalogPath, Context context){
        if(catalogPath == null || context == null){
            return false;
        }
        /*boolean isEmpty = FileUtils.isCatalogEmpty(catalogPath, context);
        return isEmpty;*/
        File file = new File(FileUtils.getPath(catalogPath, context));
        if(file.exists()){
            return file.listFiles().length >0;
        }
        return false;
    }

    public static boolean checkImageExist(String fileName, String catalog, Context context){
        return checkFileExist(fileName, catalog, IMAGES, context);
    }

    public static boolean checkRecordExist(String fileName, String catalog, Context context){
        return checkFileExist(fileName, catalog, RECORDS, context);
    }

    private static boolean checkFileExist(String fileName, String catalog, String type, Context context){
        if (fileName == null || catalog == null) {
            return false;
        }
       // boolean found = FileUtils.checkFileExist(fileName, context.getFilesDir().getAbsolutePath() + "/" + catalog + "/"+type);
        boolean found = FileUtils.checkFileExist(fileName, catalog + "/"+type, context);
        return found;
    }

    public static String getImagePath(String setCatalog, Context context){
        File file = FileUtils.getInternalCatalog(getImagesCatalog(setCatalog), context);
        return file.getAbsolutePath();
    }

    public static String getRecordsPath(String setCatalog, Context context){
        File file = FileUtils.getInternalCatalog(getRecordsCatalog(setCatalog), context);
        return file.getAbsolutePath();
    }

    public static String getMediaPath(String setCatalog, String mediaType, Context context){
        if(mediaType.equals(IMAGES)){
            return getImagePath(setCatalog, context);
        } else {
            return getRecordsPath(setCatalog, context);
        }
    }

    public static String getPath(String catalgo, Context context){
        return FileUtils.getPath(catalgo, context);
    }



}
