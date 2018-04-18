package fr.tnducrocq.carmanager.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 10/04/2018.
 */

public class Car implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("team_id")
    @Expose
    private String teamId;

    @SerializedName("kilometers")
    @Expose
    private int kilometers;

    @SerializedName("revision")
    @Expose
    private int revision;

    @SerializedName("consumption")
    @Expose
    private float consumption;

    @SerializedName("fuel")
    @Expose
    private float fuel;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("url")
    @Expose
    private String url;

    public Car(Map<String, Object> map) {
        brand = (String) map.get("brand");
        name = (String) map.get("name");
        teamId = (String) map.get("team_id");
        kilometers = ((Long) map.get("kilometers")).intValue();
        revision = ((Long) map.get("revision")).intValue();
        fuel = ((Double) map.get("fuel")).floatValue();
        consumption = ((Double) map.get("consumption")).floatValue();
        image = (String) map.get("image");
        url = (String) map.get("url");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("brand", brand);
        map.put("name", name);
        map.put("team_id", teamId);
        map.put("kilometers", new Long(kilometers));
        map.put("revision", new Long(revision));
        map.put("fuel", new Double(fuel));
        map.put("consumption", new Double(consumption));
        map.put("image", image);
        map.put("url", url);
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.brand);
        dest.writeString(this.name);
        dest.writeString(this.teamId);
        dest.writeInt(this.kilometers);
        dest.writeInt(this.revision);
        dest.writeFloat(this.fuel);
        dest.writeFloat(this.consumption);
        dest.writeString(this.image);
        dest.writeString(this.url);
    }

    public Car() {
    }

    protected Car(Parcel in) {
        this.id = in.readString();
        this.brand = in.readString();
        this.name = in.readString();
        this.teamId = in.readString();
        this.kilometers = in.readInt();
        this.revision = in.readInt();
        this.fuel = in.readFloat();
        this.consumption = in.readFloat();
        this.image = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel source) {
            return new Car(source);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Car{\n");
        sb.append("id='").append(id).append('\'');
        sb.append(",\n\t brand='").append(brand).append('\'');
        sb.append(",\n\t name='").append(name).append('\'');
        sb.append(",\n\t teamId='").append(teamId).append('\'');
        sb.append(",\n\t kilometers=").append(kilometers);
        sb.append(",\n\t revision=").append(revision);
        sb.append(",\n\t fuel=").append(fuel);
        sb.append(",\n\t consumption=").append(consumption);
        sb.append(",\n\t image=").append(image);
        sb.append(",\n\t url=").append(url);
        sb.append("\n}");
        return sb.toString();
    }
}
