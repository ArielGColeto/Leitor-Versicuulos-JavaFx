package com.igreja.app.model;

public class Verse {

    private String book;
    private int chapter;
    private int verse;
    private String text;

    public Verse(String book, int chapter, int verse, String text) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.text = text;
    }

    public String getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerse() {
        return verse;
    }

    public String getText() {
        return text;
    }

    public String getReference() {
        return book + " " + chapter + ":" + verse;
    }

    @Override
    public String toString() {
        return getReference() + " - " + text;
    }
}