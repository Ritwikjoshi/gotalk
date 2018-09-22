package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModel {

    @SerializedName("lat")
    @Expose
    private String lattitude;

    @SerializedName("lng")
    @Expose
    private String longitude;

    public LocationModel(String lattitude, String longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
