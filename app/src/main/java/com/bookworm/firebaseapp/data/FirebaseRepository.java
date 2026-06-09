package com.bookworm.firebaseapp.data;

import com.bookworm.firebaseapp.models.Book;
import com.bookworm.firebaseapp.models.MyBookEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository {

    public interface BooksCallback {
        void onResult(List<Book> books);
        void onError(Exception exception);
    }

    public interface MyListCallback {
        void onResult(List<MyBookEntry> entries);
        void onError(Exception exception);
    }

    public interface CompletionCallback {
        void onComplete();
        void onError(Exception exception);
    }

    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    public FirebaseRepository() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void fetchBooks(final BooksCallback callback) {
        firestore.collection("books").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Book> books = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        book.setId(document.getId());
                        books.add(book);
                    }
                    callback.onResult(books);
                })
                .addOnFailureListener(callback::onError);
    }

    public void seedBooksIfEmpty(final CompletionCallback callback) {
        firestore.collection("books").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        callback.onComplete();
                        return;
                    }

                    List<Book> sample = new ArrayList<>();
                    sample.add(new Book("", "1984", "George Orwell", ""));
                    sample.add(new Book("", "To Kill a Mockingbird", "Harper Lee", ""));
                    sample.add(new Book("", "The Hobbit", "J. R. R. Tolkien", ""));
                    sample.add(new Book("", "Pride and Prejudice", "Jane Austen", ""));

                    final int[] remaining = {sample.size()};
                    for (Book book : sample) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("title", book.getTitle());
                        data.put("author", book.getAuthor());
                        data.put("imageUrl", book.getImageUrl());

                        firestore.collection("books")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {
                                    remaining[0]--;
                                    if (remaining[0] == 0) {
                                        callback.onComplete();
                                    }
                                })
                                .addOnFailureListener(callback::onError);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    public void addToMyList(Book book, final CompletionCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError(new IllegalStateException("User not logged in"));
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("bookId", book.getId());
        data.put("title", book.getTitle());
        data.put("author", book.getAuthor());
        data.put("imageUrl", book.getImageUrl());
        data.put("returnDateMillis", null);
        data.put("addedAt", System.currentTimeMillis());

        firestore.collection("users")
                .document(user.getUid())
                .collection("myList")
                .add(data)
                .addOnSuccessListener(documentReference -> callback.onComplete())
                .addOnFailureListener(callback::onError);
    }

    public void fetchMyList(final MyListCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError(new IllegalStateException("User not logged in"));
            return;
        }

        firestore.collection("users")
                .document(user.getUid())
                .collection("myList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MyBookEntry> entries = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MyBookEntry entry = document.toObject(MyBookEntry.class);
                        entry.setId(document.getId());
                        entries.add(entry);
                    }
                    callback.onResult(entries);
                })
                .addOnFailureListener(callback::onError);
    }

    public void updateReturnDate(String entryId, long returnDateMillis, final CompletionCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError(new IllegalStateException("User not logged in"));
            return;
        }

        DocumentReference ref = firestore.collection("users")
                .document(user.getUid())
                .collection("myList")
                .document(entryId);

        ref.update("returnDateMillis", returnDateMillis)
                .addOnSuccessListener(unused -> callback.onComplete())
                .addOnFailureListener(callback::onError);
    }

    public void removeFromMyList(String entryId, final CompletionCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError(new IllegalStateException("User not logged in"));
            return;
        }

        firestore.collection("users")
                .document(user.getUid())
                .collection("myList")
                .document(entryId)
                .delete()
                .addOnSuccessListener(unused -> callback.onComplete())
                .addOnFailureListener(callback::onError);
    }

    public void login(String email, String password, CompletionCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.onComplete())
                .addOnFailureListener(callback::onError);
    }

    public void register(String email, String password, CompletionCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.onComplete())
                .addOnFailureListener(callback::onError);
    }

    public void logout() {
        auth.signOut();
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public String getCurrentUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return user == null ? null : user.getEmail();
    }
}

