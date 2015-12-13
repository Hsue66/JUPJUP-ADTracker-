package com.project.adtracker;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;

public class LocationMap extends NMapActivity 
		implements OnMapStateChangeListener, OnCalloutOverlayListener{

	// API-KEY
		public static final String API_KEY = "502329deec4e80c62825d26529ee52fb";
		// ���̹� �� ��ü
		NMapView mMapView = null;
		// �� ��Ʈ�ѷ�
		NMapController mMapController = null;
		// ���� �߰��� ���̾ƿ�
		LinearLayout MapContainer;
		
	//���������� ���ҽ��� �����ϱ� ���� ��ü
		NMapViewerResourceProvider mMapViewerResourceProvider = null;

		// �������� ������
		private	NMapOverlayManager mOverlayManager;
		
		private NMapMyLocationOverlay mMyLocationOverlay;
		
		private NMapLocationManager mMapLocationManager;

		private NMapCompassManager mMapCompassManager;
		
		private double longitude;
		private double latitude;
		private String locationName;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		locationName = getIntent().getExtras().getString("location");
		
		setTitle(locationName);
		
		setContentView(R.layout.location_map);
		
		MapContainer = (LinearLayout) findViewById(R.id.LocationMapContainer);
		
		mMapView = new NMapView(this);
		
		mMapController = mMapView.getMapController();
		
		mMapView.setApiKey(API_KEY);
		
		MapContainer.addView(mMapView);
		
		mMapView.setClickable(true);

		mMapView.setBuiltInZoomControls(true, null);
		
		mMapView.setOnMapStateChangeListener(this);
		
		/******************* �������� ���� �ڵ� ���� ********************/
		// �������� ���ҽ� ������ü �Ҵ�
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// �������� ������ �߰�
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		
		// �������� �̺�Ʈ ���
		mOverlayManager.setOnCalloutOverlayListener(this);
		/******************* �������� ���� �ڵ� �� ********************/
		
		
	}
	
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {

		if (errorInfo == null) // success
		{ 
			longitude = getIntent().getDoubleExtra("long", 126.000000);
			latitude = getIntent().getDoubleExtra("lat", 37.556107);
			
			mMapController.setMapCenter(new NGeoPoint(longitude,latitude ),12);
			// mMapController.setMapCenter(new NGeoPoint(126.923967 , 37.556107),12);

			putSpotAd(longitude,latitude);
			
		} else { // fail

			android.util.Log.e("NMAP",

			"onMapInitHandler: error=" + errorInfo.toString());

		}

	}

	public void putSpotAd(double longitude, double latitude)
	{
		// �������̵��� �����ϱ� ���� id�� ����
		int markerId = NMapPOIflagType.SPOT2;
		
		// ǥ���� ��ġ �����͸� �����Ѵ�. -- ������ ���ڰ� �������̸� �ν��ϱ� ���� id��
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		poiData.addPOIitem(126.921539,37.550790, "ģ���� ����", markerId, 0);
		poiData.addPOIitem(126.921704,37.552700, "Ʈ������", markerId, 0);
		poiData.endPOIdata();

		// ��ġ �����͸� ����Ͽ� �������� ����
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		
		// id���� 0���� ������ ��� �������̰� ǥ�õǰ� �ִ� ��ġ�� ������ �߽ɰ� ZOOM�� �缳��
	//	poiDataOverlay.showAllPOIdata(0);
	}
	
	
	@Override
	public void onZoomLevelChange(NMapView mapview, int level) {}

	@Override
	public void onMapCenterChange(NMapView mapview, NGeoPoint center) {}

	
	@Override
	public void onAnimationStateChange(
					NMapView arg0, int animType, int animState) {}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {}

	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(this, arg1.getTitle(), Toast.LENGTH_SHORT).show();
		return null;
	}

}
