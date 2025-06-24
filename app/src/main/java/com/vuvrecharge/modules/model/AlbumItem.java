package com.vuvrecharge.modules.model;

/**
 * Created by Pawan on 4/8/2016.
 */
public class AlbumItem {
    String _ID;
    String ARTIST;
    String NUMBER_OF_SONGS;
    String ALBUM;
    public AlbumItem(String _ID, String ARTIST, String NUMBER_OF_SONGS,
                     String ALBUM){

        this._ID=_ID;
        this.ARTIST=ARTIST;
        this.NUMBER_OF_SONGS=NUMBER_OF_SONGS;
        this.ALBUM=ALBUM;
    };
    public AlbumItem(){

    }
    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }


    public String getALBUM() {
        return ALBUM;
    }

    public void setALBUM(String ALBUM) {
        this.ALBUM = ALBUM;
    }

    public String getNUMBER_OF_SONGS() {
        return NUMBER_OF_SONGS;
    }

    public void setNUMBER_OF_SONGS(String NUMBER_OF_SONGS) {
        this.NUMBER_OF_SONGS = NUMBER_OF_SONGS;
    }
}
