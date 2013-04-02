package com.example.clienttest.login;

import com.example.clienttest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	Button btRegister;
	Button btLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
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
				finish();
				startActivity(new Intent("com.example.clienttest.MainActivity"));
//				finishActivity(getParent());
				//getParent().finish();
				
			}
		});
	}

}
