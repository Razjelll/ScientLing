package com.dyszlewskiR.edu.scientling.data.file;

import android.content.Context;

import com.dyszlewskiR.edu.scientling.utils.FileUtils;

/**
 * Klasa odpowiedzialna za tworzenie nowych klas dla plików ktore chcemy zapisać.
 */

public class FileNameCreator {
    /**Liczba określająca ile pierwszych liter rootname ma posłużyć do utworzenia nazwy pliku.
     * Wartość -1 oznacza, że żadne ograniczenie nie będzie zastosowane.
     * Ograniczenie długości ma na celu zmniejszenie rozmiaru bazy danych.
     */
    private static final int ROOT_NAME_LENGHT = 4;

    /**
     * Metoda znajdująca pierwszą wolną nazwę utworzoną na podstawie rootName
     * Jeżeli rootName już istnieje na dysku, wtedy do nazwy zostanie dodana liczba a następnie
     * nastąpi ponowne sprawdzenie. W przypadku znalezienia podanej nazwy numer zostanie zwiększony
     * i nazwa zostanie sprawdzona jeszcze raz
     * @param rootName nazwa na podstawie której będzie tworzona nazwa dla pliku
     * @param catalog katalog w którym bedzie sprawdzane istnienie pliku
     * @param extension rozszerzenie jakie będize miał plik
     * @return
     */
    public static String getName(String rootName, String catalog, String extension){
        boolean find;
        int number = 0;
        String shortRootname = rootName.substring(0, ROOT_NAME_LENGHT);
        String fileName = shortRootname + "." + extension;
        do{
            if(number != 0){
                fileName = shortRootname + number + "." + extension;
             }
            find = FileUtils.checkFileExist(catalog, fileName);
            number++;
        } while(find);
        return fileName;
    }
}
