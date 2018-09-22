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

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("complain_description")
    @Expose
    private String description;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("status")
    @Expose
    private String status;

    public ComplaintModel(String id, String rev, String category, String description, String username, String age, String status) {
        this.id = id;
        this.rev = rev;
        this.category = category;
        this.description = description;
        this.username = username;
        this.age = age;
        this.status = status;
    }

    public ComplaintModel(String category, String description, String username, String age, String status) {
        this.category = category;
        this.description = description;
        this.username = username;
        this.age = age;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
