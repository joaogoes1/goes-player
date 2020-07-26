package com.example.goesplayer;

import android.net.Uri;

public class Music {

    private long idNumber;
    private String name;
    private String title;
    private String artist;
    private String album;
    private String gender;
    private String folder;
    private Uri uri;

    public Music(long id, String archiveName, String musicName, String artistName, String albumName, String gender, String folder, Uri uri) {
        this.idNumber = id;
        this.name = archiveName;
        this.title = musicName;
        this.artist = artistName;
        this.album = albumName;
        this.gender = gender;
        this.folder = folder;
        this.uri = uri;
    }

    public long getIdNumber(){
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return gender;
    }

    public String getFolder() {
        return folder;
    }

    public Uri getUri() {
        return uri;
    }
}