package com.bookworm.firebaseapp.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.adapters.MyListAdapter;
import com.bookworm.firebaseapp.data.FirebaseRepository;
import com.bookworm.firebaseapp.models.MyBookEntry;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {

    private FirebaseRepository repository;
    private MyListAdapter adapter;
    private final List<MyBookEntry> myBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        repository = new FirebaseRepository();
        ListView listView = findViewById(R.id.listMyBooks);

        adapter = new MyListAdapter(this, myBooks, this::updateReturnDate, this::removeBook);
        listView.setAdapter(adapter);

        if (!repository.isLoggedIn()) {
            Toast.makeText(this, "Login first to view your list", Toast.LENGTH_SHORT).show();
            return;
        }

        loadMyList();
    }

    private void loadMyList() {
        repository.fetchMyList(new FirebaseRepository.MyListCallback() {
            @Override
            public void onResult(List<MyBookEntry> entries) {
                myBooks.clear();
                myBooks.addAll(entries);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MyListActivity.this,
                        "Error loading list: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateReturnDate(MyBookEntry entry, long dateMillis) {
        repository.updateReturnDate(entry.getId(), dateMillis, new FirebaseRepository.CompletionCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(MyListActivity.this, "Return date updated", Toast.LENGTH_SHORT).show();
                loadMyList();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MyListActivity.this,
                        "Could not update date: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeBook(MyBookEntry entry) {
        repository.removeFromMyList(entry.getId(), new FirebaseRepository.CompletionCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(MyListActivity.this, "Book removed", Toast.LENGTH_SHORT).show();
                loadMyList();
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MyListActivity.this,
                        "Could not remove book: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

