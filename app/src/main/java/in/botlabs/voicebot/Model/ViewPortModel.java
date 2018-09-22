package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewPortModel {
    @SerializedName("northeast")
    @Expose
    private LocationModel northeast;

    @SerializedName("southwest")
    @Expose
    private LocationModel southwest;

    public ViewPortModel(LocationModel northeast, LocationModel southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public LocationModel getNortheast() {
        return northeast;
    }

    public void setNortheast(LocationModel northeast) {
        this.northeast = northeast;
    }

    public LocationModel getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LocationModel southwest) {
        this.southwest = southwest;
    }
}
