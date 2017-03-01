package com.hshc.sale.rest.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PrintTest {
	 public static List<String> replaceStrings(String str, Map<String,Object> parameters) {  
	        List<String> result = new ArrayList<String>();  
	        Pattern p = Pattern.compile("#\\{(.+?)\\}");  
	        Matcher m = p.matcher(str);
	        int n =2;
	        while (m.find()){
	        	System.out.println(m.group());
	        }
	        return result;  
	    }  
	public static void main(String[] args) {
		
//			Document doc;
//			try {
//				doc =  Jsoup.parse(new File("D:\\document\\word\\打印\\入库单.html"),"UTF-8");
//				System.out.println(doc.html());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
		replaceStrings("#{1}alpha#{beta}char#{gerghergergrgr}lie#{delta}def",null);
	}
}
