package com.project.adtracker.async;

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

import com.project.adtracker.OnTaskCompleted;

import android.os.AsyncTask;
import android.util.Log;

public class ResJsp extends AsyncTask<Void, String, ArrayList<String>> {
	
	private InputStream inputstream = null;
	private String myUUID;
	private OnTaskCompleted taskCompleted;
	
	private ArrayList<String> parseLocalRank = new ArrayList<String>();
	
	private String navItems[] = new String[5];
	private String parseResult = "";
	public ResJsp(String myUUID, OnTaskCompleted taskCompleted )
	{
		this.myUUID = myUUID;
		this.taskCompleted = taskCompleted;
	}
	protected ArrayList<String> doInBackground(Void... param) {
		try {
			HttpClient client = new DefaultHttpClient();
			String postURL = "http://203.253.146.198:8080/Grade2Test/JSP/resAndroid.jsp";
			HttpPost post = new HttpPost(postURL);
			
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("UUID",
					myUUID ));
			
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
				parseResult += line;
			}
			if (resEntity != null) {
				// Log.i("RESPONSE", EntityUtils.toString(resEntity));
			}
			
			/****ÆÄ½Ì*****/
			try {
				JSONArray jAr = new JSONArray(parseResult);

				for (int i = 0; i < jAr.length(); i++) {
					JSONObject rank = jAr.getJSONObject(i);
					parseLocalRank.add(rank.getString("address"));
					
				}
			} catch (JSONException e) {
				Log.d("tag", "parser error");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Log.i("parsing", result);
		Log.i("parsing2", navItems[0] + navItems[1] + navItems[2] + navItems[3]
				+ navItems[4]);
		
	
		return parseLocalRank;
	}
	protected void onPostExecute(ArrayList<String> result){
		super.onPostExecute(result);
		if(result.size() != 0)
			taskCompleted.onTaskCompleted(result);
	}
}