package xyz.vixandrade.cbeautyandroidtest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName() + ": ";

    private ProgressDialog mProgressDialog;

    private Button mSearchButton;
    private EditText mSearchProfile;

    private TextView mUserName;
    private TextView mUserBio;
    private TextView mUserFollowers;
    private TextView mUserRepos;
    private ImageView mUserAvatar;

    private TextView mUserRepoName1;
    private TextView mUserRepoName2;
    private TextView mUserRepoName3;

    private TextView mUserRepoUrl1;
    private TextView mUserRepoUrl2;
    private TextView mUserRepoUrl3;

    private TextView mUserRepoWatchers1;
    private TextView mUserRepoWatchers2;
    private TextView mUserRepoWatchers3;

    private TextView mUserRepoIssues1;
    private TextView mUserRepoIssues2;
    private TextView mUserRepoIssues3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();
        configProgressDialog();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.show();

                String profile = mSearchProfile.getText().toString();

                if (profile.isEmpty()) {
                    clearProfileInfo();
                    showDefaultErrorMessage();
                    return;
                }


                ProfileHelper.fetchProfileInfo(profile, new ProfileHelper.ProfileHelperCallback() {
                    @Override
                    public void onSuccess(UserProfile userProfile) {

                        ProfileHelper.fetchReposInfo(userProfile, new ProfileHelper.ProfileHelperCallback() {
                            @Override
                            public void onSuccess(UserProfile userProfile) {
                                syncWithUiThread(userProfile);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Log.e(LOG_TAG, errorMessage);
                                showDefaultErrorMessage();
                            }
                        });

                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(LOG_TAG, "Profile Request Failed -> " + errorMessage);
                        syncWithUiThread(null);
                        showDefaultErrorMessage();
                    }
                });
            }
        });
    }

    private void loadViews() {
        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchProfile = (EditText) findViewById(R.id.search_profile);

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserBio = (TextView) findViewById(R.id.user_bio);
        mUserFollowers = (TextView) findViewById(R.id.user_folowers);
        mUserRepos = (TextView) findViewById(R.id.user_repos);
        mUserAvatar = (ImageView) findViewById((R.id.user_avatar));

        mUserRepoName1 = (TextView) findViewById(R.id.repo1_name);
        mUserRepoName2 = (TextView) findViewById(R.id.repo2_name);
        mUserRepoName3 = (TextView) findViewById(R.id.repo3_name);

        mUserRepoUrl1 = (TextView) findViewById(R.id.repo1_url);
        mUserRepoUrl2 = (TextView) findViewById(R.id.repo2_url);
        mUserRepoUrl3 = (TextView) findViewById(R.id.repo3_url);

        mUserRepoWatchers1 = (TextView) findViewById(R.id.repo1_watchers);
        mUserRepoWatchers2 = (TextView) findViewById(R.id.repo2_watchers);
        mUserRepoWatchers3 = (TextView) findViewById(R.id.repo3_watchers);

        mUserRepoIssues1 = (TextView) findViewById(R.id.repo1_issues);
        mUserRepoIssues2 = (TextView) findViewById(R.id.repo2_issues);
        mUserRepoIssues3 = (TextView) findViewById(R.id.repo3_issues);
    }

    private void configProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.user_profile_dialog_title));
        mProgressDialog.setMessage(getString(R.string.user_profile_dialog_message));
        mProgressDialog.setCancelable(true);
        mProgressDialog.setIndeterminate(false);
    }

    private void clearProfileInfo() {
        mUserName.setText("");
        mUserBio.setText("");
        mUserFollowers.setText("");
        mUserRepos.setText("");
        mUserAvatar.setImageResource(android.R.color.transparent);
        mUserRepoName1.setText("");
        mUserRepoName2.setText("");
        mUserRepoName3.setText("");
        mUserRepoUrl1.setText("");
        mUserRepoUrl2.setText("");
        mUserRepoUrl3.setText("");
        mUserRepoWatchers1.setText("");
        mUserRepoWatchers2.setText("");
        mUserRepoWatchers3.setText("");
        mUserRepoIssues1.setText("");
        mUserRepoIssues2.setText("");
        mUserRepoIssues3.setText("");
    }

    private void fillProfileInfo(UserProfile profile) {

        mUserName.setText(profile.getName());
        mUserBio.setText(profile.getBio());
        mUserFollowers.setText(getString(R.string.user_profile_followers, profile.getFollowers()));
        mUserRepos.setText(getString(R.string.user_profile_repos, profile.getRepos()));

        try {
            Glide.with(this).load(profile.getAvatar())
                    .asBitmap()
                    .fitCenter()
                    .into(mUserAvatar);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load user avatar -> " + e.getMessage());
        }
    }

    private void fillRepoInfo(UserProfile profile) {

        UserRepo repo1 = profile.getRepo(0);
        UserRepo repo2 = profile.getRepo(1);
        UserRepo repo3 = profile.getRepo(2);

        if (repo1 != null) {
            mUserRepoName1.setText(repo1.getName());
            mUserRepoUrl1.setText(repo1.getUrl());
            mUserRepoWatchers1.setText(String.valueOf(repo1.getWatchers()));
            mUserRepoIssues1.setText(String.valueOf(repo1.getIssues()));
        }

        if (repo2 != null) {
            mUserRepoName2.setText(repo2.getName());
            mUserRepoUrl2.setText(repo2.getUrl());
            mUserRepoWatchers2.setText(String.valueOf(repo2.getWatchers()));
            mUserRepoIssues2.setText(String.valueOf(repo2.getIssues()));
        }

        if (repo3 != null) {
            mUserRepoName3.setText(repo3.getName());
            mUserRepoUrl3.setText(repo3.getUrl());
            mUserRepoWatchers3.setText(String.valueOf(repo3.getWatchers()));
            mUserRepoIssues3.setText(String.valueOf(repo3.getIssues()));
        }

    }

    private void syncWithUiThread(@Nullable final UserProfile profile) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (profile == null) clearProfileInfo();
                else {
                    fillProfileInfo(profile);
                    fillRepoInfo(profile);
                }

                if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.hide(); 
            }
        });
    }

    private void showDefaultErrorMessage() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.hide();
                Toast.makeText(MainActivity.this, R.string.user_profile_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
