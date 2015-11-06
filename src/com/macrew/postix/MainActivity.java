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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.LinearLayout;

import android.widget.ListView;

public class MainActivity extends Activity {

	Button categories, search, cancel, done, new_search, last_search;
	Button sub_categories, cancel1, done1, purple_button, red_button;
	ListView listview, listview1;
	LinearLayout linear_layout1, linear_layout2;
	String categoryNAME = "", subCategoryNAME = "";
	String categoryID, subCategoryID;
	EditText keyword;
	SharedPreferences sharedpreferences;

	getCategoriesTask getCategoriesObj;
	getSubCategoryTask getSubCategoryObj;
	Boolean isConnected;
	LazyAdapter mAdapter;
	int cat_pos = -1, subcat_pos = -1;
	Boolean cat_clicked = false, subcat_clicked = false;

	LinearLayout linearLayout1, keyword_layout;
	Button red_button_key, green_button_key;

	public static ArrayList<HashMap<String, String>> CategoriesList = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> SubCategoriesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.postix1);

		categories = (Button) findViewById(R.id.categories);
		sub_categories = (Button) findViewById(R.id.sub_categories);
		search = (Button) findViewById(R.id.search);
		listview = (ListView) findViewById(R.id.listview);
		linear_layout1 = (LinearLayout) findViewById(R.id.linear_layout1);
		cancel = (Button) findViewById(R.id.cancel);
		done = (Button) findViewById(R.id.done);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear_layout2 = (LinearLayout) findViewById(R.id.linear_layout2);
		cancel1 = (Button) findViewById(R.id.cancel1);
		done1 = (Button) findViewById(R.id.done1);
		purple_button = (Button) findViewById(R.id.purple_button);
		red_button = (Button) findViewById(R.id.red_button);
		last_search = (Button) findViewById(R.id.last_search);
		new_search = (Button) findViewById(R.id.new_search);
		keyword = (EditText) findViewById(R.id.keyword);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		keyword_layout = (LinearLayout) findViewById(R.id.keyword_layout);
		red_button_key = (Button) findViewById(R.id.red_button_key);
		green_button_key = (Button) findViewById(R.id.green_button_key);

		linear_layout1.setVisibility(View.INVISIBLE);
		linear_layout2.setVisibility(View.INVISIBLE);

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (sharedpreferences.getString("KEYWORD", "") == null
				|| sharedpreferences.getString("KEYWORD", "").equals("")) {
			categories.setVisibility(View.VISIBLE);
			sub_categories.setVisibility(View.VISIBLE);
			linearLayout1.setVisibility(View.VISIBLE);
			keyword_layout.setVisibility(View.INVISIBLE);
			keyword.setVisibility(View.INVISIBLE);

			categories.setText(sharedpreferences.getString("CATEGORY_SELECTED",
					""));

			if ((sharedpreferences.getString("CATEGORY_SELECTED", "") == null || sharedpreferences
					.getString("CATEGORY_SELECTED", "").equals(""))) {

				categories.setText("Categories");

			}

			sub_categories.setText(sharedpreferences.getString(
					"SUBCATEGORY_SELECTED", ""));

			if ((sharedpreferences.getString("SUBCATEGORY_SELECTED", "") == null || sharedpreferences
					.getString("SUBCATEGORY_SELECTED", "").equals(""))) {

				sub_categories.setText("Sub Categories");

			}

		}

		else {
			categories.setVisibility(View.INVISIBLE);
			sub_categories.setVisibility(View.INVISIBLE);

			linearLayout1.setVisibility(View.INVISIBLE);
			keyword.setVisibility(View.VISIBLE);
			keyword_layout.setVisibility(View.VISIBLE);
			keyword.setHint(sharedpreferences.getString("KEYWORD", ""));
		}

		/**
		 * To get categories
		 */
		if (CategoriesList.equals(null) || CategoriesList.size() <= 0) {
			getCategories();
		}

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		last_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				if (sharedpreferences.contains("CATEGORY_SELECTED")
						|| sharedpreferences.contains("SUBCATEGORY_SELECTED")) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.remove("CATEGORY_SELECTED");
					editor.remove("SUBCATEGORY_SELECTED");
					editor.remove("KEYWORD");

					editor.remove("CATEGORY_ID");
					editor.remove("SUBCATEGORY_ID");

					editor.commit();
					sharedpreferences.edit().clear().commit();
				}
				Intent i = new Intent(MainActivity.this, country.class);
				startActivity(i);

			}
		});

		new_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				if (sharedpreferences.contains("CATEGORY_SELECTED")
						|| sharedpreferences.contains("SUBCATEGORY_SELECTED")) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.remove("CATEGORY_SELECTED");
					editor.remove("SUBCATEGORY_SELECTED");
					editor.remove("KEYWORD");

					editor.remove("CATEGORY_ID");
					editor.remove("SUBCATEGORY_ID");

					editor.commit();
					sharedpreferences.edit().clear().commit();
				}

				Intent i = new Intent(MainActivity.this, country.class);
				startActivity(i);

			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {

				cat_pos = myItemInt;
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);
				linear_layout1.setVisibility(View.INVISIBLE);
				myView.setBackgroundColor(Color.GRAY);
				myView.getFocusables(myItemInt);
				myView.setSelected(true);

				HashMap<String, String> map = (HashMap<String, String>) listview
						.getItemAtPosition(myItemInt);

				categoryNAME = map.get("NAME");
				categoryID = map.get("ID");
				Log.i("category name selected==", "==" + categoryNAME);
				Log.i("category id selected==", "==" + categoryID);
				// Log.i("position===", "===" + myItemInt);

				categories.setText(categoryNAME);
				sub_categories.setText("Sub Categories");
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = sharedpreferences.edit();
				editor.putString("CATEGORY_SELECTED", categoryNAME);
				editor.putString("CATEGORY_ID", categoryID);

				editor.commit();

			}
		});

		categories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString("SUBCATEGORY_SELECTED", "");

				editor.commit();

				cat_clicked = true;
				subcat_clicked = false;
				mAdapter = new LazyAdapter(CategoriesList, MainActivity.this);
				listview.setAdapter(mAdapter);
				listview.setSelectionAfterHeaderView();
				listview.setSelection(cat_pos);
				Log.i("CategoriesList.size==", "==" + CategoriesList.size());
				if (CategoriesList == null || CategoriesList.size() <= 0) {

					new AlertsUtils(MainActivity.this, AlertsUtils.TRY_AGAIN);
				}

				else {
					linear_layout2.setVisibility(View.INVISIBLE);
					Animation bottomUp = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.listview_bottom_up);

					linear_layout1.startAnimation(bottomUp);

					linear_layout1.setVisibility(View.VISIBLE);
				}

			}
		});

		sub_categories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cat_clicked = false;
				subcat_clicked = true;
				linear_layout1.setVisibility(View.INVISIBLE);

				if (sharedpreferences.getString("CATEGORY_SELECTED", "")
						.equals("")
						|| sharedpreferences.getString("CATEGORY_SELECTED", "") == null) {
					new AlertsUtils(MainActivity.this,
							AlertsUtils.SELECT_CATEGORY);
				}

				else {

					getSubcategory();
				}

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);
				linear_layout1.setVisibility(View.INVISIBLE);

			}
		});

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout1.startAnimation(bottomDown);
				linear_layout1.setVisibility(View.INVISIBLE);

			}
		});

		cancel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout2.startAnimation(bottomDown);
				linear_layout2.setVisibility(View.INVISIBLE);

			}
		});

		done1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout2.startAnimation(bottomDown);
				linear_layout2.setVisibility(View.INVISIBLE);

			}
		});

		listview1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {

				subcat_pos = myItemInt;
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.listview_bottom_down);

				linear_layout2.startAnimation(bottomDown);
				linear_layout2.setVisibility(View.INVISIBLE);

				myView.setBackgroundColor(Color.GRAY);
				myView.getFocusables(myItemInt);
				myView.setSelected(true);
				HashMap<String, String> map = (HashMap<String, String>) listview1
						.getItemAtPosition(myItemInt);

				subCategoryNAME = map.get("NAME");
				subCategoryID = map.get("ID");

				Log.i("Sub category selected==", "==" + subCategoryNAME);
				Log.i("sub_category id==", "=" + subCategoryID);
				// Log.i("position===", "===" + myItemInt);

				sub_categories.setText(subCategoryNAME);
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = sharedpreferences.edit();
				editor.putString("SUBCATEGORY_SELECTED", subCategoryNAME);
				editor.putString("SUBCATEGORY_ID", subCategoryID);
				editor.commit();

			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String keyword_text = keyword.getText().toString();
				Editor editor = sharedpreferences.edit();
				editor.putString("KEYWORD", keyword_text);
				Log.i("keyword==", "=" + keyword_text);
				editor.commit();
				linear_layout2.setVisibility(View.INVISIBLE);
				linear_layout1.setVisibility(View.INVISIBLE);
				Intent i = new Intent(MainActivity.this, practice.class);
				startActivity(i);

			}
		});

		red_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			
				keyword_layout.setVisibility(View.VISIBLE);
				linearLayout1.setVisibility(View.INVISIBLE);
				keyword.setVisibility(View.VISIBLE);
				categories.setVisibility(View.INVISIBLE);
				sub_categories.setVisibility(View.INVISIBLE);

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.remove("CATEGORY_SELECTED");
				editor.remove("SUBCATEGORY_SELECTED");
				editor.remove("CATEGORY_ID");
				editor.remove("SUBCATEGORY_ID");
				editor.putString("KEYWORD", "");
				editor.commit();
				keyword.setHint(sharedpreferences.getString("KEYWORD", ""));
				
				if(sharedpreferences.getString("KEYWORD", "").equals("")|| sharedpreferences.getString("KEYWORD", "")==null){
					keyword.setHint("Keyword");
				}
				
				else {
					keyword.setHint(sharedpreferences.getString("KEYWORD", ""));
				}

			}
		});

		red_button_key.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				keyword_layout.setVisibility(View.INVISIBLE);
				linearLayout1.setVisibility(View.VISIBLE);
				keyword.setVisibility(View.INVISIBLE);
				categories.setVisibility(View.VISIBLE);
				sub_categories.setVisibility(View.VISIBLE);

				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = sharedpreferences.edit();
				editor.putString("SUBCATEGORY_SELECTED", "");
				editor.putString("SUBCATEGORY_ID", "");
				editor.putString("CATEGORY_SELECTED", "");
				editor.putString("CATEGORY_ID", "");
				editor.remove("KEYWORD");
				editor.commit();

				categories.setText("Categories");
				sub_categories.setText("Sub Categories");
				cat_pos = -1;
			}
		});
	}

	protected void getSubcategory() {
		getSubCategoryObj = new getSubCategoryTask();

		isConnected = NetConnection.checkInternetConnectionn(MainActivity.this);
		if (isConnected == true) {
			getSubCategoryObj.execute();
		}

		else {
			new AlertsUtils(MainActivity.this,
					AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	private void getCategories() {
		getCategoriesObj = new getCategoriesTask();

		isConnected = NetConnection.checkInternetConnectionn(MainActivity.this);
		if (isConnected == true) {
			getCategoriesObj.execute();
		}

		else {
			new AlertsUtils(MainActivity.this,
					AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	/**** <><><><<><><><><> ASYNC TASK TO GET CATEGORIES <><><><><><><><> ***********/
	public class getCategoriesTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(MainActivity.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.categories_url);

			String result = null;

			try {

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get categoires status==", "STATUS OK");

					result = out.toString();
					// Log.i("get categories result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting categories===" + e);
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

				if (webServiceResponse.equals("True")) {
					JSONArray dataArray = jsonObj.getJSONArray("data");
					for (int i = 0; i < dataArray.length(); i++) {
						Map<String, String> CategoriesDetails = new HashMap<String, String>();
						JSONObject data = dataArray.getJSONObject(i);
						String id = data.getString("id");
						String name = data.getString("name");
						CategoriesDetails.put("ID", id);
						CategoriesDetails.put("NAME", name);
						CategoriesList.add((HashMap) CategoriesDetails);
					}
					// Log.i("Categories List===", "" + CategoriesList);
				}

				else {
					new AlertsUtils(MainActivity.this,
							AlertsUtils.ERROR_OCCURED);
				}

			}

			catch (JSONException e) {

			}

			catch (Exception e) {

			}
		}
	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/
	/*********************** ADAPTER CLASS ******************************************/

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		public ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

		public LazyAdapter(ArrayList<HashMap<String, String>> CategoriesList,
				MainActivity mainActivity) {
			items = CategoriesList;
			mInflater = LayoutInflater.from(mainActivity);
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

				holder.item = (TextView) convertView.findViewById(R.id.item);
				holder.RR1 = (RelativeLayout) convertView
						.findViewById(R.id.RR1);
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.item.setText(items.get(position).get("NAME"));

			if (cat_clicked == true) {
				if (position == cat_pos) {
					Log.i("*****country*****", "********country*****");
					holder.RR1.setBackgroundColor(Color.parseColor("#A0A0A0"));

					// for focus on it
					int top = (holder.RR1 == null) ? 0 : holder.RR1.getTop();
					((ListView) parent).setSelectionFromTop(position, top);
				} else {
					holder.RR1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				}

			}

			if (subcat_clicked == true) {
				Log.i("*****state*****", "********state*****");
				Log.i("state_pos===", "==" + subcat_pos);
				if (position == subcat_pos) {
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
		TextView item;
		RelativeLayout RR1;
	}

	/**** <><><><<><><><><> ASYNC TASK TO GET SUB CATEGORIES <><><><><><><><> ***********/
	public class getSubCategoryTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(MainActivity.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.subCategories_url);

			String result = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				
				Log.i("cat id=====","=="+categoryID);
				nameValuePairs.add(new BasicNameValuePair("catid", sharedpreferences.getString("CATEGORY_ID", "")));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get sub_categoires status==", "STATUS OK");

					result = out.toString();
					// Log.i("get sub_categories result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting sub_categories");
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

				if (webServiceResponse.equals("True")) {
					JSONArray dataArray = jsonObj.getJSONArray("data");
					SubCategoriesList = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < dataArray.length(); i++) {
						Map<String, String> SubCategoriesDetails = new HashMap<String, String>();
						JSONObject data = dataArray.getJSONObject(i);
						String id = data.getString("id");
						String name = data.getString("name");
						SubCategoriesDetails.put("ID", id);
						SubCategoriesDetails.put("NAME", name);
						SubCategoriesList.add((HashMap) SubCategoriesDetails);
					}
					
					Animation bottomUp = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.listview_bottom_up);

					linear_layout2.startAnimation(bottomUp);
					linear_layout2.setVisibility(View.VISIBLE);

					mAdapter = new LazyAdapter(SubCategoriesList,
							MainActivity.this);
					listview1.setAdapter(mAdapter);
					listview1.setSelectionAfterHeaderView();
					listview1.setSelection(subcat_pos);
				}

				else {
					new AlertsUtils(MainActivity.this,
							AlertsUtils.ERROR_OCCURED);
				}

			}

			catch (JSONException e) {

			}

			catch (Exception e) {

			}
		}
	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/
	@Override
	public void onBackPressed() {
		Intent MainActivityIntent = new Intent(MainActivity.this, country.class);
		MainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(MainActivityIntent);
	
		//finish();
		super.onBackPressed();
	}
}
