package com.vuvrecharge.preferences;

import androidx.annotation.Keep;

import com.vuvrecharge.modules.model.AlbumItem;
import com.vuvrecharge.modules.model.ArtistItem;
import com.vuvrecharge.modules.model.AudioItem;
import com.vuvrecharge.modules.model.ContactData;
import com.vuvrecharge.modules.model.UserData;
import com.vuvrecharge.modules.model.VideoItem;

import java.util.List;

@Keep
public interface UserPreferences {

    void setUserLogin(boolean statusLogin);

    boolean isUserLogin();

    void setUserSkip(boolean statusSkip);

    boolean isUserSkip();

    void setUserType(String statusType);

    String getUserType();

    String getUserId();

    String getUsername();

    void setUserData(String userData);

    UserData getUserData();

    String getUserDataS();

    void setAppLanguage(String data);

    long getGoal();

    void setGoal(long data);

    String getAppLanguage();

    void clearUser();

    String getToken();

    void setToken(String type);

    String getEarnings();

    void setEarnings(String type);

    String getVersion();

    void setVersion(String type);

    String getViewType();

    void setViewType(String type);

    void setAllVideos(String videos);

    List<VideoItem> getAllVideos();

    void setAllAudio(String videos);

    List<AudioItem> getAllAudio();

    void setAllAlbum(String videos);

    List<AlbumItem> getAllAlbum();

    void setAllArtist(String videos);

    List<ArtistItem> getAllArtist();

    void setContact(String contact);

    List<ContactData> getContact();
    void setFcmToken(String fcmToken);

    String getFcmToken();
    
    void setBlockAmount(String blockAmt);

    String getBlockAmount();
}
