package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NearestModel {

    @SerializedName("geometry")
    @Expose
    private GeometryModel geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("opening_hours")
    @Expose
    private OpeningModel openingHours;

    @SerializedName("photos")
    @Expose
    private ArrayList<PhotosModel> photos;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    public NearestModel(GeometryModel geometry, String icon, String username, String id, String name, OpeningModel openingHours, ArrayList <PhotosModel> photos, String rating, String vicinity) {
        this.geometry = geometry;
        this.icon = icon;
        this.username = username;
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.rating = rating;
        this.vicinity = vicinity;
    }

    public GeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryModel geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningModel getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningModel openingHours) {
        this.openingHours = openingHours;
    }

    public ArrayList <PhotosModel> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList <PhotosModel> photos) {
        this.photos = photos;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
