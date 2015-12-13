package kyungryun.adtracker;

import android.os.AsyncTask;
import android.util.Log;

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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KyungRyun on 2014-12-31.
 */
public class AsyncJsp extends AsyncTask<Void, String, Void> {

    private double mlatitude;
    private double mlongitude;
    private String mUUID;
    public void setAsyncJsp(double latitude, double longitude, String deviceUUID){
        mlatitude = latitude;
        mlongitude = longitude;
        mUUID = deviceUUID;
    }

    protected Void doInBackground(Void... param) {
        try {
            HttpClient client = new DefaultHttpClient();
            String postURL = "http://203.253.146.198:8080/Grade2Test/JSP/server.jsp";

            HttpPost post = new HttpPost(postURL);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UUID",  mUUID));
            params.add(new BasicNameValuePair("Latitude", Double.toString(mlatitude)));
            params.add(new BasicNameValuePair("Longitude", Double.toString(mlongitude)));
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
