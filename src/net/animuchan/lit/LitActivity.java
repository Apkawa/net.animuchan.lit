package net.animuchan.lit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

import net.animuchan.lit.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.github.ysamlan.horizontalpager.HorizontalPager;



public class LitActivity extends Activity  implements OnClickListener {
	private String log_tag = "LitActivity";
	private String text = "";
	public static final String PREFS_NAME = "LIT";
	private SharedPreferences settings = null;
	
    /** Called when the activity is first created. */
    
	public String readService() {
		//HttpHost target = new HttpHost("lit.animuchan.net", 80);
		String result = "\n";
		try {
			URL url = new URL("http://lit.animuchan.net/json/");
	        URLConnection conn = url.openConnection();
	        // Get the response
	        BufferedReader rd = new BufferedReader(new 
	        			InputStreamReader(conn.getInputStream()));
	        String line = "";
	        while ((line = rd.readLine()) != null) {
	        	result = result + line;
		    }
	        
	        return result;
		}
		catch (Exception e)	{
			Log.e("LitActivity", "readService", e);
		}
		return result;
		
	}
	
	
	public String getRandomText() {
		String json_text = this.readService();
		Log.i(this.log_tag, "json_text: "+ json_text);
		String text = new String("FAIL");
		try {
			JSONObject json = new JSONObject(json_text);
			text = "";
			for (int i = 0; i < 4; i++) {
				text = text + json.getString(String.format("l%s", i)) + "\n";
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Log.e("LitActivity", "Error",e);
		}
		return text;
		
	}
	
	public String getCurrentText() {
		return this.text;
	}
	
	
	
	private void updateTextView() {
		this.text = this.getRandomText();
		TextView tv = (TextView)findViewById(R.id.TextView1);
        tv.setText(this.text);
        saveState();
         
        
	}
	
	private void saveState(){
		TextView tv = (TextView)findViewById(R.id.TextView1);
		this.text = (String) tv.getText();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("last_text", this.text);
		editor.commit();

	}
	private void restoreState() {
		
		
		if (this.text.length() == 0){
			updateTextView();
			saveState();
		} else {
			TextView tv = (TextView)findViewById(R.id.TextView1);
	        tv.setText(this.text);
		}
	}
	
	public void onClick(View v){
			Log.i(this.log_tag, "onClick: "+v.getId());
			switch (v.getId()){
				case R.id.TextView1:
					this.updateTextView();
					break;
				case R.id.Background:
					this.updateTextView();
					break;
			}
		}

//	@Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        HorizontalPager realViewSwitcher = new HorizontalPager(getApplicationContext());
//        
//        realViewSwitcher.addView(this.findViewById(R.id.Background));
//	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_NAME, 0);
        
        
        if (savedInstanceState != null) {
	        this.text = savedInstanceState.getString("last_text");
	        if (this.text == null) {
	        	this.text = "";
	        }
        } else {
        	this.text = "";// settings.getString("last_text", "");
        }
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);
        
        TextView tv = (TextView)findViewById(R.id.TextView1);
        tv.setOnClickListener(this);
        
        this.findViewById(R.id.Background).setOnClickListener(this);
       

        
                
        restoreState();
        
    }
    @Override
    protected void onStop(){
       super.onStop();
       saveState();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("last_text", this.text);
    }

       
}