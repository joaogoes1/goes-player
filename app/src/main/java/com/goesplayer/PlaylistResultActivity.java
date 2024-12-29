package com.goesplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class PlaylistResultActivity extends AppCompatActivity {

    private BancoController crud;
    private PlayerService musicSrv;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar mainToolbar = findViewById(R.id.mainToolbarResult);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        crud = new BancoController(this);
        String nome = getIntent().getStringExtra("nome");

        TextView textView = findViewById(R.id.result_titulo);
        textView.setText(nome);

        final int valor = crud.acharPlaylist(nome);

        ArrayList<String> nomeDasMusicas = new ArrayList<>();
        final ArrayList<Music> arrayList;
        if (crud.countNumOfMusics(valor) != 0) {
        Cursor cursor = crud.consultaPlayMus(valor);

        if (cursor != null)
            arrayList = crud.musicsOfPlaylist(cursor);
        else
            arrayList = new ArrayList<>();

        for (Music i : arrayList) {
                nomeDasMusicas.add(i.getTitle());
            }
        }
        else {
            nomeDasMusicas.clear();
            nomeDasMusicas.add("Lista Vazia!");
            arrayList = null;
        }

        StringAdapter adapter = new StringAdapter(this, nomeDasMusicas);
        ListView listView = findViewById(R.id.result_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayList != null){
                    musicSrv.PlayList(arrayList);
                    musicSrv.setPosicao(position);
                    musicSrv.aleatorio = false;
                    musicSrv.ordenarPlayList();
                    musicSrv.reproduzir();
                    musicSrv.abrirPlayerTela();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, PlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.MusicBinder binder = (PlayerService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {        }
    };
}
