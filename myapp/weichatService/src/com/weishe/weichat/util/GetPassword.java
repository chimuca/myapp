package com.weishe.weichat.util;

import org.apache.commons.codec.digest.DigestUtils;

public class GetPassword {
	 public static void main(String[] args){
	
		String password = "cama101025" ;
		 
		 System.out.print(DigestUtils.md5Hex(password).toLowerCase()+"\n");
		 System.out.print(DigestUtils.md5Hex(password).toUpperCase());
	 }

}
