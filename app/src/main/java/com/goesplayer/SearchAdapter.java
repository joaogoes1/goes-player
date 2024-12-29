package com.goesplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<Music> {

    boolean vazio = false;

    public SearchAdapter(@NonNull Context context, @NonNull ArrayList<Music> objects) {
        super(context, 0, objects);

        if (objects.isEmpty()) vazio = true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View searchListView = convertView;
        if (searchListView == null)
            searchListView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);

        final Music currentMusic = getItem(position);

        TextView musicTextView = searchListView.findViewById(R.id.nome_musica);
        TextView artistaTextView = searchListView.findViewById(R.id.nome_artista);
        if (vazio) musicTextView.setText(R.string.nenhum_resultado_encontrado);
        else{
            musicTextView.setText(currentMusic.getTitle());
            artistaTextView.setText(currentMusic.getArtist());
        }

        return searchListView;
    }
}
