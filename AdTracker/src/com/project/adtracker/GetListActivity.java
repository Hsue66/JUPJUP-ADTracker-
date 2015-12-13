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
	String address; 	// ����Ʈ���� �о�� �ּ�
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list);
		
		mAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple);
		
		mListView = (ListView)findViewById(R.id.listview);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(onClickListItem);
		
		mAdapter.add("����Ư���� ������ ������");
		mAdapter.add("��⵵ ��õ�� �߾ӵ�");
		mAdapter.add("����Ư���� ���α� ���õ�");
		mAdapter.add("�λ걤���� ���� �ʷ�3��");
		mAdapter.add("����Ư���� ���빮�� âõ��");
		
		
		
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
