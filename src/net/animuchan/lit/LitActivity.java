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
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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


public class LitActivity extends Activity  implements OnClickListener{
	private String log_tag = "LitActivity";
	
    /** Called when the activity is first created. */
    
	public String readService() {
		//HttpHost target = new HttpHost("lit.animuchan.net", 80);
		String result = "";
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
		//String json_text = "{\"l3\": \"\u043c\u043e\u0437\u0433\u0438 \u043f\u043e \u043f\u043e\u043b\u0443 \u0440\u0430\u0441\u0442\u0435\u043a\u043b\u0438\u0441\u044c\", \"l2\": \"\u043d\u043e\u0433\u043e\u0439 \u0437\u0430 \u0447\u0442\u043e \u0442\u043e \u0437\u0430\u0446\u0435\u043f\u0438\u043b\u0441\u044f\", \"id\": 212.0, \"l0\": \"\u043e\u043b\u0435\u0433 \u043b\u0438\u0445\u0438\u043c \u043a\u0430\u0432\u0430\u043b\u0435\u0440\u0438\u0441\u0442\u043e\u043c\", \"l1\": \"\u0431\u0440\u0430\u043b \u0442\u0443\u0440\u043d\u0438\u043a\u0435\u0442 \u0432 \u043c\u0435\u0442\u0440\u043e \u043d\u043e \u0440\u0430\u0437\"}";
		String json_text = this.readService();
		Log.i(this.log_tag, "json_text: "+ json_text);
		String text = new String("FAIL");
		try {
			JSONObject json = new JSONObject(json_text);
			//text = json.getString("l0");
		
			text = "";
			for (int i = 0; i < 3; i++) {
				text = text + json.getString(String.format("l%s", i)) + "\n";
			}
		} catch (JSONException e) {
			// TODO: handle exception
			Log.e("LitActivity", "Error",e);
		}
		return text;
		
	}
	
	private void updateTextView() {
		TextView tv = (TextView)findViewById(R.id.TextView1);
        tv.setText(this.getRandomText());
	}
	
	public void onClick(View v){
			Log.i(this.log_tag, "onClick: "+v.getId());
			switch (v.getId()){
				case R.id.TextView1:
					this.updateTextView();
					break;
			}
		}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView tv = (TextView)findViewById(R.id.TextView1);
        tv.setOnClickListener(this);
        
        this.updateTextView();
        
    }
}