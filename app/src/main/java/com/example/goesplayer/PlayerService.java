package com.example.goesplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PlayerService extends Service {


    private final IBinder musicBind = new MusicBinder();
    private NotificationManager notificationManager;
    private BancoController bancoController;

    //Todas as musicas
    public ArrayList<Music> todasAsMusicas = new ArrayList<>(MainActivity.todasMusicas);

    //Playlist em execução
    private ArrayList<Music> playlist;
    private Random random = new Random();

    public static boolean imageAlbum = false;
    public static boolean mainCriada = false;
    public static boolean tocando = false;
    private static final int NOTIFICACAO_ID = 1;

    //Variáveis do MediaPlayer
    public MediaPlayer mediaPlayer;
    private int posicao = 0;
    public boolean aleatorio = false;
    public Music musicaAtual = null;
    byte[] imgBytes = null;
    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            tocando = true;
            imgBytes = null;
            MainActivity.toolbar.setVisibility(View.VISIBLE);
            atualizarTextos();
            mediaPlayer.start();

            updateTimeMusicThread(mp);
            criarNotificacao();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            reproduzirProxima();
        }
    };

    //Váriaveis do AudioManager
    private AudioManager audioManager;
    int focusResult;
    private AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mediaPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (tocando) mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mediaPlayer.pause();
                    break;
            }
        }
    };

    public void PlayList(ArrayList<Music> songList) {
        if (playlist != null) {
            this.playlist.clear();
            this.playlist = null;
        }
        this.playlist = new ArrayList<>(songList);
    }

    public ArrayList<Music> getPlaylist() {
        return playlist;
    }

    public int getPosicao() {
        return this.posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public PlayerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        focusResult = audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        bancoController = new BancoController(getBaseContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        audioManager.abandonAudioFocus(focusChangeListener);
        notificationManager.cancelAll();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public class MusicBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        releaseMediaPlayer();
        return super.onUnbind(intent);
    }

    public void ordenarPlayList() {
        if (!aleatorio) {
            Collections.shuffle(playlist);
            aleatorio = true;
        } else {
            Collections.sort(playlist, new Comparator<Music>() {
                public int compare(Music a, Music b) {
                    return a.getTitle().compareTo(b.getTitle());
                }
            });
            aleatorio = false;
        }
    }

    public void reproduzir() {
        mediaPlayer.reset();
        musicaAtual = playlist.get(posicao);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), musicaAtual.getUri());
            new BancoController(getBaseContext()).salvarUltimaMusica(musicaAtual.getTitle(), musicaAtual.getArtist());
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            //
        }
    }

    public void voltarAReproduzir() {
        mediaPlayer.start();
        criarNotificacao();
        tocando = true;
    }

    public void pausar() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            tocando = false;
            criarNotificacao();
        }
    }

    public void reproduzirAnterior() {
        posicao--;
        if (posicao < 0) posicao = playlist.size() - 1;
        reproduzir();
    }

    public void reproduzirProxima() {
        try {
            if (PlayerActivity.exibindoLetra) {
                PlayerActivity.exibindoLetra = false;
                ((PlayerActivity) getApplicationContext()).exibirLetras();
            }
        } catch (Exception e) {
            //
        }
        posicao++;
        if (posicao >= playlist.size()) posicao = 0;
        reproduzir();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mediaPlayer.start();
                if (PlayerActivity.playerPlayButton.getDrawable().equals(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_player_branco)))
                    PlayerActivity.playerPlayButton.setImageResource(R.drawable.ic_pause_toolbar);
                tocando = true;
            }
        });
    }

    public void updateTimeMusicThread(final MediaPlayer mediaPlayer) {
        new Thread() {
            public void run() {
                boolean conseguiu = false;
                while (!conseguiu) {
                    if (PlayerActivity.seekBar != null && PlayerActivity.playerDuration != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.US);
                        Date date = new Date(mediaPlayer.getDuration());
                        PlayerActivity.playerDuration.setText(sdf.format(date));
                        conseguiu = true;
                    }
                }
                for ( ; ; ) {
                    if (PlayerActivity.seekBar != null) {
                        PlayerActivity.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        try {
                            if (tocando) PlayerActivity.seekBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);
                            Thread.sleep(1000);
                        } catch (IllegalStateException e) {
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }.start();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            audioManager.abandonAudioFocus(focusChangeListener);
        }
    }

    public void atualizarTextos() {
        //Main Activity
        if(MainActivity.toolbarImageAlbum != null) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(), musicaAtual.getUri());
                imgBytes = retriever.getEmbeddedPicture();
                Bitmap albumBitmap;
                if (imgBytes != null) {
                    albumBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                    MainActivity.toolbarImageAlbum.setImageBitmap(albumBitmap);
                } else {
                    MainActivity.toolbarImageAlbum.setImageResource(R.mipmap.teste_album);
                }
                retriever.release();
            } catch (RuntimeException ex) {
                Log.i("Erro no RETRIEVER", "O erro foi no ATUALIZARTEXTOS do PLAYERSERVICE");
            }
        }

        if (MainActivity.toolbarMusica != null) MainActivity.toolbarMusica.setText(musicaAtual.getTitle());
        if (MainActivity.toolbarArtista != null) MainActivity.toolbarArtista.setText(musicaAtual.getArtist());

        //Player Activity
        if (PlayerActivity.playerMusica != null && PlayerActivity.playerArtista != null) {
            PlayerActivity.playerMusica.setText(musicaAtual.getTitle());
            PlayerActivity.playerArtista.setText(musicaAtual.getArtist());
        }


        if (PlayerActivity.playerImageAlbum != null) {
            if (imgBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                PlayerActivity.playerImageAlbum.setImageBitmap(bitmap);
            } else {
                PlayerActivity.playerImageAlbum.setImageResource(R.mipmap.teste_album);
            }
        }


        if (PlayerActivity.playerDuration != null) {
            int aux = mediaPlayer.getDuration() / 1000;
            int minute = (aux / 60);
            int second = (aux % 60);
            String sDuration = minute < 10 ? "0" + minute : minute + "";
            sDuration += ":" + (second < 10 ? "0" + second : second);

            PlayerActivity.playerDuration.setText(sDuration);
        }

        if (PlayerActivity.playerPlayButton != null) if (tocando)
            PlayerActivity.playerPlayButton.setImageResource(R.drawable.ic_pause_toolbar);
        else PlayerActivity.playerPlayButton.setImageResource(R.drawable.ic_play_player_branco);

        if (MainActivity.toolbarPlayButton != null)
            if (tocando) MainActivity.toolbarPlayButton.setImageResource(R.drawable.ic_pause_small);
            else MainActivity.toolbarPlayButton.setImageResource(R.drawable.ic_play_toolbar);
    }

    public void abrirPlayerTela() {
        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
        intent.putExtra("musica", musicaAtual.getTitle());
        intent.putExtra("artista", musicaAtual.getArtist());
        intent.putExtra("aleatorio", aleatorio);

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(), musicaAtual.getUri());
            byte[] imgBytes = retriever.getEmbeddedPicture();
            if (imgBytes != null) {
                intent.putExtra("imagemBitmap", imgBytes);
                imageAlbum = true;
            } else {
                imageAlbum = false;
            }
            retriever.release();
        } catch (RuntimeException ex) {
            // something went wrong with the file, ignore it and continue
        }
        startActivity(intent);
    }

    private void criarNotificacao() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "MusicPlayer")
                .setContentTitle(musicaAtual.getTitle())
                .setContentText(musicaAtual.getTitle())
                .setPriority(NotificationCompat.PRIORITY_MAX);
        if (tocando) {
            mBuilder.setSmallIcon(R.drawable.ic_play_player_branco);
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_pause_toolbar);
        }
        Intent resultIntent = new Intent(this, PlayerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        stackBuilder.addParentStack(PlayerActivity.class);
        PendingIntent resultPendindIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendindIntent);
        notificationManager.notify(NOTIFICACAO_ID, mBuilder.build());
    }
}