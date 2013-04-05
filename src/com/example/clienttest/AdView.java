package com.example.clienttest;

import android.content.Context;
import android.view.View;

public class AdView extends View{

	@SuppressWarnings("static-access")
	public AdView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if(inflater != null){       
//            inflater.inflate(R.layout.ad_bar, this);
//        }
		this.inflate(context,R.layout.ad_bar,null);
		 //context.getLayoutInflater().inflate( R.layout.ad_bar, null );
	}

}
