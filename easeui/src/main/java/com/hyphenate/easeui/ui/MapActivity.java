package com.hyphenate.easeui.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;

import java.util.List;

/**
 * 两个用户A, B
 * 结合环信和地图,完成共享位置
 * 对于A来说
 * 1.地图功能,获取A自己位置,获取到之后发送给好友B
 * 2.好友B位置也需要获取到:
 *
 * 对B来说:
 * 1.地图功能,获取B自己位置,获取到之后发送给好友A
 * 2.好友A位置也需要获取到:
 *
 * //页面要求的功能如下:
 * 1.定位自己的位置,将位置发送给好友
 * 2.接收好友位置消息,要求消息不能再聊天页面展示(因为这个是位置消息)
 * 3.收到好友位置后展示marker
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private LatLng mUserLatLng;
    private Button mLocateBtn;
    private MapView mBmapView;
    private EditText mEt;
    private Button mSearchBtn;
    private PoiSearch mPoiSearch = PoiSearch.newInstance();;
    private WalkNaviLaunchParam mParam;
    private String mFriend;

    public static void startAct(Context context, String friend){
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("data", friend);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mFriend = getIntent().getStringExtra("data");
        initView();
        mBaiduMap = mBmapView.getMap();


        locate();
        //地图单机时间
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点地图
                showToastShort("被点了");
                addMark(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //点击点地图覆盖物
                showToastShort(mapPoi.getName() + "被点了");
                return false;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                //ToastUtil.showToastShort("marker" + marker.getPosition());
                //发起不行导航
                walkNavi(mUserLatLng,marker.getPosition());
                return false;
            }
        });

        //注册监听去接收消息
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

            for (int i = 0; i < messages.size(); i++) {
                EMMessage emMessage = messages.get(i);
                EMCmdMessageBody body = (EMCmdMessageBody) emMessage.getBody();
                //String action="share,"+latitude+","+longitude;//action可以自定义
                String action = body.action();
                //收到好友位置消息之后,需要在地图页面将好友位置标识出来,addmarker
                String[] split = action.split(",");
                if (split != null && split.length==3){
                    if ("share".equals(split[0])){
                        //说明这个透传消息就是好友的位置
                        double latitude = Double.valueOf(split[1]);
                        double longitude = Double.valueOf(split[2]);

                        LatLng latLng = new LatLng(latitude, longitude);
                        //添加新的marker之前将旧的移除
                        mBaiduMap.clear();
                        //定义Maker坐标点

                        //构建Marker图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_end);
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions option = new MarkerOptions()
                                .position(latLng)
                                .icon(bitmap);
                        //在地图上添加Marker，并显示
                        Log.d("share", "纬度: "+latitude+",经度:"+longitude);


                        mBaiduMap.addOverlay(option);
                    }
                }
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }
        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    private void walkNavi(final LatLng start, final LatLng end) {
        // 获取导航控制类
// 引擎初始化
        WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {

            @Override
            public void engineInitSuccess() {
                //引擎初始化成功的回调
                routeWalkPlanWithParam(start,end);
            }

            @Override
            public void engineInitFail() {
                //引擎初始化失败的回调
            }
        });
    }

    //发起算路
    private void routeWalkPlanWithParam(LatLng start, LatLng end) {
//起终点位置
//构造WalkNaviLaunchParam
        mParam = new WalkNaviLaunchParam().stPt(start).endPt(end);
        //发起算路
        WalkNavigateHelper.getInstance().routePlanWithParams(mParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                //开始算路的回调
            }

            @Override
            public void onRoutePlanSuccess() {
                //算路成功
                //跳转至诱导页面
                Intent intent = new Intent(MapActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
                //算路失败的回调
            }
        });
    }

    private void addMark(LatLng latLng) {
        //定义Maker坐标点
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void initView() {
        mLocateBtn = (Button) findViewById(R.id.btn_locat);
        mLocateBtn.setOnClickListener(this);
        mBmapView = (MapView) findViewById(R.id.bmapView);
        mBmapView.setOnClickListener(this);
        mEt = (EditText) findViewById(R.id.et);
        mSearchBtn = (Button) findViewById(R.id.btn_search);
        mSearchBtn.setOnClickListener(this);
    }

    private void locate() {
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }

    @Override
    public void onClick(View v) {
        //easeui 做为library,这里不能使用switch
        int id = v.getId();
        if (R.id.btn_locat == id){
            go2LatLng(mUserLatLng);
        }else if (R.id.btn_search == id){
            search();
        }
    }

    public void showToastShort(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //poi解锁
    private void search() {
        String content = mEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            showToastShort("输入内容");
            return;
        }
        mPoiSearch.setOnGetPoiSearchResultListener(listener);
        /**
         * 以用户为中心，搜索半径10000米以内的餐厅
         */
        if (mUserLatLng != null){
            mPoiSearch.searchNearby(new PoiNearbySearchOption()
                    .location(mUserLatLng)
                    .radius(10000)
                    .keyword(content)
                    .pageNum(10));
        }
    }
    //创建POI检索监听器
    OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();

                //创建PoiOverlay对象
                PoiOverlay poiOverlay = new PoiOverlay(mBaiduMap);

                //设置Poi检索数据
                poiOverlay.setData(poiResult);

                //将poiOverlay添加至地图并缩放至合适级别
                poiOverlay.addToMap();
                poiOverlay.zoomToSpan();
            }
        }
        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };

    private void go2LatLug(LatLng latLng) {
        if (latLng != null) {
            MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(status2);
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mBmapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mUserLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mBaiduMap.setMyLocationData(locData);

            //定位到自己位置之后发送给好友，并且消息
            sendLocationMsg(location.getLatitude(), location.getLongitude());
        }
    }

    private void sendLocationMsg(double latitude, double longitude) {
        //透传消息:透传消息不会存入本地数据库中，所以在 UI 上是不会显示的
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        String action="share,"+latitude+","+longitude;//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);

        cmdMsg.setTo(mFriend);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBmapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mBmapView.onDestroy();
        mBmapView = null;
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

    }

    //将地图视图中心移动到某个位置
    private void go2LatLng(LatLng latLng) {
        if (latLng != null) {
            MapStatusUpdate status2 = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(status2);
        }
    }
}
