package com.macrew.postix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.macrew.alertUtils.AlertsUtils;
import com.macrew.alertUtils.NetConnection;
import com.macrew.imageloader.ImageLoader;

import com.macrew.services.WebServices;
import com.macrew.postix.R;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.util.Log;

import android.view.MotionEvent;

import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import android.view.WindowManager.LayoutParams;

import android.view.View.OnClickListener;

import android.view.View.OnTouchListener;

import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import android.widget.TextView;

import android.widget.VideoView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class thirdScreen extends Activity {

	Button new_search, last_search, offer, appointment, full_presentation,
			close, messages;

	VideoView videoView1;
	int screen_width, screen_height;
	RelativeLayout RelativeLayout2, RR1;
	MediaController mc;
	Dialog dialog;
	String msg;
	LinearLayout linearlayout1, LL1;
	SharedPreferences sharedpreferences;
	TextView textView1, Title;
	String closed, closed1;
	Context context;
	String first_name, last_name, phoneNo, emailId, bid_amount, message_text;
	String id, title, img_url;
	offerTask offerObj;
	appointmentTask appointmentObj;
	Boolean isConnected;
	getVideosTask getVideosObj;
	int width, height;
	int videosSize;
	Boolean isClicked = false;
	ImageView img;
	List<ImageView> allImgs;
	Boolean isAlreadyClicked = false;
	Boolean isPortraitOrLandscape = false;
	int selectedPosition;
	Boolean isClosedClick = false;

	ArrayList<String> videos = new ArrayList<String>();
	public static ArrayList<HashMap<String, String>> videoList = new ArrayList<HashMap<String, String>>();

	int windowwidth;
	int windowheight;
	private android.widget.RelativeLayout.LayoutParams layoutParams2;
	private CustomHorizontalScrollView horizontalScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		  
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
		setContentView(R.layout.video);
		final Dialog dialog = new Dialog(this, R.style.full_screen_dialog);

		new_search = (Button) findViewById(R.id.new_search);
		last_search = (Button) findViewById(R.id.last_search);
		LL1 = (LinearLayout) findViewById(R.id.LL1);
		offer = (Button) findViewById(R.id.offer);
		appointment = (Button) findViewById(R.id.appointment);
		full_presentation = (Button) findViewById(R.id.full_presentation);
		RR1 = (RelativeLayout) findViewById(R.id.RR1);
		RelativeLayout2 = (RelativeLayout) findViewById(R.id.RelativeLayout2);
		linearlayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
		textView1 = (TextView) findViewById(R.id.textView1);
		
		close = (Button) findViewById(R.id.close);
		Title = (TextView) findViewById(R.id.title);
		Log.i("IDIDIDIDIIDIDIDIDIDIIDID","==="+Title);
		messages = (Button) findViewById(R.id.messages);

		videoView1 = (VideoView) findViewById(R.id.videoView1);

		videoList.clear();
		
		
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		id = sharedpreferences.getString("VIDEO_ID", "");
		Log.i("id==========================", "" + id);
		title = sharedpreferences.getString("VIDEO_TITLE", "");
		Log.i("title==========================", "" + title);
		img_url = sharedpreferences.getString("URL", "");
		Log.i("url==========================", "" + img_url);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;

		
		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		windowheight = getWindowManager().getDefaultDisplay().getHeight();
		getVideos();
		Log.i("title******","==="+title);
		Log.i("title id ******","==="+Title);
		Title.setText(title);

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				videoView1.stopPlayback();
				Log.i("close", "clicked");
				closed = "yes";
				isClosedClick = true;
				//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				RelativeLayout2.setVisibility(View.INVISIBLE);

				new_search.setVisibility(View.VISIBLE);
				last_search.setVisibility(View.VISIBLE);

				offer.setVisibility(View.VISIBLE);
				LL1.setVisibility(View.VISIBLE);
				appointment.setVisibility(View.VISIBLE);
				messages.setVisibility(View.VISIBLE);
				full_presentation.setVisibility(View.VISIBLE);

				textView1.setVisibility(View.VISIBLE);
				

				//finish();
			//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				Intent launchNext = new Intent(getApplicationContext(),
						thirdScreen.class);
				launchNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				startActivity(launchNext);

			}
		});

		RelativeLayout2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("windowwidth & windowheight",
						"==" + RelativeLayout2.getWidth() + "=="
								+ RelativeLayout2.getHeight());
				layoutParams2 = (RelativeLayout.LayoutParams) RelativeLayout2
						.getLayoutParams();

				/*
				 * RelativeLayout.LayoutParams layoutParams2 = new
				 * RelativeLayout.LayoutParams(RelativeLayout2.getWidth(),
				 * RelativeLayout2.getHeight());
				 */
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:

					int x_cord = (int) event.getRawX();
					int y_cord = (int) event.getRawY();
					if (x_cord > windowwidth) {
						x_cord = windowwidth;
					}
					if (y_cord > windowheight) {
						y_cord = windowheight;
					}
					//layoutParams2.leftMargin = x_cord - 1;
					///layoutParams2.rightMargin = x_cord - 1;
					//layoutParams2.bottomMargin = y_cord - 1;
					//layoutParams2.topMargin = y_cord - 1;

					layoutParams2.leftMargin = x_cord - 5;
					 layoutParams2.rightMargin = x_cord - 5;
					 layoutParams2.bottomMargin = y_cord - 5;
					 layoutParams2.topMargin = y_cord - 5;

					RelativeLayout2.setLayoutParams(layoutParams2);
					break;
				default:
					break;
				}
				return true;
			}
		});

		mc = new MediaController(this);

		offer.setOnClickListener(new OnClickListener() {

			Button cancel, send;
			EditText firstName, lastName, email, phone, bid, message;
			TextView textView1;

			@Override
			public void onClick(View v) {

				dialog.setCancelable(true);

				if (isClicked == false) {
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				}

				isClicked = true;

				dialog.setContentView(R.layout.offer);

				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog.getWindow().setBackgroundDrawable(d);

				cancel = (Button) dialog.findViewById(R.id.cancel);
				send = (Button) dialog.findViewById(R.id.send);
				firstName = (EditText) dialog.findViewById(R.id.firstName);
				lastName = (EditText) dialog.findViewById(R.id.lastName);
				email = (EditText) dialog.findViewById(R.id.email);
				phone = (EditText) dialog.findViewById(R.id.phone);
				bid = (EditText) dialog.findViewById(R.id.bid);
				message = (EditText) dialog.findViewById(R.id.message);
				textView1 = (TextView) dialog.findViewById(R.id.textView1);

				message.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);

							lp.setMargins(0, 60, 0, 0);
							textView1.setLayoutParams(lp);
							textView1.setGravity(Gravity.CENTER_HORIZONTAL);
						} else {
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);

							lp.setMargins(0, 15, 0, 0);
							textView1.setLayoutParams(lp);
							textView1.setGravity(Gravity.CENTER_HORIZONTAL);

						}
					}
				});

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				send.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						first_name = firstName.getText().toString();
						last_name = lastName.getText().toString();
						emailId = email.getText().toString();
						phoneNo = phone.getText().toString();
						bid_amount = bid.getText().toString();
						message_text = message.getText().toString();

						if (first_name.equals("") || first_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"First Name is required.");
						} else if (last_name.equals("")
								|| last_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Last Name is required.");
						} else if (emailId.equals("") || emailId.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Email is required.");
						} else if (bid_amount.equals("")
								|| bid_amount.equals("")) {
							new AlertsUtils(thirdScreen.this,
									" Bid Amount must be zero or greater.");
						}

						else if (emailId
								.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
								&& emailId.length() > 0) {
							dialog.dismiss();
							presentationTask();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									"This email address is invalid.");
						}
					}

					private void presentationTask() {
						offerObj = new offerTask();

						isConnected = NetConnection
								.checkInternetConnectionn(thirdScreen.this);
						if (isConnected == true) {
							offerObj.execute();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									AlertsUtils.NO_INTERNET_CONNECTION);
						}

					}
				});
				dialog.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;
				dialog.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

				// dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

				dialog.show();

			}
		});

		appointment.setOnClickListener(new OnClickListener() {
			Button app_cancel, app_send;
			EditText app_firstName, app_lastName, app_email, app_phone,
					app_message;
			TextView textView1;

			@Override
			public void onClick(View v) {

				dialog.setCancelable(true);

				if (isClicked == false) {
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				}

				isClicked = true;

				dialog.setContentView(R.layout.appointment);
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog.getWindow().setBackgroundDrawable(d);

				app_cancel = (Button) dialog.findViewById(R.id.app_cancel);
				app_send = (Button) dialog.findViewById(R.id.app_send);
				app_firstName = (EditText) dialog
						.findViewById(R.id.app_firstName);
				app_lastName = (EditText) dialog
						.findViewById(R.id.app_lastName);
				app_email = (EditText) dialog.findViewById(R.id.app_email);
				app_phone = (EditText) dialog.findViewById(R.id.app_phone);
				app_message = (EditText) dialog.findViewById(R.id.app_message);
				textView1 = (TextView) dialog.findViewById(R.id.textView1);

				app_message
						.setOnFocusChangeListener(new OnFocusChangeListener() {

							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);

									lp.setMargins(0, 60, 0, 0);
									textView1.setLayoutParams(lp);
									textView1
											.setGravity(Gravity.CENTER_HORIZONTAL);
								} else {
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);

									lp.setMargins(0, 15, 0, 0);
									textView1.setLayoutParams(lp);
									textView1
											.setGravity(Gravity.CENTER_HORIZONTAL);

								}
							}
						});

				app_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				app_send.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						first_name = app_firstName.getText().toString();
						last_name = app_lastName.getText().toString();
						emailId = app_email.getText().toString();
						phoneNo = app_phone.getText().toString();
						message_text = app_message.getText().toString();

						if (first_name.equals("") || first_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"First Name is required.");
						} else if (last_name.equals("")
								|| last_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Last Name is required.");
						} else if (emailId.equals("") || emailId.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Email is required.");
						}

						else if (emailId
								.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
								&& emailId.length() > 0) {
							dialog.dismiss();
							presentationTask();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									"This email address is invalid.");
						}
					}

					private void presentationTask() {
						appointmentObj = new appointmentTask();

						isConnected = NetConnection
								.checkInternetConnectionn(thirdScreen.this);
						if (isConnected == true) {
							appointmentObj.execute();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									AlertsUtils.NO_INTERNET_CONNECTION);
						}

					}
				});
				dialog.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;
				dialog.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				dialog.show();
			}
		});

		messages.setOnClickListener(new OnClickListener() {

			Button app_cancel, app_send;
			EditText app_firstName, app_lastName, app_email, app_phone,
					app_message;
			TextView textView1;

			@Override
			public void onClick(View v) {

				dialog.setCancelable(true);

				if (isClicked == false) {
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				}

				isClicked = true;

				dialog.setContentView(R.layout.messages);
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog.getWindow().setBackgroundDrawable(d);

				app_cancel = (Button) dialog.findViewById(R.id.app_cancel);
				app_send = (Button) dialog.findViewById(R.id.app_send);
				app_firstName = (EditText) dialog
						.findViewById(R.id.app_firstName);
				app_lastName = (EditText) dialog
						.findViewById(R.id.app_lastName);
				app_email = (EditText) dialog.findViewById(R.id.app_email);
				app_phone = (EditText) dialog.findViewById(R.id.app_phone);
				app_message = (EditText) dialog.findViewById(R.id.app_message);
				textView1 = (TextView) dialog.findViewById(R.id.textView1);

				app_message
						.setOnFocusChangeListener(new OnFocusChangeListener() {

							public void onFocusChange(View v, boolean hasFocus) {
								if (!hasFocus) {
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);

									lp.setMargins(0, 60, 0, 0);
									textView1.setLayoutParams(lp);
									textView1
											.setGravity(Gravity.CENTER_HORIZONTAL);
								} else {
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);

									lp.setMargins(0, 15, 0, 0);
									textView1.setLayoutParams(lp);
									textView1
											.setGravity(Gravity.CENTER_HORIZONTAL);

								}
							}
						});

				app_cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				app_send.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						first_name = app_firstName.getText().toString();
						last_name = app_lastName.getText().toString();
						emailId = app_email.getText().toString();
						phoneNo = app_phone.getText().toString();
						message_text = app_message.getText().toString();

						if (first_name.equals("") || first_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"First Name is required.");
						} else if (last_name.equals("")
								|| last_name.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Last Name is required.");
						} else if (emailId.equals("") || emailId.equals(" ")) {
							new AlertsUtils(thirdScreen.this,
									"Email is required.");
						}

						else if (emailId
								.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
								&& emailId.length() > 0) {
							dialog.dismiss();
							presentationTask();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									"This email address is invalid.");
						}
					}

					private void presentationTask() {
						appointmentObj = new appointmentTask();

						isConnected = NetConnection
								.checkInternetConnectionn(thirdScreen.this);
						if (isConnected == true) {
							appointmentObj.execute();
						}

						else {
							new AlertsUtils(thirdScreen.this,
									AlertsUtils.NO_INTERNET_CONNECTION);
						}

					}
				});
				dialog.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;
				dialog.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				dialog.show();

			}
		});

		new_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.remove("CATEGORY_SELECTED");
				editor.remove("SUBCATEGORY_SELECTED");
				editor.remove("VIDEO_ID");
				editor.remove("URL");
				editor.remove("VIDEO_TITLE");
				editor.remove("CATEGORY_ID");
				editor.remove("SUBCATEGORY_ID");

				editor.commit();
				finish();

				Intent i = new Intent(thirdScreen.this, country.class);
				startActivity(i);

			}
		});

		last_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = sharedpreferences.edit();
				// editor.remove("CATEGORY_SELECTED");
				// editor.remove("SUBCATEGORY_SELECTED");
				editor.remove("VIDEO_ID");
				editor.remove("VIDEO_TITLE");
				// editor.remove("CATEGORY_ID");
				editor.remove("URL");
				// editor.remove("SUBCATEGORY_ID");

				editor.commit();
				finish();

			}
		});

		full_presentation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String url = "http://postix.net/view-presentation/" + id;
				Log.i("url for full presentation==", "==" + url);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}
		});

	}

	private void getVideos() {
		getVideosObj = new getVideosTask();

		isConnected = NetConnection.checkInternetConnectionn(thirdScreen.this);
		if (isConnected == true) {
			getVideosObj.execute();
		}

		else {
			new AlertsUtils(thirdScreen.this,
					AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.i("Landscape==", "==Landscape");
			isPortraitOrLandscape = true;
			new_search.setVisibility(View.INVISIBLE);
			last_search.setVisibility(View.INVISIBLE);
			LL1.setVisibility(View.INVISIBLE);
			offer.setVisibility(View.INVISIBLE);
			messages.setVisibility(View.INVISIBLE);
			appointment.setVisibility(View.INVISIBLE);
			full_presentation.setVisibility(View.INVISIBLE);
			close.setVisibility(View.VISIBLE);
			// close.setEnabled(false);
			Title.setVisibility(View.INVISIBLE);
			close.setEnabled(true);
			textView1.setVisibility(View.INVISIBLE);
			

			RelativeLayout2.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					LL1.setVisibility(View.INVISIBLE);

					return true;

				}
			});

			RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);

			RelativeLayout2.setLayoutParams(layout_description);
			RelativeLayout2.setGravity(Gravity.CENTER);

			RR1.setPadding(0, 0, 0, 0);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			isPortraitOrLandscape = true;
			Log.i("PORTRAIT==", "==PORTRAIT");

			new_search.setVisibility(View.INVISIBLE);
			last_search.setVisibility(View.INVISIBLE);
			textView1.setVisibility(View.INVISIBLE);
		
			Title.setVisibility(View.INVISIBLE);
			offer.setVisibility(View.INVISIBLE);
			messages.setVisibility(View.INVISIBLE);
			appointment.setVisibility(View.INVISIBLE);
			full_presentation.setVisibility(View.INVISIBLE);

			close.setVisibility(View.VISIBLE);
			close.setEnabled(true);
			RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);

			RelativeLayout2.setLayoutParams(layout_description);
			RR1.setPadding(0, 0, 0, 0);
			RelativeLayout2.setGravity(Gravity.CENTER);

		}
	}

	/**** <><><><<><><><><> ASYNC TASK TO MAKE AN OFFER<><><><><><><><> ***********/
	public class offerTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(thirdScreen.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.offer);

			String result = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("OFFER URL==", "==" + WebServices.offer
						+ "bid[presentation_id]=" + id + "&"
						+ "bid[first_name]=" + first_name + "&"
						+ "bid[last_name]=" + last_name + "&" + "bid[email]="
						+ emailId + "&" + "bid[phone]=" + phoneNo + "&"
						+ "bid[amount]=" + bid_amount + "&" + "bid[note]="
						+ message_text + "&" + "bid[api]=" + "1");
				nameValuePairs.add(new BasicNameValuePair(
						"bid[presentation_id]", id));
				nameValuePairs.add(new BasicNameValuePair("bid[first_name]",
						first_name));
				nameValuePairs.add(new BasicNameValuePair("bid[last_name]",
						last_name));
				nameValuePairs
						.add(new BasicNameValuePair("bid[email]", emailId));
				nameValuePairs
						.add(new BasicNameValuePair("bid[phone]", phoneNo));
				nameValuePairs.add(new BasicNameValuePair("bid[amount]",
						bid_amount));
				nameValuePairs.add(new BasicNameValuePair("bid[note]",
						message_text));
				nameValuePairs.add(new BasicNameValuePair("bid[api]", "1"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("offer status==", "STATUS OK");

					result = out.toString();
					Log.i("offer result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "***OFFER*** result==" + result);
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(result);
				String webServiceResponse = jsonObj.getString("status");

				if (webServiceResponse.equals("SUCCESS")) {
					new AlertsUtils(thirdScreen.this, AlertsUtils.SUCCESS);

				}

				else {
					new AlertsUtils(thirdScreen.this, AlertsUtils.PRIVATE_MODE);
				}

			}

			catch (JSONException e) {
				Log.i("JSONException==", "=" + e);
			}

			catch (Exception e) {
				Log.i("Exception==", "=" + e);
			}

		}

	}

	/******************** ending ***************************************/

	/**** <><><><<><><><><> ASYNC TASK TO MAKE AN APPOINTMENT<><><><><><><><> ***********/
	public class appointmentTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(thirdScreen.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.offer);

			String result = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("FIRST NAME==", "==" + first_name);
				nameValuePairs.add(new BasicNameValuePair(
						"bid[presentation_id]", id));
				nameValuePairs.add(new BasicNameValuePair("bid[first_name]",
						first_name));
				nameValuePairs.add(new BasicNameValuePair("bid[last_name]",
						last_name));
				nameValuePairs
						.add(new BasicNameValuePair("bid[email]", emailId));
				nameValuePairs
						.add(new BasicNameValuePair("bid[phone]", phoneNo));
				nameValuePairs.add(new BasicNameValuePair("bid[amount]", "0"));
				nameValuePairs.add(new BasicNameValuePair("bid[note]",
						message_text));
				nameValuePairs.add(new BasicNameValuePair("bid[api]", "1"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("appointment status==", "STATUS OK");

					result = out.toString();
					Log.i("appointment result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "***APPOINTMENT*** result=="
						+ result);
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(result);
				String webServiceResponse = jsonObj.getString("status");

				if (webServiceResponse.equals("SUCCESS")) {
					new AlertsUtils(thirdScreen.this, AlertsUtils.SUCCESS);

				}

				else {
					new AlertsUtils(thirdScreen.this, AlertsUtils.PRIVATE_MODE);
				}

			}

			catch (JSONException e) {
				Log.i("JSONException==", "=" + e);
			}

			catch (Exception e) {
				Log.i("Exception==", "=" + e);
			}

		}

	}

	/******************** ending ***************************************/

	/**** <><><><<><><><><> ASYNC TASK TO GET VIDEOS<><><><><><><><> ***********/
	public class getVideosTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;
		private Activity activity;
		Context thirdScreen = getApplicationContext();

		public ImageLoader imageLoader;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(thirdScreen.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();

			String result = null;

			try {

				Log.i("3rd screen URL==", "==" + WebServices.presentation1 + id
						+ "?" + "api" + "=" + "1");
				HttpGet httpget = new HttpGet(WebServices.presentation1 + id
						+ "?" + "api" + "=" + "1");

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpget);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get search(video 3rd pg) status==", "STATUS OK");

					result = out.toString();
					Log.i("get search(video) result==", "3rd pg**==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting videos 3rd pg==" + e);
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			videos.clear();
			// activity = thirdScreen;
			imageLoader = new ImageLoader(thirdScreen.getApplicationContext());
			try {
				JSONObject jsonObj = new JSONObject(result);

				JSONArray dataArray = jsonObj.getJSONArray("data");
				Log.i("LENGTH+++","=="+dataArray.length());
				for (int i = 0; i < dataArray.length(); i++) {
					Log.i("LENGTH+++","=="+dataArray.length());
					Map<String, String> videoDetails = new HashMap<String, String>();

					JSONObject data = dataArray.getJSONObject(i);
					String mediaId = data.getString("mediaId");
					String file = data.getString("file");
					String videoImage = data.getString("videoImage");
					String title = data.getString("title");

					videoDetails.put("mediaId", mediaId);
					videoDetails.put("file", file);
					videoDetails.put("videoImage", videoImage);
					videoDetails.put("title", title);

					videos.add(videoImage);

					videoList.add((HashMap) videoDetails);

				}
				videosSize = videos.size();
				Log.i("no of videos===", "==" + videosSize);

				width = getWindowManager().getDefaultDisplay().getWidth();
				height = getWindowManager().getDefaultDisplay().getHeight();

				int LL1_width = LL1.getWidth();
				int LL1_height = LL1.getHeight();

				horizontalScrollView = new CustomHorizontalScrollView(
						thirdScreen.this, videosSize, LL1_width);
				horizontalScrollView.setVerticalScrollBarEnabled(false);
				horizontalScrollView.setHorizontalScrollBarEnabled(false);
				LL1.addView(horizontalScrollView);

				LinearLayout container = new LinearLayout(thirdScreen.this);
				container.setLayoutParams(new LayoutParams(LL1_width,
						LL1_height));
				container.removeAllViews();
				Context thirdScreen = getApplicationContext();

				ImageLoader imageLoader;
				imageLoader = new ImageLoader(
						thirdScreen.getApplicationContext());

				if (videos.size() <= 0 || videos.equals(null)) {

					ImageView img1 = new ImageView(thirdScreen.this);
					LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
							LL1_width, LL1_height);
					img1.setLayoutParams(parms);

					img1.setScaleType(ImageView.ScaleType.FIT_XY);

					img1.setBackgroundResource(R.drawable.nt_img);
					container.addView(img1);

				}

				else {

					for (int i = 0; i <= videosSize; i++) {
						Log.i("i===", "==" + i);
						Log.i("i videosSize**===", "==" + videosSize);
						img = new ImageView(thirdScreen.this);

						Log.i("LL1 dimensions==", "width==" + LL1_width
								+ "..height==" + LL1_height);
						Log.i("screen width n height==", "width==" + width
								+ "height==" + height);
						LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
								LL1_width, LL1_height);
						img.setLayoutParams(parms);

						img.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
						img.setTag(i);
						img.setId(i);

						// imageLoader.DisplayImage(videos.get(0), img);
						imageLoader.DisplayImage(img_url, img);

						container.addView(img);

						img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								selectedPosition = (Integer) v.getId();

								Log.i("img ID===", "==" + selectedPosition);

								RelativeLayout2.setVisibility(View.VISIBLE);
								Log.i("web button clicked",
										"web button clikced");

								Editor e = sharedpreferences.edit();
								e.putString("clicked", "yes");
								e.commit();

								RelativeLayout2.setVisibility(View.VISIBLE);
								RelativeLayout2.bringToFront();

								mc.setAnchorView(videoView1);
								mc.setMediaPlayer(videoView1);

								Uri uri = Uri.parse(videoList.get(
										selectedPosition).get("file"));
								Log.i("vidoe url===",
										"=="
												+ (videoList
														.get(selectedPosition)
														.get("file")));
								videoView1.setMediaController(mc);
								videoView1.requestFocus();
								videoView1.setVideoURI(uri);
								setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
								videoView1
										.setOnPreparedListener(new OnPreparedListener() {
											public void onPrepared(
													MediaPlayer mediaPlayer) {
												// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
												videoView1.start();

											}
										});

							}
						});

						// allImgs.add(img);
					}

				}
				horizontalScrollView.addView(container);

			} catch (JSONException e) {
				Log.i("JSONException=", "=" + e);
			}

			// catch (Exception e) { Log.i("Exception=", "=" + e); }

		}
	}

	public class CustomHorizontalScrollView extends HorizontalScrollView
			implements OnTouchListener, OnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 300;

		private static final int SWIPE_THRESHOLD_VELOCITY = 300;
		private static final int SWIPE_PAGE_ON_FACTOR = 10;

		private GestureDetector gestureDetector;
		private int scrollTo = 0;
		private int maxItem = 0;
		private int activeItem = 0;
		private float prevScrollX = 0;
		private boolean start = true;
		private int itemWidth = 0;
		private float currentScrollX;
		private boolean flingDisable = true;

		public CustomHorizontalScrollView(Context context) {
			super(context);
			setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

		public CustomHorizontalScrollView(Context context, int maxItem,
				int itemWidth) {
			this(context);
			this.maxItem = maxItem;
			this.itemWidth = itemWidth;
			gestureDetector = new GestureDetector(this);
			this.setOnTouchListener(this);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}
			Boolean returnValue = gestureDetector.onTouchEvent(event);

			int x = (int) event.getRawX();

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (start) {
					this.prevScrollX = x;
					start = false;
				}
				break;
			case MotionEvent.ACTION_UP:
				start = true;
				this.currentScrollX = x;
				int minFactor = itemWidth / SWIPE_PAGE_ON_FACTOR;

				if ((this.prevScrollX - this.currentScrollX) > minFactor) {
					if (activeItem < maxItem - 1)
						activeItem = activeItem + 1;

				} else if ((this.currentScrollX - this.prevScrollX) > minFactor) {
					if (activeItem > 0)
						activeItem = activeItem - 1;
				}
				System.out.println("horizontal : " + activeItem);
				scrollTo = activeItem * itemWidth;
				this.smoothScrollTo(scrollTo, 0);
				returnValue = true;
				break;
			}
			return returnValue;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (flingDisable)
				return false;
			boolean returnValue = false;
			float ptx1 = 0, ptx2 = 0;
			if (e1 == null || e2 == null)
				return false;
			ptx1 = e1.getX();
			ptx2 = e2.getX();
			// right to left

			Configuration config = getResources().getConfiguration();

			if (ptx1 - ptx2 > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (activeItem < maxItem - 1)
					activeItem = activeItem + 1;

				returnValue = true;

			} else if (ptx2 - ptx1 > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (activeItem > 0)
					activeItem = activeItem - 1;

				returnValue = true;
			}
			scrollTo = activeItem * itemWidth;
			this.smoothScrollTo(0, scrollTo);
			return returnValue;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		if (isPortraitOrLandscape == true) {
			/*
			 * Intent MainActivityIntent = new Intent(thirdScreen.this,
			 * thirdScreen.class); startActivity(MainActivityIntent);
			 */

			videoView1.stopPlayback();
			Log.i("Called", "Called");
			closed = "yes";
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			RelativeLayout2.setVisibility(View.INVISIBLE);

			new_search.setVisibility(View.VISIBLE);
			last_search.setVisibility(View.VISIBLE);
			messages.setVisibility(View.VISIBLE);
			offer.setVisibility(View.VISIBLE);
			LL1.setVisibility(View.VISIBLE);
			appointment.setVisibility(View.VISIBLE);
			full_presentation.setVisibility(View.VISIBLE);

			textView1.setVisibility(View.VISIBLE);
			

			finish();

			Intent launchNext = new Intent(getApplicationContext(),
					thirdScreen.class);
			launchNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			startActivity(launchNext);
		} else {
			Intent MainActivityIntent = new Intent(thirdScreen.this,
					practice.class);
			MainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(MainActivityIntent);
			
			//finish();

		}

		super.onBackPressed();
	}
	
	
	/**************************************************************************************/
}