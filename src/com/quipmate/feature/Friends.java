package com.quipmate.feature;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.quipmate.R;
import com.quipmate.constants.AppProperties;

public class Friends extends Activity implements OnClickListener {

	EditText etemail, etpassword;
	TextView tvstatus;
	Button blogin;
	ImageView logo;
	Drawable image;
	Session s;
	String sessionid, myprofileid, profileid, url, data, email, password,
			friend_data;
	String img = "http://www.quipmate.com/upload_pic/2isk92li26mi0b7m02e4kuvq421353130910719754526674026619136382609593295_t.jpg";
	HttpResponse r;
	JSONArray friend;
	JSONObject last, name, pimage;
	DefaultHttpClient client;
	HttpGet get;
	int k;
	int status;
	LinearLayout llay;
	ScrollView scroll;
	ImageView ivOnline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.friends);
//		scroll = (ScrollView) findViewById(R.id.svFiendList);
//		s = new Session(getApplicationContext());
//		sessionid = s.getValue("sessionid");
//		myprofileid = s.getValue("profileid");
//
//		client = AppProperties.appUserClient;
//		url = AppProperties.URL + "?action=friend_fetch&profileid="
//				+ myprofileid;
//		get = new HttpGet(url);
//		List<Cookie> cookieList = client.getCookieStore().getCookies();
//		System.out.println("Cookie list: ");
//		for (Cookie c : cookieList) {
//			System.out.println(c.toString());
//		}
//		System.out.println(s.getValue("session_name") + ":" + sessionid);
//		System.out.println(s.getValue("session_name") + "=" + sessionid);
//
//		try {
//			Thread nthread = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						System.out.println("Requesting friendlist");
//						r = client.execute(get);
//						status = r.getStatusLine().getStatusCode();
//						System.out.println("Stage -1");
//						if (status == 200) {
//							System.out
//									.println("Satus 200 obtained from Quipmate");
//							HttpEntity e = r.getEntity();
//							System.out.println("Stage 1");
//							data = EntityUtils.toString(e);
//							System.out.println(data);
//							friend_data = new JSONObject(data)
//									.getString("action");
//							friend = new JSONArray(friend_data);
//							System.out.println(data.toString());
//
//							String name_data = new JSONObject(data)
//									.getString("name");
//							name = new JSONObject(name_data);
//
//							String photo_data = new JSONObject(data)
//									.getString("pimage");
//							pimage = new JSONObject(photo_data);
//
//						} else {
//							System.out.println("Status is not 200 " + status);
//						}
//					} catch (UnknownHostException e) {
//						System.out
//								.println("Unable to reach Quipmate. Check Internet Connection !");
//						e.printStackTrace();
//					} catch (ClientProtocolException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					} catch (JSONException e) {
//						e.printStackTrace();
//					} catch (Exception e) {
//						System.out.println("Some Unknown exception ocurred");
//						e.printStackTrace();
//					}
//				}
//			});
//			nthread.start();
//			nthread.join();
//			llay = new LinearLayout(Friends.this);
//			llay.setOrientation(1);
//			llay.setClickable(true);
//			llay.setOnClickListener(this);
//			final int friend_count = friend.length();
//			final LinearLayout[] lhlay = new LinearLayout[friend_count];
//			System.out.println("Inside second tab setting content !");
//			System.out.println("Length of friend_list: " + friend_count);
//			final ImageView iv[] = new ImageView[friend_count];
//			final TextView tv[] = new TextView[friend_count];
//			Thread run = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					k = 0;
//					while (k < friend_count) {
//						iv[k] = new ImageView(Friends.this);
//						tv[k] = new TextView(Friends.this);
//						try {
//							image = photo_fetch(pimage.get(
//									(friend.get(k)).toString()).toString());
//							tv[k].setText(name.get((friend.get(k)).toString())
//									.toString());
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						iv[k].setBackgroundDrawable(image);
//						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//								40, 40);
//						layoutParams.setMargins(0, 0, 5, 0);
//						iv[k].setLayoutParams(layoutParams);
//						lhlay[k] = new LinearLayout(Friends.this);
//						lhlay[k].setClickable(true);
//						try {
//							lhlay[k].setTag(friend.get(k).toString());
//						} catch (JSONException e1) {
//							e1.printStackTrace();
//						}
//						lhlay[k].setOnClickListener(new OnClickListener() {
//							public void onClick(View v) {
//								System.out.println("This always works");
//								System.out.println(k);
//								try {
//									profileid = (String) v.getTag();
//									Intent intent = new Intent(
//											"com.example.quipmate.CHAT");
//									intent.putExtra("profileid", profileid);
//									intent.putExtra("name", name.get(profileid)
//											.toString());
//									startActivity(intent);
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//						});
//						lhlay[k].setPadding(5, 5, 5, 5);
//						lhlay[k].addView(iv[k]);
//						lhlay[k].addView(tv[k]);
//						ivOnline = new ImageView(Friends.this);
//						ivOnline.setBackgroundResource(R.drawable.online);
//						lhlay[k].addView(ivOnline);
//						lhlay[k].setGravity(Gravity.LEFT);
//						llay.addView(lhlay[k]);
//						k++;
//					}
//				}
//			});
//			run.start();
//			run.join();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		scroll.addView(llay);
	}

	private Drawable photo_fetch(String img) {
		Drawable image = null;
		try {
			System.out.println("Inside Run !");
			java.net.URL u = new java.net.URL(img);
			Object content = u.getContent();
			InputStream is = (InputStream) content;
			image = Drawable.createFromStream(is, "src");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Returning ffrom heew !");
		return image;
	}

	@Override
	public void onClick(View v) {
		System.out.println("Clicked Scroll");

	}
}
