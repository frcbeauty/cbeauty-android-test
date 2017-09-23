package xyz.vixandrade.cbeautyandroidtest;

public class UserRepo {

    private String name;
    private String url;
    private int watchers;
    private int issues;

    public UserRepo(String name, String url, int watchers, int issues) {
        this.name = name;
        this.url = url;
        this.watchers = watchers;
        this.issues = issues;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getWatchers() {
        return watchers;
    }

    public int getIssues() {
        return issues;
    }
}
