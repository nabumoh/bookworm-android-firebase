package com.bookworm.firebaseapp.models;

import org.junit.Assert;
import org.junit.Test;

public class BookTest {

    @Test
    public void constructor_setsFields() {
        Book book = new Book("id-1", "Clean Code", "Robert C. Martin", "");

        Assert.assertEquals("id-1", book.getId());
        Assert.assertEquals("Clean Code", book.getTitle());
        Assert.assertEquals("Robert C. Martin", book.getAuthor());
    }
}

