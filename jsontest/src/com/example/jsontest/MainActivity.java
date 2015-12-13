package com.example.jsontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	String Url = "http://203.253.146.198:8080/Grade2Test/JSP/resAndroid.jsp";
	TextView showText;
	Button getBtn;
    ResJsp task;
    JSONArray jArray;
    InputStream inputstream = null;
    String result="";
    String strData = "";
    String navItems[] = new String[5];
    
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        getBtn = (Button) findViewById(R.id.button);
	        getBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					task = new ResJsp();
					task.execute();
					doJSON(result);
				}
			});
	        showText = (TextView)findViewById(R.id.textview);
	       
	   }

	public class ResJsp extends AsyncTask<Void, String, Void> {

		protected Void doInBackground(Void... param) {
			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = "http://203.253.146.198:8080/Grade2Test/JSP/resAndroid.jsp";
				StringBuilder builder = new StringBuilder();
				HttpPost post = new HttpPost(postURL);

				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("UUID",
						"00000000-30b8-1686-ffff-ffffdb05456b"));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);

				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				inputstream = resEntity.getContent();
				BufferedReader bufreader = new BufferedReader(
						new InputStreamReader(inputstream, "MS949"));
				String line = null;

				while ((line = bufreader.readLine()) != null) {
					result += line;
				}
				if (resEntity != null) {
					// Log.i("RESPONSE", EntityUtils.toString(resEntity));
				}
				
				/****파싱*****/
				try {
					JSONArray jAr = new JSONArray(result);

					for (int i = 0; i < jAr.length(); i++) {
						JSONObject rank = jAr.getJSONObject(i);
						navItems[i] = rank.getString("address");
						strData += rank.getString("rank") + ":"
								+ rank.getString("address") + "\n";
					}
				} catch (JSONException e) {
					Log.d("tag", "parser error");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	public void doJSON(String result) {
		Log.i("parsing", navItems[0] + navItems[1] + navItems[2] + navItems[3]
				+ navItems[4]);
		showText.setText(strData);
	}

}

