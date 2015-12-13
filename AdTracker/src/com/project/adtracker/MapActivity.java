package com.project.adtracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.project.adtracker.async.ReqJsp;
import com.project.adtracker.async.ResJsp;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends NMapActivity implements 
				OnTaskCompleted,OnMapStateChangeListener, OnCalloutOverlayListener
{
    JSONArray jArray;
    String result="";
    ArrayList<String> navItems = new ArrayList<String>();
	
    
	//private String[] navItems = { "서울특별시 마포구 동교동", "경기도 과천시 중앙동", "서울특별시 구로구 수궁동",
		//	"부산광역시 동구 초량3동", "서울특별시 서대문구 창천동" };

	private ListView lvNavList;
	private FrameLayout flContainer;
	
	private DrawerLayout dlDrawer;
	private ActionBarDrawerToggle dtToggle;
	
	
	private double longitude;
	private double latitude;

	Geocoder mCoder;
	String address; 	// 리스트에서 읽어온 주소
	
	public static int refresh = 0;
	// API-KEY
	public static final String API_KEY = "502329deec4e80c62825d26529ee52fb";
	// 네이버 맵 객체
	private NMapView mMapView = null;
	// 맵 컨트롤러
	private NMapController mMapController = null;
	// 맵을 추가할 레이아웃
	private LinearLayout MapContainer;

	
	// 위치정보를 위해 사용할 인자
	private SaveUUID createUUID;
	private String myUUID = "";
    private String doName = "";
    private String siName = "";
    private String dongName = "";
    private ReqJsp task;
    
    
	//오버레이의 리소스를 제공하기 위한 객체
	NMapViewerResourceProvider mMapViewerResourceProvider = null;

	// 오버레이 관리자
	
	private	NMapOverlayManager mOverlayManager;
	
	private NMapMyLocationOverlay mMyLocationOverlay;
	
	private NMapLocationManager mMapLocationManager;

	private NMapCompassManager mMapCompassManager;
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// 백그라운드
		startService(new Intent("com.project.adtracker"));
		
		
		MapContainer = (LinearLayout) findViewById(R.id.MapContainer);
		
		mMapView = new NMapView(this);
	
		//setContentView(mMapView);
		mMapController = mMapView.getMapController();

		
		mMapView.setApiKey(API_KEY);
		
		// 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
		MapContainer.addView(mMapView);

		
		mMapView.setClickable(true);

		mMapView.setBuiltInZoomControls(true, null);

		super.setMapDataProviderListener(onDataProviderListener);

		// 지도에 대한 상태 변경 이벤트 연결

		mMapView.setOnMapStateChangeListener(this);
		
		/******************* 오버레이 관련 코드 시작 ********************/
		// 오버레이 리소스 관리객체 할당
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버레이 관리자 추가
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
		
		// 오버레이 이벤트 등록
		mOverlayManager.setOnCalloutOverlayListener(this);
		/******************* 오버레이 관련 코드 끝 ********************/
		
		/***UUID***/
		createUUID = new SaveUUID();
		myUUID = createUUID.getDevicesUUID(getBaseContext());

		/******************* 드로워 용 ********************/
		mCoder = new Geocoder(this);
		
		
		lvNavList = (ListView) findViewById(R.id.activity_main_nav_list);
		flContainer = (FrameLayout) findViewById(R.id.activity_main_container);
		
		lvNavList.setAdapter(new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, navItems));
		lvNavList.setOnItemClickListener(new DrawerItemClickListener());

		dlDrawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
		dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
				R.drawable.tog, R.string.open_drawer,
				R.string.close_drawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

		};
		dlDrawer.setDrawerListener(dtToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		/******************* 비동기 작업 ****************************/
		new ResJsp(myUUID, this).execute();
		
		task = new ReqJsp();
        task.execute();
        
        
	}
	

	
	@Override
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {

		if (errorInfo == null) { // success
			
			startMyLocation();// 현재위치로 이동

		} else { // fail

			android.util.Log.e("NMAP",

			"onMapInitHandler: error=" + errorInfo.toString());

		}

	}

	@Override
	public void onZoomLevelChange(NMapView mapview, int level) {

	}

	@Override
	public void onMapCenterChange(NMapView mapview, NGeoPoint center) {

	}

	@Override
	public void onAnimationStateChange(NMapView arg0, int animType,

	int animState) {

	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {

	}

	private void startMyLocation() 
	{
		mMapLocationManager = new NMapLocationManager(this);
		
		mMapLocationManager.setUpdateFrequency(1800*1000, 0);
		
		mMapLocationManager.enableMyLocation(false);
		
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);

		if (!isMyLocationEnabled) 
		{

			Toast.makeText(MapActivity.this,
					"Please enable a My Location source in system settings",Toast.LENGTH_LONG).show();

			Intent goToSettings = new Intent(
			Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(goToSettings);
			finish();
		}
		else {

		}
	}

	private void stopMyLocation() {

		if (mMyLocationOverlay != null) {

			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);
				mMapCompassManager.disableCompass();
				mMapView.setAutoRotateEnabled(false, false);
				MapContainer.requestLayout();
			}
		}
	}

	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark,
				NMapError errInfo) {

			if (errInfo != null) {

				Log.e("myLog", "Failed to findPlacemarkAtLocation: error="
						+ errInfo.toString());
				return;
			} 
			else {

				//Toast.makeText(MapActivity.this, placeMark.toString(),
					//	Toast.LENGTH_LONG).show();
				doName = placeMark.doName;
				siName = placeMark.siName;
				dongName = placeMark.dongName;
				
				/**********주소설정**********/
				task.setAsyncJsp(myUUID, doName, siName, dongName);
				
			}
		}
	};

	private final NMapLocationManager.OnLocationChangeListener 
				onMyLocationChangeListener = 
					new NMapLocationManager.OnLocationChangeListener() {
		
		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager,
				NGeoPoint myLocation) {
			if (mMapController != null && refresh == 0) {

				mMapController.animateTo(myLocation);
				mMapController.setMapCenter(myLocation,13);
				
				
				// 오버레이들을 관리하기 위한 id값 생성
				int markerId = NMapPOIflagType.PIN;

				// 표시할 위치 데이터를 지정한다. -- 마지막 인자가 오버레이를 인식하기 위한 id값
				NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
				poiData.beginPOIdata(1);
				poiData.addPOIitem(myLocation, "현위치", markerId, 0);
				
				poiData.endPOIdata();

				// 위치 데이터를 사용하여 오버레이 생성
				NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
				
				// id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로 지도의 중심과 ZOOM을 재설정
				poiDataOverlay.showAllPOIdata(0);
				
				refresh = 1;
			}

			Log.d("myLog", "myLocation  lat " + myLocation.getLatitude());
			Log.d("myLog", "myLocation  lng " + myLocation.getLongitude());
			findPlacemarkAtLocation(myLocation.getLongitude(),
					myLocation.getLatitude());

			return true;

		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

		}

		@Override
		public void onLocationUnavailableArea(

		NMapLocationManager locationManager, NGeoPoint myLocation) {

			/*
			 * Toast.makeText(NaverMyLocation.this,
			 * 
			 * "Your current location is unavailable area.",
			 * 
			 * Toast.LENGTH_LONG).show();
			 */
			stopMyLocation();

		}

	};

	@Override
	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(this, arg1.getTitle(), Toast.LENGTH_SHORT).show();
		return null;
	}
	
	
	/******************* 액션바 토글 ********************/
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		dtToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		dtToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener 
	{
		
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) 
		{
			findLnglat(navItems.get(position));
			Intent i= new Intent(MapActivity.this, LocationMap.class);
			i.putExtra("location", navItems.get(position));
			i.putExtra("long",longitude);
			i.putExtra("lat",latitude);
				
			dlDrawer.closeDrawer(lvNavList);
			startActivity(i);
				
		}
	
	}
	
	/******************* 좌표값 찾기 ********************/
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
	
	/**액션바 붙이기**/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_list, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**액션바 버튼 선택시 동작**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items

		if (dtToggle.onOptionsItemSelected(item)) {
			return true;
		}
	    switch (item.getItemId()) {
	        case R.id.menu_item_refresh:
	            refresh=0;
	            return true;
	        case R.id.menu_regAd:
	        	Intent i= new Intent(MapActivity.this, RegistrationAd.class);
	        	startActivity(i);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**종료 시키기**/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         
        switch (keyCode) {
        //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
        case KeyEvent.KEYCODE_BACK:
             
            //Toast.makeText(this, "한번 더 누르시면 백그라운드로 전환됩니다.", Toast.LENGTH_SHORT).show();
             
            new AlertDialog.Builder(this)
            .setTitle("프로그램 종료")
            .setMessage("프로그램을 종료 하시겠습니까?\n백그라운드모드로 전환하시려면 홈키를 누르세요.")
            .setPositiveButton("예", new DialogInterface.OnClickListener() {             
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 프로세스 종료.
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            })
            .setNegativeButton("아니오", null)
            .show();         
            break;
        default:
            break;
        }
         
        return super.onKeyDown(keyCode, event);
    }


    /*********************ResJsp로부터 리스트 받아오기**********************/
	@Override
	public void onTaskCompleted(ArrayList<String> navItems) {
		// TODO Auto-generated method stub
		this.navItems = navItems;
		lvNavList.setAdapter(new ArrayAdapter<String>(MapActivity.this,
				android.R.layout.simple_list_item_1, this.navItems));
	}

	
}