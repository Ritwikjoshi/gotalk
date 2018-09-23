package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintModel {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("_rev")
    @Expose
    private String rev;

    @SerializedName("fooditem")
    @Expose
    private String fooditem;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("seatnumber")
    @Expose
    private String seatnumber;

    @SerializedName("status")
    @Expose
    private String status;

    public ComplaintModel(String id, String rev, String fooditem, String username, String seatnumber, String status) {
        this.id = id;
        this.rev = rev;
        this.fooditem = fooditem;
        this.username = username;
        this.seatnumber = seatnumber;
        this.status = status;
    }

    public ComplaintModel(String fooditem, String username, String age, String status) {
        this.username = username;
        this.fooditem = fooditem;
        this.seatnumber = age;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getFooditem() {
        return fooditem;
    }

    public void setFooditem(String fooditem) {
        this.fooditem = fooditem;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(String seatnumber) {
        this.seatnumber = seatnumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
