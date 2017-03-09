package com.soon.basetools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
 /*
 * 用于向指定网址POST发送数据
 */
public class SendPost {
	public String sendPostParams(String url,String params){
		PrintWriter out=null;
		System.out.println(params);
		BufferedReader bufferedReader=null;
		String result="";
		try {
			URL realurl=new URL(url);
			URLConnection conn=realurl.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(15* 1000);
			conn.setDoInput(true);
			out=new PrintWriter(conn.getOutputStream());
			out.print(params);
			out.flush();
			
			bufferedReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				result+="\n"+line;
			}
			System.out.println(result);
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "数据提交失败！请检查网络设置。";
		}catch (Exception e) {
			e.printStackTrace();
			return "数据提交失败！请检查网络设置。";
		}finally{
			try {
				if(out!=null){out.close();}
				if(bufferedReader!=null){bufferedReader.close();}
			} catch (IOException e) {
				return "数据提交失败！请检查网络设置。";
			}
		}
		return result;
	}
	public static String sendPostParamsForTM(String url,String params){
		PrintWriter out=null;
		System.out.println(params);
		BufferedReader bufferedReader=null;
		String result="";
		try {
			URL realurl=new URL(url);
			URLConnection conn=realurl.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(15* 1000);
			conn.setDoInput(true);
			out=new PrintWriter(conn.getOutputStream());
			out.print(params);
			out.flush();
			
			bufferedReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line=bufferedReader.readLine())!=null) {
				result+="\n"+line;
			}
			System.out.println(result);
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "数据提交失败！请检查网络设置。";
		}catch (Exception e) {
			e.printStackTrace();
			return "数据提交失败！请检查网络设置。";
		}finally{
			try {
				if(out!=null){out.close();}
				if(bufferedReader!=null){bufferedReader.close();}
			} catch (IOException e) {
				return "数据提交失败！请检查网络设置。";
			}
		}
		return result;
	}
}
