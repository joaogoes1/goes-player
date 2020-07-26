package com.example.goesplayer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class ArtistFragment extends Fragment {

    ArrayList<String> artistList = new ArrayList<>();

    public ArtistFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_list, container, false);
        TextView titulo = rootView.findViewById(R.id.titulo_text);
        titulo.setText("Artistas: ");

        criarLista();

        ListView listView = rootView.findViewById(R.id.music_list_view);
        StringAdapter adapter = new StringAdapter(getContext(), artistList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra("name", artistList.get(position));
                intent.putExtra("type", ResultActivity.ARTISTA);
                startActivity(intent);
            }
        });

        return rootView;
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

    private void criarLista() {
        ArrayList<String> listAux = new ArrayList<>();
        for (int i = 0; i < MainActivity.todasMusicas.size(); i++) listAux.add(MainActivity.todasMusicas.get(i).getArtist());
        artistList = new ArrayList<>(new HashSet<>(listAux));
        for (int i = 0; i < artistList.size(); i++) if (artistList.get(i).equals("<unknown>")) artistList.remove(i);
        Collections.sort(artistList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }
}