package xyz.vixandrade.cbeautyandroidtest;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private String id;
    private String name;
    private String bio;
    private int followers;
    private int repos;
    private String avatar;
    private List<UserRepo> repoList;

    public UserProfile(String id, String name, String bio, int followers, int repos, String avatar) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.followers = followers;
        this.repos = repos;
        this.avatar = avatar;
        this.repoList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public int getFollowers() {
        return followers;
    }

    public int getRepos() {
        return repos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void addRepo(UserRepo repo) {
        repoList.add(repo);
    }

    public UserRepo getRepo(int index) {
        if (repoList.size() <= index) return null;
        return repoList.get(index);
    }
}
