package com.zhy.http.okhttp.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.http.okhttp.R;
import com.zhy.http.okhttp.utils.UploadMultiFile;
import com.zhy.http.okhttp.utils.UploadMultiFile.ResultCallBack;

public class MainActivity extends Activity {


	private static final String TAG = "MainActivity";

	private TextView mTv;
	private ImageView mImageView;
	private ProgressBar mProgressBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		//		mTv = (TextView) findViewById(R.id.id_textview);
		//		mImageView = (ImageView) findViewById(R.id.id_imageview);
		//		mProgressBar = (ProgressBar) findViewById(R.id.id_progress);
		//		mProgressBar.setMax(100);


		File file01 = new File("sdcard/test.png");
		File file02 = new File("sdcard/test.png");
		File file03 = new File("sdcard/test.png");

		List<File> files= new ArrayList<File>();
		files.add(file01);
		files.add(file02);
		files.add(file03);

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", "10210");


		if ("有网") {

			UploadMultiFile httpPost = new UploadMultiFile(this, "Server URL addre", files, map, new ResultCallBack() {

				@Override
				public void Succeed(String result) {

				}

				@Override
				public void Failure(String result) {

				}
			});
			httpPost.execute();
		}else{
			Log.d("wangly", "无网络");
		}
	}


}
