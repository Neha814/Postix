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
import org.json.JSONTokener;

import com.macrew.alertUtils.AlertsUtils;
import com.macrew.alertUtils.NetConnection;
import com.macrew.entities.entity;
import com.macrew.imageloader.ImageLoader;
import com.macrew.postix.MainActivity.getCategoriesTask;
import com.macrew.postix.country.getCountryTask;
import com.macrew.services.WebServices;
import com.macrew.url.Url;
import com.macrew.postix.R;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class secondScreenCopy extends Activity {

	Button category, sub_category, new_search, keyword_button, last_search;
	SharedPreferences sharedpreferences;
	String category_text, sub_category_text;
	entity entity_object;
	Button red, green;
	String key;
	ListView listView;
	LazyAdapter mAdapter;
	getVideosTask getVideosObj;
	Boolean isConnected;
	int tag = 0;
	Integer button_pos = null;
	String pager, total_count;
	getSecondPageTask getSecondPageObj;
	Boolean isLoading = false;
	int counter = 0;
	private Context context = null;
	RelativeLayout RR1, RR2;
	Boolean isClicked = false;
	Boolean isClicked1 = false;
	Boolean isClicked2 = false;
	Boolean isRestricted = false;
	String Password;
	passwordTask passwordObj;
	int screen_width , screen_height;
	float screenDensity ;
	
	entity e;
	public static ArrayList<HashMap<String, String>> searchList = new ArrayList<HashMap<String, String>>();

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.video_listview);
		
		searchList.clear();

		category = (Button) findViewById(R.id.category);
		sub_category = (Button) findViewById(R.id.sub_category);

		new_search = (Button) findViewById(R.id.new_search);
		keyword_button = (Button) findViewById(R.id.keyword_button);
		last_search = (Button) findViewById(R.id.last_search);
		listView = (ListView) findViewById(R.id.listView);
		RR1 = (RelativeLayout) findViewById(R.id.RR1);
		RR2 = (RelativeLayout) findViewById(R.id.RR2);
		red = (Button) findViewById(R.id.red);
		green = (Button) findViewById(R.id.green);

		category.setVisibility(View.INVISIBLE);
		sub_category.setVisibility(View.INVISIBLE);
		keyword_button.setVisibility(View.INVISIBLE);

		red.setVisibility(View.INVISIBLE);
		
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
		
		screenDensity = getResources().getDisplayMetrics().density;
		
		
		 

		getVideos();
		final Dialog dialog = new Dialog(secondScreenCopy.this,
				R.style.full_screen_dialog);
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		if (sharedpreferences.getString("KEYWORD", "") == null
				|| sharedpreferences.getString("KEYWORD", "").equals("")) {

			category.setVisibility(View.VISIBLE);
			sub_category.setVisibility(View.VISIBLE);
			keyword_button.setVisibility(View.INVISIBLE);

			category_text = sharedpreferences
					.getString("CATEGORY_SELECTED", "");

			sub_category_text = sharedpreferences.getString(
					"SUBCATEGORY_SELECTED", "");

			if (!category_text.isEmpty()) {

				category.setText(category_text);
				
				
					if(sub_category_text.isEmpty()){
						Log.i("dfddd","ffdfd");
						int x = category.getWidth();
						
						
						
						RR1.setPadding(((screen_width-(category.getWidth()))/4), 0,((screen_width-(category.getWidth()))/4), 0);
						RelativeLayout.LayoutParams rel_bottone = new RelativeLayout.LayoutParams((int) (180*screenDensity+0.5f),(int) (45*screenDensity+0.5f));
						category.setLayoutParams(rel_bottone);
						//category.setWidth(250);
					}
			}

			else {
				category.setVisibility(View.INVISIBLE);
				
				
			}

			if (!sub_category_text.isEmpty()) {

				sub_category.setText(sub_category_text);
			}

			else {
				sub_category.setVisibility(View.INVISIBLE);
			}

			if (sub_category_text.isEmpty() && category_text.isEmpty()) {

				RR1.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 0f));

				RR2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 9f));

			}
		}

		else {
			category.setVisibility(View.INVISIBLE);
			sub_category.setVisibility(View.INVISIBLE);

			if (sharedpreferences.getString("KEYWORD", "").equals("")) {
				keyword_button.setVisibility(View.INVISIBLE);

				RR1.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 0f));

				RR2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0, 10f));

			}

			else {
				keyword_button.setVisibility(View.VISIBLE);
				keyword_button.setText(sharedpreferences.getString("KEYWORD",
						""));

			}

		}

		red.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRestricted = false;
				Animation bottomDown = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.slide_out_left_green);
				bottomDown.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						green.setVisibility(View.VISIBLE);
						red.setVisibility(View.INVISIBLE);
						mAdapter.notifyDataSetChanged();

					}
				});
				red.startAnimation(bottomDown);

			}
		});

		green.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.setCancelable(true);
				isRestricted = true;
				if (isClicked == false) {
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				}

				isClicked = true;

				dialog.setContentView(R.layout.private_mode);

				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog.getWindow().setBackgroundDrawable(d);

				Button cancel;
				Button go;

				cancel = (Button) dialog.findViewById(R.id.cancel);
				go = (Button) dialog.findViewById(R.id.go);

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				go.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();

						Animation bottomDown = AnimationUtils.loadAnimation(
								getApplicationContext(),
								R.anim.slide_out_right_red);
						bottomDown
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {

									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										green.setVisibility(View.INVISIBLE);
										red.setVisibility(View.VISIBLE);
										mAdapter.notifyDataSetChanged();

									}
								});
						green.startAnimation(bottomDown);

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
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				if (sharedpreferences.contains("CATEGORY_SELECTED")
						|| sharedpreferences.contains("SUBCATEGORY_SELECTED")) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.remove("CATEGORY_SELECTED");
					editor.remove("SUBCATEGORY_SELECTED");
					editor.remove("CATEGORY_ID");
					editor.remove("SUBCATEGORY_ID");
					editor.remove("KEYWORD");
					editor.commit();

				}
				Intent i = new Intent(secondScreenCopy.this, country.class);
				startActivity(i);

			}
		});

		last_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sharedpreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				if (sharedpreferences.contains("CATEGORY_SELECTED")
						|| sharedpreferences.contains("SUBCATEGORY_SELECTED")) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
					//editor.remove("CATEGORY_SELECTED");
					//editor.remove("SUBCATEGORY_SELECTED");

					//editor.remove("CATEGORY_ID");
					//editor.remove("SUBCATEGORY_ID");
					editor.commit();

				}
				Intent i = new Intent(secondScreenCopy.this, MainActivity.class);
				startActivity(i);

			}
		});

	}

	private void getVideos() {
		getVideosObj = new getVideosTask();

		isConnected = NetConnection
				.checkInternetConnectionn(secondScreenCopy.this);
		if (isConnected == true) {
			getVideosObj.execute();
		}

		else {
			new AlertsUtils(secondScreenCopy.this,
					AlertsUtils.NO_INTERNET_CONNECTION);
		}

	}

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		private Activity activity;
		public ImageLoader imageLoader;

		
	

		final Dialog dialog_passowrd = new Dialog(secondScreenCopy.this,
				R.style.full_screen_dialog);

		public LazyAdapter(ArrayList<HashMap<String, String>> searchList,
				secondScreenCopy secondScreenCopy) {

			mInflater = LayoutInflater.from(secondScreenCopy);
			activity = secondScreenCopy;
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		@Override
		public int getCount() {
			
			Log.i("search list sizwe==","=="+searchList.size()+"=="+searchList.size()/2+"=="+((searchList.size()/2)+1));
			if(searchList.size()>1){
				if(searchList.size()%2==0){
					return (searchList.size()/2);
				}
				
				else {
			return (((searchList.size()/2)+1));
				}
			}
			
			else {
				return (searchList.size());
			}
		}

		@Override
		public Object getItem(int position) {
			
			Log.i("position of srch list===","=="+searchList.get(position));
			return searchList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			
			final int pos = position;
			tag = 2 * position;
			
			
			
			Log.i("SRCH LIST sizeeeeee===",""+searchList.size());
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.video_listitem, null);

				holder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView1);

				holder.imageView2 = (ImageView) convertView
						.findViewById(R.id.imageView2);

				holder.button1 = (Button) convertView
						.findViewById(R.id.button1);
				holder.button2 = (Button) convertView
						.findViewById(R.id.button2);
				holder.button1.setTag(0);
				holder.button2.setTag(1);
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 1) {
				holder.imageView.setBackgroundResource(R.drawable.back3);
				if(searchList.size()>1){
				holder.imageView2.setBackgroundResource(R.drawable.back2);
				}

			} else if (position % 2 == 2) {
				holder.imageView.setBackgroundResource(R.drawable.back2);
				if(searchList.size()>1){
				holder.imageView2.setBackgroundResource(R.drawable.back1);
				}
			}

			else {
				holder.imageView.setBackgroundResource(R.drawable.back1);
				if(searchList.size()>1){
				holder.imageView2.setBackgroundResource(R.drawable.back3);
				}
			}
			Log.i("tag2===","="+tag);
			if (tag > 6) {
				
				getSecondPage();
				
			}
			if (tag < searchList.size()) {
				
				if (searchList.get(tag).get("is_mature").equals("0")
						|| isRestricted == true) {
					
					if((searchList.get(tag).get("extension")).equals("null")){
						
						holder.imageView.setImageResource(R.drawable.nt_img);
						Log.i("tag==", "==" + tag);
						Log.i("position==", "==" + position);
					}
						
					else {
						imageLoader.DisplayImage(searchList.get(tag).get("url"),
						holder.imageView);
						Log.i("tag==", "==" + tag);
						Log.i("position==", "==" + position);

					}
					
				}

				else {
					holder.imageView.setImageResource(R.drawable.view1);
				}
			
				tag++;
				
			
				
				if(searchList.size()>1){
					
					
					if (searchList.get(tag).get("is_mature").equals("0")
						|| isRestricted == true) {
					
					
				
					if((searchList.get(tag).get("extension")).equals("null")){
						
						holder.imageView2.setImageResource(R.drawable.nt_img);
						Log.i("tag==", "==" + tag);
						Log.i("position==", "==" + position);
					}
						
					else {
						imageLoader.DisplayImage(searchList.get(tag).get("url"),
						holder.imageView2);
						Log.i("tag==", "==" + tag);
						Log.i("position==", "==" + position);

					}

				}
				
				
				else {
					holder.imageView2.setImageResource(R.drawable.view1);
				}
									
					tag++;
				
			}

			}
			holder.button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					button_pos = (Integer) v.getTag();
					int item_selected = 2 * pos + button_pos;
					HashMap<String, String> details = searchList
							.get(item_selected);
					
					if (details.get("is_mature").equals("0")) {
						Log.i("is_private===","=="+details.get("is_private"));
						if (details.get("is_private").equals("0")||details.get("is_private")==null) {

							Intent i = new Intent(secondScreenCopy.this,
									thirdScreen.class);

							// i.putExtra("HASHMAP", details);
							sharedpreferences = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());
							Editor editor = sharedpreferences.edit();
							editor.putString("VIDEO_ID", details.get("id"));
							editor.putString("VIDEO_TITLE",
									details.get("title"));
							editor.putString("URL", details.get("url"));
							editor.commit();

							startActivity(i);
						}

						else {
							Button ok;
							dialog_passowrd.setCancelable(true);

							if (isClicked2 == false) {
								dialog_passowrd.requestWindowFeature(Window.FEATURE_NO_TITLE);
							}

							isClicked2 = true;

							dialog_passowrd.setContentView(R.layout.private_img);

							dialog_passowrd.getWindow().setLayout(LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
							dialog_passowrd.getWindow().setFormat(PixelFormat.TRANSLUCENT);
							dialog_passowrd.getWindow().setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

							Drawable d = new ColorDrawable(Color.BLACK);
							d.setAlpha(130);
							
							ok = (Button) dialog_passowrd.findViewById(R.id.ok);
							
							ok.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dialog_passowrd.dismiss();	
								}
							});
							dialog_passowrd.getWindow().setBackgroundDrawable(d);
							dialog_passowrd.getWindow().getAttributes().windowAnimations =

									R.style.dialog_animation_top;
							dialog_passowrd.getWindow()
											.setSoftInputMode(
													WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

									

							dialog_passowrd.show();
						}
					}

					else if (isRestricted == true) {
						sharedpreferences = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						Editor editor = sharedpreferences.edit();
						editor.putString("VIDEO_ID", details.get("id"));
						editor.putString("VIDEO_TITLE", details.get("title"));
						editor.putString("URL", details.get("url"));
						
						editor.commit();
						dialog_passowrd.setCancelable(true);

						if (isClicked1 == false) {
							dialog_passowrd
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
						}

						isClicked1 = true;

						dialog_passowrd.setContentView(R.layout.password);

						Button cancel, go;
						final EditText password;

						cancel = (Button) dialog_passowrd
								.findViewById(R.id.cancel);
						go = (Button) dialog_passowrd.findViewById(R.id.go);
						password = (EditText) dialog_passowrd
								.findViewById(R.id.password);

						dialog_passowrd.getWindow().setLayout(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						dialog_passowrd.getWindow().setFormat(
								PixelFormat.TRANSLUCENT);
						dialog_passowrd
								.getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

						Drawable d = new ColorDrawable(Color.BLACK);
						d.setAlpha(130);
						dialog_passowrd.getWindow().setBackgroundDrawable(d);

						cancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog_passowrd.dismiss();

							}
						});

						go.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Password = password.getText().toString();
								dialog_passowrd.dismiss();
								getPassword();

							}

							private void getPassword() {
								passwordObj = new passwordTask();

								isConnected = NetConnection
										.checkInternetConnectionn(activity);
								if (isConnected == true) {
									passwordObj.execute();
								}

								else {
									new AlertsUtils(activity,
											AlertsUtils.NO_INTERNET_CONNECTION);
								}

							}
						});

						dialog_passowrd.getWindow().getAttributes().windowAnimations =

						R.style.dialog_animation_top;
						dialog_passowrd
								.getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

						dialog_passowrd.show();
					} else {
						
					}

				}
			});
			
			if(searchList.size()>1 &&(searchList.size()%2)!=0 ){

			holder.button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					button_pos = (Integer) v.getTag();
					int item_selected = 2 * pos + button_pos;
					HashMap<String, String> details = searchList
							.get(item_selected);
					
					if (details.get("is_mature").equals("0")) {
						
						if (details.get("is_private").equals("0")||details.get("is_private")==null) {
							Intent i = new Intent(secondScreenCopy.this,
									thirdScreen.class);
							// i.putExtra("HASHMAP", details);
							sharedpreferences = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());
							Editor editor = sharedpreferences.edit();
							editor.putString("VIDEO_ID", details.get("id"));
							editor.putString("VIDEO_TITLE",
									details.get("title"));
							editor.putString("URL", details.get("url"));

							editor.commit();

							startActivity(i);
						}

						else {
							
							Button ok;
							dialog_passowrd.setCancelable(true);

							if (isClicked2 == false) {
								dialog_passowrd.requestWindowFeature(Window.FEATURE_NO_TITLE);
							}

							isClicked2 = true;

							dialog_passowrd.setContentView(R.layout.private_img);

							dialog_passowrd.getWindow().setLayout(LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
							dialog_passowrd.getWindow().setFormat(PixelFormat.TRANSLUCENT);
							dialog_passowrd.getWindow().setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

							Drawable d = new ColorDrawable(Color.BLACK);
							d.setAlpha(130);
							
							ok = (Button) dialog_passowrd.findViewById(R.id.ok);
							
							ok.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dialog_passowrd.dismiss();	
								}
							});
							dialog_passowrd.getWindow().setBackgroundDrawable(d);
							dialog_passowrd.getWindow().getAttributes().windowAnimations =

									R.style.dialog_animation_top;
							dialog_passowrd.getWindow()
											.setSoftInputMode(
													WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

									

							dialog_passowrd.show();
						}
					}

					else if (isRestricted == true) {

						sharedpreferences = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						Editor editor = sharedpreferences.edit();
						editor.putString("VIDEO_ID", details.get("id"));
						editor.putString("VIDEO_TITLE", details.get("title"));

						editor.commit();

						dialog_passowrd.setCancelable(true);
						Button cancel, go;
						final EditText password;

						if (isClicked1 == false) {
							dialog_passowrd
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
						}

						isClicked1 = true;

						dialog_passowrd.setContentView(R.layout.password);

						dialog_passowrd.getWindow().setLayout(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						dialog_passowrd.getWindow().setFormat(
								PixelFormat.TRANSLUCENT);
						dialog_passowrd
								.getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

						Drawable d = new ColorDrawable(Color.BLACK);
						d.setAlpha(130);
						dialog_passowrd.getWindow().setBackgroundDrawable(d);

						cancel = (Button) dialog_passowrd
								.findViewById(R.id.cancel);
						go = (Button) dialog_passowrd.findViewById(R.id.go);
						password = (EditText) dialog_passowrd
								.findViewById(R.id.password);

						cancel.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog_passowrd.dismiss();

							}
						});

						go.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Password = password.getText().toString();
								dialog_passowrd.dismiss();
								getPassword();

							}

							private void getPassword() {
								passwordObj = new passwordTask();

								isConnected = NetConnection
										.checkInternetConnectionn(activity);
								if (isConnected == true) {
									passwordObj.execute();
								}

								else {
									new AlertsUtils(activity,
											AlertsUtils.NO_INTERNET_CONNECTION);
								}

							}
						});

						dialog_passowrd.getWindow().getAttributes().windowAnimations =

						R.style.dialog_animation_top;
						dialog_passowrd
								.getWindow()
								.setSoftInputMode(
										WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

						dialog_passowrd.show();
					}
				

				}
			});
			
		}

			return convertView;
		}

	}

	class ViewHolder {
		ImageView imageView, imageView2;
		Button button1, button2;
	}

	/**** <><><><<><><><><> ASYNC TASK TO GET VIDEOS<><><><><><><><> ***********/
	public class getVideosTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;
		final Dialog dialog1 = new Dialog(secondScreenCopy.this,
				R.style.full_screen_dialog);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(secondScreenCopy.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();

			String result = null;

			try {

				HttpGet httpget = new HttpGet(WebServices.search + "?"
						+ "search[type]" + "=" + "general" + "&"
						+ "search[keywords]" + "="
						+ sharedpreferences.getString("KEYWORD", "") + "&"
						+ "search[category_id]" + "="
						+ sharedpreferences.getString("CATEGORY_ID", "") + "&"
						+ "search[country_code]" + "="
						+ sharedpreferences.getString("code", "") + "&"
						+ "search[sub_category_id]" + "=" + "" + "&" + "sort"
						+ "=" + "0" + "&" + "api" + "=" + "1" + "&"
						+ "search[distance]=" + "0" + "&" + "search[city]="
						+ sharedpreferences.getString("city", "") + "&"
						+ "search[state]="
						+ sharedpreferences.getString("stateCode", ""));

				Log.i("URL to get images=",
						"="
								+ WebServices.search
								+ "?"
								+ "search[type]"
								+ "="
								+ "general"
								+ "&"
								+ "search[keywords]"
								+ "="
								+ sharedpreferences.getString("KEYWORD", "")
								+ "&"
								+ "search[category_id]"
								+ "="
								+ sharedpreferences
										.getString("CATEGORY_ID", "") + "&"
								+ "search[country_code]" + "="
								+ sharedpreferences.getString("code", "") + "&"
								+ "search[sub_category_id]" + "=" + "" + "&"
								+ "sort" + "=" + "0" + "&" + "api" + "=" + "1"
								+ "&" + "search[distance]=" + "0" + "&"
								+ "search[city]="
								+ sharedpreferences.getString("city", "") + "&"
								+ "search[state]="
								+ sharedpreferences.getString("stateCode", ""));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpget);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get search(video) status==", "STATUS OK");

					result = out.toString();
					Log.i("get search(video) result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting videos==" + e);
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

				JSONArray dataArray = jsonObj.getJSONArray("data");

				for (int i = 0; i < dataArray.length(); i++) {
					Map<String, String> SearchDetails = new HashMap<String, String>();
					JSONObject data = dataArray.getJSONObject(i);
					String id = data.getString("ID");
					String title = data.getString("title");
					String media_url = data.getString("media_url");
					String filename = data.getString("filename");
					String extension = data.getString("extension");
					String description = data.getString("description");
					String is_mature = data.getString("is_mature");
					String is_private = data.getString("is_private");
					String url = media_url + filename + "." + extension;

					SearchDetails.put("url", url);
					SearchDetails.put("id", id);
					SearchDetails.put("title", title);
					SearchDetails.put("media_url", media_url);
					SearchDetails.put("filename", filename);
					SearchDetails.put("extension", extension);
					SearchDetails.put("description", description);
					SearchDetails.put("is_mature", is_mature);
					SearchDetails.put("is_private", is_private);

					Log.i("mature====", "" + is_mature);
					searchList.add((HashMap) SearchDetails);
				}

			
				
				if(jsonObj.get("pager") instanceof JSONObject){
					JSONObject pagerObj = jsonObj.getJSONObject("pager");
				pager = pagerObj.getString("next");
				pager = pager.replace("amp;", "");
				total_count = pagerObj.getString("total_count");
				Log.i("searchList==",""+searchList);
				}
				
				else if(jsonObj.get("pager") instanceof JSONArray){
					Log.i("ARRAY===","====ARRAY");
					Log.i("searchList==",""+searchList);
					total_count="0";
				}
				

				mAdapter = new LazyAdapter(searchList, secondScreenCopy.this);
				listView.setAdapter(mAdapter);

			}

			catch (JSONException e) {
				Log.i("JSONEXception==", "==" + e);
				Button new_search;

				dialog1.setCancelable(true);

				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.result_not_found);
				dialog1.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog1.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog1.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog1.getWindow().setBackgroundDrawable(d);

				new_search = (Button) dialog1.findViewById(R.id.new_search);

				new_search.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (sharedpreferences.contains("CATEGORY_SELECTED")
								|| sharedpreferences
										.contains("SUBCATEGORY_SELECTED")) {
							SharedPreferences.Editor editor = sharedpreferences
									.edit();
							editor.putString("CATEGORY_SELECTED", "");
							editor.putString("SUBCATEGORY_SELECTED", "");
							editor.putString("SUBCATEGORY_ID", "0");
							editor.putString("CATEGORY_ID", "");
							editor.commit();
						}
						
						Intent i = new Intent(secondScreenCopy.this,
								country.class);
						
						startActivity(i);
						dialog1.dismiss();

					}
				});
				dialog1.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;
				dialog1.show();
			}

			catch (Exception e) {
				/*
				 * new AlertsUtils(secondScreenCopy.this,
				 * AlertsUtils.ERROR_OCCURED);
				 */

				Button new_search;

				dialog1.setCancelable(true);

				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.result_not_found);
				dialog1.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog1.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				dialog1.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(130);
				dialog1.getWindow().setBackgroundDrawable(d);

				new_search = (Button) dialog1.findViewById(R.id.new_search);

				new_search.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (sharedpreferences.contains("CATEGORY_SELECTED")
								|| sharedpreferences
										.contains("SUBCATEGORY_SELECTED")) {
							SharedPreferences.Editor editor = sharedpreferences
									.edit();
							editor.putString("CATEGORY_SELECTED", "");
							editor.putString("SUBCATEGORY_SELECTED", "");
							editor.putString("SUBCATEGORY_ID", "0");
							editor.putString("CATEGORY_ID", "");
							editor.commit();
						}
						Intent i = new Intent(secondScreenCopy.this,
								MainActivity.class);
						startActivity(i);

						dialog1.dismiss();

					}
				});
				dialog1.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;

				dialog1.show();
			}
		}
	}

	public void getSecondPage() {
		if (isLoading == false) {

			if (searchList.size() < Integer.parseInt(total_count)) {
				counter++;

				getSecondPageObj = new getSecondPageTask();

				isConnected = NetConnection
						.checkInternetConnectionn(secondScreenCopy.this);
				if (isConnected == true) {
					getSecondPageObj.execute();
				}

				else {
					new AlertsUtils(secondScreenCopy.this,
							AlertsUtils.NO_INTERNET_CONNECTION);
				}

			}

			else {

			}

			isLoading = true;
		} else {

		}

	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/

	/**** <><><><<><><><><> ASYNC TASK TO GET VIDEOS<><><><><><><><> ***********/
	public class getSecondPageTask extends AsyncTask<Void, Integer, String> {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.loading, null);
		RelativeLayout footer_layout = (RelativeLayout) view
				.findViewById(R.id.footer_layout);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			listView.addFooterView(view);
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();

			String result = null;

			try {

				HttpGet httpget = new HttpGet("http://postix.net" + pager);

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpget);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("get second pg status==", "STATUS OK");

					result = out.toString();
					// Log.i("get second pg result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "getting second pg==" + e);
			}

			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			listView.removeFooterView(view);

			try {
				JSONObject jsonObj = new JSONObject(result);

				JSONArray dataArray = jsonObj.getJSONArray("data");

				for (int i = 0; i < dataArray.length(); i++) {
					Map<String, String> SearchDetails = new HashMap<String, String>();
					JSONObject data = dataArray.getJSONObject(i);
					String id = data.getString("ID");
					String title = data.getString("title");
					String media_url = data.getString("media_url");
					String filename = data.getString("filename");
					String extension = data.getString("extension");
					String description = data.getString("description");
					String is_mature = data.getString("is_mature");
					String is_private = data.getString("is_private");
					String url = media_url + filename + "." + extension;

					SearchDetails.put("url", url);
					SearchDetails.put("id", id);
					SearchDetails.put("title", title);
					SearchDetails.put("media_url", media_url);
					SearchDetails.put("filename", filename);
					SearchDetails.put("extension", extension);
					SearchDetails.put("description", description);
					SearchDetails.put("is_mature", is_mature);
					SearchDetails.put("is_private", is_private);
					searchList.add((HashMap) SearchDetails);

					Log.i("mature====", "" + is_mature);
				}

				
				
				
				if(jsonObj.get("pager") instanceof JSONObject){
					JSONObject pagerObj = jsonObj.getJSONObject("pager");
				pager = pagerObj.getString("next");
				pager = pager.replace("amp;", "");
				total_count = pagerObj.getString("total_count");
				}
				
				else if(jsonObj.get("pager") instanceof JSONArray){
					Log.i("ARRAY===","====ARRAY");
					total_count="0";
				}
				isLoading = false;
				mAdapter.notifyDataSetChanged();

			}

			catch (JSONException e) {
				new AlertsUtils(secondScreenCopy.this,
						AlertsUtils.ERROR_OCCURED);

			}

			catch (Exception e) {
				new AlertsUtils(secondScreenCopy.this,
						AlertsUtils.ERROR_OCCURED);
			}
		}
	}

	/*********** <><><><><><> end of async class <><><><><><><><><><><><> **********/

	/**
	 * Password
	 */

	public class passwordTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(secondScreenCopy.this, "Loading...",
					"Please Wait", true, false);

			dialog.show();

		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.password);

			String result = null;

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				Log.i("password====", "==" + Password);
				Log.i("pid=====",
						"==" + sharedpreferences.getString("VIDEO_ID", ""));
				nameValuePairs
						.add(new BasicNameValuePair("password", Password));
				nameValuePairs.add(new BasicNameValuePair("pid",
						sharedpreferences.getString("VIDEO_ID", "")));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("password status==", "STATUS OK");

					result = out.toString();
					Log.i("password result==", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "password===" + e);
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
				String status = jsonObj.getString("result");
				if (status.equals("true")) {
					Intent i = new Intent(secondScreenCopy.this,
							thirdScreen.class);
					startActivity(i);
				}

				else {
					new AlertsUtils(secondScreenCopy.this,
							AlertsUtils.INVALID_PASSWORD);
				}
			}

			catch (JSONException e) {

			}

			catch (Exception e) {

			}
		}

	}
	@Override
	public void onBackPressed() {
		Intent MainActivityIntent = new Intent(secondScreenCopy.this, MainActivity.class);
		MainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(MainActivityIntent);
	//	 finish();
		
		super.onBackPressed();
	}
}
