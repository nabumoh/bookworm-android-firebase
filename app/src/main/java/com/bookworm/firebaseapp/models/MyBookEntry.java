package com.bookworm.firebaseapp.models;

public class MyBookEntry {
    private String id;
    private String bookId;
    private String title;
    private String author;
    private String imageUrl;
    private Long returnDateMillis;

    public MyBookEntry() {
        // Required by Firestore deserialization.
    }

    public MyBookEntry(String id, String bookId, String title, String author, String imageUrl, Long returnDateMillis) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.returnDateMillis = returnDateMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getReturnDateMillis() {
        return returnDateMillis;
    }

    public void setReturnDateMillis(Long returnDateMillis) {
        this.returnDateMillis = returnDateMillis;
    }
}

