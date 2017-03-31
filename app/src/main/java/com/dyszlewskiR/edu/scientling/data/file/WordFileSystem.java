package com.dyszlewskiR.edu.scientling.data.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.dyszlewskiR.edu.scientling.utils.BitmapUtils;
import com.dyszlewskiR.edu.scientling.utils.FileUtils;
import com.dyszlewskiR.edu.scientling.utils.UriUtils;

import java.io.IOException;

/**
 * Klasa odpowiedzialna za zapisywanie obrazków i nagrań.
 * TODO Klasa powinna odczytywaj w jakiej pamięci ma zostać zapisany plik
 * Klasa stanowi dodatkową warstwę zapisu pliku, na której można wykonywać dodatkowe działania,
 * takie jak przeskalowanie obrazków
 */

public class WordFileSystem {

    private static final int MAX_IMAGE_SIZE = 300;

    public static void saveImage(String fileName, String setCatalog, byte[] data, boolean resize, Context context) throws IOException {
        if(resize ){
            data = getResizedData(data, MAX_IMAGE_SIZE);
        }
        //TODO zrobić wykrywanie z jakiej pamięci korzystamy w tej chwili i zapisanie w odpowiednim miejsu
        FileUtils.saveFileInternalStorage(setCatalog, fileName,data,context);
    }

    /**
     * Metoda zwracająca dane zmniejszonego obrazka
     * @param data
     * @param maxSize
     * @return
     */
    private static byte[] getResizedData(byte[] data, int maxSize){
        Bitmap bitmap = BitmapUtils.getBitmap(data);
        //w przypadku obrazka mniejszego od rozmiaru do którego chcieliśmy konwertować zwracamy orginalne dane
        if(bitmap.getWidth() < maxSize && bitmap.getHeight() < maxSize){
            return data;
        }
        bitmap = BitmapUtils.resize(bitmap, maxSize,false);
        return BitmapUtils.toByteArray(bitmap);
    }

    public static void saveImage(String fileName, String setCatalog,  Uri uri, Context context, boolean resize) throws IOException {
        byte[] data = UriUtils.toByteArray(uri, context);
        saveImage(fileName, setCatalog, data, resize, context);
    }


    public static void saveRecord(String fileName, String setCatalog, byte[] data, Context context) throws IOException{
        //TODO sprawdzenie w jakiej pamięci powinno zostać zapisane nagranie
        FileUtils.saveFileInternalStorage(setCatalog, fileName, data, context);
    }

    public static void saveRecord(String fileName, String setCatalog, Uri uri, Context context) throws IOException {
        byte[] data = UriUtils.toByteArray(uri, context);
        saveRecord(fileName, setCatalog, data, context);
    }

    public static Uri getImageUri(String fileName, String setCatalog, Context context ){
        //TODO pobierać z odpowiedniej pamięcia
        return FileUtils.getInternalStorageUri(setCatalog, fileName, context);
    }

    public static Uri getRecordUri(String fileName, String setCatalog, Context context){
        return FileUtils.getInternalStorageUri(setCatalog, fileName, context);
    }


    // TODO można zrobić jedną metodę dla obrazków i nagrań
    public static void deleteImage(String fileName, String secCatalog, Context context){
        FileUtils.deleteFileInternalStorage(fileName, secCatalog, context);
    }

    public static void deleteRecord(String fileName, String setCatalog, Context context){
        FileUtils.deleteFileInternalStorage(fileName, setCatalog, context);
    }


}
