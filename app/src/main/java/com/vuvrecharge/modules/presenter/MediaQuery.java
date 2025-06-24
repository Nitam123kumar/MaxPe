package com.vuvrecharge.modules.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.vuvrecharge.modules.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawan on 4/8/2016.
 */
public class MediaQuery {
    private Context context;
    private int count = 0;
    private Cursor cursor;
    List<VideoItem> videoItems;

    public MediaQuery(Context context) {
        this.context = context;
    }

    public List<VideoItem> getAllVideo() {
        String selection = null;

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };

        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC");

        videoItems = new ArrayList<VideoItem>();
        VideoItem videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoItem();
            videoItem.set_ID(cursor.getString(0));
            videoItem.setARTIST(cursor.getString(1));
            videoItem.setTITLE(cursor.getString(2));
            videoItem.setDATA(cursor.getString(3));
            videoItem.setDISPLAY_NAME(cursor.getString(4));
            videoItem.setDURATION(cursor.getString(5));
            videoItem.setSIZE(cursor.getString(6));
            videoItems.add(videoItem);
        }
        return videoItems;
    }

    public int getVideoCount() {
        int count = 0;
        count = (getAllVideo()).size();
        return count;

    }
}
