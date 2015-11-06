package com.macrew.postix;



import com.macrew.postix.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class home extends Activity{
	Dialog dialog ;
	Boolean isClicked=false;
	Boolean isDialogOpen = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		setContentView(R.layout.home);
		
		
		/*dialog = new Dialog(this, R.style.full_screen_dialog);
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
		
		Button ok;
		
		
		ok = (Button) dialog.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			
		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
				Intent i = new Intent (home.this, country.class);
				startActivity(i);
				
			}
		});
		
		
		dialog.getWindow().getAttributes().windowAnimations =

				R.style.dialog_animation_top;
				dialog.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				dialog.show();*/
		
	}
}
