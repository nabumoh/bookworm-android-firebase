package com.bookworm.firebaseapp.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.adapters.BooksAdapter;
import com.bookworm.firebaseapp.data.FirebaseRepository;
import com.bookworm.firebaseapp.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BrowseBooksActivity extends AppCompatActivity {

    private FirebaseRepository repository;
    private BooksAdapter adapter;
    private final List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        repository = new FirebaseRepository();
        ListView listView = findViewById(R.id.listBrowseBooks);

        adapter = new BooksAdapter(this, books, this::handleAddToList);
        listView.setAdapter(adapter);

        loadBooks();
    }

    private void loadBooks() {
        repository.seedBooksIfEmpty(new FirebaseRepository.CompletionCallback() {
            @Override
            public void onComplete() {
                repository.fetchBooks(new FirebaseRepository.BooksCallback() {
                    @Override
                    public void onResult(List<Book> result) {
                        books.clear();
                        books.addAll(result);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception exception) {
                        Toast.makeText(BrowseBooksActivity.this,
                                "Error loading books: " + exception.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(BrowseBooksActivity.this,
                        "Error initializing books: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAddToList(Book book) {
        if (!repository.isLoggedIn()) {
            Toast.makeText(this, "Login first to add books", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.addToMyList(book, new FirebaseRepository.CompletionCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(BrowseBooksActivity.this,
                        "Book added to your list",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(BrowseBooksActivity.this,
                        "Could not add book: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

