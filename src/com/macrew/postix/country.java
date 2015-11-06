package com.macrew.postix;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.macrew.alertUtils.AlertsUtils;
import com.macrew.alertUtils.NetConnection;
import com.macrew.services.WebServices;
import com.macrew.postix.R;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class country extends Activity {
	Button country, search, cancel, done, state_button;
	Button cancel1, done1;

	EditText city, state;
	ListView listview, listview1;
	getCountryTask getCountryObj;
	getStatesTask getStatesObj;
	Boolean isConnected;
	LazyAdapter mAdapter;
	SharedPreferences sharedpreferences;
	LinearLayout linear_layout, linear_layout1;
	String countryName, code;
	String stateName, stateCode;
	int state_pos= -1, country_pos;
	Boolean country_clicked=false , state_clicked=false;
	Button home;
	Boolean isClicked=false;
	Boolean isDialogOpen = false;
	Dialog dialog ;

	public static ArrayList<HashMap<String, String>> CountryList = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> StateList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if( getIntent().getBooleanExtra("Exit me", false)){
		    finish();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.country);
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		sharedpreferences.edit().clear().commit();
		
		dialog = new Dialog(this, R.style.full_screen_dialog);
		dialog.setCancelable(true);
		isDialogOpen = true;

		if (isClicked == false) {
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		isClicked = true;

		dialog.setContentView(R.layout.hom1);

		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(145);
		dialog.getWindow().setBackgroundDrawable(d);
		
		getCountry();
		getStates();
		 

		country = (Button) findViewById(R.id.country);
		city = (EditText) findViewById(R.id.city);
		state = (EditText) findViewById(R.id.state);
		search = (Button) findViewById(R.id.search);
		cancel = (Button) findViewById(R.id.cancel);
		done = (Button) findViewById(R.id.done);
		state_button = (Button) findViewById(R.id.state_button);
		cancel1 = (Button) findViewById(R.id.cancel1);
		done1 = (Button) findViewById(R.id.done1);
		listview = (ListView) findViewById(R.id.listview);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
		linear_layout1 = (LinearLayout) findViewById(R.id.linear_layout1);
		home = (Button) findViewById(R.id.home);

		

		Editor e = sharedpreferences.edit();
		e.putString("country", "USA");
		e.putString("code", "US");
		e.commit();

		state.setVisibility(View.INVISIBLE);
		
	
		
		
		
		
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
				Button ok;
			
				
				ok = (Button) dialog.findViewById(R.id.ok);
				ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
				
				
				dialog.getWindow().getAttributes().windowAnimations =

						R.style.dialog_animation_top;
						dialog.getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
						dialog.show();
				
			//	Intent i = new Intent (country.this, home.class);
				//startActivity(i);

			}
		});

		state_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				state_clicked = true;
				country_clicked = false;
				if (StateList == null || StateList.size() <= 0) {

					linear_layout1.setVisibility(View.INVISIBLE);
					linear_layout.setVisibility(View.INVISIBLE);
					new AlertsUtils(country.this, AlertsUtils.TRY_AGAIN);
				}

				else {

					mAdapter = new LazyAdapter(StateList, country.this);
					listview1.setAdapter(mAdapter);
					listview1.setSelectionAfterHeaderView();
					listview1.setSelection(state_pos);
					linear_layout.setVisibility(View.INVISIBLE);

					Animation bottomUp = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.listview_bottom_up);

					linear_layout1.startAnimation(bottomUp);

					linear_layout1.setVisibility(View.VISIBLE);

				}

			}
		});

		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				
				state_pos = position;

				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);

				linear_layout1.setVisibility(View.INVISIBLE);

				view.setBackgroundColor(Color.GRAY);
				view.getFocusables(position);
				view.setSelected(true);
				HashMap<String, String> map = (HashMap<String, String>) listview1
						.getItemAtPosition(position);

				stateName = map.get("NAME");
				stateName = URLEncoder.encode(stateName);
				stateCode = map.get("CODE");

				state_button.setText(stateName);

				Log.i("STATE===", "=" + stateName);
				Log.i("staste CODE===", "=" + stateCode);

				Editor editor = sharedpreferences.edit();
				editor.putString("state", stateName);
				editor.putString("stateCode", stateCode);
				editor.commit();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout.startAnimation(bottomDown);
				linear_layout.setVisibility(View.INVISIBLE);

			}
		});

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout.startAnimation(bottomDown);
				linear_layout.setVisibility(View.INVISIBLE);

			}
		});

		cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);
				linear_layout1.setVisibility(View.INVISIBLE);

			}
		});

		done1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);
				linear_layout1.setVisibility(View.INVISIBLE);

			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String city1 = city.getText().toString();
				//city1 = city1.replace(" ", "%20");
				city1 = URLEncoder.encode(city1);
				Editor editor = sharedpreferences.edit();
				if (sharedpreferences.getString("country", "").equals("USA")) {

				}

				else {
					String STATE = state.getText().toString();
					STATE = URLEncoder.encode(STATE);
					editor.putString("stateCode", STATE);
				}
				editor.putString("city", city1);
				editor.commit();
				Log.i("**state**",
						"=" + sharedpreferences.getString("stateCode", ""));
				Log.i("**city**", "" + sharedpreferences.getString("city", ""));
				Log.i("**country**",
						"==" + sharedpreferences.getString("country", ""));
				Intent i = new Intent(country.this, MainActivity.class);
				startActivity(i);
				

			}
		});

		country.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				country_clicked = true;
				state_clicked = false;
				mAdapter = new LazyAdapter(CountryList, country.this);
				listview.setAdapter(mAdapter);

				listview.setSelectionAfterHeaderView();
				listview.setSelection(country_pos);

				if (CountryList == null || CountryList.size() <= 0) {

					linear_layout1.setVisibility(View.INVISIBLE);
					linear_layout.setVisibility(View.INVISIBLE);
					new AlertsUtils(country.this, AlertsUtils.TRY_AGAIN);
				} else {

					linear_layout1.setVisibility(View.INVISIBLE);
					Animation bottomUp = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.listview_bottom_up);

					linear_layout.startAnimation(bottomUp);

					linear_layout.setVisibility(View.VISIBLE);
					/*
					 * Animation animation = AnimationUtils.loadAnimation(this,
					 * R.anim.listview_animation); animation.setDuration(500);
					 * linear_layout.setAnimation(animation);
					 * linear_layout.animate(); animation.start();
					 */
				}

			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				country_pos = position;
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout.startAnimation(bottomDown);

				linear_layout.setVisibility(View.INVISIBLE);

				view.setBackgroundColor(Color.GRAY);
				view.getFocusables(position);
				view.setSelected(true);
				HashMap<String, String> map = (HashMap<String, String>) listview
						.getItemAtPosition(position);

				countryName = map.get("NAME");
				code = map.get("CODE");

				if (countryName.equals("USA")) {
					state.setVisibility(View.INVISIBLE);
					state_button.setVisibility(View.VISIBLE);
				}

				else {
					state.setVisibility(View.VISIBLE);
					state_button.setVisibility(View.INVISIBLE);
				}

				country.setText(countryName);

				Log.i("COUNTRY===", "=" + countryName);
				Log.i("CODE===", "=" + code);

				Editor editor = sharedpreferences.edit();
				editor.putString("country", countryName);
				editor.putString("code", code);
				editor.commit();

			}
		});

	}

	private void getStates() {
		getStatesObj = new getStatesTask();

		isConnected = NetConnection.checkInternetConnectionn(country.this);
		if (isConnected == true) {
			getStatesObj.execute();
		}

		else {
			new AlertsUtils(country.this, AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	private void getCountry() {
		getCountryObj = new getCountryTask();

		isConnected = NetConnection.checkInternetConnectionn(country.this);
		if (isConnected == true) {
			getCountryObj.execute();
		}

		else {
			new AlertsUtils(country.this, AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	/**** <><><><<><><><><> ASYNC TASK TO GET Countries <><><><><><><><> ***********/
	public class getCountryTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			/*
			 * dialog = ProgressDialog.show(country.this, "Loading...",
			 * "Please Wait", true, false);
			 * 
			 * dialog.show();
			 */
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.country);

			String result = null;

			try {

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get country status==", "STATUS OK");

					result = out.toString();
					// Log.i("get country result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting countries");
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(result);

				JSONArray dataArray = jsonObj.getJSONArray("data");
				for (int i = 0; i < dataArray.length(); i++) {
					Map<String, String> CountryDetails = new HashMap<String, String>();
					JSONObject data = dataArray.getJSONObject(i);
					String code = data.getString("code");
					String name = data.getString("name");
					CountryDetails.put("CODE", code);
					CountryDetails.put("NAME", name);
					CountryList.add((HashMap) CountryDetails);
				}
				// Log.i("Country List===", "" + CountryList);

			}

			catch (JSONException e) {
				new AlertsUtils(country.this, AlertsUtils.ERROR_OCCURED);

			}

			catch (Exception e) {
				new AlertsUtils(country.this, AlertsUtils.ERROR_OCCURED);
			}
		}
	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/

	/**** <><><><<><><><><> ASYNC TASK TO GET States<><><><><><><><> ***********/
	public class getStatesTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			/*
			 * dialog = ProgressDialog.show(country.this, "Loading...",
			 * "Please Wait", true, false);
			 * 
			 * dialog.show();
			 */
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.state);

			String result = null;

			try {

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get state status==", "STATUS OK");

					result = out.toString();
					// Log.i("get state result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting states");
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();

			try {
				JSONObject jsonObj = new JSONObject(result);

				JSONArray dataArray = jsonObj.getJSONArray("data");

				StateList = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < dataArray.length(); i++) {
					Map<String, String> StateDetails = new HashMap<String, String>();
					JSONObject data = dataArray.getJSONObject(i);
					String code = data.getString("code");
					String name = data.getString("name");
					StateDetails.put("CODE", code);
					StateDetails.put("NAME", name);
					StateList.add((HashMap) StateDetails);
				}
				// Log.i("State List===", "" + StateList);

			}

			catch (JSONException e) {
				new AlertsUtils(country.this, AlertsUtils.ERROR_OCCURED);

			}

			catch (Exception e) {
				new AlertsUtils(country.this, AlertsUtils.ERROR_OCCURED);
			}
		}
	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/

	/*********************** ADAPTER CLASS ******************************************/

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

		public LazyAdapter(ArrayList<HashMap<String, String>> CountryList,
				country country) {

			items = CountryList;

			mInflater = LayoutInflater.from(country);
		}

		@Override
		public int getCount() {

			return items.size();
		}

		@Override
		public Object getItem(int position) {

			return items.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.category_listitem,
						null);

				holder.country = (TextView) convertView.findViewById(R.id.item);
				holder.RR1 = (RelativeLayout) convertView
						.findViewById(R.id.RR1);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.country.setText(items.get(position).get("NAME"));
			
			Log.i("country clicked==","=="+country_clicked);
			Log.i("state clicked==","=="+state_clicked);

			if(country_clicked ==true){
			if (position == country_pos) {
				Log.i("*****country*****","********country*****");
				holder.RR1.setBackgroundColor(Color.parseColor("#A0A0A0"));

				// for focus on it
				int top = (holder.RR1 == null) ? 0 : holder.RR1.getTop();
				((ListView) parent).setSelectionFromTop(position, top);
			} else {
				holder.RR1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			
			
			}
			
			if(state_clicked==true){
				Log.i("*****state*****","********state*****");
				Log.i("state_pos===","=="+state_pos);
			if (position == state_pos  ) {
				holder.RR1.setBackgroundColor(Color.parseColor("#A0A0A0"));

				// for focus on it
				int top = (holder.RR1 == null) ? 0 : holder.RR1.getTop();
				((ListView) parent).setSelectionFromTop(position, top);
			} else {
				holder.RR1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			
		
			}

			return convertView;
		}

	}

	/****************** ENDING OF ADAPTER CLASS ************************************/
	class ViewHolder {
		TextView country;
		RelativeLayout RR1;

	}
	
	@Override
	public void onBackPressed() 
	{	 super.onBackPressed();
	
	//finish();
/*	if(isDialogOpen == true){
	dialog.dismiss();
	}*/
	
	 Intent startMain = new Intent(Intent.ACTION_MAIN);
	 startMain.addCategory(Intent.CATEGORY_HOME);
 //startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 startMain.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
 startMain.putExtra("Exit me", true);
	 startActivity(startMain);
	 finish();
	     
	}
}

