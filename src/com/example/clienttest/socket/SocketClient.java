package com.example.clienttest.socket;



import java.io.*;
import java.net.*;

import android.os.AsyncTask;
import android.util.Log;


public class SocketClient extends AsyncTask<String,Void,String>{
    static Socket client;
    String siteIn ="";
    int portIn = 0;
    
    public SocketClient(String site, int port){
    	siteIn=site;
    	portIn=port;
    }
    
    public String sendMsg(String msg){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println(msg);
            out.flush();
            return in.readLine();
        }catch(IOException e){
            e.printStackTrace();
            Log.i("AsyncTask", e.toString());
        }
        return "";
    }
    public void closeSocket(){
        try{
        	if (client !=null)
        		client.close();
        }catch(IOException e){
            e.printStackTrace();
            Log.i("AsyncTask", e.toString());
        }
    }
    protected void init() throws IOException{
        try{
            client = new Socket(siteIn,portIn);
            System.out.println("Client is created! site:"+siteIn+" port:"+portIn);
        }catch (UnknownHostException e){
            e.printStackTrace();
            Log.i("AsyncTask", e.toString());
        }
//        catch (IOException e){
//            e.printStackTrace();
//            
//        }
    }

    @Override
    protected void onPreExecute() {
        Log.i("AsyncTask", "onPreExecute");
    }
	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try{
			init();
			
			return sendMsg(arg0[0]);
		}
		catch(IOException e){
            Log.i("AsyncTask", e.toString());
			return "";
		}
		
		
	}
	protected void onPostExecute(String res) {
        if (isCancelled()) {
        	res = null;
        }
        closeSocket();
	}


}
