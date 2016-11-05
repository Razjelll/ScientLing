package com.wingedrabbits.edu.scientling.data.database.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Klasa służąca do odczytywania plików sql zawierajacych zapytania do twotzenia i aktualizacji bazy danych.
 */

public class SQLiteQueryReader {

    //private InputStream mStream;

    private final String STATEMENTS_SEP = ";";
    private final String OPEN_BRACKET = "(";
    private final String COMMENT = "--";
    private final String START_COMMENT = "/*";
    private final String END_COMMENT = "*/";



    public SQLiteQueryReader()
    {
        //mStream = inputStream;

    }

    /**
     * Metoda służąca do odczytywania plików sql w postani strumienia wejściowego. Skorzystano ze strumienia wejściowego
     * ponieważ w Androidzie dostep do plików odbywa się albo przez system plików, gdzie trzeba znać położenie danego pliku.
     * Sprawę mogą komplikować dwa rodzaje pamięci - wewnętrzna i zewnętrzna. Drugim sposobem jest korzystanie z pliku jako zasobu.
     * Aby otworzyć taki plik, potrzebma jest klasa Context, która jest składową Androida. Klasa SQLiteQueryReader jest klasą narzędziową,
     * więc nie chcialem, aby miała jakiekolwiek powiązania z API Androida. Dzięki temu, klasa w łatwiejszy sposób moze być wykorzystywana
     * w innych projektach.
     * Metoda korzysta z kontenera ArrayList<String> w którym są przechowywane pojszczególne zapytania.
     * Podczas parsowania przechodzimy po każdym słowie odzielnie. Pozwala to na pozbycie się zbędnych spacji, dzięki czemu
     * wynikowe zapytanie będzie szczuplejsze. Strumien wejściowy może zawierać kilka zapytań. Każde z takich zapytań musi być zakończone znakiem ;,
     * aby metoda rozpoznała jego koniec. Jeśli Scanner odczyta słowo zawierający znak ;, dodaje słowo do listy zapytań, czyści obiekt klasy StringBuilder
     * a następnie przeszukuje pozostalą część strumienia. Jeśli w danym słowie nie ma znaku kończącego zapytanie dodaje do wyniku spację, ponieważ pozwala to odzielić
     * od siebie poszczególne słowa. Jeśli Scanner przejdzie do końca struminia algorytm kończy się a metoda zwaraca listę zapytań.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    public ArrayList<String> readFromStream(InputStream stream) throws IOException {  //TODO refaktoryzacja
        ArrayList<String> statementsList = new ArrayList<>();
        StringBuilder statementBuilder = new StringBuilder();
        if(stream == null)
        {
            return statementsList;
        }
        Scanner scanner = new Scanner(stream);
        boolean isComment = false;
        while(scanner.hasNext())
        {
            String word = scanner.next();

            if(word.contains(START_COMMENT)) //serveStartComment
            {
                isComment = true;
                word = word.substring(0,word.lastIndexOf(START_COMMENT));
                if(!word.equals(""))
                {
                    statementBuilder.append(word);
                    statementBuilder.append(" ");
                }
            } else if(word.contains(END_COMMENT)) //serveEndComment
            {
                isComment = false;
                word = word.substring(word.lastIndexOf(END_COMMENT)+2, word.length());
            }
            if(word.contains(COMMENT)) //serveComment
            {
                if(scanner.hasNextLine())
                {
                    scanner.nextLine();
                }
            }
            else
            {
                if(!isComment) { // appendWord
                    statementBuilder.append(word);
                    if(word.contains(STATEMENTS_SEP)) { //serveStatementSeparator
                        String statement = statementBuilder.toString();
                        statementsList.add(statement);
                        statementBuilder.setLength(0);
                    }
                    else
                    {
                        statementBuilder.append(" ");
                    }
                }

            }
        }
        scanner.close();
        stream.close();
        return statementsList;
    }


}
