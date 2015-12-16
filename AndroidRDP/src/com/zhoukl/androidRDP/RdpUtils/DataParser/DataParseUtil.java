package com.zhoukl.androidRDP.RdpUtils.DataParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @description：提供数据格式(json/xml)的处理总接口，对外屏蔽了内部的具体实现方案。比如：具体采用org.json、gson、fastjson等根据具体情况而定，但不影响接口的使用者。
 * @author zhoukl
 * @date 2014年10月23日 上午10:42:41
 */
public class DataParseUtil {

    public static ArrayList<Object> fromJson(String json, Type typeOfT) {
        Gson mGson = new GsonBuilder().serializeNulls().create();
        return mGson.fromJson(json, typeOfT);
    }
    
    public static Object fromJson1(String json, Type typeOfT) {
        Gson mGson = new GsonBuilder().serializeNulls().create();
        return mGson.fromJson(json, typeOfT);
    }

    public static String getJsonArrayString (String jsonData, String jsonArrayName) {
		String result = "";
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonData);
			result = jsonObject.optJSONArray(jsonArrayName).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void parseJson2List(String jsonData, ArrayList<HashMap<String, String>> dataList) {
		JSONArray jsonArray;
		JSONObject jsonObject;
		try {
			jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				HashMap<String, String> data = new HashMap<String, String>();
				for (int j = 0; j < jsonObject.names().length(); j++) {
					String name = jsonObject.names().getString(j);
					data.put(name, jsonObject.getString(name));
				}
				dataList.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public static void parseJson2Map(String jsonData, HashMap<String, String> mapData) {
		// 解析JSON数据，首先要创建一个JsonReader对象
/*		JsonReader reader = new JsonReader(new StringReader(jsonData));
		try {
			reader.beginObject();
			mapData = new HashMap<String, String> ();
    		while (reader.hasNext()) {
    			mapData.put(reader.nextName(), reader.nextString());
    		}
    		reader.endObject();
    		reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Gson gson = new Gson();
		java.lang.reflect.Type listType = new TypeToken<HashMap<String, String>>(){}.getType();
		mapData = gson.fromJson(jsonData, listType); 
	}
	
/*	
	public static void parseJson2List(String jsonData, ArrayList<HashMap<String, String>> dataList) {
		Gson gson = new Gson();
		java.lang.reflect.Type listType = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
		dataList = gson.fromJson(jsonData, listType); 
	}
*/
	
	public static void parseXml2Map(String jsonData, HashMap<String, String> mapData) {
		
	}

	
}
