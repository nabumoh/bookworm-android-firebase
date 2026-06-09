package com.bookworm.firebaseapp.models;

public class Book {
    private String id;
    private String title;
    private String author;
    private String imageUrl;

    public Book() {
        // Required by Firestore deserialization.
    }

    public Book(String id, String title, String author, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

