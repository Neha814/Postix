package com.macrew.alertUtils;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertsUtils {
	Context context;
	AlertDialog.Builder alert;
	
	public static final String SELECT_CATEGORY = "Please select the category.";
	public static final String ENTER_KEYWORD = "Please enter keyword.";
	public static final String TRY_AGAIN = "Please try again.";
	public static final String PRIVATE_MODE = "This presentation may currently be in 'private' mode or may have been deleted.Please sign into your account to modify.";
	public static final String ERROR_OCCURED = "Some error occured. Please try again.";
	public static final String SELECT_CATEGORY_AND_SUBCATEGORY = "Please select sub-category.";
	public static final String SELECT_SUBCATEGORY = "Please select the sub-category.";
	public static final String NO_INTERNET_CONNECTION = "No internet connection.Please try again.";
	public static final String SUCCESS = "Successfully Send.";
	public static final String INVALID_PASSWORD = "Invalid Password.Please try again";
	
	public AlertsUtils(Context cxt, String message)
	{
		this.context = cxt;
		
		alert=new AlertDialog.Builder(context);
		alert.setMessage(message)
			 .setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
									     
					}
		}).show();
		
	}
	
	
	/****************
	 * 
	 * public AlertsUtils(Context cxt, String message)
{
	this.context = cxt;
	
	alert=new AlertDialog.Builder(context);
	alert.setMessage(message)
		 .setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
								     
				}
	}).show();
	
}
	 */
	 /*******************/
	
	
	
	
		public static  String  convertToupperCase(String NAME) {
			
		
		    String[] exampleSplit = NAME.split(" ");
		    
		    StringBuffer sb = new StringBuffer();
		    for (String word : exampleSplit) {
		        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1));
		        sb.append(" ");
		    }
		   
			return sb.toString();
		
	}
}
