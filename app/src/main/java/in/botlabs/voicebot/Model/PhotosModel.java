package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhotosModel {

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("html_attributions")
    @Expose
    private ArrayList<String> imageUrl;

    public PhotosModel(String height, ArrayList <String> imageUrl) {
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public ArrayList <String> getImageUrl() {

//        imageUrl.get(0).replace("\u003ca href=\"","");
        return imageUrl;
    }

    public void setImageUrl(ArrayList <String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
