package com.dyszlewskiR.edu.scientling.utils;

import com.dyszlewskiR.edu.scientling.data.models.Translation;

import java.util.List;

/**
 * Created by Razjelll on 20.12.2016.
 */

public class TranslationListToString {

    public static String toString(List<Translation> translations)
    {
        StringBuilder builder = new StringBuilder();
        int listSize = translations.size();
        for(int i=0; i< listSize; i++)
        {
            builder.append(translations.get(i).getContent());
            if(i != listSize-1)
            {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
