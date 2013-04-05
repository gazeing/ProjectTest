package com.example.clienttest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.clienttest.database.Qrcode;
import com.example.clienttest.database.QrcodeDataOperator;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupMenu;

public class HistoryActivity extends Activity {

//	private List<String> data = new ArrayList<String>();
//	protected List<String> getData() {
//		return data;
//	}
	ArrayList<HashMap<String, String>> hashlist = new ArrayList<HashMap<String, String>>();  
	protected String getDataElement(int index){
		return hashlist.get(index).get("ItemTitle");
	}
	private ListView lView;
	int positionGlobal = 0;
	String qrDataGlobal = "";
	QrcodeDataOperator qdo;
	
	private QrcodeDataOperator getQdo() {
		return qdo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		qdo = new QrcodeDataOperator(this);
		
		
		//for test
		
		for (int i =0;i<20;i++){
			Qrcode q = new Qrcode("Test data");
			qdo.insert(q);	
		}
		
		
		
//		lView = new ListView(this);
//		this.addContentView(lView,null);
		setContentView(R.layout.history);
		lView = (ListView) findViewById(R.id.history_list);
		
		TextView tv = (TextView) findViewById(R.id.tvtitle);
		tv.setText("History");
//		AdView av = new AdView(this);
//		lView.addFooterView(av);
//		
//		FrameLayout footView = (FrameLayout) LayoutInflater.from(this) .inflate(R.layout.ad_bar, null);
//		lView.addFooterView(footView); 
		
		SimpleAdapter simpAdp = new SimpleAdapter(this,
													hashlist,R.layout.listitem,
	                                                new String[] {"ItemTitle", "ItemText"},   
	                                                new int[] {R.id.ItemTitle,R.id.ItemText});
		lView.setAdapter(simpAdp);
		lView.setBackgroundResource(R.drawable.background);

		
		lView.setOnItemClickListener(new OnItemClickListener() {  
	      	  
            @Override  
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {  
                

            	showPopup(childView);
            	positionGlobal = position;
            	qrDataGlobal = getDataElement(position);
            	
            }  
        }); 
	}
	
    @SuppressLint("NewApi")
    //the function that responds user's click on list item, show the popup menu
	protected void showPopup(View v) {  
        PopupMenu popup = new PopupMenu(this, v);  
        MenuInflater inflater = popup.getMenuInflater();  
        inflater.inflate(R.menu.action_chose, popup.getMenu());  
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	 
            @Override
            //the logic to deal with the menu selection
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch(item.getItemId()){
                case R.id.go_to_web:
                	goToTheWeb();
                	break;
                case R.id.add_to_favor:
                	addToFavorite();
                	break;
                case R.id.scan_detail:

                	showScanDetails();
                	break;
                case R.id.send_out:
                	;
                	break;
                case R.id.delete_item:
                	deleteSelectedRecord();
                	break;
                default:
                	break;
                }
                return true;
            }
        });
        popup.show();  
    }

    
    public void goToTheWeb(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null){
    		StartBrowser sb = new StartBrowser(qc.getRawdata(),this);
    		sb.startBrowse();
    	}
    }

    public void deleteSelectedRecord(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null){
    		qdo.delete(qrDataGlobal);
    		onResume();
    	}
    }
    public void addToFavorite(){
    	QrcodeDataOperator qdo = getQdo();
    	Qrcode qc = qdo.query(qrDataGlobal);
    	if (qc!=null){
    		qdo.insertToFavorite(qc);
    	}
    }

	public void showScanDetails(){
    	String text = "";
    	//SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
   
//    	QrcodeDataOperator qdo = getQdo();
//    	Qrcode qc = qdo.query(qrDataGlobal);
//    	if (qc!=null)
//    		text = "Rawdata: \n"+qc.getRawdata()+"\nHashcode: \n"+qc.getHashcode()+"\nTime: \n"+TransferTimeFormat(qc.getTimeStamp());
//    	Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
//    				.setView(new ScanDetail(this,text))
//    				.setTitle("Scan Details").show();
//    	dialog.show();
    }
    @SuppressLint("SimpleDateFormat")
	public String TransferTimeFormat(long time){
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    	return sdf.format(new Date(time));
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		hashlist.clear();

		QrcodeDataOperator qdo = getQdo();
		List<Qrcode> qrs = qdo.queryAll();
		for (Qrcode q : qrs){

	        HashMap<String, String> map = new HashMap<String, String>();  
	        map.put("ItemTitle", q.getRawdata());  
	        map.put("ItemText", TransferTimeFormat(q.getTimeStamp())); 
	        hashlist.add(map);
		}
		lView.invalidateViews();
	}

//	public void addList(String contents){
//		data.add(contents);
////		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData());
////		lView.setAdapter(adp);
//		
//	}
}
