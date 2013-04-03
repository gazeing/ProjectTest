package com.example.clienttest.login;

import org.json.JSONObject;

import com.example.clienttest.R;
import com.example.clienttest.http.SendDataToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	Button btRegister;
	Button btLogin;
	EditText etName,etAccount, etPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		etName = (EditText) findViewById(R.id.nameEdit);
		etAccount = (EditText) findViewById(R.id.accountEdit);
		etPassword= (EditText) findViewById(R.id.passwordEdit);
		
		btRegister = (Button) this.findViewById(R.id.btn_register);
		btRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.example.clienttest.login.RegisterActivity"));
				
			}
		});
		
		btLogin = (Button) this.findViewById(R.id.btn_login);
		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			try {	
				String name = etName.getText().toString();
				String account = etAccount.getText().toString();
				String password = etPassword.getText().toString();
				
				JSONObject json = new JSONObject();  
                json.put("Name", name);  
                json.put("Account", account);  
                json.put("Password", password); 
                
//                String result = client.doPost(mServerBaseUrl + SET_DATA,   
//                        json.toString(), "application/json"); 
                
				SendDataToServer sd = new SendDataToServer("http://49.156.19.75");
				Log.i("LoginPostData",json.toString());
				String res = sd.doPost(json.toString(), "application/json");
				Log.i("LoginPostResult",res);
				if (res.compareTo("No result!")!=0){
					finish();
					startActivity(new Intent("com.example.clienttest.MainActivity"));
				}
				else{
					Toast.makeText(getBaseContext(), "Wrong password.", Toast.LENGTH_SHORT).show();
				}

//				finishActivity(getParent());
				//getParent().finish();
			}
			catch(Exception e)
			{
				Log.i("LoginPost",e.toString());
			}
				
			}
		});
	}

}
