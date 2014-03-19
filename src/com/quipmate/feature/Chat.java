package com.quipmate.feature;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
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

public class Chat extends Activity implements OnKeyListener {

	Bundle idata;
	String uname;
	TextView tvTitle;
	final String PROD_URL = "http://www.quipmate.com/ajax/write.php";
	final String DEV_URL = "http://50.57.224.99/ajax/write.php";
	String URL = PROD_URL;
	EditText etemail, etpassword;
	TextView tvstatus;
	Button blogin;
	ImageView logo;
	Drawable image;
	Session s;
	String sessionid, myprofileid, profileid, url, data, email, password,
			message_data;
	String img = "http://www.quipmate.com/upload_pic/2isk92li26mi0b7m02e4kuvq421353130910719754526674026619136382609593295_t.jpg",
			actionby, msg;
	HttpResponse r;
	HttpPost post;
	String website = "http://www.quipmate.com/";
	JSONObject last, name, pimage;
	JSONArray message;
	HttpClient client;
	HttpGet get;
	int k;
	int status;
	LinearLayout llay;
	LinearLayout lhlay;
	EditText etChatBox;
	ScrollView svFiendList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chat);
		Intent intent = getIntent();
		idata = intent.getExtras();
		profileid = (String) idata.get("profileid");
		uname = (String) idata.get("name");
		System.out.println(profileid + uname);
		svFiendList = (ScrollView) findViewById(R.id.svFiendList);
		llay = (LinearLayout) findViewById(R.id.llMsg);
		tvTitle = (TextView) findViewById(R.id.tvChatTitle);
		tvTitle.setText(uname);
		etChatBox = (EditText) findViewById(R.id.etChatBox);
		etChatBox.setOnKeyListener(this);
		s = new Session(getApplicationContext());
		sessionid = s.getValue("sessionid");
		myprofileid = s.getValue("profileid");

		client = AppProperties.appUserClient;
		url = URL + "?action=message_fetch&profileid=" + profileid + "&start=0";
		System.out.println(url);
		get = new HttpGet(url);

		Thread nthread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Requesting previous chat");
					r = client.execute(get);
					status = r.getStatusLine().getStatusCode();
					System.out.println("Stage -1");
					if (status == 200) {
						System.out.println("Satus 200 obtained from Quipmate");
						HttpEntity e = r.getEntity();
						System.out.println("Stage 1");
						data = EntityUtils.toString(e);
						String myprofileid_data = new JSONObject(data)
								.getString("myprofileid");
						System.out.println(myprofileid_data.toString());
						message_data = new JSONObject(data).getString("action");
						message = new JSONArray(message_data.toString());
						System.out.println(message.toString());
						String name_data = new JSONObject(data)
								.getString("name");
						name = new JSONObject(name_data);

						String photo_data = new JSONObject(data)
								.getString("pimage");
						pimage = new JSONObject(photo_data);

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
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("Some Unknown exception occured");
					e.printStackTrace();
				}
			}
		});
		nthread.start();
		try {
			nthread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final int message_count = message.length();
		System.out.println("Length of message_list: " + message_count);
		final LinearLayout[] lhlay = new LinearLayout[message_count];
		final ImageView iv[] = new ImageView[message_count];
		final TextView tv[] = new TextView[message_count];
		Thread run = new Thread(new Runnable() {
			@Override
			public void run() {
				k = message_count - 1;
				while (k >= 0) {
					iv[k] = new ImageView(Chat.this);
					tv[k] = new TextView(Chat.this);
					try {

						System.out.println(message.get(k));
						String actionby = new JSONObject(message.get(k)
								.toString()).getString("actionby");
						String msg = new JSONObject(message.get(k).toString())
								.getString("message");
						image = photo_fetch(pimage.get(actionby).toString());
						tv[k].setText(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					iv[k].setBackgroundDrawable(image);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							40, 40);
					layoutParams.setMargins(0, 0, 5, 0);
					iv[k].setLayoutParams(layoutParams);
					lhlay[k] = new LinearLayout(Chat.this);
					lhlay[k].setPadding(5, 5, 5, 5);
					lhlay[k].addView(iv[k]);
					lhlay[k].addView(tv[k]);
					llay.addView(lhlay[k]);
					k--;
				}
			}
		});
		run.start();
		try {
			run.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Boolean typing = true;
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
			if (v.getId() == R.id.etChatBox) {
				String msg = etChatBox.getText().toString();
				etChatBox.setText("");
				sendMesage(msg);
				typing = true;
				return true;
			}
		} else {

			if (typing == true && v.getId() == R.id.etChatBox) {
				typing = false;
				url = website + "chat/typing_new";
				System.out.println(url);
				post = new HttpPost(url);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				try {
					nameValuePairs.add(new BasicNameValuePair("profileid",
							myprofileid));
					nameValuePairs.add(new BasicNameValuePair("userid",
							profileid));
					nameValuePairs.add(new BasicNameValuePair("name", name.get(
							myprofileid).toString()));
					post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Thread send_typing_msg = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							r = client.execute(post);
							status = r.getStatusLine().getStatusCode();
							if (status == 200) {
								System.out
										.println("Satus 200 obtained from Quipmate");
								r.getEntity().consumeContent();
							} else {
								System.out.println("Status is not 200 "
										+ status);
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
							System.out
									.println("Some Unknown exception occured");
							e.printStackTrace();
						}
					}
				});
				send_typing_msg.start();
				try {
					send_typing_msg.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	public void sendMesage(String msg) {
		System.out.println(msg);
		url = website + "chat/chat_new";
		System.out.println(url);
		post = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		try {
			nameValuePairs
					.add(new BasicNameValuePair("profileid", myprofileid));
			nameValuePairs.add(new BasicNameValuePair("userid", profileid));
			nameValuePairs.add(new BasicNameValuePair("message", msg));
			nameValuePairs.add(new BasicNameValuePair("name", name.get(
					myprofileid).toString()));
			nameValuePairs.add(new BasicNameValuePair("photo", pimage.get(
					myprofileid).toString()));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Thread send_msg = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Requesting previous chat");
					r = client.execute(post);
					status = r.getStatusLine().getStatusCode();
					System.out.println("Stage -1");
					if (status == 200) {
						System.out.println("Satus 200 obtained from Quipmate");
						HttpEntity e = r.getEntity();
						System.out.println("Stage 1");
						data = EntityUtils.toString(e);
						System.out.println(data.toString());
						String sentby = new JSONObject(data)
								.getString("sentby");
						ImageView iv = new ImageView(Chat.this);
						TextView tv = new TextView(Chat.this);
						tv.setText(new JSONObject(data).getString("message"));
						iv.setBackgroundDrawable(photo_fetch(pimage.get(sentby)
								.toString()));
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								40, 40);
						layoutParams.setMargins(0, 0, 5, 0);
						iv.setLayoutParams(layoutParams);
						lhlay = new LinearLayout(Chat.this);
						lhlay.setPadding(5, 5, 5, 5);
						lhlay.addView(iv);
						lhlay.addView(tv);
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
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("Some Unknown exception occured");
					e.printStackTrace();
				}
			}
		});
		send_msg.start();
		try {
			send_msg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		llay.addView(lhlay);
		svFiendList.post(new Runnable() {
			@Override
			public void run() {
				svFiendList.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

}
