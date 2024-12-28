package com.goesplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    public static final int MUSICA = 1;
    public static final int ARTISTA = 2;
    public static final int ALBUM = 3;
    public static final int GENDER = 4;
    public static final int PLAYSLIST = 5;
    public static final int FOLDER = 6;

    private String nome;
    private int type;
    private ArrayList<Music> musicList = new ArrayList<>();

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

        nome = getIntent().getStringExtra("name");
        type = getIntent().getIntExtra("type", 0);
        setMusicList(type, nome);

        TextView textView = findViewById(R.id.result_titulo);
        textView.setText(nome);

        MusicAdapter adapter = new MusicAdapter(this, musicList, MusicAdapter.COMPLETE);
        ListView listView = findViewById(R.id.result_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (musicList != null) {
                    musicSrv.PlayList(musicList);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMusicList(int type, String nome) {
        switch (type) {
            case ARTISTA:
                //@TODO: Arrumar o algoritmo para ele pesquisar a substring de cada artista
                for (int i = 0; i < MainActivity.todasMusicas.size(); i++)
                    if (MainActivity.todasMusicas.get(i).getArtist().equals(nome))
                        musicList.add(MainActivity.todasMusicas.get(i));
                break;
            case ALBUM:
                for (int i = 0; i < MainActivity.todasMusicas.size(); i++)
                    if (MainActivity.todasMusicas.get(i).getAlbum().equals(nome))
                        musicList.add(MainActivity.todasMusicas.get(i));
                break;
            case GENDER:
                for (int i = 0; i < MainActivity.todasMusicas.size(); i++)
                    if (MainActivity.todasMusicas.get(i).getGenre().equals(nome))
                        musicList.add(MainActivity.todasMusicas.get(i));
                break;
            case FOLDER:
                for (int i = 0; i < MainActivity.todasMusicas.size(); i++)
                    if (MainActivity.todasMusicas.get(i).getFolder().equals(nome))
                        musicList.add(MainActivity.todasMusicas.get(i));
                break;
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
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
