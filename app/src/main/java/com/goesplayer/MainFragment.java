package com.goesplayer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton aleatorioButton = view.findViewById(R.id.aleatorio);
        aleatorioButton.setImageResource(R.drawable.ic_aleatorio);
        aleatorioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).musicSrv.aleatorio = false;
                ((MainActivity) getActivity()).musicSrv.ordenarPlayList();
                ((MainActivity) getActivity()).musicSrv.reproduzir();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView musicaView = getView().findViewById(R.id.primeiro_musica_text);
        TextView artistaView = getView().findViewById(R.id.artist_text);

        BancoController db = new BancoController(getContext());
        musicaView.setText(db.carregarUltimaMusica()[0]);
        artistaView.setText(db.carregarUltimaMusica()[1]);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (PlayerService.tocando) MainActivity.toolbar.setVisibility(View.VISIBLE);
        else MainActivity.toolbar.setVisibility(View.INVISIBLE);
        if(((MainActivity) getActivity()).musicSrv != null && ((MainActivity) getActivity()).musicSrv.musicaAtual != null) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getContext(), ((MainActivity) getActivity()).musicSrv.musicaAtual.getUri());
                ((MainActivity) getActivity()).musicSrv.imgBytes = retriever.getEmbeddedPicture();
                Bitmap albumBitmap;
                if (((MainActivity) getActivity()).musicSrv.imgBytes != null) {
                    albumBitmap = BitmapFactory.decodeByteArray(((MainActivity) getActivity()).musicSrv.imgBytes, 0, ((MainActivity) getActivity()).musicSrv.imgBytes.length);
                    MainActivity.toolbarImageAlbum.setImageBitmap(albumBitmap);
                } else {
                    MainActivity.toolbarImageAlbum.setImageResource(R.mipmap.teste_album);
                }
                retriever.release();
            } catch (Exception ex) {
                Log.i("Erro no RETRIEVER", "O erro foi no ATUALIZARTEXTOS do PLAYERSERVICE");
            }

            MainActivity.toolbarMusica.setText(((MainActivity) getActivity()).musicSrv.musicaAtual.getTitle());
            MainActivity.toolbarArtista.setText(((MainActivity) getActivity()).musicSrv.musicaAtual.getArtist());
        }
    }
}
