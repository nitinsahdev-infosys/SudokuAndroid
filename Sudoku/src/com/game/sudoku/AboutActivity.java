package com.game.sudoku;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		try {
			PackageManager pm = this.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(),
					PackageManager.GET_META_DATA);

			TextView appVersionValTextView = (TextView) findViewById(R.id.appVersionVal);
			appVersionValTextView.setText(pi.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}
}
