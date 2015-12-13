package com.project.adtracker.async;

import java.io.IOException;
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
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.project.adtracker.RegisterInfo;

public class AddJsp extends AsyncTask<Void, String, Void> {


	RegisterInfo regInfo;
	public AddJsp(RegisterInfo regInfo)
	{
		this.regInfo = regInfo;
	}
	
    protected Void doInBackground(Void... param) {
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://203.253.146.198:8080/Grade2Test/JSP/addAndroid.jsp";

            HttpPost post = new HttpPost(postURL);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("st_name",  regInfo.getName()));
            params.add(new BasicNameValuePair("st_address", regInfo.getAddress()));
            params.add(new BasicNameValuePair("st_phone", regInfo.getPhone()));
            params.add(new BasicNameValuePair("st_coupon", regInfo.getCoupon()));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                Log.i("RESPONSE", EntityUtils.toString(resEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

 
}