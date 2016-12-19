package com.supr.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
/**
 * 基于 httpclient 4.3.1版本的 http工具类
 * @author mcSui
 *
 */
public class HttpTookit {
 
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
 
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(12000).setSocketTimeout(12000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }
 
    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doGet(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Accept-Language", "h-CN,zh;q=0.8");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Cookie", "cuid=8780434576; pac_uid=1_461504978; tvfe_boss_uuid=45ed13effdef7c2d; _ga=GA1.2.1774682431.1474358434; qq_slist_autoplay=on; p_o2_uin=461504978; eas_sid=41i4A860n6c4R7f5b4y2r9T5X6; _gscu_661903259=74361873d03lq711; __v3_c_review_11558=2; __v3_c_last_11558=1480672639279; __v3_c_visitor=1474362080564300; o_cookie=461504978; pgv_info=ssid=s2566788794; pgv_pvid=2900881596; qz_gdt=hukemwhfg4ez6fgi4yza; sig=h014ee0d9abafc93fc1655787312b03678e04846992f7da6b22f92d78399c7775119f37d046ddeefec9; ptcz=d3be93205c42cb3556ad0c302c30299ac98e239b447d623fb4a62f9c1ebfecc0; pt2gguin=o0461504978; uin=o0461504978; skey=@CdUS1AGTw; ptisp=ctc");
            httpGet.setHeader("Host", "mp.weixin.qq.com");
            httpGet.setHeader("refer", "http://weixin.sogou.com/");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.1717.400 QQBrowser/9.5.9637.400");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doPost(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /** 
     * 百度链接实时推送 
     * @return
     */  
    public static String post(String postUrl,String url){  
    	HttpPost httpPost = new HttpPost(postUrl);
    	String result;
		try {
			StringEntity entity = new StringEntity(url);
			httpPost.setEntity(entity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
			    httpPost.abort();
			    throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity respEntity = response.getEntity();
			result = null;
			if (respEntity != null){
			    result = EntityUtils.toString(respEntity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }  
    
    
    public static void main(String []args){
        String getData = doGet("http://www.oschina.net/",null);
        System.out.println(getData);
        System.out.println("----------------------分割线-----------------------");
        String postData = doPost("http://www.oschina.net/",null);
        System.out.println(postData);
    }

    public static String doGetFile(String url) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Host", "mmbiz.qpic.cn");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.1717.400 QQBrowser/9.5.9637.400");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
            }else {
                HttpEntity entity = response.getEntity();
                String result = null;
                if (entity != null) {
                    String postFix = ".jpg";
                    if(url.contains("=png")){
                        postFix = ".png";
                    }else if(url.contains("=jpeg")){
                        postFix = ".jpeg";
                    }else if(url.contains("=gif")){
                        postFix = ".gif";
                    }
                    String fileName = RandomUtils.getRandomStr()+postFix;
                    InputStream is = entity.getContent();
                    OutputStream os = new FileOutputStream(new File("/data/SuprBlog/webapps/ROOT/uploads/" + fileName));

                    byte[] b = new byte[1024];
                    int length = -1;
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    os.flush();
                    response.close();
                    return fileName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doXueqiu(String url) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpGet.setHeader("Accept-Language", "h-CN,zh;q=0.8");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Cookie", "s=771cr8z4l0; bid=add01093b8ddc15abbd9fe25690c716c_iudlu9t2; webp=0; xq_a_token=dc259294c63023c28170214757f31ffaaac7886b; xqat=dc259294c63023c28170214757f31ffaaac7886b; xq_r_token=4cf6e6f54b7b22c1af06a59667d812eae0e9066c; xq_token_expire=Fri%20Jan%2013%202017%2009%3A17%3A00%20GMT%2B0800%20(CST); xq_is_login=1; u=6128617541; __utma=1.1286973276.1476681438.1481874226.1482110204.33; __utmc=1; __utmz=1.1481537554.25.2.utmcsr=coriger.com|utmccn=(referral)|utmcmd=referral|utmcct=/article/html/90; Hm_lvt_1db88642e346389874251b5a1eded6e3=1481537561,1481537565,1481537569,1481783147; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1482112141");
            httpGet.setHeader("Host", "xueqiu.com");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.1952.400 QQBrowser/9.5.10026.400");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doXueqiuDetail(String url) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            HttpGet httpGet = new HttpGet(url);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpGet.setHeader("Accept-Language", "h-CN,zh;q=0.8");
            httpGet.setHeader("Connection", "keep-alive");
            httpGet.setHeader("Cookie", "s=771cr8z4l0; bid=add01093b8ddc15abbd9fe25690c716c_iudlu9t2; webp=0; xq_a_token=dc259294c63023c28170214757f31ffaaac7886b; xqat=dc259294c63023c28170214757f31ffaaac7886b; xq_r_token=4cf6e6f54b7b22c1af06a59667d812eae0e9066c; xq_token_expire=Fri%20Jan%2013%202017%2009%3A17%3A00%20GMT%2B0800%20(CST); xq_is_login=1; u=6128617541; __utma=1.1286973276.1476681438.1481874226.1482110204.33; __utmc=1; __utmz=1.1481537554.25.2.utmcsr=coriger.com|utmccn=(referral)|utmcmd=referral|utmcct=/article/html/90; Hm_lvt_1db88642e346389874251b5a1eded6e3=1481537561,1481537565,1481537569,1481783147; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1482112141");
            httpGet.setHeader("Host", "xueqiu.com");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.1952.400 QQBrowser/9.5.10026.400");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
