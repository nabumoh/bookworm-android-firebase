package com.bookworm.firebaseapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.data.FirebaseRepository;

public class MainActivity extends AppCompatActivity {

    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new FirebaseRepository();

        Button btnBrowseBooks = findViewById(R.id.btnBrowseBooks);
        Button btnMyList = findViewById(R.id.btnMyList);
        Button btnAuth = findViewById(R.id.btnAuth);

        btnBrowseBooks.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BrowseBooksActivity.class)));

        btnMyList.setOnClickListener(v ->
        {
            if (!repository.isLoggedIn()) {
                Toast.makeText(MainActivity.this,
                        "Please login first",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                return;
            }
            startActivity(new Intent(MainActivity.this, MyListActivity.class));
        });

        btnAuth.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AuthActivity.class)));
    }
}

