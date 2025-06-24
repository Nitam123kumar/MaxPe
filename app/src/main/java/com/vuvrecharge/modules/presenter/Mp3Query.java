package com.vuvrecharge.modules.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.vuvrecharge.modules.model.AlbumItem;
import com.vuvrecharge.modules.model.ArtistItem;
import com.vuvrecharge.modules.model.AudioItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawan on 4/8/2016.
 */
public class Mp3Query {
    private Context context;

    public Mp3Query(Context context) {
        this.context = context;
    }

    public List<AudioItem> getAllAudio() {

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM
        };

        String selection = MediaStore.Audio.Media.DATA
                + " like " + "'%.mp3%'";
        //MediaStore.Audio.Media.DATE_ADDED + " DESC"
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);

        List<AudioItem> audioItems = new ArrayList<>();
        AudioItem audioItem;
        while (cursor.moveToNext()) {
            audioItem = new AudioItem();
            audioItem.set_ID(cursor.getString(0));
            audioItem.setARTIST(cursor.getString(1));
            audioItem.setTITLE(cursor.getString(2));
            audioItem.setDATA(cursor.getString(3));
            audioItem.setDISPLAY_NAME(cursor.getString(4));
            audioItem.setDURATION(cursor.getString(5));
            audioItem.setSIZE(cursor.getString(6));
            audioItem.setALBUM(cursor.getString(7));
            audioItems.add(audioItem);
        }

        return audioItems;
    }

    public List<AudioItem> getAllAudio(String selection, String short_order) {

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM
        };

        //  String selection = MediaStore.Audio.Media.DATA + " like " + "'%.mp3%'";
        //MediaStore.Audio.Media.DATE_ADDED + " DESC"
        String sortOrder = "";
        if (short_order.equals("By Date")) {
            sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";
        } else {
            sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        }

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder);

        List<AudioItem> audioItems = new ArrayList<>();
        AudioItem audioItem;
        while (cursor.moveToNext()) {
            audioItem = new AudioItem();
            audioItem.set_ID(cursor.getString(0));
            audioItem.setARTIST(cursor.getString(1));
            audioItem.setTITLE(cursor.getString(2));
            audioItem.setDATA(cursor.getString(3));
            audioItem.setDISPLAY_NAME(cursor.getString(4));
            audioItem.setDURATION(cursor.getString(5));
            audioItem.setSIZE(cursor.getString(6));
            audioItem.setALBUM(cursor.getString(7));
            audioItems.add(audioItem);
        }

        return audioItems;
    }

    public List<AlbumItem> getAllAlbums() {

        List<AlbumItem> albumItems = new ArrayList<>();

        Cursor cursor1 = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                        MediaStore.Audio.Albums.ALBUM}, null, null,
                MediaStore.Audio.Albums.ALBUM + " ASC");
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                AlbumItem albumItem = new AlbumItem();
                albumItem.setARTIST(cursor1.getString(0));
                albumItem.set_ID(cursor1.getString(1));
                albumItem.setNUMBER_OF_SONGS(cursor1.getString(2));
                albumItem.setALBUM(cursor1.getString(3));
                albumItems.add(albumItem);
            }
        }
        List<AlbumItem> albumItems_new = new ArrayList<>();
        List<AlbumItem> albumItems_other = new ArrayList<>();
        List<AlbumItem> albumItems_merge = new ArrayList<>();
        albumItems_new = albumItems;
        while (albumItems_new.size() > 0) {
            albumItems_other = new ArrayList<>();
            int count_new = 0;
            String album = albumItems_new.get(0).getALBUM();
            String id = albumItems_new.get(0).get_ID();
            String artist = albumItems_new.get(0).getARTIST();
            for (AlbumItem albumItem : albumItems_new) {
                String album_new = albumItem.getALBUM();
                String number_of_songs_count = albumItem.getNUMBER_OF_SONGS();
                if (album.equals(album_new)) {
                    count_new = count_new + Integer.parseInt(number_of_songs_count);
                } else {
                    albumItems_other.add(albumItem);
                }
            }
            albumItems_new = albumItems_other;
            AlbumItem albumItem = new AlbumItem();
            albumItem.setARTIST(artist);
            albumItem.set_ID(id);
            albumItem.setNUMBER_OF_SONGS(count_new + "");
            albumItem.setALBUM(album);
            albumItems_merge.add(albumItem);
        }

        return albumItems_merge;
    }

    public List<ArtistItem> getAllArtists() {

        List<ArtistItem> artistItems = new ArrayList<>();

        Cursor cursor1 = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Artists.ARTIST,
                        MediaStore.Audio.Artists._ID,
                        MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS}, null, null,
                MediaStore.Audio.Artists.ARTIST + " ASC");
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                ArtistItem artistItem = new ArtistItem();
                artistItem.setARTIST(cursor1.getString(0));
                artistItem.set_ID(cursor1.getString(1));
                artistItem.setNUMBER_OF_SONGS(cursor1.getString(2));
                artistItems.add(artistItem);
            }
        }
        List<ArtistItem> artistItems_new = new ArrayList<>();
        List<ArtistItem> artistItems_other = new ArrayList<>();
        List<ArtistItem> artistItems_merge = new ArrayList<>();
        artistItems_new = artistItems;
        while (artistItems_new.size() > 0) {
            artistItems_other = new ArrayList<>();
            int count_new = 0;
            String id = artistItems_new.get(0).get_ID();
            String artist = artistItems_new.get(0).getARTIST();
            for (ArtistItem artistItem : artistItems_new) {
                String number_of_songs_count = artistItem.getNUMBER_OF_SONGS();
                String artist_new = artistItem.getARTIST();
                if (artist.equals(artist_new)) {
                    count_new = count_new + Integer.parseInt(number_of_songs_count);
                } else {
                    artistItems_other.add(artistItem);
                }
            }
            artistItems_new = artistItems_other;
            ArtistItem artistItem = new ArtistItem();
            artistItem.setARTIST(artist);
            artistItem.set_ID(id);
            artistItem.setNUMBER_OF_SONGS(count_new + "");
            artistItems_merge.add(artistItem);
        }

        return artistItems_merge;
    }

    public int getAudioCount() {
        int count = 0;
        count = (getAllAudio()).size();
        return count;
    }
}
