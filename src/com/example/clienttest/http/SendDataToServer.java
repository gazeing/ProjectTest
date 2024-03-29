package com.example.clienttest.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

public class SendDataToServer {
	
	String url;
	
	
	public SendDataToServer(String url) {
		super();
		this.url = url;
	}


	public String doPost(Map<String,String> params){
		HttpOperation ho = new HttpOperation(url);
		 if (params != null) {  
	            Iterator<Entry<String, String>> iter = params.entrySet().iterator();  
	            while(iter.hasNext()) {  
	                Entry<String, String> entry = (Entry<String, String>)iter.next();  
	                //data.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
	                ho.AddValueToParams(entry.getKey(), entry.getValue());
	            }  
		 }
		
		HttpAsyncTask hat = new HttpAsyncTask(ho);
//		try {
			//String res = ho.HttpClientPostMethod("q", "helloworld");
		String res ="";
		try {
			res = hat.execute("").get();
			//res = ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			if (res==""||res==null)
				res = "No result!";
			Log.i("HTTPresponce",res);
		
		
		return res;
		
	}
	
    public String doPost( String data, String contentType)  {
    	String res="";
    	HttpOperation ho = new HttpOperation(url);
    	HttpAsyncTask hat = new HttpAsyncTask(ho);


		try {
			res = hat.execute(data,contentType).get();
			//res = ho.HttpClientPostMethod();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			if (res==""||res==null)
				res = "No result!";
			Log.i("HTTPresponce",res);
    	
    	return res;
    }
	  
 
}

