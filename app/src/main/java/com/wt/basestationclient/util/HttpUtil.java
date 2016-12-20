package com.wt.basestationclient.util;

import android.net.http.AndroidHttpClient;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by teng on 20/12/16.
 */
public class HttpUtil {

    public static String doGet(String url) {
        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("Android");
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute(ClientContext.COOKIE_STORE,new BasicCookieStore());

        try {
            HttpGet httpGet = new HttpGet(new URI(url));
            HttpResponse httpResponse = androidHttpClient.execute(httpGet,basicHttpContext);
            HttpEntity httpEntity = httpResponse.getEntity();
            String httpResult = EntityUtils.toString(httpEntity);
            LogUtil.i(httpResult);
            return httpResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String url,Map<String,String> paramMap) {
        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("Android");
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute(ClientContext.COOKIE_STORE,new BasicCookieStore());

        try {
            HttpPost httpPost = new HttpPost(new URI(url));
            List<NameValuePair> list = new ArrayList<>();
            for (String mapKey : paramMap.keySet()) {
                list.add(new BasicNameValuePair(mapKey,paramMap.get(mapKey)));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
            HttpResponse httpResponse = androidHttpClient.execute(httpPost, basicHttpContext);
            HttpEntity httpEntity = httpResponse.getEntity();
            String httpResult = EntityUtils.toString(httpEntity);
            LogUtil.i(httpResult);
            return httpResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
