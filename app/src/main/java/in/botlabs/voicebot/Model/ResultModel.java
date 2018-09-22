package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultModel {

    @SerializedName("results")
    @Expose
    private ArrayList<NearestModel> results;

    public ResultModel(ArrayList <NearestModel> results) {
        this.results = results;
    }

    public ArrayList <NearestModel> getResults() {
        return results;
    }

    public void setResults(ArrayList <NearestModel> results) {
        this.results = results;
    }
}
