package net.animuchan.lit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONException;





public class LitActivity extends Activity
    implements OnClickListener {

	private String log_tag = "LitActivity";
	private String text = "";
	private static final String PREFS_NAME = "LIT";
	private SharedPreferences settings = null;
	
	private class GetTask extends
			AsyncTask<Void, Void, String> {
		private ProgressDialog _progressDialog;
		
		public GetTask() {
			_progressDialog = ProgressDialog.show(LitActivity.this,
					"Loading...",
					"Please wait", true);
			
		}
		
		@Override
		protected String doInBackground(Void... params){
			return LitActivity.this.getRandomText();
		}
		
		protected void onPostExecute(String text) {
			LitActivity.this.setCurrentText(text);
			_progressDialog.dismiss();
		}
	}
    /** Called when the activity is first created. */
    
	public String readService() {
		String result = "\n";
		try {
			URL url = new URL("http://lit.animuchan.net/json/");
	        URLConnection conn = url.openConnection();
	        BufferedReader rd = new BufferedReader(new
	        			InputStreamReader(conn.getInputStream()));
	        String line;
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
		String text = "FAIL";
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
	
	public void setCurrentText(String text){
		this.text = text;
		TextView tv = (TextView)findViewById(R.id.TextView1);
        tv.setText(this.text);
	}
	
	
	private void updateTextView() {
		this.setCurrentText(this.getRandomText());
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
			GetTask task = new GetTask();
			switch (v.getId()){
				case R.id.TextView1:
					task.execute();
					//this.updateTextView();
					break;
				case R.id.Background:
					task.execute();
					//this.updateTextView();
					break;
			}
		}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_NAME, 0);
        
        
        if (savedInstanceState != null && savedInstanceState.containsKey("last_text")) {
	        this.text = savedInstanceState.getString("last_text");
	        if (this.text == null) {
	        	this.text = "";
	        }
        } else {
        	this.text = settings.getString("last_text", "");
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
        saveState();
        savedInstanceState.putString("last_text", this.text);
    }

       
}