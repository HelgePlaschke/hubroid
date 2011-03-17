/**
 * Hubroid - A GitHub app for Android
 *
 * Copyright (c) 2011 Eddie Ringle.
 *
 * Licensed under the New BSD License.
 */

package net.idlesoft.android.apps.github;

import org.idlesoft.libraries.ghapi.GitHubAPI;

import net.idlesoft.android.apps.github.activities.Dashboard;
import net.idlesoft.android.apps.github.activities.Hubroid;
import net.idlesoft.android.apps.github.activities.Search;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HubroidApplication extends Application {
    private static final boolean DEVELOPER_MODE = true;

    private static boolean IS_LOGGED_IN;

    private static String USERNAME;

    private static String PASSWORD;

    private static final GitHubAPI GITHUB_API = new GitHubAPI();

    public static boolean isLoggedIn() {
    	return IS_LOGGED_IN;
    }

    public boolean checkIfLoggedIn() {
    	final SharedPreferences prefs = getSharedPreferences(Hubroid.PREFS_NAME, 0);
    	if (prefs.getString("username", "").equals("")
    			|| prefs.getString("password", "").equals("")) {
    		IS_LOGGED_IN = false;
    		GITHUB_API.goStealth();
    	} else {
    		/* If previously not logged in, set logged in username and password */
    		if (IS_LOGGED_IN == false) {
    			USERNAME = prefs.getString("username", "");
    			PASSWORD = prefs.getString("password", "");
    			GITHUB_API.authenticate(USERNAME, PASSWORD);
    		}
    		IS_LOGGED_IN = true;
    	}
    	return IS_LOGGED_IN;
    }

    public static String getUsername() {
    	return USERNAME;
    }

    public static String getPassword() {
    	return PASSWORD;
    }

    public static GitHubAPI getGitHubAPI() {
    	return GITHUB_API;
    }

    @Override
    public void onCreate() {
        /* Enable Strict Mode if DEVELOPER_MODE is set */
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll()
                    .permitDiskReads().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
                    .penaltyDeath().build());
        }
        super.onCreate();
    }

    public static void setupActionBar(final Activity pActivity, final String pTitle, final boolean pShowSearch, final boolean pLinkIcon) {
    	final TextView title = (TextView) pActivity.findViewById(R.id.tv_top_bar_text);
    	if (pTitle == null) {
    		title.setText("Hubroid");
    	} else {
    		title.setText(pTitle);
    	}

    	if (pLinkIcon) {
	    	final OnClickListener onActionBarIconClick = new OnClickListener() {
	    		public void onClick(View v) {
	    			pActivity.startActivity(new Intent(pActivity, Dashboard.class));
	    		}
	    	};

	    	final ImageView icon = (ImageView) pActivity.findViewById(R.id.iv_top_bar_icon);
	    	icon.setOnClickListener(onActionBarIconClick);
    	}

    	final ImageView search = (ImageView) pActivity.findViewById(R.id.btn_search);
    	if (pShowSearch) {
    		final OnClickListener onActionBarSearchClick = new OnClickListener() {
				public void onClick(View v) {
					pActivity.startActivity(new Intent(pActivity, Search.class));
				}
			};
			search.setOnClickListener(onActionBarSearchClick);
    	} else {
    		search.setVisibility(View.GONE);
    	}
    }

    public static void setupActionBar(final Activity pActivity) {
    	setupActionBar(pActivity, "Hubroid", true, true);
    }

    public static void setupActionBar(final Activity pActivity, final String pTitle) {
    	setupActionBar(pActivity, pTitle, true, true);
    }

    public static void setupActionBar(final Activity pActivity, final boolean pShowSearch) {
    	setupActionBar(pActivity, "Hubroid", pShowSearch, true);
    }
}
