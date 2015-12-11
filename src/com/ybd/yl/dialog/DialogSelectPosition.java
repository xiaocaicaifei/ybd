package com.ybd.yl.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ybd.common.L;
import com.ybd.common.tools.PaseJson;
import com.ybd.yl.BaseActivity;
import com.ybd.yl.R;
import com.ybd.yl.cascade.model.CityModel;
import com.ybd.yl.cascade.model.DistrictModel;
import com.ybd.yl.cascade.model.ProvinceModel;
import com.ybd.yl.cascade.service.XmlParserHandler;

/**
 * 弹出框
 * 选择省市
 * 
 * @author cyf
 * @version $Id: DialogSelectPosition.java, v 0.1 2015-11-17 上午10:00:15 cyf Exp $
 */
public class DialogSelectPosition extends BaseActivity implements OnClickListener,
                                                      OnWheelChangedListener {
    private WheelView                   mViewProvince;
    private WheelView                   mViewCity;
    //    private WheelView mViewDistrict;

    //    protected String[] mProvinceDatas;
    //    protected Map<String, String> mProvinceDatasMap = new HashMap<String, String>();
    //    protected Map<String, String> mProvinceDatasMap2 = new HashMap<String, String>();
    //    protected Map<String, String> mCitisDatasMap = new HashMap<String, String>();
    //    protected Map<String, String> mCitisDatasMap2 = new HashMap<String, String>();
    //    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    //    protected Map<String, String[]> mDistrictDatasMap2 = new HashMap<String, String[]>();
    //    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 
    protected List<Map<String, Object>> mProvinceDatasList = new ArrayList<Map<String, Object>>();
    protected List<Map<String, Object>> mCityDataDatasList = new ArrayList<Map<String, Object>>();
    protected List<Map<String, Object>> mCityDataDatasListSelect = new ArrayList<Map<String, Object>>();
    protected String                    mCurrentProviceName;
    protected String                    mCurrentProviceCode;
    protected String                    mCurrentCityName;
    protected String                    mCurrentCityCode;
    //    protected String mCurrentDistrictName ="";
    protected String                    mCurrentZipCode    = "";
    private Button                      positiveButton;                                           //确定按钮
    private Button                      negativeButton;                                           //取消按钮

    @Override
    protected void initComponentBase() {
        setContentView(R.layout.dialog_select_location);
        init();
        setUpViews();
        setUpListener();
        setUpData();
    }

    /**
     * 初始化控件
     */
    private void init() {
        positiveButton = (Button) findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(this);
        negativeButton = (Button) findViewById(R.id.negativeButton);
        negativeButton.setOnClickListener(this);
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        //        mViewDistrict = (WheelView) findViewById(R.id.id_district);
    }

    private void setUpListener() {
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        //        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas2();
        String[] mProvinceDatas = new String[mProvinceDatasList.size()];
        int i = 0;
        for (Map<String, Object> map : mProvinceDatasList) {
            mProvinceDatas[i] = map.get("provinceName").toString();
            i++;
        }
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(activity, mProvinceDatas));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        //        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        }
        //        else if (wheel == mViewDistrict) {
        //            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
        //            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        //        }
    }

    /**
     * 
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCityDataDatasListSelect.get(pCurrent).get("cityName").toString();
        mCurrentCityCode=mCityDataDatasListSelect.get(pCurrent).get("cityCode").toString();
        //        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        //        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        //
        //        if (areas == null) {
        //            areas = new String[] { "" };
        //        }
        //        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        //        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        //        mCurrentProviceName = mProvinceDatas[pCurrent];
        mCurrentProviceName = mProvinceDatasList.get(pCurrent).get("provinceName").toString();
        mCurrentProviceCode = mProvinceDatasList.get(pCurrent).get("provinceCode").toString();
        //        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : mCityDataDatasList) {
            String code = map.get("cityCode").toString();
            if (code.subSequence(0, 2).equals(mCurrentProviceCode.subSequence(0, 2))) {
                list.add(map);
            }
        }
        String[] cities = new String[list.size()];
        mCityDataDatasListSelect.clear();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String name = map.get("cityName").toString();
            mCityDataDatasListSelect.add(map);
            cities[i] = name;
        }
        //        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        //        if (cities == null) {
        //            cities = new String[] { "" };
        //        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                //            Log.v("dddd", mCurrentProviceName+"/"+mCurrentCityName);
                Intent intent = new Intent();
                intent.putExtra("name", mCurrentProviceName + "-" + mCurrentCityName);
                intent.putExtra("code", mCurrentCityCode);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.negativeButton:
                finish();
                break;
            default:
                break;
        }
    }

    protected void initProvinceDatas2() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("position.txt");
            String text = readTextFromSDcard(input);
            JSONArray array = new JSONArray(text);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String code = object.getString("code");
                String name = object.getString("name");
                Map<String, Object> map = new HashMap<String, Object>();
                if (code.substring(code.length() - 4).equals("0000")) {
                    map.put("provinceName", name);
                    map.put("provinceCode", code);
                    mProvinceDatasList.add(map);
                } else {
                    map.put("cityName", name);
                    map.put("cityCode", code);
                    mCityDataDatasList.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(EncodingUtils.getString(str.getBytes(), "utf-8"));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    //    protected void initProvinceDatas()
    //    {
    //        List<ProvinceModel> provinceList = null;
    //        AssetManager asset = getAssets();
    //        try {
    //            InputStream input = asset.open("province_data.xml");
    //            SAXParserFactory spf = SAXParserFactory.newInstance();
    //            SAXParser parser = spf.newSAXParser();
    //            XmlParserHandler handler = new XmlParserHandler();
    //            parser.parse(input, handler);
    //            input.close();
    //            provinceList = handler.getDataList();
    //            if (provinceList!= null && !provinceList.isEmpty()) {
    //                mCurrentProviceName = provinceList.get(0).getName();
    //                List<CityModel> cityList = provinceList.get(0).getCityList();
    //                if (cityList!= null && !cityList.isEmpty()) {
    //                    mCurrentCityName = cityList.get(0).getName();
    ////                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
    ////                    mCurrentDistrictName = districtList.get(0).getName();
    ////                    mCurrentZipCode = districtList.get(0).getZipcode();
    //                }
    //            }
    //            //*/
    //            mProvinceDatas = new String[provinceList.size()];
    //            for (int i=0; i< provinceList.size(); i++) {
    //                mProvinceDatas[i] = provinceList.get(i).getName();
    //                List<CityModel> cityList = provinceList.get(i).getCityList();
    //                String[] cityNames = new String[cityList.size()];
    //                for (int j=0; j< cityList.size(); j++) {
    //                    cityNames[j] = cityList.get(j).getName();
    //                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
    //                    String[] distrinctNameArray = new String[districtList.size()];
    ////                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
    ////                    for (int k=0; k<districtList.size(); k++) {
    ////                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
    ////                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
    ////                        distrinctArray[k] = districtModel;
    ////                        distrinctNameArray[k] = districtModel.getName();
    ////                    }
    //                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
    //                }
    //                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
    //            }
    //        } catch (Throwable e) {  
    //            e.printStackTrace();  
    //        } finally {
    //            
    //        } 
    //    }
}
