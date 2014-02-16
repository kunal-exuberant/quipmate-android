package com.quipmate.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.quipmate.R;
import com.quipmate.constants.AppProperties;
import com.quipmate.utils.CommonMethods;
import com.quipmate.utils.JSONTask;
import com.quipmate.utils.NetworkHelper;

public class Login extends Activity implements OnClickListener {

	private ProgressBar progressBar;
	EditText etemail, etpassword;
	private Button blogin;
	Session session;
	String email, password;
	private JSONObject last;
	private boolean isPressed = false;

	private JSONArray result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		initView();

		session = new Session(Login.this);
		if (session.hasKey(AppProperties.PARAM_EMAIL)) {
			etemail.setText(session.getValue(AppProperties.PARAM_EMAIL));
		}
		if (session.hasKey(AppProperties.PARAM_PASSWORD)) {
			etpassword.setText(session.getValue(AppProperties.PARAM_PASSWORD));
		}

	}

	private void initView() {
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		progressBar.setVisibility(View.GONE);
		etemail = (EditText) findViewById(R.id.etemail);
		etpassword = (EditText) findViewById(R.id.etpassword);
		blogin = (Button) findViewById(R.id.blogin);
		blogin.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		System.out.println("Back button pressed ! Leaving app !");
	}

	@Override
	public void onClick(View v) {
		if (isPressed) {
			return;
		}
		isPressed = true;
		if (v.getId() == R.id.blogin) {
			attemptLogin();
		}
		isPressed = false;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	private void attemptLogin() {
		progressBar.setVisibility(View.VISIBLE);

		System.out.println("Trying to log you in");
		email = etemail.getText().toString();
		password = etpassword.getText().toString();

		session.setValue(AppProperties.PARAM_EMAIL, email);
		session.setValue(AppProperties.PARAM_PASSWORD, password);
		session.commit();

		try {

			if (NetworkHelper.checkNetworkConnection(Login.this)) {
				List<NameValuePair> apiParams = new ArrayList<NameValuePair>();
				apiParams.add(new BasicNameValuePair(AppProperties.ACTION,
						AppProperties.LOGIN));
				apiParams.add(new BasicNameValuePair(
						AppProperties.PARAM_PASSWORD, password));
				apiParams.add(new BasicNameValuePair(AppProperties.PARAM_EMAIL,
						email));
				result = (new JSONTask(AppProperties.URL,
						AppProperties.METHOD_GET, apiParams)).execute().get();
				last = result.getJSONObject(0);
				if (last != null) {

					if (last.has(AppProperties.ACK)
							&& last.getString(AppProperties.ACK).equals(
									AppProperties.ACK_CODE)) {
						session.setValue(AppProperties.SESSION_ID,
								last.getString(AppProperties.SESSION_ID));
						session.setValue(AppProperties.PROFILE_ID,
								last.getString(AppProperties.MY_PROFILE_ID));
						session.setValue(AppProperties.SESSION_NAME,
								last.getString(AppProperties.SESSION_NAME));
						if (session.commit()) {

							Intent intent = new Intent(this, Friends.class);
							startActivity(intent);
							finish();

						} else {
							System.out
									.println("Some problem in Sigin. Please try again.");
						}
					} else if (last.has(getString(R.string.error))) {
						progressBar.setVisibility(View.INVISIBLE);
						JSONObject error = last
								.getJSONObject(getString(R.string.error));
						if (error.getString(getString(R.string.code)).equals(
								AppProperties.WRONG_CREDENTIAL_CODE)) {
							CommonMethods.ShowInfo(Login.this,
									getString(R.string.invalid_credential))
									.show();
						}
					}
				}
			} else {
				progressBar.setVisibility(View.INVISIBLE);
				CommonMethods.ShowInfo(Login.this,
						getString(R.string.network_error)).show();
			}
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			// should not happen
			e.printStackTrace();
		}

	}

}
