package com.zhy.http.okhttp.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Multipart上传(需要httpmime.jar)
 */

public class UploadMultiFile extends AsyncTask<String, Void, String> {

	private static final String TAG = UploadMultiFile.class.getSimpleName();

	private Context mContext;

	private long totalSize;
	private String URL ="";

	Map<String, String> params;

	private List<File> files;

	private ResultCallBack mBack;

	private ProgressDialogHandler progressHandler;

	public UploadMultiFile(Context context, String url,List<File> files,Map<String, String> map,ResultCallBack listener) {
		this.mContext = context;
		this.URL = url;
		this.files = files;
		this.params = map;
		this.progressHandler = new ProgressDialogHandler(mContext, true);
		this.mBack =listener;

	}

	/**
	 * <p>
	 * show ProgressDialog
	 * </p>
	 */
	private void showProgressDialog(){
		if (progressHandler != null) {
			progressHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
		}
	}
	/**
	 * <p>
	 * show dismissProgressDialog
	 * </p>
	 */
	private void dismissProgressDialog(){
		if (progressHandler != null) {
			progressHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
			progressHandler = null;
		}
	}

	@Override
	protected void onPreExecute() {
		showProgressDialog();
	}

	@Override
	protected String doInBackground(String... params01) {
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();

		HttpPost httpPost = new HttpPost(URL);
		try {


			MultipartEntity entity = new MultipartEntity();

			if (files != null && files.size() > 0) {
				for(File file : files){
					entity.addPart("filename", new FileBody(file));
				}
			}

			if (params != null && params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}

			totalSize = entity.getContentLength();
			Log.d(TAG,"totalSize: " + totalSize);
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			if (response.getStatusLine().getStatusCode() == 200) {
				/*取出响应字符串*/
				result = EntityUtils.toString(response.getEntity());
			} else {
				Log.e(TAG,
						"Error Response: " +
								response.getStatusLine().toString());
			}
			Log.i(TAG,"result: " + result);
		} catch (Exception e) {
			Log.v("wangly", e.getMessage());
			if (mBack!=null) {
				mBack.Failure(e.getMessage());
			}
		}

		return result;
	}

	/**
	 * 执行AsynTask 
	 */
	public void execute(){
		this.execute("");
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG,"result: " + result);
		dismissProgressDialog();
		if (!"".equals(result)&& result!=null) {
			if ("0000".equals(result)) {

				if (mBack!=null) {
					mBack.Succeed(result);
				}
			}else {

				if (mBack!=null) {
					mBack.Failure(result);
				}
			}
		}else {
			if (mBack!=null) {
				mBack.Failure("Server return Error！");
			}
			return ;
		}
	}

	@Override
	protected void onCancelled() {
		Log.i(TAG,"onCancelled...");
	}

	public interface ResultCallBack{
		void Succeed(String result);
		void Failure(String result);
	}
}
