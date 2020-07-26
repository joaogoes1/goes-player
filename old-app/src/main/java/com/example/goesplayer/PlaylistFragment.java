package com.example.goesplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class PlaylistFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private BancoController crud;
    ListView listView;
    View rootView;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.music_list, container, false);

        crud = new BancoController(getContext());
        atulizarView();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = getView().findViewById(R.id.titulo_text);
        textView.setText("Playlist");

        FloatingActionButton addPlaylistButton = view.findViewById(R.id.aleatorio);
        addPlaylistButton.setImageResource(R.drawable.ic_adicionar);
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Digite o nome da Playlist: ");
                final EditText editText = new EditText(getContext());
                builder.setView(editText);
                builder.setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        crud.criarPlaylist(getContext(), editText.getText().toString());
                        dialog.dismiss();
                        atulizarView();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = view.findViewById(R.id.nome_single);
        String nome = textView.getText().toString();


        Intent intent = new Intent(getContext(), PlaylistResultActivity.class);
        intent.putExtra("nome", nome);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new String[] {"Excluir"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Excluir playlist?");
                builder1.setMessage("Tem certeza desta ação?");
                builder1.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = crud.acharPlaylist(cursor.getString(1));
                        crud.deletarPlaylist(getContext(), id);
                        dialog.dismiss();
                        atulizarView();
                    }
                });
                builder1.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

    public void atulizarView() {
        Cursor cursor = crud.carregaPlaylist();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.item_view_single, cursor, new String[]{DataBaseOpenHelper.DB_NOME}, new int[]{R.id.nome_single}, 0);
        listView = rootView.findViewById(R.id.music_list_view);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setAdapter(adapter);
    }
}
