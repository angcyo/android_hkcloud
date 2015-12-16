package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author wsd
 * @Description:封装网络请求结果
 * @date 2012-12-4 下午3:22:17
 */
public class Response {
	private HttpResponse response = null;

	/**
	 * 构造Response实例.
	 * 
	 * @param response
	 */
	public Response(HttpResponse response) {
		this.response = response;
	}

	/**
	 * 将请求结果转换为输入流.
	 * 
	 * @return
	 * @throws Exception
	 */
	public InputStream asStream() throws Exception {
		try {
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				return entity.getContent();
			}
			return null;
		}
		catch (IllegalStateException e) {
			throw new Exception(e.getMessage());
		}
		catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 将请求结果转换为字符串.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String asString() throws Exception {
		InputStream is = null;
		String result = null;
		try {
			final HttpEntity entity = response.getEntity();
			is = entity.getContent();
			result = convertStreamToString(is);
			if (is == null || result == null || result.isEmpty()) {
				throw new Exception("服务器无响应");
			}
			return result;
		}
		catch (IllegalStateException e) {
			throw new Exception(e.getMessage());
		}
		catch (IOException e) {
			throw new Exception(e.getMessage());
		}
		finally {
			closeInputStream(is);
		}
	}
	
	/*
     * 数据流转成string
     * @prama is return string
     */
    public String convertStreamToString(InputStream is) {
        byte[] byteArray = convertStreamToByteArray(is);
        if (byteArray != null && byteArray.length > 0) {
            try {
                return new String(byteArray, "UTF-8");
            }
            catch (Exception e) {
                // ignore.
            }
        }
        return "";
    }
    
    public byte[] convertStreamToByteArray(InputStream is) {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos == null ? null : baos.toByteArray();
        }
        catch (IOException e) {
            return null;
        }
        finally {
            closeOutputStream(baos);
        }
    }
    
    public void closeOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
                os = null;
            }
            catch (IOException e) {
                // ignore.
            }
        }
    }
    
    public static void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
                is = null;
            }
            catch (IOException e) {
                // ignore.
            }
        }
    }

	/**
	 * 将请求结果转换为JSONObject.
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject asJSONObject() throws Exception {
		String result = asString();
		try {
			return new JSONObject(result);
		}
		catch (JSONException jsonException) {
			//DebugTool.error("JSON格式有误: " + result, jsonException);
			throw new Exception(jsonException.getMessage());
		}
	}

	/**
	 * 将请求结果转换为JSONArray.
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray asJSONArray() throws Exception {
		String result = asString();
		try {
			return new JSONArray(asString());
		}
		catch (JSONException jsonException) {
			//DebugTool.error("JSON格式有误: " + result, jsonException);
			throw new Exception(jsonException.getMessage());
		}
	}

	public HttpEntity getEntity() throws Exception {
		try {
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				throw new Exception("获取数据失败");
			}

			return entity;
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
