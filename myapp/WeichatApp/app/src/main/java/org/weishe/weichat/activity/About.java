package org.weishe.weichat.activity;

import org.weishe.weichat.R;
import org.weishe.weichat.util.UpdateManager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 */
public class About extends Activity {

	private TextView mVersion;
	private Button mUpdate;

	// private ImageView iBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// 获取客户端版本信息
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mVersion = (TextView) findViewById(R.id.about_version);
			mVersion.setText("版本：" + info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}

		mUpdate = (Button) findViewById(R.id.about_update);
		mUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UpdateManager.getUpdateManager().checkAppUpdate(About.this,
						true);
			}
		});
	}

}
