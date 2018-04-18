package fr.tnducrocq.carmanager.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 10/04/2018.
 */

public class User implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("family_name")
    @Expose
    private String familyName;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("photo_url")
    @Expose
    private String photoUrl;

    @SerializedName("team_id")
    @Expose
    private String teamId;

    @SerializedName("team_enable")
    @Expose
    private boolean teamEnable;

    @SerializedName("team_role")
    @Expose
    private String teamRole;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean isTeamEnable() {
        return teamEnable;
    }

    public void setTeamEnable(boolean teamEnable) {
        this.teamEnable = teamEnable;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }


    public User(FirebaseUser firebaseUser) {
        familyName = firebaseUser.getDisplayName();
        mail = firebaseUser.getEmail();
        displayName = firebaseUser.getDisplayName();
        photoUrl = "https://lh3.googleusercontent.com/" + firebaseUser.getPhotoUrl().getEncodedPath();
    }

    public User(GoogleSignInAccount account) {
        familyName = account.getFamilyName();
        mail = account.getEmail();
        displayName = account.getDisplayName();
        photoUrl = "https://lh3.googleusercontent.com/" + account.getPhotoUrl().getEncodedPath();
    }

    public User(Map<String, Object> map) {
        familyName = (String) map.get("family_name");
        mail = (String) map.get("mail");
        displayName = (String) map.get("display_name");
        photoUrl = (String) map.get("photo_url");
        teamId = (String) map.get("team_id");
        teamEnable = (Boolean) map.get("team_enable");
        teamRole = (String) map.get("team_role");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("family_name", familyName);
        user.put("mail", mail);
        user.put("display_name", displayName);
        user.put("photo_url", photoUrl);

        user.put("team_id", teamId);
        user.put("team_enable", teamEnable);
        user.put("team_role", teamRole);
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.familyName);
        dest.writeString(this.displayName);
        dest.writeString(this.mail);
        dest.writeString(this.photoUrl);
        dest.writeString(this.teamId);
        dest.writeByte(this.teamEnable ? (byte) 1 : (byte) 0);
        dest.writeString(this.teamRole);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.familyName = in.readString();
        this.displayName = in.readString();
        this.mail = in.readString();
        this.photoUrl = in.readString();
        this.teamId = in.readString();
        this.teamEnable = in.readByte() != 0;
        this.teamRole = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
