package in.botlabs.voicebot.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningModel {

    @SerializedName("open_now")
    @Expose
    private boolean Open;

    public OpeningModel(boolean open) {
        Open = open;
    }

    public boolean isOpen() {
        return Open;
    }

    public void setOpen(boolean open) {
        Open = open;
    }
}
