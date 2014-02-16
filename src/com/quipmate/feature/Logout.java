package com.quipmate.feature;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Logout extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Logout Class startred");
		Session sess = new Session(getApplicationContext());
		System.out.println(sess.getValue("sessionid")+" "+sess.getValue("profileid") );
		sess.delValue("sessionid");
		sess.delValue("profileid");
		sess.commit();
		System.out.println(sess.getValue("sessionid")+" "+sess.getValue("profileid") );
		Intent i = new Intent(this, Login.class);
		startActivity(i);
		finish();
	}
}
