package in.botlabs.voicebot.Objects;

public class OptionsObject {

    private String option;
    private String url;

    public OptionsObject(String option, String url) {
        this.option = option;
        this.url = url;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
