package com.quipmate.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

import com.example.quipmate.R;
import com.quipmate.constants.AppProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonMethods.
 */
public class CommonMethods {
	
	/**
	 * Gets the login url.
	 *
	 * @param password the password
	 * @param email the email
	 * @return the login url
	 */
	public static String getLoginUrl(String password, String email) {
		String URL = AppProperties.URL + "?action=login&password=" + password
				+ "&email=" + email;
		return URL;
	}

	/**
	 * Show info.
	 * 
	 * @param context
	 *            the context
	 * @param displayMessage
	 *            the display message
	 * @return the alert dialog. builder
	 */
	public static AlertDialog.Builder ShowInfo(Context context,
			String displayMessage) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(displayMessage)
				.setCancelable(false)
				.setPositiveButton(R.string.OK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Do nothing
							}
						});
		return builder;
	}

	/**
	 * Display toast.
	 * 
	 * @param context
	 *            the context
	 * @param text
	 *            the text
	 * @param duration
	 *            the duration
	 */
	public static void displayToast(Context context, String text, int duration) {
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	/**
	 * Display toast.
	 * 
	 * @param context
	 *            the context
	 * @param text
	 *            the text
	 */
	public static void displayToast(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	

}
