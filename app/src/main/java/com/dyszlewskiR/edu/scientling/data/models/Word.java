package com.dyszlewskiR.edu.scientling.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Model Słówka. Obiekty modelu służą do reprezentowania encji danych. Obiekty modelu to obiektuy klas, których aktywności i inny kod używaja do zapisywania, pobierania
 * i wyświetlania danych na temat danego obiektu. Obiekty modelu nie odpowiadają dokładnie tabelom, ale są ich przybliżonymi odpowiednikami. Różnica między tabelami baz danych a obiektami modelu
 * wynika ze związków pomiędzy tabelami bazy danych
 */

public class Word implements Parcelable{

    private long id;
    private String content;
    private String transcription;
    private Definition definition;
    private PartOfSpeech partsOfSpeech;
    private Category category;
    private long lessonId; //TODO przemyśleć to
    private byte difficult;
    private byte masterLevel;
    private boolean selected;
    private ArrayList<Translation> translations;
    private ArrayList<Sentence> sentences;
    private ArrayList<Hint> hints;
    private ArrayList<Mnemonic> mnemonics;
    //private ArrayList<String> synonyms;
    //private ArrayList<String> antonyms;
    //private ArrayList<Note> notes;
    private Image image;
    private Record record;

    //TODO dodać ograniczenia do setterów

    public Word() {
    }

    public Word(long id) {
        this.id = id;
    }

    public boolean hasSentences()
    {
        return sentences != null && sentences.size() != 0;
    }

    public boolean hasHints()
    {
        return hints != null;
    }

    public boolean hasDefinition()
    {
        return definition != null;
    }

    public boolean hasImage()
    {
        return image != null;
    }

    public boolean hasRecord()
    {
        return record != null;
    }


    protected Word(Parcel in) {
        id = in.readLong();
        content = in.readString();
        transcription = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        lessonId = in.readLong();
        difficult = in.readByte();
        masterLevel = in.readByte();
        selected = in.readByte() != 0;
        sentences = in.createTypedArrayList(Sentence.CREATOR);
        //in.readTypedList(translations, Translation.CREATOR);
        translations = in.createTypedArrayList(Translation.CREATOR);
        definition = in.readParcelable(Definition.class.getClassLoader());
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public PartOfSpeech getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public void setPartsOfSpeech(PartOfSpeech partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public byte getDifficult() {
        return difficult;
    }

    public void setDifficult(byte difficult) {
        this.difficult = difficult;
    }

    public byte getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(byte masterLevel) {
        this.masterLevel = masterLevel;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<Translation> translations) {
        this.translations = translations;
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList<Sentence> sentences) {
        this.sentences = sentences;
    }

    public ArrayList<Hint> getHints() {
        return hints;
    }

    public void setHints(ArrayList<Hint> hints) {
        this.hints = hints;
    }

    public ArrayList<Mnemonic> getMnemonics() {
        return mnemonics;
    }

    public void setMnemonics(ArrayList<Mnemonic> mnemonics) {
        this.mnemonics = mnemonics;
    }

    /*public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<String> antonyms) {
        this.antonyms = antonyms;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }*/

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeString(transcription);
        dest.writeParcelable(category, flags);
        dest.writeLong(lessonId);
        dest.writeByte(difficult);
        dest.writeByte(masterLevel);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeTypedList(sentences);
        dest.writeTypedList(translations);
        dest.writeParcelable(definition, flags);
    }
}
