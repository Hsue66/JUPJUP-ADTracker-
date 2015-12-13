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
		// 네이버 맵 객체
		NMapView mMapView = null;
		// 맵 컨트롤러
		NMapController mMapController = null;
		// 맵을 추가할 레이아웃
		LinearLayout MapContainer;
		
	//오버레이의 리소스를 제공하기 위한 객체
		NMapViewerResourceProvider mMapViewerResourceProvider = null;

		// 오버레이 관리자
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
		
		/******************* 오버레이 관련 코드 시작 ********************/
		// 오버레이 리소스 관리객체 할당
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버레이 관리자 추가
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		
		// 오버레이 이벤트 등록
		mOverlayManager.setOnCalloutOverlayListener(this);
		/******************* 오버레이 관련 코드 끝 ********************/
		
		
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
		// 오버레이들을 관리하기 위한 id값 생성
		int markerId = NMapPOIflagType.SPOT2;
		
		// 표시할 위치 데이터를 지정한다. -- 마지막 인자가 오버레이를 인식하기 위한 id값
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
		poiData.addPOIitem(126.921539,37.550790, "친구네 집빈날", markerId, 0);
		poiData.addPOIitem(126.921704,37.552700, "트릭아이", markerId, 0);
		poiData.endPOIdata();

		// 위치 데이터를 사용하여 오버레이 생성
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		
		// id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로 지도의 중심과 ZOOM을 재설정
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
