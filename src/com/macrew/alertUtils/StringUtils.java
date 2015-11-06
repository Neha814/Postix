package com.macrew.alertUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
  
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
	
	
	public static String removeDuplicates(String txt, String splitterRegex)
	{
	    List<String> values = new ArrayList<String>();
	    String[] splitted = txt.split(splitterRegex);
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < splitted.length; ++i)
	    {
	        if (!values.contains(splitted[i]))
	        {
	            values.add(splitted[i]);
	            sb.append(',');
	            sb.append(splitted[i]);
	        }
	    }
	    return sb.substring(1);

	}
	
	
	
	
}
