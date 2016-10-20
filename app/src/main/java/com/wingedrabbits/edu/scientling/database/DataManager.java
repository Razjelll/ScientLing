package com.wingedrabbits.edu.scientling.database;

import com.wingedrabbits.edu.scientling.model.Word;

import java.util.List;

/**
 * Created by Razjelll on 18.10.2016.
 */

public interface DataManager {
    Word getWord(long wordId);
    List<Word> getMovieHeaders();
}
