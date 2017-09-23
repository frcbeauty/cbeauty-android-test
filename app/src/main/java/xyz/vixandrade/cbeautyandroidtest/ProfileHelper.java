package xyz.vixandrade.cbeautyandroidtest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class ProfileHelper {

    private ProfileHelper() {
        // Empty Constructor.
    }

    public static void fetchProfileInfo(final String id, final ProfileHelperCallback callback) {

        String requestUrl = "https://api.github.com/users/" + id;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final String jsonData = response.body().string();
                        JSONObject rootObj = new JSONObject(jsonData);

                        String name = rootObj.getString("login");
                        String bio = rootObj.getString("bio");
                        int followers = rootObj.getInt("followers");
                        int repos = rootObj.getInt("public_repos");
                        String avatar = rootObj.getString("avatar_url");

                        UserProfile userProfile = new UserProfile(id, name, bio, followers, repos, avatar);
                        callback.onSuccess(userProfile);

                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                }

                else {
                    callback.onFailure("Fetch Profile Response not Successful");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    public static void fetchReposInfo(final UserProfile profile, final ProfileHelperCallback callback) {
        String requestUrl = "https://api.github.com/search/repositories?q=user:" + profile.getId();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    try {
                        final String jsonData = response.body().string();
                        JSONObject rootObj = new JSONObject(jsonData);
                        JSONArray reposObj = rootObj.getJSONArray("items");

                        JSONObject firstRepoObj = reposObj.getJSONObject(0);
                        JSONObject secondRepoObj = reposObj.getJSONObject(1);
                        JSONObject thirdRepoObj = reposObj.getJSONObject(2);

                        UserRepo firstRepo = getUserRepo(firstRepoObj);
                        UserRepo secondRepo = getUserRepo(secondRepoObj);
                        UserRepo thirdRepo = getUserRepo(thirdRepoObj);

                        if (firstRepo != null) profile.addRepo(firstRepo);
                        if (secondRepo != null) profile.addRepo(secondRepo);
                        if (thirdRepo != null) profile.addRepo(thirdRepo);

                        callback.onSuccess(profile);

                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                }

                else {
                    callback.onFailure("Fetch Repos Response not Successful");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    private static UserRepo getUserRepo(JSONObject repoObj) {
        try {

            String name = repoObj.getString("name");
            String url = repoObj.getString("url");
            int watchers = repoObj.getInt("watchers_count");
            int issues = repoObj.getInt("open_issues");

            return new UserRepo(name, url, watchers, issues);

        } catch(JSONException e) {
            return null;
        }
    }

    public interface ProfileHelperCallback {
        void onSuccess(UserProfile userProfile);
        void onFailure(String errorMessage);
    }
}
