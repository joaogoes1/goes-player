package com.example.goesplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerActivity extends AppCompatActivity {


    public static ImageButton playerPlayButton;
    public static ImageButton playerAnteriorButton;
    public static ImageButton playerProximaButton;
    public static ImageButton playerAleatorioButton;
    public static ImageButton playerRepetirButton;
    public static ImageView playerImageAlbum;
    public static TextView playerMusica;
    public static TextView playerArtista;
    public static TextView playerDuration;
    public static TextView currentTimeText;
    public static SeekBar seekBar;

    private ScrollView letraScrollView;
    private TextView letraTextView;
    public static boolean exibindoLetra = false;

    public PlayerService musicSrv;
    private Intent playIntent;
    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar playerActionBar = findViewById(R.id.player_actionbar);
        setSupportActionBar(playerActionBar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        inicializarViews();
        setBotoes();
        inicializarActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem itemLetras = menu.add(0, R.id.menu_letra, 0, "@string/menu_exibir_letras");
        itemLetras.setIcon(R.drawable.ic_letra_da_musica);
        itemLetras.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.menu_letra:
                exibirLetras();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        musicSrv = new PlayerService();
        if (playIntent == null) {
            playIntent = new Intent(this, PlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onPause() {
        status = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
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

    private void inicializarActivity() {
        status = true;

        playerMusica.setText(getIntent().getStringExtra("musica"));
        playerArtista.setText(getIntent().getStringExtra("artista"));

        if (PlayerService.imageAlbum) {
            Bitmap albumBitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("imagemBitmap"), 0, getIntent().getByteArrayExtra("imagemBitmap").length);
            playerImageAlbum.setImageBitmap(albumBitmap);
        } else {
            playerImageAlbum.setImageResource(R.mipmap.teste_album);
        }

        if (!getIntent().getBooleanExtra("aleatorio", false)) {
            playerAleatorioButton.setImageResource(R.drawable.ic_aleatorio);
        } else {
            playerAleatorioButton.setImageResource(R.drawable.ic_aleatorio_amarelo);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentTimeText.setText(gerarTempo(progress * 1000));
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    Date date = new Date(progress * 1000);
                    currentTimeText.setText(sdf.format(date));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pos = seekBar.getProgress() * 1000;
                musicSrv.seekTo(pos);
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                Date date = new Date(musicSrv.mediaPlayer.getCurrentPosition());
                currentTimeText.setText(sdf.format(date));
            }
        });
    }

    public void inicializarViews() {
        playerPlayButton = findViewById(R.id.reproduction_play);
        playerAnteriorButton = findViewById(R.id.reproduction_anterior);
        playerProximaButton = findViewById(R.id.reproduction_proximo);
        playerAleatorioButton = findViewById(R.id.reproduction_aleatorio);
        playerRepetirButton = findViewById(R.id.reproduction_repetir);
        playerMusica = findViewById(R.id.reproduction_musica);
        playerArtista = findViewById(R.id.reproduction_artista);
        playerImageAlbum = findViewById(R.id.reproduction_imagem);
        playerDuration = findViewById(R.id.reproduction_tempo_total);
        currentTimeText = findViewById(R.id.reproduction_tempo_de_execucao);
        playerMusica.setText("");
        playerArtista.setText("");
        seekBar = findViewById(R.id.reproduction_seekbar);
        letraScrollView = findViewById(R.id.reproduction_letra_listview);
        letraScrollView.setVisibility(View.INVISIBLE);
        letraTextView = findViewById(R.id.reproduction_letra_textview);
    }

    public void setBotoes() {
        playerPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSrv.mediaPlayer.isPlaying()) {
                    musicSrv.pausar();
                    playerPlayButton.setImageResource(R.drawable.ic_play_player_branco);
                } else {
                    musicSrv.voltarAReproduzir();
                    playerPlayButton.setImageResource(R.drawable.ic_pause_toolbar);
                }
            }
        });

        playerAnteriorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exibindoLetra) exibirLetras();
                musicSrv.reproduzirAnterior();
            }
        });

        playerProximaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exibindoLetra) exibirLetras();
                musicSrv.reproduzirProxima();
            }
        });

        playerAleatorioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.ordenarPlayList();

                if (musicSrv.aleatorio) {
                    playerAleatorioButton.setImageResource(R.drawable.ic_aleatorio_amarelo);
                } else {
                    playerAleatorioButton.setImageResource(R.drawable.ic_aleatorio);
                }
            }
        });

        playerRepetirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayerActivity.this, "Botão de repetir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String gerarTempo(long time) {
        long aux = time / 1000;
        int minute = (int) (aux / 60);
        int second = (int) (aux % 60);
        String sCurrentTime = minute < 10 ? "0" + minute : minute + "";
        sCurrentTime += ":" + (second < 10 ? "0" + second : second);
        return sCurrentTime;
    }

    public void exibirLetras() {
        if (!exibindoLetra) {
            if (musicSrv != null && musicSrv.musicaAtual != null) {
                String uri = Uri.parse("http://api.vagalume.com.br/search.php")
                        .buildUpon()
                        .appendQueryParameter("mus", playerMusica.getText().toString())
                        .appendQueryParameter("art", gerarArtist(playerArtista.getText().toString()))
                        .build().toString();

                if (uri != null)
                    new VagalumeAsyncTask().execute(uri);
            }
        } else {
            letraScrollView.setVisibility(View.INVISIBLE);
            playerImageAlbum.setVisibility(View.VISIBLE);
            playerMusica.setVisibility(View.VISIBLE);
            playerArtista.setVisibility(View.VISIBLE);
            exibindoLetra = false;
        }
    }

    private String gerarArtist(String artista) {
        if (artista.contains(" feat. "))
            return artista.substring(0, artista.indexOf(" feat. "));
        else if (artista.contains(" ft. "))
            return artista.substring(0, artista.indexOf(" ft. "));
        else if (artista.contains(" featuring "))
            return artista.substring(0, artista.indexOf(" featuring "));
        else if (artista.contains(","))
            return artista.substring(0, artista.indexOf(","));
        else if (artista.contains("/"))
            return artista.substring(0, artista.indexOf("/"));
        else if (artista.contains(";"))
            return artista.substring(0, artista.indexOf(";"));
        else return artista;
    }

    private class VagalumeAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            return jParser.getJSONFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            try {
                JSONObject mus = (JSONObject) json.getJSONArray("mus").get(0);
                letraTextView.clearComposingText();
                letraTextView.setText(mus.getString("text"));

                letraScrollView.setVisibility(View.VISIBLE);
                playerImageAlbum.setVisibility(View.INVISIBLE);
                playerMusica.setVisibility(View.INVISIBLE);
                playerArtista.setVisibility(View.INVISIBLE);
                exibindoLetra = true;
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Não foi possivel carregar a letra dessa música", Toast.LENGTH_LONG).show();
            }
        }
    }
}