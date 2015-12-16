package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RdpJSONEntity extends StringEntity{
	public static final String CT_APPLICATION_JSON = "application/json";
	
	public RdpJSONEntity(List<BasicNameValuePair> paramsList) throws UnsupportedEncodingException {
		this(convertParamToJSON(paramsList).toString(), "UTF-8");
	}
	
	public RdpJSONEntity(List<BasicNameValuePair> paramsList, String charset) throws UnsupportedEncodingException {
		super(convertParamToJSON(paramsList).toString(), charset);
		setContentType(CT_APPLICATION_JSON);
	}
	
	public RdpJSONEntity(String json) throws UnsupportedEncodingException {
		this(json, "UTF-8");
	}

	public RdpJSONEntity(String json, String charset) throws UnsupportedEncodingException {
		super(json, charset);
		setContentType(CT_APPLICATION_JSON);
	}
	
    public static JSONObject convertParamToJSON(List<BasicNameValuePair> paramsList) {
        JSONObject jsonObject = new JSONObject();
        for (BasicNameValuePair entry : paramsList) {
            try {
                if (entry.getValue().startsWith("[") && entry.getValue().endsWith("]")) {
                    JSONArray array = new JSONArray(entry.getValue());
                    jsonObject.put(entry.getName(), array);
                } else {
                    jsonObject.put(entry.getName(), entry.getValue());
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}

