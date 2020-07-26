package com.example.goesplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StringAdapter extends ArrayAdapter<String> {


    public StringAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_single, parent, false);

        TextView musicTextView = listItemView.findViewById(R.id.nome_single);
        musicTextView.setText(getItem(position));

        return listItemView;
    }
}
