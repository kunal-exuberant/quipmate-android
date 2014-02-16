package com.quipmate.feature;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QmChatService extends Service {

	String website = "http://www.quipmate.com/", url, sessionid, myprofileid,
			data, online_data;
	HttpPost post;
	Session s;
	HttpResponse rChatOnline, rChatUpdate;
	int status;
	List<NameValuePair> nameValuePairs;
	Random rand = new Random();
	int random = rand.nextInt(1000000000);
	String ran = "" + random + "";

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("This is on !");
		s = new Session(getApplicationContext());
		sessionid = s.getValue("sessionid");
		myprofileid = s.getValue("profileid");
		new QmChatUpdate().execute();
		new QmChatOnline().execute();

	}

	class QmChatOnline extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("Sevice QmChatOnline! onPreExecute");
			url = website + "chat/online";
			System.out.println(url);
			post = new HttpPost(url);
			nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs
					.add(new BasicNameValuePair("profileid", myprofileid));
			nameValuePairs.add(new BasicNameValuePair("random", ran));
			// nameValuePairs.add(new BasicNameValuePair("name",
			// name.get(myprofileid).toString()));
			nameValuePairs.add(new BasicNameValuePair("name", "Kunal Singh"));
		}

		@Override
		protected String doInBackground(String... arg0) {

			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				System.out.println("Service online Intent Started");
				HttpClient client = null;
				client = new DefaultHttpClient();
				rChatOnline = client.execute(post);
				status = rChatOnline.getStatusLine().getStatusCode();
				System.out.println("Stage -1");
				if (status == 200) {
					System.out
							.println("Online Satus 200 obtained from Quipmate");
					HttpEntity e = rChatOnline.getEntity();
					System.out.println("Stage 1");
					online_data = EntityUtils.toString(e);
				} else {
					System.out.println("Status is not 200 " + status);
				}
			} catch (UnknownHostException e) {
				System.out
						.println("Unable to reach Quipmate. Check Internet Connection !");
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Some Unknown exception occured");
				e.printStackTrace();
			}
			return online_data;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			System.out.println("Sevice QmChatUpdate has been cancelled");
		}

		@Override
		protected void onPostExecute(String data) {
			super.onPostExecute(data);
			System.out
					.println("Sevice QmChatOnline has been executed ! onPostExecute");
			System.out.print(data);
			System.out.println(data.toString());
			try {
				String online_data = new JSONObject(data).getString("user");
				Session sess = new Session(getApplicationContext());
				sess.setValue("online", online_data);
				System.out.println(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			BroadcastReceiver broadcast = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {

				}
			};

			new QmChatOnline().execute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			System.out
					.println("Sevice QmChatOnline Executing! onProgessUpdate");
		}

	}

	class QmChatUpdate extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("Sevice QmChatUpdate! onPreExecute");
			url = website + "chat/chat_update";
			System.out.println(url);
			post = new HttpPost(url);

			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("profileid", myprofileid));
			nameValuePairs.add(new BasicNameValuePair("random", ran));
		}

		@Override
		protected String doInBackground(String... arg0) {

			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				System.out.println("Service Intent Started");
				HttpClient client = new DefaultHttpClient();
				rChatUpdate = client.execute(post);
				status = rChatUpdate.getStatusLine().getStatusCode();
				System.out.println("Stage -1");
				if (status == 200) {
					System.out.println("Satus 200 obtained from Quipmate");
					HttpEntity e = rChatUpdate.getEntity();
					System.out.println("Stage 1");
					data = EntityUtils.toString(e);
				} else {
					System.out.println("Status is not 200 " + status);
				}
			} catch (UnknownHostException e) {
				System.out
						.println("Unable to reach Quipmate. Check Internet Connection !");
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Some Unknown exception occured");
				e.printStackTrace();
			}
			System.out.println(data);
			return data;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			System.out.println("Sevice QmChatUpdate has been cancelled");
		}

		@Override
		protected void onPostExecute(String data) {
			super.onPostExecute(data);
			System.out
					.println("Sevice QmChatUpdate has been executed ! onPostExecute");
			System.out.print(data);
			System.out.println(data.toString());
			try {
				ImageView iv = new ImageView(QmChatService.this);
				TextView tv = new TextView(QmChatService.this);
				tv.setText(new JSONObject(data).getString("message"));
				iv.setBackgroundDrawable(photo_fetch(new JSONObject(data)
						.getString("photo")));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						40, 40);
				layoutParams.setMargins(0, 0, 5, 0);
				iv.setLayoutParams(layoutParams);
				LinearLayout lhlay = new LinearLayout(QmChatService.this);
				lhlay.setPadding(5, 5, 5, 5);
				lhlay.addView(iv);
				lhlay.addView(tv);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			BroadcastReceiver broadcast = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {

				}
			};

			new QmChatUpdate().execute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			System.out
					.println("Sevice QmChatUpdate Executing! onProgessUpdate");
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Drawable photo_fetch(String img) {
		Drawable image = null;
		try {
			System.out.println("Inside Run !");
			java.net.URL u = new java.net.URL("http://www.quipmate.com/" + img);
			Object content = u.getContent();
			InputStream is = (InputStream) content;
			image = Drawable.createFromStream(is, "src");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Returning ffrom heew !");
		return image;
	}

}
