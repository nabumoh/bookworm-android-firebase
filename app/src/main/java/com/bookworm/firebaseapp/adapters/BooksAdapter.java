package com.bookworm.firebaseapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.models.Book;
import com.bumptech.glide.Glide;

import java.util.List;

public class BooksAdapter extends BaseAdapter {

    public interface OnAddClickListener {
        void onAddClick(Book book);
    }

    private final Context context;
    private final List<Book> books;
    private final OnAddClickListener onAddClickListener;

    public BooksAdapter(Context context, List<Book> books, OnAddClickListener onAddClickListener) {
        this.context = context;
        this.books = books;
        this.onAddClickListener = onAddClickListener;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        Book book = books.get(position);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvAuthor = view.findViewById(R.id.tvAuthor);
        ImageView imageCover = view.findViewById(R.id.imageCover);
        Button btnAdd = view.findViewById(R.id.btnAddToList);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.book_placeholder)
                    .into(imageCover);
        } else {
            imageCover.setImageResource(R.drawable.book_placeholder);
        }

        btnAdd.setOnClickListener(v -> onAddClickListener.onAddClick(book));

        return view;
    }
}

