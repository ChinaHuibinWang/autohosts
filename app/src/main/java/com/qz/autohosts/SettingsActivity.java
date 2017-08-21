package com.qz.autohosts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by chenzhiyong on 16/4/17.
 */
public class SettingsActivity extends AppCompatActivity {

	TextView mAboutTxt;
	TextView mHostGithubTxt;
	TextView mAppGithubTxt;
	EditText mEditText;
	Button mButtonDefaultURL;
	Button mButtonOK;
	Button mbuttonBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mAboutTxt = (TextView) findViewById(R.id.tips);
		mHostGithubTxt = (TextView) findViewById(R.id.host_github_txt);
		mAppGithubTxt = (TextView) findViewById(R.id.app_github_txt);
		mEditText = (EditText) findViewById(R.id.editText);
		mButtonDefaultURL = (Button) findViewById(R.id.default_url);
		mButtonOK = (Button) findViewById(R.id.button_ok);
		mbuttonBack = (Button) findViewById(R.id.button_back);

		setTitle(R.string.settings);

		SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		String hostURL = preferences.getString("hostURL", "https://raw.githubusercontent.com/yulei88/autohosts/master/data/hosts");
		mEditText.setText(hostURL);

		String aboutText = getString(R.string.about_detail);
		String hostGithub = getString(R.string.host_github);
		String appGithub = getString(R.string.app_github);

		mAboutTxt.setText(Html.fromHtml(aboutText));
		mHostGithubTxt.setText(Html.fromHtml(hostGithub));
		mAppGithubTxt.setText(Html.fromHtml(appGithub));

		mAboutTxt.setMovementMethod(LinkMovementMethod.getInstance());
		mHostGithubTxt.setMovementMethod(LinkMovementMethod.getInstance());
		mAppGithubTxt.setMovementMethod(LinkMovementMethod.getInstance());

		mAboutTxt.setVisibility(View.GONE);
		mHostGithubTxt.setVisibility(View.GONE);
		mAppGithubTxt.setVisibility(View.GONE);

		mButtonDefaultURL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.setText("https://raw.githubusercontent.com/yulei88/autohosts/master/data/hosts");
				SharedPreferences preferences = getSharedPreferences("user",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("hostURL", mEditText.getText().toString());
				editor.commit();
			}
		});

		mButtonOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getSharedPreferences("user",Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("hostURL", mEditText.getText().toString());
				editor.commit();
			}
		});

		mbuttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsActivity.this.finish();
			}
		});
	}

	private Context getContext() {
		return this;
	}
}
