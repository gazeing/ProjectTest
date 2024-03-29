package com.example.clienttest.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;



public class LoginProcess extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void postLoginData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
         
        /* login.php returns true if username and password is equal to saranga */
        HttpPost httppost = new HttpPost("http://www.sencide.com/blog/login.php");
 
        try {
            // Add user name and password
//         EditText uname = (EditText)findViewById(R.id.txt_username);
//         String username = uname.getText().toString();
// 
//         EditText pword = (EditText)findViewById(R.id.txt_password);
//         String password = pword.getText().toString();
          
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("username", username));
//            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
            // Execute HTTP Post Request
            Log.w("SENCIDE", "Execute HTTP Post Request");
            HttpResponse response = httpclient.execute(httppost);
             
            String str = inputStreamToString(response.getEntity().getContent()).toString();
            Log.w("SENCIDE", str);
             
            if(str.toString().equalsIgnoreCase("true"))
            {
             Log.w("SENCIDE", "TRUE");
           //  result.setText("Login successful");  
            }else
            {
             Log.w("SENCIDE", "FALSE");
           //  result.setText(str);            
            }
 
        } catch (ClientProtocolException e) {
         e.printStackTrace();
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
   
    private StringBuilder inputStreamToString(InputStream is) {
     String line = "";
     StringBuilder total = new StringBuilder();
     // Wrap a BufferedReader around the InputStream
     BufferedReader rd = new BufferedReader(new InputStreamReader(is));
     // Read response until the end
     try {
      while ((line = rd.readLine()) != null) {
        total.append(line);
      }
     } catch (IOException e) {
      e.printStackTrace();
     }
     // Return full string
     return total;
    }

}
