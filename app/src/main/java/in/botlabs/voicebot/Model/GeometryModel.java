package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryModel {

    @SerializedName("location")
    @Expose
    private LocationModel location;

    @SerializedName("viewport")
    @Expose
    private ViewPortModel viewPort;

    public GeometryModel(LocationModel location, ViewPortModel viewPort) {
        this.location = location;
        this.viewPort = viewPort;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public ViewPortModel getViewPort() {
        return viewPort;
    }

    public void setViewPort(ViewPortModel viewPort) {
        this.viewPort = viewPort;
    }
}
