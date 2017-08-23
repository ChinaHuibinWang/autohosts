package com.qz.autohosts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.qz.autohosts.util.Utils;

/**
 * Created by chenzhiyong on 16/4/17.
 */
public class LogActivity extends AppCompatActivity {
	private static final String TAG = "LogActivity";

	TextView mTextView;
	ProgressDialog mProgressDialog;
	StringBuffer hostString = new StringBuffer();

	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mTextView = (TextView) findViewById(R.id.detail_txt);

		String log = Utils.ParamGet(this, "Log", "");
		if(log == ""){
			log = "No Log.";
		} else {
			log = log.replace("||", " ");
			log = log.replace(Constants.LOG_GET_HOST_SUCCESS_WHEN_REBOOT, getString(R.string.log_get_host_success_when_reboot));
			log = log.replace(Constants.LOG_GET_HOST_SUCCESS_WHEN_ACTIVE, getString(R.string.log_get_host_success_when_active));
			log = log.replace(Constants.LOG_GET_HOST_FAILED_WHEN_REBOOT, getString(R.string.log_get_host_failed_when_reboot));
			log = log.replace(Constants.LOG_GET_HOST_FAILED_WHEN_ACTIVE, getString(R.string.log_get_host_failed_when_active));
			log = log.replace(Constants.LOG_REASON_FileUsed, getString(R.string.log_reason_fileused));
			log = log.replace(Constants.LOG_REASON_NOROOT, getString(R.string.log_reason_noroot));
			log = log.replace(Constants.LOG_SUGGEST_Root, getString(R.string.log_suggest_root));
			log = log.replace(Constants.LOG_SUGGEST_Reboot, getString(R.string.log_suggest_reboot));
		}

		setTitle(R.string.log);
		mTextView.setText(log);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_clear) {
			new AlertDialog.Builder(LogActivity.this)
					.setTitle(R.string.dlg_title)
					.setMessage(R.string.dlg_info_clearlog)
					.setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Utils.LogClear(LogActivity.this);
							mTextView.setText("No Log.");
						}
					})
					.setNegativeButton(R.string.dlg_cancel, null)
					.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
