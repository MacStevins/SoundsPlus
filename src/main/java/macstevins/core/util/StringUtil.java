package macstevins.core.util;

import java.util.Locale;

public class StringUtil {

	public static String capitalizeWord(String word) { return word.substring(0, 1).toUpperCase(Locale.ROOT).concat(word.substring(1).toLowerCase(Locale.ROOT)); }

	public static String capitalizeEachWord(String string) {
		
		StringBuilder builder = new StringBuilder();
		
		for(String s : string.split(" "))
			builder.append(capitalizeWord(s)).append(" ");
		
		return builder.toString().trim();
	
	}
	
	public static String capitalizeEachWord(String[] string) {
		
		StringBuilder builder = new StringBuilder();
		
		for(String s : string)
			builder.append(capitalizeWord(s)).append(" ");
		
		return builder.toString().trim();
	
	}

}
