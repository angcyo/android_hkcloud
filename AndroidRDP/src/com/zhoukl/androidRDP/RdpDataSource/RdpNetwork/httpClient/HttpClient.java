package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork.httpClient;

import android.content.Context;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;


//import com.fpi.epma.product.common.http.HttpClient.GzipDecompressingEntity;
/*
 * 封装网络请求操作
 * **/
public class HttpClient {
	// 请求超时设置
	public static final int CONNECTION_TIMEOUT = 30 * 1000;
	public static final int SO_TIMEOUT = 30 * 1000;
	public static final int SO_File_TIMEOUT = 120 * 1000;
	// 在发生异常时自动重试次数
	private static final int AUTO_RETRY_TIMES = 2;
	// WEB服务器地址
	// TODO: ???
	private static final String BASE_SERVER = ""; //Configuration.BASE_PUBLIC_SERVER;
	// 声明APACHE HttpClient实例
	protected static DefaultHttpClient httpClient;
	/**
	 * 请求拦截器, 设置请求支持GZIP.
	 */
	private static HttpRequestInterceptor gzipReqInterceptor = new HttpRequestInterceptor() {
		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			String gzipHead = "Accept-Encoding";
			if (!request.containsHeader(gzipHead)) {
				request.addHeader(gzipHead, "gzip");
			}
		}
	};

	/**
	 * 响应拦截器, 解码经过GZIP压缩的响应结果.
	 */
	private static HttpResponseInterceptor gzipResInterceptor = new HttpResponseInterceptor() {
		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			HttpEntity entity = response.getEntity();
			Header header = entity.getContentEncoding();
			if (header != null) {
				HeaderElement[] codecs = header.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().indexOf("gzip") >= 0) {
						response.setEntity(new GzipDecompressingEntity(response.getEntity()));
						return;
					}
				}
			}
		}
	};

	/**
	 * 请求异常自动恢复处理策略
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount > AUTO_RETRY_TIMES) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				// Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) {
				// Unknown host
				return false;
			}
			if (exception instanceof ConnectException) {
				// Connection refused
				return false;
			}
			if (exception instanceof SSLException) {
				// SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			return idempotent;
		}
	};
	protected int connectTimeout, soTimeout;
	private Context context = null;
	// HTTP cookies 集合
	private HashMap<String, String> cookies = new HashMap<String, String>();

	/**
	 * 构造HttpClient实例
	 */
	public HttpClient(Context context) {
		this.context = context;

		connectTimeout = CONNECTION_TIMEOUT;
		soTimeout = SO_TIMEOUT;
	}

	/**
	 * 添加Cookie.
	 *
	 * @param key
	 * @param value
	 */
	public void addCookie(String key, String value) {
		cookies.put(key, value);
	}

	// 添加cookies
	private void addCookieToHeader(HttpUriRequest uriRequest) {
		if (!cookies.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> cookie : cookies.entrySet()) {
				sb.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
			}
			String str = sb.toString();
			if (!(str == null)) {
				uriRequest.addHeader("Cookie", sb.toString());
			}
		}
		else {
			uriRequest.removeHeaders("Cookie");
		}
	}

	/**
	 * 清空Cookie
	 */
	public void clearCookie() {
		cookies.clear();
	}

	private URI createURI(String url) throws Exception {
		URI uri = null;
		try {
			if (!url.startsWith("http://")) {
				url = BASE_SERVER + url;
			}
			//DebugTool.warn("调用的URL" + url);
			uri = new URI(url);
		}
		catch (URISyntaxException e) {
			//DebugTool.error("URL格式不正确: " + url, e);
			throw new Exception("URL格式不正确: " + url);
		}
		return uri;
	}

	private HttpUriRequest createUriRequest(String getOrPost, URI uri, ArrayList<BasicNameValuePair> params) throws Exception {
		HttpUriRequest uriRequest = null;

		if (HttpPost.METHOD_NAME.equalsIgnoreCase(getOrPost)) {
			HttpPost post = new HttpPost(uri);
			post.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			try {
				HttpEntity requestEntity = null;
				if (params != null) {
					requestEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				}
				post.setEntity(requestEntity);
			}
			catch (Exception e) {
				throw e;
			}
			uriRequest = post;
		}
		else {
			uriRequest = new HttpGet(uri);
		}

		return uriRequest;
	}

	private String encodeParams(ArrayList<BasicNameValuePair> params) throws Exception {
		if (params == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			if (i != 0) {
				sb.append("&");
			}
			try {
				sb.append(URLEncoder.encode(params.get(i).getName(), "UTF-8")).append("=").append(URLEncoder.encode(params.get(i).getValue(), "UTF-8"));
			}
			catch (UnsupportedEncodingException e) {
				throw new Exception(e.getMessage(), e);
			}
		}

		return sb.toString();
	}

	public Response get(String url) throws Exception {
		return get(url, null);
	}

	public Response get(String url, ArrayList<BasicNameValuePair> params) throws Exception {
		if (params == null) {
			params = new ArrayList<BasicNameValuePair>();
		}

		if (url.indexOf("?") < 0) {
			url += "?" + encodeParams(params);
		}
		else {
			url += "&" + encodeParams(params);
		}

		return request(url, params, HttpGet.METHOD_NAME);
	}

	public DefaultHttpClient getHttpClient() {
		if (httpClient == null) {
			initHttpClient();
		}
		return httpClient;
	}

	/**
	 * 初始化HttpClient基本配置
	 */
	private void initHttpClient() {
		// 初始化Http参数
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(basicHttpParams, 10);  // 设置网络连接池
		HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);

		// 注册协议管理类型
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		// 创建带ThreadSafeClientConnManager的HttpClient实例
		ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
		httpClient = new DefaultHttpClient(threadSafeClientConnManager, basicHttpParams);

		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);

		/*
		 * 设置请求与响应拦截器. 使用GZIP压缩和解压请求, 可以更好的节省电池的电量.
		 */
		httpClient.addRequestInterceptor(gzipReqInterceptor, 0);
		// Support GZIP
		httpClient.addResponseInterceptor(gzipResInterceptor);
		httpClient.setHttpRequestRetryHandler(requestRetryHandler);
	}

	public Response loadImage(String imageUrl) throws Exception {
		return request(imageUrl, null, HttpGet.METHOD_NAME);
	}

	public Response post(String url) throws Exception {

		return post(url, new ArrayList<BasicNameValuePair>());
	}

	public Response post(String url, ArrayList<BasicNameValuePair> params) throws Exception {

		return post(url, params, null);
	}

	public Response post(String url, ArrayList<BasicNameValuePair> params, File file) throws Exception {
		if (params == null) {
			params = new ArrayList<BasicNameValuePair>();
		}

		return request(url, params, HttpPost.METHOD_NAME);
	}

	/**
	 * 移除代理服务器.
	 */
	public void removeProxy() {
		httpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	/**
	 * 发送网络请求.
	 *
	 * @param url
	 *            请求URL
	 * @param params
	 *            请求参数(可以为null)
	 * @param file
	 *            上传的文件(可以为null)
	 * @param getOrPost
	 *            请求方式: GET or POST
	 * @return
	 * @throws IllegalUrlException
	 * @throws HttpRequestException
	 * @throws NetworkNotFoundException
	 */
	private Response request(String url, ArrayList<BasicNameValuePair> params, String getOrPost) throws Exception {
		//DebugTool.debug("sending " + getOrPost + " request to [" + url + "]...");

		DefaultHttpClient wrapClient = getHttpClient();

		// 设置网络代理: CMNET or CMWAP.
		setupNetworkProxy();

		Response result = null;
		HttpResponse httpResponse = null;
		HttpUriRequest uriRequest = null;

		URI uri = createURI(url);

		// 创建 GET or POST uriRequest
		uriRequest = createUriRequest(getOrPost, uri, params);
		// 设置HTTP连接请求参数
		setupHTTPConnectionParams(uriRequest);

		try {
			httpResponse = wrapClient.execute(uriRequest);
			result = new Response(httpResponse);
		}
		catch (Exception e) {
			//DebugTool.error(e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}

		if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != 200) {
			//DebugTool.error("http response from [" + url + "] is null...", null);
			throw new Exception("getting HttpResponse error: " + url);
		}

		return result;
	}

	/**
	 * 设置网络请求代理服务器.
	 * <p>
	 * 当使用GPRS连接网络时, 需要设置代理为: <code>
	 *       httpClient.setProxy("10.0.0.172", 80, "http");
	 *   </code>
	 * </p>
	 *
	 * @param host
	 *            主机名或IP地址
	 * @param port
	 *            端口号
	 * @param scheme
	 *            协议(http or https)
	 */
	public void setProxy(String host, int port, String scheme) {
		HttpHost proxy = new HttpHost(host, port, scheme);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	/**
	 * 设置HTTP连接请求参数
	 */
	private void setupHTTPConnectionParams(HttpUriRequest uriRequest) {
		HttpConnectionParams.setConnectionTimeout(uriRequest.getParams(), CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(uriRequest.getParams(), SO_TIMEOUT);

		httpClient.setHttpRequestRetryHandler(requestRetryHandler);

		uriRequest.addHeader("Accept-Encoding", "gzip, deflate");
		uriRequest.addHeader("Accept-Charset", "UTF-8,*;q=0.5");
		uriRequest.addHeader("Cache-Control", "no-cache");

		addCookieToHeader(uriRequest);
	}

	private void setupNetworkProxy() throws Exception {
		// TODO: ???
		/*
		// 判断网络类型
		switch (DeviceTool.checkNetWorkType(context)) {
			case Constant.NETWORK_TYPE_NET:
				// CMNET连接, 移除代理
				removeProxy();
				break;
			case Constant.NETWORK_TYPE_WAP:
				// CMWAP连接, 设置代理
				setProxy("10.0.0.172", 80, "http");
				break;
			default:
				// 抛出无网络异常
				throw new NetWorkNotFoundException("无网络连接");
		}
		*/
	}

	/**
	 * 关闭网络连接.
	 */
	public void shutdown() {
		httpClient.getConnectionManager().shutdown();
	}

	/**
	 * GZIP实体解压缩实现类
	 */
	private static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException, IllegalStateException {
			InputStream wrappedInputStream = wrappedEntity.getContent();
			return new GZIPInputStream(wrappedInputStream);
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

}
