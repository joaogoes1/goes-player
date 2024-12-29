package com.goesplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends ArrayAdapter<Music> {

    public static final int ONLY_MUSIC = 1;
    public static final int COMPLETE = 0;
    int type;
    private ArrayList<String> listaGenre = new ArrayList<>();

    public MusicAdapter(Activity context, ArrayList<Music> music, int type) {
        super(context, 0, music);

        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            if (type == MusicAdapter.COMPLETE) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
            } else {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_single, parent, false);
            }
        }

        final Music currentMusic = getItem(position);

        if (type == MusicAdapter.COMPLETE){
            TextView musicTextView = listItemView.findViewById(R.id.nome_musica);
            musicTextView.setText(currentMusic.getTitle());
            TextView artistaTextView = listItemView.findViewById(R.id.nome_artista);
            artistaTextView.setText(currentMusic.getArtist());
            ImageView imageView = listItemView.findViewById(R.id.image_item_list);
            atualizarImagem(currentMusic, imageView);
        } else {
            TextView musicTextView = listItemView.findViewById(R.id.nome_single);
            musicTextView.setText(currentMusic.getTitle());
        }

        return listItemView;
    }

    private void atualizarImagem(Music music, ImageView imageView) {

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getContext(), music.getUri());
            byte[] imgBytes = retriever.getEmbeddedPicture();
            Bitmap albumBitmap;
            if (imgBytes != null) {
                albumBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                imageView.setImageBitmap(albumBitmap);
            } else {
                imageView.setImageResource(R.mipmap.teste_album);
            }
            retriever.release();
        } catch (Exception ex) {
        }
    }
}
