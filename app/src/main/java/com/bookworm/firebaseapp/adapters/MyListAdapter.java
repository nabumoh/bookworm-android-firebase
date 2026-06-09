package com.bookworm.firebaseapp.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bookworm.firebaseapp.R;
import com.bookworm.firebaseapp.models.MyBookEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyListAdapter extends BaseAdapter {

    public interface OnSetDateListener {
        void onDateSelected(MyBookEntry entry, long dateMillis);
    }

    public interface OnRemoveListener {
        void onRemove(MyBookEntry entry);
    }

    private final Context context;
    private final List<MyBookEntry> entries;
    private final OnSetDateListener onSetDateListener;
    private final OnRemoveListener onRemoveListener;

    public MyListAdapter(Context context,
                         List<MyBookEntry> entries,
                         OnSetDateListener onSetDateListener,
                         OnRemoveListener onRemoveListener) {
        this.context = context;
        this.entries = entries;
        this.onSetDateListener = onSetDateListener;
        this.onRemoveListener = onRemoveListener;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_book, parent, false);
        }

        MyBookEntry entry = entries.get(position);

        TextView tvTitle = view.findViewById(R.id.tvMyTitle);
        TextView tvAuthor = view.findViewById(R.id.tvMyAuthor);
        TextView tvReturnDate = view.findViewById(R.id.tvReturnDate);
        Button btnSetDate = view.findViewById(R.id.btnSetReturnDate);
        Button btnRemove = view.findViewById(R.id.btnRemove);

        tvTitle.setText(entry.getTitle());
        tvAuthor.setText(entry.getAuthor());

        Long returnDateMillis = entry.getReturnDateMillis();
        if (returnDateMillis == null) {
            tvReturnDate.setText("Return date: Not set");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            tvReturnDate.setText("Return date: " + format.format(returnDateMillis));
        }

        btnSetDate.setOnClickListener(v -> showDatePicker(entry));
        btnRemove.setOnClickListener(v -> onRemoveListener.onRemove(entry));

        return view;
    }

    private void showDatePicker(MyBookEntry entry) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth, 0, 0, 0);
                    selected.set(Calendar.MILLISECOND, 0);
                    onSetDateListener.onDateSelected(entry, selected.getTimeInMillis());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }
}

