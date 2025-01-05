package com.goesplayer.data.datasource

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.util.Log
import com.goesplayer.DataBaseOpenHelper
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dboh: DataBaseOpenHelper = DataBaseOpenHelper(context)

    fun carregarImagem() {
    }

    fun salvarImagem(imagem: ByteArray?) {
    }

    fun consultarImagem(): String {
        return ""
    }

    fun salvarNome(nome: String?) {
    }

    fun salvarUltimaMusica(musica: String?, artista: String?) {
        val contentValues = ContentValues()

        val db = dboh.writableDatabase
        contentValues.put("_id", 1)
        contentValues.put("musica", musica)
        contentValues.put("artista", artista)

        db.update(DataBaseOpenHelper.ULTIMA_MUSICA_TABLE, contentValues, null, null)
        db.close()
    }

    fun carregarUltimaMusica(): Array<String> {
        val db = dboh.readableDatabase

        val cursor = db.query(
            DataBaseOpenHelper.ULTIMA_MUSICA_TABLE,
            arrayOf("_id", "musica", "artista"),
            null,
            null,
            null,
            null,
            null
        )
        if (cursor != null) cursor.moveToFirst()
        db.close()

        return try {
            arrayOf(cursor!!.getString(1), cursor!!.getString(2))
        } catch (e: NullPointerException) {
            arrayOf("Última música", "Último artista")
        }
    }

    fun acharPlaylist(nome: String): Int {
        val db = dboh.readableDatabase

        //Cursor cursor = db.rawQuery("select _id from playlist where playlist.nome like ?", new String[]{nome});
        val cursor = db.query(
            "playlist",
            arrayOf("_id"),
            "playlist.nome like ?",
            arrayOf(nome),
            null,
            null,
            null
        )
        if (cursor != null) cursor.moveToFirst()
        db.close()

        val valor = cursor.getInt(0)
        cursor.close()
        return valor
    }

    // TODO: Verify if the playlist name already exists
    fun createPlaylist(nome: String): Boolean {
        val contentValues = ContentValues()

        val db = dboh.writableDatabase
        contentValues.put(DataBaseOpenHelper.DB_NOME, nome)

        val result =
            db.insert(DataBaseOpenHelper.PLAYLIST_TABLE, null, contentValues)
        db.close()

        return result > 0
    }

    fun addToPlaylist(playlist: Long, music: Long): Boolean {
        val contentValues = ContentValues()

        val db = dboh.writableDatabase
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_MUSICA, music)
        contentValues.put(DataBaseOpenHelper.PLAYMUS_ID_PLAYLIST, playlist)

        val result =
            db.insert(DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE, null, contentValues)
        db.close()

        return result > 0
    }

    fun loadPlaylists(): List<Playlist> {
        val results = ArrayList<Playlist>()
        val cursor: Cursor
        val campos = arrayOf(DataBaseOpenHelper.DB_ID, DataBaseOpenHelper.DB_NOME)
        val db = dboh.readableDatabase
        cursor = db.query(
            DataBaseOpenHelper.PLAYLIST_TABLE,
            campos,
            null,
            null,
            null,
            null,
            DataBaseOpenHelper.DB_NOME,
            null
        )

        cursor.moveToFirst()
        val idColumnIndex = cursor.getColumnIndex(DataBaseOpenHelper.DB_ID)
        val nameColumnIndex = cursor.getColumnIndex(DataBaseOpenHelper.DB_NOME)
        if (idColumnIndex < 0 || nameColumnIndex < 0) return emptyList()
        while (cursor.moveToNext()) {
            results.add(Playlist(cursor.getLong(idColumnIndex), cursor.getString(nameColumnIndex)))
        }

        db.close()
        cursor.close()
        return results
    }

    fun consultaPlayMus(qualPlaylist: Int): Cursor {
        val db = dboh.readableDatabase
        val cursor = db.rawQuery(
            "select playlistMusica.idMusica from playlist, playlistMusica where playlist._id like ? and playlistMusica.idPlaylist = playlist._id",
            arrayOf("" + qualPlaylist)
        )

        if (cursor != null) cursor.moveToFirst()
        db.close()
        return cursor
    }

    fun musicsOfPlaylist(cursor: Cursor): ArrayList<Music> {
        cursor.moveToFirst()
        val listIds = ArrayList<Long>()
        val musics = ArrayList<Music>()

        do {
            listIds.add(cursor.getInt(0).toLong())
        } while (cursor.moveToNext())

//        for (i in OldMainActivity.todasMusicas) {
//            if (listIds.contains(i.id)) musics.add(i)
//        }

        return musics
    }

    fun deletePlaylist(id: Long): Boolean {
        var result: Int
        val db = dboh.writableDatabase

        if (countNumOfMusics(id) != 0L) {
            result = db.delete(
                DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE,
                "idPlaylist = ?",
                arrayOf("" + id)
            )

            if (result == 0) {
                Log.e("DELETE PLAYLIST", "Error on exclude data on playlistMusica table")
                return false
            }
        }
        result = db.delete(DataBaseOpenHelper.PLAYLIST_TABLE, "_id = ?", arrayOf("" + id))
        return result != 0
    }

    fun countNumOfMusics(playlist: Long): Long {
        val db = dboh.writableDatabase

        return DatabaseUtils.queryNumEntries(
            db,
            DataBaseOpenHelper.PLAYLIST_MUSICA_TABLE,
            "idPlaylist = ?",
            arrayOf(playlist.toString() + "")
        )
    }
}