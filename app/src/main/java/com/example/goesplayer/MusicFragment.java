package com.example.goesplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MusicFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private BancoController crud;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.music_list, container, false);
        TextView titulo = rootView.findViewById(R.id.titulo_text);
        titulo.setText("Musicas: ");
        crud = new BancoController(getContext());

        MusicAdapter adapter = new MusicAdapter(getActivity(), MainActivity.todasMusicas, MusicAdapter.COMPLETE);
        ListView listView = rootView.findViewById(R.id.music_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).musicSrv.PlayList(MainActivity.todasMusicas);
                ((MainActivity) getActivity()).musicSrv.setPosicao(position);
                ((MainActivity) getActivity()).musicSrv.reproduzir();
                ((MainActivity) getActivity()).musicSrv.abrirPlayerTela();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{"Adicionar à playlist"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Escolher uma playlist:");
                Cursor cursor = crud.carregaPlaylist();
                final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.item_view_single, cursor, new String[]{DataBaseOpenHelper.DB_NOME}, new int[]{R.id.nome_single}, 0);
                builder1.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Music musica = MainActivity.todasMusicas.get(position);
                        Cursor cursor1 = (Cursor) adapter.getItem(which);
                        String string = cursor1.getString(1);
                        int valor = crud.acharPlaylist(string);
                        crud.adicionarAPlaylist(valor, (int) musica.getIdNumber(), getContext());
                        dialog.dismiss();
                    }
                });
                builder1.create();
                builder1.show();
            }
        });
        builder.create();
        builder.show();
        return (true);
    }
}