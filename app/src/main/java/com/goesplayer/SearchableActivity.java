package com.goesplayer;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchableActivity extends ListActivity{

    ArrayList<Music> musicas;
    private PlayerService musicSrv;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Buscar(query);
        }

        setListAdapter(new SearchAdapter(this, musicas));
    }

    private void Buscar(String query){
        musicas = new ArrayList<>();
        ArrayList<Music> artistas = new ArrayList<>();
        ArrayList<Music> generos = new ArrayList<>();
        ArrayList<Music> albuns = new ArrayList<>();
        ArrayList<Music> pastas = new ArrayList<>();
        ArrayList<Music> playlists = new ArrayList<>();

        for (int i = 0; i < MainActivity.todasMusicas.size(); i++){
            if(!MainActivity.todasMusicas.get(i).getTitle().equals("") && MainActivity.todasMusicas.get(i).getTitle().contains(query)) musicas.add(MainActivity.todasMusicas.get(i));
            if(!MainActivity.todasMusicas.get(i).getArtist().equals("") && MainActivity.todasMusicas.get(i).getArtist().contains(query)) artistas.add(MainActivity.todasMusicas.get(i));
            if(!MainActivity.todasMusicas.get(i).getGenre().equals("") && MainActivity.todasMusicas.get(i).getGenre().contains(query)) generos.add(MainActivity.todasMusicas.get(i));
            if(!MainActivity.todasMusicas.get(i).getAlbum().equals("") && MainActivity.todasMusicas.get(i).getAlbum().contains(query)) albuns.add(MainActivity.todasMusicas.get(i));
            if(!MainActivity.todasMusicas.get(i).getFolder().equals("") && MainActivity.todasMusicas.get(i).getFolder().contains(query)) pastas.add(MainActivity.todasMusicas.get(i));
        }
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        musicSrv.PlayList(musicas);
        musicSrv.setPosicao(position);
        musicSrv.reproduzir();
        musicSrv.abrirPlayerTela();
    }
}
