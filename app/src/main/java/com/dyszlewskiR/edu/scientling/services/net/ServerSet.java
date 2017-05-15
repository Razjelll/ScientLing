package com.dyszlewskiR.edu.scientling.services.net;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dyszlewskiR.edu.scientling.data.models.models.VocabularySet;
import com.dyszlewskiR.edu.scientling.services.net.services.UploadSetService;

public class ServerSet {
    public static void upload(VocabularySet set,String description, boolean database, boolean images, boolean records, Context context){
        Log.d("UploadSet", "startUploadService");
        Intent intent = new Intent(context, UploadSetService.class);
        intent.putExtra("id", set.getId());
        intent.putExtra("name", set.getName());
        if(database){
            if(!description.equals("")){
                intent.putExtra("desc", description);
            }
        }
        intent.putExtra("database", database);
        if(images || records){
            intent.putExtra("globalId", set.getGlobalId());
        }
        intent.putExtra("images", images);
        intent.putExtra("records",records);
        context.startService(intent);
    }

    public static void changeDescription(VocabularySet set, String description){

    }
}
