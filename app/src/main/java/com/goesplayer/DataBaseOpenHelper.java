package com.goesplayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "player_database";
    private static final int DB_VERSION = 1;
    public static final String DB_ID = "_id";
    public static final String DB_NOME = "nome";
    public static final String PERFIL_TABLE = "perfil";
    public static final String ULTIMA_MUSICA_TABLE = "ultimaMusica";
    public static final String PLAYLIST_TABLE = "playlist";
    public static final String PLAYLIST_MUSICA_TABLE = "playlistMusica";
    public static final String PLAYMUS_ID_PLAYLIST = "idPlaylist";
    public static final String PLAYMUS_ID_MUSICA = "idMusica";


    public DataBaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PLAYLIST_TABLE + " ( "
                + DB_ID + " integer primary key autoincrement, "
                + DB_NOME + " text not null )");

        db.execSQL("create table " + PLAYLIST_MUSICA_TABLE + " ( "
                + DB_ID + " integer primary key autoincrement, "
                + PLAYMUS_ID_MUSICA + " integer not null, "
                + PLAYMUS_ID_PLAYLIST + " integer not null, "
                + "constraint fk_idPlaylist foreign key (idPlaylist) references playlis (id) )");

        db.execSQL("create table " + PERFIL_TABLE + " ( _id integer primary key, nome text not null, imagem blob)");
        db.execSQL("create table " + ULTIMA_MUSICA_TABLE + " (_id integer primary key, musica text not null, artista text not null)");
        db.execSQL("insert into " + ULTIMA_MUSICA_TABLE + " values (1, 'Última música', 'Último artista')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYLIST_MUSICA_TABLE);
        onCreate(db);
    }
}