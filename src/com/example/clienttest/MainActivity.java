package com.example.clienttest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.clienttest.encryption.StringTransfer;
import com.example.clienttest.http.HttpAsyncTask;
import com.example.clienttest.http.HttpOperation;
import com.example.clienttest.http.SendDataToServer;
import com.example.clienttest.location.CollectLocation;
import com.example.clienttest.phone_info.CollectPhoneInformation;
import com.example.clienttest.R;
import com.example.clienttest.scan.Contents;
import com.example.clienttest.scan.Intents;
import com.example.clienttest.scan.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;



import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	Button bt;
	Button btHttp;
	TextView tv;
	EditText et;
	ImageView im;
	LocationListener locationListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locationListener  = new LocationListener()  
	    {  
	  
	        @Override  
	        public void onLocationChanged(Location location)  
	        {  
	        	//CollectLocation.this.location=location; 
	        	Log.i("location= ",location.toString());
	        }  
	  
	        @Override  
	        public void onProviderDisabled(String provider)  
	        {  
	        	Log.i("locationprovider= ",provider);
	        }  
	  
	        @Override  
	        public void onProviderEnabled(String provider)  
	        {  
	        	Log.i("locationprovider= ",provider);
	        }  
	  

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				Log.i("locationstatus= ",provider +" "+ status+" "+extras.toString());
			}  
	          
	    };
		
		bt = (Button) findViewById(R.id.button1);
		bt.setText("Send");
		tv = (TextView) findViewById(R.id.textView1);
		et = (EditText) findViewById(R.id.editText1);
		im = (ImageView) findViewById(R.id.imageView1);
		
		bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
//		         SocketClient client = new SocketClient("192.168.15.108",12345);
//		         //System.out.println(client.sendMsg("nimei1"));
//		         AsyncTask<String, Void, String> txt = client.execute( et.getText().toString());
//		         try {
//					tv.setText(txt.get());
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				CollectLocation cl = new CollectLocation(v.getContext(),locationListener);
//				//cl.sentGPSLocationRequest();
//
//				String lo = cl.getLocationInfoString(cl.getLocation());
//				if ((lo==null)||(lo.length()==0))
//					lo =  "Geocoder Service is not available.";
//				tv.setText(lo);
//				int nRet = 0;
//				
//				startActivityForResult(new Intent("com.example.clienttest.location.TestMapView"),nRet);
		      
			TestGenerateQrcode();	
				
			}});
		btHttp = (Button) findViewById(R.id.button2);
		btHttp.setText("Http");
		btHttp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				

					String res ="no result";
					res = testServer();
					tv.setText(res);
	
			}
		});
	}
	@SuppressWarnings("deprecation")
	protected void TestGenerateQrcode() {
		// TODO Auto-generated method stub
		QRCodeEncoder qrCodeEncoder;
		String USE_VCARD_KEY = "USE_VCARD";
		String content = et.getText().toString();
		if (content.length()==0)
			content="test";
		
	    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
	    Display display = manager.getDefaultDisplay();
	    int width = display.getWidth();
	    int height = display.getHeight();
	    int smallerDimension = width < height ? width : height;
	    smallerDimension = smallerDimension * 5 / 8;
	    
	    Intent intent = getIntent();
	    if (intent == null) {
	      return;
	    }
	    intent.setAction(Intents.Encode.ACTION);
	    intent.putExtra(Intents.Encode.DATA, content);
	    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
	    intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE);

	    try {
	      boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
	      qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension, useVCard);
	      Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
	      if (bitmap == null) {
	        Log.w("test", "Could not encode barcode");
	        //showErrorMessage(R.string.msg_encode_contents_failed);
	        qrCodeEncoder = null;
	        return;
	      }
	      ImageView view = (ImageView) findViewById(R.id.imageView2);
	      view.setImageBitmap(bitmap);

//	      TextView contents = (TextView) findViewById(R.id.contents_text_view);
//	      if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
//	        contents.setText(qrCodeEncoder.getDisplayContents());
//	        setTitle(qrCodeEncoder.getTitle());
//	      } else {
//	        contents.setText("");
//	        setTitle("");
//	      }
	    } catch (WriterException e) {
	      Log.w("test", "Could not encode barcode", e);
	      //showErrorMessage(R.string.msg_encode_contents_failed);
	      qrCodeEncoder = null;
	    }
	}
	public String testEncryption(){
		String mac = new CollectPhoneInformation(getApplication()).getMacAddress();
		
		String plaintext = "This is a test for encryption.";
		String cipher = "";
		try {
			cipher = StringTransfer.encrypt(mac, plaintext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String res ="";
		try {
			res = StringTransfer.decrypt(mac, cipher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public String testServer(){
		String name = "test";
		String account = "test@test.com";
		String password = "testpassword";
		
		String res ="";
		
		JSONObject json = new JSONObject();  
        try {
			json.put("Name", name);
			json.put("Account", account);  
	        json.put("Password", password); 
			SendDataToServer sd = new SendDataToServer("http://192.168.15.141/RestTest.svc/json/100");
			Log.i("LoginPostData",json.toString());
			res = sd.doPost(json.toString(), "application/json");
			Log.i("LoginPostResult",res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		
		return res;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode ==0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("Url");
				Bitmap barcode = intent.getParcelableExtra("Bmp");
				tv.setText(contents);

				

				if (barcode != null)
				{
				        int width = barcode.getWidth();
				        int height = barcode.getHeight();
//				        int newWidth = 200;
//				        int newHeight = 200;
//
//				        // calculate the scale - in this case = 0.4f
//				        float scaleWidth = ((float) newWidth) / width;
//				        float scaleHeight = ((float) newHeight) / height;

				        // createa matrix for the manipulation
				        Matrix matrix = new Matrix();
				        // resize the bit map
				        matrix.postScale(width, height);
				        // rotate the Bitmap
				        matrix.postRotate(180);
				        //mOptions.inSampleSize = 2;
				        // recreate the new Bitmap
				        Bitmap resizedBitmap = Bitmap.createBitmap(barcode, 0, 0,
				                          width/2, height/2, matrix, true);

				        // make a Drawable from Bitmap to allow to set the BitMap
				        // to the ImageView, ImageButton or what ever
				        //@SuppressWarnings("deprecation")
						//BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
				        im.setImageBitmap(resizedBitmap);
				}
			
//				QrcodeDataOperator qdo = ((MainActivity) this.getParent()).getQdo();
//				qdo.insert(new Qrcode(contents));
				
			} else if (resultCode == RESULT_CANCELED) {
			// Handle cancel
				System.out.println(" scan error!");
		}
		//((MainActivity) this.getParent()).getTabHost().setCurrentTabByTag("History");
	}
	}

}
