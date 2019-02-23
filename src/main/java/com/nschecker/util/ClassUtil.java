package com.nschecker.util;

public class ClassUtil {

	public static String processNameForClass(String name) {
		return name.toUpperCase().replace(" ", "-");
	}
	
}
