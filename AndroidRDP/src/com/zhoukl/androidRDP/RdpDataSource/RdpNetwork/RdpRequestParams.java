package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhoukl.androidRDP.RdpUtils.MD5Security;
import com.zhoukl.androidRDP.RdpUtils.StreamTool;
import com.zhoukl.androidRDP.RdpUtils.Security.RSAUtil;
import com.zhoukl.androidRDP.RdpUtils.Security.huixinRSA.Base64Encoder;
import com.zhoukl.androidRDP.RdpUtils.Security.huixinRSA.RsaHelper;

import android.text.TextUtils;

/**
 * @description：
 * @author zhoukl(67073753@qq.com)
 * @date 2015-4-21 上午11:25:27
 */
public class RdpRequestParams {
	private static final String TAG = "RdpRequestParams";
	private static String ENCODING = "UTF-8";

	protected ConcurrentHashMap<String, String> urlParams;
	protected ConcurrentHashMap<String, FileWrapper> fileParams;
	private List<ConcurrentHashMap.Entry<String, FileWrapper>> fileInfos;
	
	//TODO：后续可以考虑使用注入方式，提高扩展性，降低耦合
	private static RSAPublicKey mRSAPublicKey;
	private static String mAESKeySeed; 

	/** 需要加密的参数标识 */
	private static HashSet<String> mEncryptkeys;
	
    public static void initEncryptkeys(HashSet<String> encryptkeys, RSAPublicKey rsaPublicKey) {
        mEncryptkeys = encryptkeys;
        mRSAPublicKey = rsaPublicKey;
    }

    public static void initEncryptkeys(HashSet<String> encryptkeys, String aesKeySeed) {
        mEncryptkeys = encryptkeys;
        mAESKeySeed = aesKeySeed;
    }
    
    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }

	public RdpRequestParams() {
		init();
	}

	public RdpRequestParams(Map<String, String> source) {
		init();

		for (Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public RdpRequestParams(String key, String value) {
		init();
		put(key, value);
	}

	public RdpRequestParams(Object... keysAndValues) throws IllegalArgumentException {
		init();
		int len = keysAndValues.length;
		if (len % 2 != 0)
			throw new IllegalArgumentException("Supplied arguments must be even");
		for (int i = 0; i < len; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			String val = String.valueOf(keysAndValues[i + 1]);
			put(key, val);
		}
	}

	public void put(String key, String value) {
		// 参数加密
		if (mEncryptkeys.contains(key)) {
			if (TextUtils.isEmpty(value)) {
				value = "0";
			}
			//value = RSAUtil.encrypt(value, mRSAPublicKey);
			value = RsaHelper.encryptDataFromStr(value, mRSAPublicKey);
		}
		urlParams.put(key, value);
	}

	public void put(String key, int value) {
		put(key, value + "");
	}
	
	public void put(String key, long value) {
		put(key, value + "");
	}
	public void put(String key, double value) {
		put(key, value + "");
	}

	public void put(String key, File file) throws FileNotFoundException {
		put(key, new FileInputStream(file), file.getName());
	}

	private void put(String key, InputStream stream) {
		put(key, stream, null);
	}

	public void put(String key, InputStream stream, String fileName) {
		put(key, stream, fileName, null);
	}

	public void put(String key, InputStream stream, String fileName, String contentType) {
		if (key != null && stream != null) {
			fileParams.put(key, new FileWrapper(stream, fileName, contentType));
		}
	}

	public void remove(String key) {
		urlParams.remove(key);
		fileParams.remove(key);
	}

		public boolean isFileUpload() {
		return fileParams.isEmpty();
	}
	
	public void clear() {
		urlParams.clear();
		fileParams.clear();
	}
		
	/**
	 * Returns an HttpEntity containing all request parameters
	 */
	public HttpEntity getEntity() {
		HttpEntity entity = null;

		if (!isFileUpload()) {
			RdpMultipartEntity multipartEntity = new RdpMultipartEntity();

			fileInfos = new ArrayList<ConcurrentHashMap.Entry<String, FileWrapper>>(fileParams.entrySet());
			Collections.sort(fileInfos, new Comparator<ConcurrentHashMap.Entry<String, FileWrapper>>() {
				public int compare(ConcurrentHashMap.Entry<String, FileWrapper> o1, ConcurrentHashMap.Entry<String, FileWrapper> o2) {
					return o2.getKey().compareTo(o1.getKey());
				}
			});

			String validateStr = getValidate();
			if (!TextUtils.isEmpty(validateStr)) {
				urlParams.put("validateKey", validateStr);
			}
			for(Entry<String, String> entry : urlParams.entrySet()){
				multipartEntity.addPart(entry.getKey(), entry.getValue());
			}

			// Add file params
			int currentIndex = 0;
			int lastIndex = fileParams.entrySet().size() - 1;
			// for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
			for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileInfos) {
				FileWrapper file = entry.getValue();
				if (file.inputStream != null) {
					boolean isLast = currentIndex == lastIndex;
					String filename = file.getFileName().substring(0,file.getFileName().lastIndexOf(".")) + entry.getKey() + file.getFileName().substring(file.getFileName().lastIndexOf("."),file.getFileName().length());
					if (file.contentType != null) {
						multipartEntity.addPart(entry.getKey(), filename, file.inputStream, file.contentType, isLast);
					}
					else {
						multipartEntity.addPart(entry.getKey(), filename, file.inputStream, isLast);
					}
				}
				currentIndex++;
			}

			entity = multipartEntity;
		}
		else {
			try {
				entity = new RdpJSONEntity(getParamsList());
//				entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}

	private String getValidate() {
		StringBuffer validateValue = new StringBuffer();
		int currentIndex = 0;
		int lastIndex = fileParams.entrySet().size() - 1;
		// for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
		for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileInfos) {
			FileWrapper file = entry.getValue();
			if (file.inputStream != null) {
			    //TODO:  ????
                boolean isLast = currentIndex == lastIndex;
                byte[] bytes = StreamTool.convertStreamToByteArray(file.inputStream);
                file.inputStream = new ByteArrayInputStream(bytes);
                String fileStr = Base64Encoder.encode(bytes);
                fileStr = MD5Security.getMd5_32(fileStr).toUpperCase();
                if (isLast) {
                    validateValue.append(fileStr);
                } else {
                    validateValue.append(fileStr).append(",");
                }
			}
			currentIndex++;
		}
		return validateValue.toString();
	}

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        // BasicNameValuePair jsonPair = new BasicNameValuePair("json",
        // sendObject.toString().trim());
        // lparams.add(jsonPair);
        return lparams;
    }
	
	public String getParamString() {
		return URLEncodedUtils.format(getParamsList(), ENCODING);
	}

	private static class FileWrapper {
		public InputStream inputStream;
		public String fileName;
		public String contentType;

		public FileWrapper(InputStream inputStream, String fileName, String contentType) {
			this.inputStream = inputStream;
			this.fileName = fileName;
			this.contentType = contentType;
		}

		public String getFileName() {
			if (fileName != null) {
				return fileName;
			}
			else {
				return "nofilename";
			}
		}

		@Override
		public String toString() {
			return "FileWrapper [fileName=" + fileName + "]";
		}
		
	}
}
