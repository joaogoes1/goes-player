package com.goesplayer;

import static java.util.Collections.emptyList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.goesplayer.data.model.Music;
import com.goesplayer.data.model.Playlist;

import java.util.ArrayList;
import java.util.List;

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

        db.update(DataBaseOpenHelper.ULTIMA_MUSICA_TABLE, contentValues, null, null);
        db.close();
    }

    public String[] carregarUltimaMusica() {
        db = dboh.getReadableDatabase();

        Cursor cursor = db.query(DataBaseOpenHelper.ULTIMA_MUSICA_TABLE, new String[]{"_id", "musica", "artista"}, null, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        db.close();

        try {
            return new String[]{cursor.getString(1), cursor.getString(2)};
        } catch (NullPointerException e) {
            return new String[]{"Última música", "Último artista"};
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

    // TODO: Verify if the playlist name already exists
    public void criarPlaylist(Context context, String nome) {
        ContentValues contentValues = new ContentValues();
        long resultado;

        db = dboh.getWritableDatabase();
        contentValues.put(DataBaseOpenHelper.DB_NOME, nome);

        resultado = db.insert(DataBaseOpenHelper.PLAYLIST_TABLE, null, contentValues);
        db.close();

//        if (resultado == -1)
//            Toast.makeText(context, R.string.erro_playlist, Toast.LENGTH_SHORT).show();
//        else Toast.makeText(context, R.string.playlist_concluido, Toast.LENGTH_SHORT).show();
    }

    public boolean addToPlaylist(long playlist, long musica) {
        ContentValues contentValues = new ContentValues();
        long result;

        db = dboh.getWritableDatabase();
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_MUSICA, musica);
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_PLAYLIST, playlist);

        result = db.insert(DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, null, contentValues);
        db.close();

        return result > 0;
    }

    public List<Playlist> loadPlaylists() {
        final ArrayList<Playlist> results = new ArrayList<>();
        Cursor cursor;
        String[] campos = {DataBaseOpenHelper.DB_ID, DataBaseOpenHelper.DB_NOME};
        db = dboh.getReadableDatabase();
        cursor = db.query(DataBaseOpenHelper.PLAYLIST_TABLE, campos, null, null, null, null, DataBaseOpenHelper.DB_NOME, null);

        cursor.moveToFirst();
        final int idColumnIndex = cursor.getColumnIndex(DataBaseOpenHelper.DB_ID);
        final int nameColumnIndex = cursor.getColumnIndex(DataBaseOpenHelper.DB_NOME);
        if (idColumnIndex < 0 || nameColumnIndex < 0) return emptyList();
        while (cursor.moveToNext()) {
            results.add(new Playlist(cursor.getLong(idColumnIndex), cursor.getString(nameColumnIndex)));
        }

        db.close();
        cursor.close();
        return results;
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

        for (Music i : OldMainActivity.todasMusicas) {
            if (listIds.contains(i.getId()))
                musics.add(i);
        }

        return musics;
    }

    public boolean deletePlaylist(long id) {
        int result;
        db = dboh.getWritableDatabase();

        if (countNumOfMusics(id) != 0) {
            result = db.delete(DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, "idPlaylist = ?", new String[]{"" + id});

            if (result == 0) {
                Log.e("DELETE PLAYLIST", "Error on exclude data on playlistMusica table");
                return false;
            }
        }
        result = db.delete(DataBaseOpenHelper.PLAYLIST_TABLE, "_id = ?", new String[]{"" + id});
        return result != 0;
    }

    public long countNumOfMusics(long playlist) {
        db = dboh.getWritableDatabase();

        return DatabaseUtils.queryNumEntries(db, DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, "idPlaylist = ?", new String[]{playlist + ""});
    }
}