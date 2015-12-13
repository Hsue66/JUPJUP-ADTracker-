package com.project.adtracker;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class GetListActivity extends Activity {

	private ListView mListView;
	private ArrayAdapter<String> mAdapter;

	private double longitude;
	private double latitude;

	Geocoder mCoder;
	String address; 	// 리스트에서 읽어온 주소
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list);
		
		mAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple);
		
		mListView = (ListView)findViewById(R.id.listview);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(onClickListItem);
		
		mAdapter.add("서울특별시 마포구 동교동");
		mAdapter.add("경기도 과천시 중앙동");
		mAdapter.add("서울특별시 구로구 수궁동");
		mAdapter.add("부산광역시 동구 초량3동");
		mAdapter.add("서울특별시 서대문구 창천동");
		
		
		
		mCoder = new Geocoder(this);
		
	}

	private OnItemClickListener onClickListItem = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			TextView loc = (TextView)arg1;
			String location = (String) loc.getText();
			findLnglat(location);
			
			Intent i= new Intent(GetListActivity.this, LocationMap.class);
			i.putExtra("location", location);
			i.putExtra("long",longitude);
			i.putExtra("lat",latitude);
			startActivity(i);
		}
	};
	
	private void findLnglat(String address)
	{
		List<Address> addr;
		try
		{
			addr = mCoder.getFromLocationName(address, 1);
		}catch(IOException e)
		{
	
			return;
		}
		
		if(addr.size() == 0)
		{
		
			return;
		}else
		{
			//mResult.setText(saddr);
			for(int i = 0; i<addr.size(); ++i)
			{
				Address lating = addr.get(i);
				latitude = lating.getLatitude();
				longitude = lating.getLongitude();
			}
		}
	}
	
}
