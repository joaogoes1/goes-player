package com.goesplayer;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FolderFragment extends Fragment {

    ArrayList<String> folderList = new ArrayList<>();

    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_list, container, false);
        TextView titulo = rootView.findViewById(R.id.titulo_text);
        titulo.setText("Pastas: ");

        criarLista();

        ListView listView = rootView.findViewById(R.id.music_list_view);
        StringAdapter adapter = new StringAdapter(getActivity(), folderList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra("name", folderList.get(position));
                intent.putExtra("type", ResultActivity.FOLDER);
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
        for (int i = 0; i < MainActivity.todasMusicas.size(); i++)
            listAux.add(MainActivity.todasMusicas.get(i).getFolder());
        folderList = new ArrayList<>(new HashSet<>(listAux));
        for (int i = 0; i < folderList.size(); i++)
            if (folderList.get(i).equals("<unknown>")) folderList.remove(i);
        Collections.sort(folderList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
