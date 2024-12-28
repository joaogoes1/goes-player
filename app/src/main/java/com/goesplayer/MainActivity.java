package com.goesplayer;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSIONS_CODE = 128;

    private BancoController crud;

    private MaterialDialog mMaterialDialog;
    public static View toolbar;
    public static TextView toolbarMusica;
    public static TextView toolbarArtista;
    public ImageButton toolbarPrevButton;
    public static ImageButton toolbarPlayButton;
    public ImageButton toolbatNextButton;
    public static ImageButton toolbarImageAlbum;

    public PlayerService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    public static ArrayList<Music> todasMusicas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        toolbar = findViewById(R.id.inc_toolbar_inferior);
        toolbar.setVisibility(View.INVISIBLE);
        setButtonsToolbar();
        setViewPager();
        toolbarMusica = findViewById(R.id.nome_musica_reproduzindo);
        toolbarArtista = findViewById(R.id.nome_artista_reproduzindo);
        toolbarImageAlbum = findViewById(R.id.toolbar_imagem);
        crud = new BancoController(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                callDialog("É preciso a permissão para encontrar as músicas em seus dispositivo.", new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);
            }
        } else {
            if (!PlayerService.mainCriada) {
                lerMusicas();
                ordenarMusicas();
                PlayerService.mainCriada = true;
            }
        }
        if (playIntent == null) {
            playIntent = new Intent(this, PlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
/*        if (PlayerService.tocando){
            toolbar.setVisibility(View.VISIBLE);
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(), musicSrv.musicaAtual.getUri());
                musicSrv.imgBytes = retriever.getEmbeddedPicture();
                Bitmap albumBitmap;
                if (musicSrv.imgBytes != null) {
                    albumBitmap = BitmapFactory.decodeByteArray(musicSrv.imgBytes, 0, musicSrv.imgBytes.length);
                    toolbarImageAlbum.setImageBitmap(albumBitmap);
                } else {
                    toolbarImageAlbum.setImageResource(R.mipmap.teste_album);
                }
                retriever.release();
            } catch (RuntimeException ex) {
                Log.i("Erro no RETRIEVER", "O erro foi no ATUALIZARTEXTOS do PLAYERSERVICE");
            }

            toolbarMusica.setText(musicSrv.musicaAtual.getTitle());
            toolbarArtista.setText(musicSrv.musicaAtual.getArtist());
        }
        else toolbar.setVisibility(View.INVISIBLE);*/
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = new SearchView(this);

        MenuItem itemPesquisa = menu.add(0, R.id.menu_search, 0, "Pesquisa");
        itemPesquisa.setIcon(R.drawable.ic_search);
        itemPesquisa.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        itemPesquisa.setActionView(searchView);

        MenuItem itemConfig = menu.add(0, R.id.configuracao, 1, "Configuração");
        itemConfig.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem itemInfos = menu.add(0, R.id.infos, 2, "Infos");
        itemConfig.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.MusicBinder binder = (PlayerService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.PlayList(todasMusicas);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //Método para criar o ViewPager
    private void setViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_playlist);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_musica);
            tabLayout.getTabAt(3).setIcon(R.drawable.ic_cantor);
            tabLayout.getTabAt(4).setIcon(R.drawable.ic_album);
            tabLayout.getTabAt(5).setIcon(R.drawable.ic_genero);
            tabLayout.getTabAt(6).setIcon(R.drawable.ic_pasta);
        }
    }

    private void setButtonsToolbar() {
        toolbarPrevButton = findViewById(R.id.anterior_toolbar);
        toolbarPlayButton = findViewById(R.id.play_toolbar);
        toolbatNextButton = findViewById(R.id.proximo_toolbar);
        ImageButton toolbarImageAlbum = findViewById(R.id.toolbar_imagem);

        toolbarPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.reproduzirAnterior();
            }
        });

        toolbarPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!musicSrv.mediaPlayer.isPlaying()) {
                    musicSrv.voltarAReproduzir();
                    toolbarPlayButton.setImageResource(R.drawable.ic_pause_small);
                } else {
                    musicSrv.pausar();
                    toolbarPlayButton.setImageResource(R.drawable.ic_play_toolbar);
                }
            }
        });

        toolbatNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.reproduzirProxima();
            }
        });

        toolbarImageAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.abrirPlayerTela();
            }
        });
    }

    private void callDialog(String message, final String[] permissions) {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage(message)
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        lerMusicas();
                        ordenarMusicas();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void lerMusicas() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {

            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nameColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int folderColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = "<unknown>";
                String thisName = musicCursor.getString(nameColumn);
                Uri thisUri = ContentUris.withAppendedId(musicUri, thisId);
                String thisGender = "<unknown>";
                String folder = musicCursor.getString(folderColumn);
                String abc = folder.substring(Environment.getExternalStorageDirectory().getPath().length());
                String def = abc.substring(0, (abc.indexOf(thisName) - 1));
                String thisFolder = def.substring((def.lastIndexOf("/") + 1));

                Uri genderUri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) thisId);
                Cursor genderCursor = getContentResolver().query(genderUri, new String[]{MediaStore.Audio.Genres.NAME}, null, null, null);
                if (genderCursor.moveToFirst()) {
                    thisGender = genderCursor.getString(genderCursor.getColumnIndex(MediaStore.Audio.Genres.NAME));
                }
                genderCursor.close();

                MediaMetadataRetriever media = new MediaMetadataRetriever();
                try {
                    media.setDataSource(this, thisUri);
                    thisAlbum = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    media.release();
                } catch (Exception e) {
                    //Ignore it
                }

                if (thisAlbum == null) thisAlbum = "<unknown>";

                todasMusicas.add(new Music(thisId, thisName, thisTitle, thisArtist, thisAlbum, thisGender, thisFolder, thisUri));
            } while (musicCursor.moveToNext());
        }
    }

    public void ordenarMusicas() {
        Collections.sort(todasMusicas, new Comparator<Music>() {
            public int compare(Music a, Music b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    public void abrirPlayer(View view) {
        musicSrv.abrirPlayerTela();
    }
}