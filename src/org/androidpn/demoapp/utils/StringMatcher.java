package org.androidpn.demoapp.utils;

public class StringMatcher {
	
	/**
	 * 
	 * @param value 需要keyword匹配的字符串
	 * @param keyword  索引中的 #ABCDEFGHIJKLMNOPQRSTUVWXYZ中的一个
	 * @return
	 */
	 public static boolean match(String value, String keyword) {
		 
	    if (value == null || keyword == null)  
	            return false;  
	    if (keyword.length() > value.length())  
                return false; 
	    
	    int i = 0, j = 0; // i->value的指针 j->keyword的指针
		 
	    do{
	    	
	    	//例子 keyword = bc value=abcde
	    	//例子 keyword = bx value=abcde
	    	
	    	if(keyword.charAt(j)==value.charAt(i)){
	    		i++;
	    		j++;
	    		
	    	}else if(j>0){
	    		break;
	    	}else{
	    		i++;
	    	}
	    	
	    	
	    }while(i < value.length() && j < keyword.length());
	   // 如果最后j等于keyword的长度说明匹配成功  
        return (j == keyword.length()) ? true : false;  
		
	 
	 }

}
