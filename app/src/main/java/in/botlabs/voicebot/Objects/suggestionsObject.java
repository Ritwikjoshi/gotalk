package in.botlabs.voicebot.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class suggestionsObject {
    @SerializedName("url")
    @Expose
    private String ImageUrl;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("itemname")
    @Expose
    private String name;

    public String getUrlFlag() {
        return urlFlag;
    }

    public void setUrlFlag(String urlFlag) {
        this.urlFlag = urlFlag;
    }

    private String urlFlag;

    public suggestionsObject(String imageUrl, String price, String description, String name, String urlFlag) {
        ImageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.name = name;
        this.urlFlag = urlFlag;
    }

    public suggestionsObject(String imageUrl, String name, String urlFlag) {
        ImageUrl = imageUrl;
        this.name = name;
        this.urlFlag = urlFlag;
    }

    public suggestionsObject(String imageUrl, String name, String description, String urlFlag) {
        ImageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.urlFlag = urlFlag;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
