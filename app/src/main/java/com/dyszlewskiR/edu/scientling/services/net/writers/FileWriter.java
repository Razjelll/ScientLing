package com.dyszlewskiR.edu.scientling.services.net.writers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileWriter {

    private OutputStream mStream;
    private Callback mCallback;
    private int mChunkSize;

    public interface Callback{
        void onProgressUpdate(long uploadedBytes);
    }

    public FileWriter(OutputStream outputStream,int chunkSize){
        mStream = outputStream;
        mChunkSize = chunkSize;
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void startZipWriting(String folderPath) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(mStream);
        final File folder = new File(folderPath);
        if(!folder.exists()){
            return;
        }
        int entriesCount = 0;

        for (final File imageEntry : folder.listFiles()) {
            ZipEntry entry = new ZipEntry(imageEntry.getName());
            zipOutputStream.putNextEntry(entry);
            entriesCount++;
            FileInputStream inputStream = new FileInputStream(imageEntry);
            int data;
            byte[] buffer = new byte[mChunkSize];
            long length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //zipOutputStream.write(inputStream.read());
                zipOutputStream.write(buffer);
                zipOutputStream.flush();
                if(mCallback!=null){
                    mCallback.onProgressUpdate(length);
                }
            }

            zipOutputStream.closeEntry();
            inputStream.close();
        }

    }

    public void close() throws IOException {
        if(mStream != null){
            mStream.close();
        }

    }
}