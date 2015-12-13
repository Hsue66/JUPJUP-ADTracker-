package kyungryun.adtracker;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;


public class MapActivity extends NMapActivity implements
        NMapView.OnMapStateChangeListener, NMapOverlayManager.OnCalloutOverlayListener
{
    public static int refresh = 0;
    // API-KEY
    public static final String API_KEY = "a36a5a441b074e83305e7cb44d1f4496";

    // 네이버 맵 객체
    NMapView mMapView = null;

    // 맵 컨트롤러
    NMapController mMapController = null;

    // 맵을 추가할 레이아웃
    LinearLayout MapContainer;

    private double longitude;
    private double latitude;
    private double altitude;
    private String mUUID;
    AsyncJsp task;

    // 오버레이의 리소스를 제공하기 위한 객체
    // NMapViewerResourceProvider mMapViewerResourceProvider = null;

    // 오버레이 관리자
    private NMapMyLocationOverlay mMyLocationOverlay;

    private NMapLocationManager mMapLocationManager;

    private NMapCompassManager mMapCompassManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mMapView = new NMapView(this);

        setContentView(mMapView);
        mMapController = mMapView.getMapController();


        mMapView.setApiKey(API_KEY);

        mMapView.setClickable(true);

        mMapView.setBuiltInZoomControls(true, null);

        super.setMapDataProviderListener(onDataProviderListener);

        // 지도에 대한 상태 변경 이벤트 연결

        mMapView.setOnMapStateChangeListener(this);

    }

    @Override
    public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {

        if (errorInfo == null) { // success

            startMyLocation();// 현재위치로 이동
            //mMapController.setMapCenter(new NGeoPoint(latitude, longitude),11);
            // mMapController.setMapCenter(new NGeoPoint(126.978371,

            // 37.5666091),

            // 11);

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

        mMapLocationManager.enableMyLocation(false);

        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);

        if (!isMyLocationEnabled)
        {

            Toast.makeText(

                    MapActivity.this,

                    "Please enable a My Location source in system settings",

                    Toast.LENGTH_LONG).show();

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

                Toast.makeText(MapActivity.this, errInfo.toString(),
                        Toast.LENGTH_LONG).show();

                return;

            } else {

                Toast.makeText(MapActivity.this, placeMark.toString(),
                        Toast.LENGTH_LONG).show();

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
                        mMapController.setMapCenter(myLocation,14);
                        refresh = 1;
                    }

                    Log.d("myLog", "myLocation  lat " + myLocation.getLatitude());

                    Log.d("myLog", "myLocation  lng " + myLocation.getLongitude());

                    latitude = myLocation.getLatitude();
                    longitude = myLocation.getLongitude();

                    SaveUUID createUUID = new SaveUUID();
                    task = new AsyncJsp();
                    task.setAsyncJsp(latitude, longitude, createUUID.getDevicesUUID(getBaseContext()));
                    task.execute();
                    //mMapController.setMapCenter(new NGeoPoint(latitude, longitude), 11);

                    findPlacemarkAtLocation(myLocation.getLongitude(),
                            myLocation.getLatitude());

                    // 위도경도를 주소로 변환

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
        return null;
    }

}