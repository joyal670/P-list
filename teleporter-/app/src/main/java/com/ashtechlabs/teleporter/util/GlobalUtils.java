package com.ashtechlabs.teleporter.util;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GlobalUtils {

	public static final int MODE_COURIER =0;
	public static final int MODE_TRUCKING =1;
	public static final int MODE_CARGO =2;
	public static final int MODE_STORAGE =3;

	public static final String JOB_ACTIVE="0";
	public static final String JOB_ACCEPTED="1";
	public static final String JOB_REJECTED="2";
	public static final String JOB_STARTED="3";
	public static final String JOB_ENDED="4";
	public static final String JOB_FINISHED="5";
	
	public static final float distance_limit=2000.0f;
	
	public static LatLng getLatLongFromGivenAddress(String youraddress) {
		
	    String uri = "https://maps.google.com/maps/api/geocode/json?key=AIzaSyB_cbVIYMYCTLEKOUgJLjQ9k8oL0Hx8dSs&address=" +
	                  youraddress + "&sensor=false";
	    uri=uri.replace(" ", "%20");
	    HttpGet httpGet = new HttpGet(uri);
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());
	        Log.e("Location", stringBuilder.toString());
	        
	        double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	        double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	               .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");

	        Log.d("latitude", String.valueOf(lat));
	        Log.d("longitude", String.valueOf(lng));
	       
	        return new LatLng(lat, lng);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }

	}
	
}
