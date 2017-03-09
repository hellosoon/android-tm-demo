package com.soon.basetools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import org.apache.http.conn.ConnectTimeoutException;

public class SendGet {
	/*
	 * 用于从一个网址获取数据。
	 */
	
	public String Doget(String url){
		BufferedReader bufferedReader=null;
		String result="";
		try {
			URL realurl=new URL(url);
			URLConnection conn=realurl.openConnection();
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
			conn.connect();
			conn.setConnectTimeout(15*1000);
			conn.setReadTimeout(15*1000);
			Map<String,List<String>> map=conn.getHeaderFields();
			for(String key:map.keySet()){
				System.out.println(key+"---->"+map.get(key));
			}
			
			bufferedReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				result+="\n"+line;
			}
		}catch (ConnectTimeoutException e) {
			System.out.println("GET请求超时！"+e);
			e.printStackTrace();
			return "请求超时，请检查网络设置！";
		}catch (SocketTimeoutException e) {
			System.out.println("GET请求超时！"+e);
			e.printStackTrace();
			return "请求超时，请检查网络设置！";
		}catch (Exception e) {
			System.out.println("GET请求异常！"+e);
			e.printStackTrace();
			return "网络异常，请检查网络设置！";
		}
		finally{
			try {
				if(bufferedReader!=null){bufferedReader.close();}
			} catch (IOException e) {
			}
		}
		return result;
	}

}
