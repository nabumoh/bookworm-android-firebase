package com.bookworm.firebaseapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.data.FirebaseRepository;

public class AuthActivity extends AppCompatActivity {

    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        repository = new FirebaseRepository();

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!isInputValid(email, password)) {
                return;
            }

            repository.login(email, password, new FirebaseRepository.CompletionCallback() {
                @Override
                public void onComplete() {
                    Toast.makeText(AuthActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(AuthActivity.this,
                            "Login failed: " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!isInputValid(email, password)) {
                return;
            }

            repository.register(email, password, new FirebaseRepository.CompletionCallback() {
                @Override
                public void onComplete() {
                    Toast.makeText(AuthActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(AuthActivity.this,
                            "Register failed: " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogout.setOnClickListener(v -> {
            repository.logout();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isInputValid(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 chars", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

