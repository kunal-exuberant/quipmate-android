package com.quipmate.feature;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.quipmate.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

public class Crush extends Activity implements OnClickListener{

	EditText etemail;
	Button badd;
	TextView tvstatus;
	ImageView ivcrush;
	Drawable image;
	String email,data,sessionid, profileid, URL = "http://www.quipmate.com/api/login.php", url, list_data, match_data;
	JSONArray rules, list, match;
	JSONObject last, name, photo;
	HttpClient client;
	HttpResponse r;
	HttpGet get;
	int status;
	Session s;
	TabHost th;
	int ITALIC = 2, BOLD_ITALIC = 3;  	/* 0 = Normal, 1 =  Bold, 2 =  Italic, 3 = Bold Italic */
	int QUES_STYLE = BOLD_ITALIC, ANS_STYLE = ITALIC;
	int Q_ANS_FONT_SIZE = 13;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.crush);
		
		s = new Session(getApplicationContext());
		sessionid = s.getValue("sessionid");
		profileid = s.getValue("profileid");
		

		th = (TabHost)findViewById(R.id.tabhost);
		th.setup();
		TabHost.TabSpec spec = th.newTabSpec("start");
		spec.setContent(R.id.tstart);
		spec.setIndicator("Add Crush");
		
		TabHost.TabSpec spec_list = th.newTabSpec("crush_list");
		client = new DefaultHttpClient();
		url = URL;
		url = url + "?action=crush_list"+"&sessionid="+sessionid+"&profileid="+profileid+"&whoru=android";
		BasicCookieStore store = new BasicCookieStore();
		System.out.println(url);
		
		get = new HttpGet(url);
		try {
			Thread clthread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						r = client.execute(get);
						status = r.getStatusLine().getStatusCode();
						if(status == 200){
							HttpEntity e = r.getEntity();
							data = EntityUtils.toString(e);
							list_data = new JSONObject(data).getString("list");
							list = new JSONArray(list_data);
							
							match_data = new JSONObject(data).getString("match");
							match = new JSONArray(match_data);
							
							String name_data = new JSONObject(data).getString("name");
							name = new JSONObject(name_data);
							System.out.println(name.toString());
							System.out.println(name.get("1000000122"));
							
							String photo_data = new JSONObject(data).getString("photo");
							photo = new JSONObject(photo_data);
							System.out.println(photo.toString());
							System.out.println(photo.get("1000000122"));
							
							
							
							int len_list = list.length();
							int len_match = match.length();
							int i = 0;
							while(i < len_list){
								System.out.print(list.get(i).toString() + ": ");								
								System.out.println(name.get((list.get(i)).toString()));
								i++;
							}
							i = 0;
							while(i < len_match){
								
								System.out.println(match.get(i));
								System.out.println(name.get((match.get(i)).toString()));
								i++;
							}
						}
						else{
							System.out.println("Status is not 200 "+status);
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			clthread.start();
			clthread.join();
		}catch(Exception e){
			e.printStackTrace();
		}
		
			spec_list.setContent(new TabHost.TabContentFactory() {
				public View createTabContent(String tag) {
					final int match_len = match.length();
					final int list_len = list.length();
					int len=0, i=0, j=0;
					len = match_len + list.length();
					ScrollView scroll = new ScrollView(Crush.this);
					scroll.setPadding(25, 25, 25, 25);
					final LinearLayout llay = new LinearLayout(Crush.this);
					llay.setOrientation(1);
					final LinearLayout lhlay[] = new LinearLayout[match_len+list_len];
					System.out.println("Inside second tab setting content !");
					System.out.println("Length of match_list: "+len);
					final ImageView iv[] = new ImageView[match_len+list_len];
					Thread run = new Thread(new Runnable() {
						@Override
						public void run() {
							int k = 0;
							while(k<match_len){
								iv[k] = new ImageView(Crush.this);
								try {
									image = photo_fetch(photo.get((match.get(k)).toString()).toString());
								} catch (JSONException e) {
									e.printStackTrace();
								} 
								iv[k].setBackgroundDrawable(image);
								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
								iv[k].setLayoutParams(layoutParams);
								k++;
							}
							int l=0;
							while(l<list_len){
								iv[k+l] = new ImageView(Crush.this);
								try {
									image = photo_fetch(photo.get((list.get(l)).toString()).toString());
								} catch (JSONException e) {
									e.printStackTrace();
								} 
								iv[k+l].setBackgroundDrawable(image);
								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
								iv[k+l].setLayoutParams(layoutParams);
								l++;
							}
						}
					});
					run.start();
					try {
						run.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					TextView tv[] = new TextView[match_len+list_len];
					try{
					while(i<match_len){
						tv[i] = new TextView(Crush.this);
						tv[i].setText(name.get((match.get(i)).toString()).toString());
						tv[i].setTextSize(Q_ANS_FONT_SIZE);
						if(i%2==0)
							tv[i].setTypeface(null, QUES_STYLE);
						else
							tv[i].setTypeface(null, ANS_STYLE);
						lhlay[i] = new LinearLayout(Crush.this);
						lhlay[i].setPadding(5, 5, 5, 5);
						lhlay[i].addView(iv[i]);
						lhlay[i].addView(tv[i]);
						llay.addView(lhlay[i]);
						i++;
					}	
					while(j<list_len){
						tv[i+j] = new TextView(Crush.this);
						tv[i+j].setText(name.get((list.get(j)).toString()).toString());
						tv[i+j].setTextSize(Q_ANS_FONT_SIZE);
						if((i+j)%2==0)
							tv[i+j].setTypeface(null, QUES_STYLE);
						else
							tv[i+j].setTypeface(null, ANS_STYLE);
						lhlay[i+j] = new LinearLayout(Crush.this);
						lhlay[i+j].setPadding(5, 5, 5, 5);
						lhlay[i+j].addView(iv[i+j]);
						lhlay[i+j].addView(tv[i+j]);
						llay.addView(lhlay[i+j]);
						j++;
					}	
					}catch (JSONException e) {
							e.printStackTrace();
					}
					scroll.addView(llay);
					return (scroll);
				}
			});
		
		spec_list.setIndicator("Crush List");
		
		
		TabHost.TabSpec spec_rules = th.newTabSpec("rules");
		client = new DefaultHttpClient();
		url = URL;
		url = url + "?action=crush_rules"+"&sessionid="+sessionid+"&profileid="+profileid+"&whoru=android"; 
		System.out.println(url);
		get = new HttpGet(url);
		try {
			Thread rthread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						r = client.execute(get);
						status = r.getStatusLine().getStatusCode();
						if(status == 200){
							HttpEntity e = r.getEntity();
							data = EntityUtils.toString(e);
							last = new JSONObject(data);
							System.out.println(last.toString());
							System.out.println(data.toString());
							data = last.getString("rules");
							rules = new JSONArray(data);
							int len = rules.length();
							int i = 0;
							while(i < len){
								
								System.out.println(rules.get(i));
								i++;
							}
						}
						else{
							System.out.println("Status is not 200 "+status);
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			rthread.start();
			rthread.join();
		
		System.out.println("Third Tab setting content !");
		spec_rules.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				ScrollView scroll = new ScrollView(Crush.this);
				scroll.setPadding(25, 25, 25, 25);
				LinearLayout llay = new LinearLayout(Crush.this);
				llay.setOrientation(1);
				System.out.println("Inside thrid tab setting content !");
				int len = rules.length(),i=0;
				TextView tv[] = new TextView[len];
				while(i<len){
					tv[i] = new TextView(Crush.this);
					try {
						tv[i].setText(rules.get(i).toString());
						tv[i].setTextSize(Q_ANS_FONT_SIZE);
						if(i%2==0)
							tv[i].setTypeface(null, QUES_STYLE);
						else
							tv[i].setTypeface(null, ANS_STYLE);
						llay.addView(tv[i]);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					i++;
				}
				scroll.addView(llay);
				return (scroll);
			}
		});
		spec_rules.setIndicator("Rules");
		th.addTab(spec);
		th.addTab(spec_list);
		th.addTab(spec_rules);
		th.setCurrentTab(1);
		
		System.out.println("Srtarting Crush !");
		s = new Session(getApplicationContext());
		sessionid = s.getValue("sessionid");
		profileid = s.getValue("profileid");
		if(sessionid.equals(null) || profileid.equals(null)){
			System.out.println("Please login to use this app");
			Intent intent1 = new Intent("android.intent.action.MAIN");
			startActivity(intent1);
		}
		System.out.println("After session check !");
		etemail = (EditText)findViewById(R.id.etemail);
		badd = (Button)findViewById(R.id.badd);
		tvstatus = (TextView)findViewById(R.id.tvstatus);
		badd.setOnClickListener(this);
	}catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.out.println("Back Button was pressed");
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("Processing of this class was resumed");
		
	}


	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("Processing of this class was RESTARTRD");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.logout:
			Intent intent = new Intent("com.example.quipmate.LOGOUT");
			startActivity(intent);
			finish();
			break;
		default:
			System.out.println("Unknown Menu Item Selected !");
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.badd){
			System.out.println("Trying to add your crush");
			try {
				email = URLEncoder.encode(etemail.getText().toString(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace(); 
			}
			client = new DefaultHttpClient();
			url = URL;
			url = url + "?action=add_crush"+"&sessionid="+sessionid+"&profileid="+profileid+"&email="+email+"&whoru=android"; 
			System.out.println(url);
			get = new HttpGet(url);
			try {
				Thread nthread = new Thread(new Runnable() {
					
					
					@Override
					public void run() {
						try {
							r = client.execute(get);
							status = r.getStatusLine().getStatusCode();
							if(status == 200){
								HttpEntity e = r.getEntity();
								data = EntityUtils.toString(e);
								last = new JSONObject(data);
								System.out.println(last.toString());
								System.out.println(data.toString());
							}
							else{
								System.out.println("Status is not 200 "+status);
							}

						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				nthread.start();
				nthread.join(); 
				tvstatus.setText(last.getString("msg"));
				if(last.getString("status").equals("Success")){
					System.out.println("Congratulations ! Crush added Successfully !");
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
	private Drawable photo_fetch(String img){
		Drawable image = null;
			try {
				System.out.println("Inside Run !");
				java.net.URL u = new java.net.URL(img);
				Object content = u.getContent();
				InputStream is = (InputStream)content;
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
}
