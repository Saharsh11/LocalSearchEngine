/**
 * 
 */
package services;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author bawankar.saharsh
 *
 */
public class OtherFunctions {

	public static Map<File, Integer> sortCall(LinkedList<Entry<File,Integer>> abc){
		return sortIt(abc);
	}

	private static Map<File, Integer> sortIt(LinkedList<Entry<File,Integer>> abc) {
		Collections.sort(abc, new Comparator<Entry<File,Integer>>() {

			@Override
			public int compare(Entry<File, Integer> o1, Entry<File, Integer> o2) {
				// TODO Auto-generated method stub
				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		 Map<File, Integer> sortedMap = new LinkedHashMap<File, Integer>();
	        for (Entry<File, Integer> entry : abc)
	        {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	        return sortedMap;
	}
	
}
