package com.example.goesplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class BancoController {

    private SQLiteDatabase db;
    private DataBaseOpenHelper dboh;

    public BancoController(Context context) {
        dboh = new DataBaseOpenHelper(context);
    }

    public void carregarImagem() {

    }

    public void salvarImagem(byte[] imagem) {

    }

    public String consultarImagem() {

        return "";
    }

    public void salvarNome(String nome) {

    }

    public void salvarUltimaMusica(String musica, String artista) {
        ContentValues contentValues = new ContentValues();

        db = dboh.getWritableDatabase();
        contentValues.put("_id", 1);
        contentValues.put("musica", musica);
        contentValues.put("artista", artista);

        db.update(DataBaseOpenHelper.ULTIMA_MUSICA_TABLE, contentValues,null,null);
        db.close();
    }

    public String[] carregarUltimaMusica() {
        db = dboh.getReadableDatabase();

        Cursor cursor = db.query(DataBaseOpenHelper.ULTIMA_MUSICA_TABLE, new String[] {"_id", "musica", "artista"}, null, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        db.close();

        try {
            return new String[]{cursor.getString(1), cursor.getString(2)};
        }catch (NullPointerException e ){
            return new String[] {"Última música", "Último artista"};
        }
    }

    public int acharPlaylist(String nome) {
        db = dboh.getReadableDatabase();

        //Cursor cursor = db.rawQuery("select _id from playlist where playlist.nome like ?", new String[]{nome});
        Cursor cursor = db.query("playlist", new String[]{"_id"}, "playlist.nome like ?", new String[]{nome}, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        db.close();

        int valor = cursor.getInt(0);
        cursor.close();
        return valor;
    }

    public void criarPlaylist(Context context, String nome) {
        ContentValues contentValues = new ContentValues();
        long resultado;

        db = dboh.getWritableDatabase();
        contentValues.put(DataBaseOpenHelper.DB_NOME, nome);

        resultado = db.insert(DataBaseOpenHelper.PLAYLIST_TABLE, null, contentValues);
        db.close();

        if (resultado == -1)
            Toast.makeText(context, R.string.erro_playlist, Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, R.string.playlist_concluido, Toast.LENGTH_SHORT).show();
    }

    public void adicionarAPlaylist(int playlist, int musica, Context context) {
        ContentValues contentValues = new ContentValues();
        long resultado;

        db = dboh.getWritableDatabase();
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_MUSICA, musica);
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_PLAYLIST, playlist);

        resultado = db.insert(DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, null, contentValues);
        db.close();

        if (resultado == -1)
            Toast.makeText(context, "Erro ao adicionar musica", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Musica adicionada com sucesso", Toast.LENGTH_SHORT).show();
    }

    public Cursor carregaPlaylist() {
        Cursor cursor;
        String[] campos = {DataBaseOpenHelper.DB_ID, DataBaseOpenHelper.DB_NOME};
        db = dboh.getReadableDatabase();
        cursor = db.query(DataBaseOpenHelper.PLAYLIST_TABLE, campos, null, null, null, null, DataBaseOpenHelper.DB_NOME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor consultaPlayMus(int qualPlaylist) {
        Cursor cursor;

        db = dboh.getReadableDatabase();
        cursor = db.rawQuery("select playlistMusica.idMusica from playlist, playlistMusica where playlist._id like ? and playlistMusica.idPlaylist = playlist._id", new String[]{"" + qualPlaylist});

        if (cursor != null) cursor.moveToFirst();
        db.close();
        return cursor;
    }

    public ArrayList<Music> musicsOfPlaylist(Cursor cursor) {

        cursor.moveToFirst();
        ArrayList<Long> listIds = new ArrayList<>();
        ArrayList<Music> musics = new ArrayList<>();

        do {
            listIds.add((long) cursor.getInt(0));
        } while (cursor.moveToNext());

        for (Music i : MainActivity.todasMusicas) {
            if (listIds.contains(i.getIdNumber()))
                musics.add(i);
        }

        return musics;
    }

    public void deletarPlaylist(Context context, int id) {
        int resultado;
        db = dboh.getWritableDatabase();

        if (countNumOfMusics(id) != 0) {
            resultado = db.delete(DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, "idPlaylist = ?", new String[]{"" + id});

            if (resultado == 0) {
                Log.e("EXCLUIR PLAYLIST", "Erro ao excluir na tabela: playlistMusica");
                Toast.makeText(context, "Erro ao excluir playlist", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        resultado = db.delete(DataBaseOpenHelper.PLAYLIST_TABLE, "_id = ?", new String[]{"" + id});

        if (resultado != 0)
            Toast.makeText(context, "Playlist excluida com sucesso", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Erro ao excluir playlist", Toast.LENGTH_SHORT).show();
    }

    public int countNumOfMusics(int playlist) {
        db = dboh.getWritableDatabase();

        return (int) DatabaseUtils.queryNumEntries(db, DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, "idPlaylist = ?", new String[]{playlist + ""});
    }
}