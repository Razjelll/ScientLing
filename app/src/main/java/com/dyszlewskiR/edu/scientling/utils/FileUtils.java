package com.dyszlewskiR.edu.scientling.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Razjelll on 22.03.2017.
 */

//TODO można to nazwać jakoś inaczej
    //TODO pousuwać niepotrzebne metody
public class FileUtils {

    private static final String LOG_TAG = "FileUtils";

    public static byte[] getBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len=0;
        while((len = inputStream.read(buffer)) != -1){
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    public static File saveFileInCache(String fileName, byte[]data, Context context) throws IOException{
        File file  = new File(context.getCacheDir(), fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.close();
        return file;
    }

    /*public static byte[] readByteArrayCacheFile(String filename ,Context context) throws IOException
    {
        BufferedReader input = null;
        File file = new File(context.getCacheDir(), filename);
        int size = (int)file.length();
        byte[] bytes = new byte[size];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        bufferedInputStream.read(bytes, 0, size);
        return bytes;
    }*/

    public static File getFileFromCache(String filename, Context context) {
        return new File(context.getCacheDir(), filename);
    }

    public static File getFileFromInternalStorage(String catalog, String filename, Context context){
        return new File(catalog,filename);
    }

    public static long getFileSize(String catalog, String filename){
        return new File(catalog, filename).length();
    }

    public static long getFileSizeFromCache(String filename, Context context){
        return new File(context.getCacheDir(), filename).length();
    }

    public static boolean deleteFileFromCache(String filename, Context context){
        File file = new File(context.getCacheDir(), filename);
        if(file != null){
            return file.delete();
        }
        return true;
    }



    public static Uri createEmptyFileInCache(String filename, Context context) throws IOException {
        File file = new File(context.getCacheDir(), filename);
        file.createNewFile();
        return Uri.fromFile(file);
    }

    public static Uri getUriFromCache(String filename, Context context){
        return Uri.fromFile(new File(context.getCacheDir(), filename));
    }



    public static boolean checkFileExist(String fileName, String catalog){
        File file = new File(catalog,fileName);
        return file.exists();
    }

    // INTERNAL STORAGE ----------------------------------------------------------------------------

    /**
     * Zapisuje plik w pamięci wewnętrznej w podanym katalogu
     * @param catalog
     * @param fileName
     * @param data
     * @param context
     * @throws IOException
     */
    public static void saveFileInternalStorage(String catalog, String fileName,byte[] data,  Context context) throws  IOException
    {
        FileOutputStream outputStream = context.openFileOutput(context.getFilesDir()+"/" + catalog+"/"+fileName, Context.MODE_PRIVATE);
        outputStream.write(data);
        outputStream.close();
    }

    public static boolean deleteFileInternalStorage(String filename, String catalog, Context context){
        File file = new File(context.getFilesDir() + "/"+  catalog, filename);
        if(file != null){
            return file.delete();
        }
        return true;
    }

    public static Uri getInternalStorageUri(String filename, String catalog, Context context){
        return Uri.fromFile(new File(context.getFilesDir() + "/" +catalog, filename));
    }

    //----------------------------------------------------------------------------------------------

    // EXTERNAL STORAGE ----------------------------------------------------------------------------

    /**
     * Sprawdza czy pamięć zewnętrzna jest gotowa do zapisu
     * @return gotowość pamięci zewnętrznej do zapisu
     */
    public static boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Sprawdza czy pamięć zewnętrzna jest gotowa do odczytu
     * @return gotowośc pamięci zewnętrzej do oczytu
     */
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * Tworzy folder określonego typu w prywatnej przestrzeni pamięci zewnętrznej. Folder będzie widoczny tylko
     * dla danej aplikacji
     * @param catalogName nazwa tworzonego katalogu
     * @param catalogType tym tworzonego katalogu (np DIRECTORY_PICTURES)
     * @param context kontekst aplikacji
     * @return referencja do nowoutworzonego katalogu
     */
    public File createCatalogExternalStorage(String catalogName,String catalogType, Context context){
        //TODO dodać tworzenie pliku nomedia
        File file = new File(context.getExternalFilesDir(catalogType), catalogName);
        if(!file.mkdirs()){ //mkdir tworzy katalog
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /**
     * Tworzenie pliku w prywatnej przestrzeni pamięci zewnętrznej w odpowiednim katalogu.
     * @param fileName nazwa tworzonego pliku
     * @param catalogType określa rodzaj katalogu w pamięci prywatnej nap (DIRECOTRY_PICTURES)
     * @param data dane z których zostanie utworzony plik
     * @param context kontekst aplikacji
     * @return referencja do utworzonego pliku
     * @throws IOException
     */
    public File saveFileExternalStorage(String fileName, String catalogType,  byte[] data, Context context) throws IOException {
        //TODO zobaczyć czy to działa
        File root = context.getExternalFilesDir(catalogType);
        //TODO można dodać katalog w razie jego braku
        File file = new File(root.getAbsolutePath(), fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
        return file;
    }

    /**
     * Usuwanie pliku z prywatnej przestrzeni pamięcia zewnętrznej z katalogu odpowiedniego typu.
     * //TODO Metodę można byłoby napisać jeszcze bez pomocy catalogType. Zamiast niego użyć
     * catalog i korzystać z context.getExternalFileDir(null). Następnie dodać do tej ścieżki catalog.
     * zobhaczyć jak to będzie działać i czy  będzie zapisywało w odpowiednim katalogu
     * @param fileName nazwa pliku który ma zostać usunięty
     * @param catalogType typ katalogu w pamięci zewnętrznej z którego ma zostać usunięty plik
     * @param context kontekst aplikacji
     * @return powodzenie usunięcia pliku
     */
    public boolean deleteFileExternalStorage(String fileName, String catalogType, Context context){
        File root = context.getExternalFilesDir(catalogType);
        File file = new File(root.getAbsolutePath(), fileName);
        if(file.exists()){
            return file.delete();
        }
        return true;
    }

    /**
     * Zwraca uri do pliku o podanej nazwie z podanego rodzaju katalogu z prywatnej przestrzeni pamięci
     * zewnętrznej
     * @param fileName nazwa pliku
     * @param catalogType typ katalogu z którego checemy otrzymać plik
     * @param context kontekst aplikacji
     * @return Uri do poszukiwanego pliku
     */
    public Uri getFileUriExternalStorage(String fileName, String catalogType, Context context){
        File root = context.getExternalFilesDir(catalogType);
        File file = new File(root.getAbsolutePath(), fileName);
        return Uri.fromFile(file);
    }

    //----------------------------------------------------------------------------------------------
}
