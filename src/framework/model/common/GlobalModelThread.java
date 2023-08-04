package framework.model.common;

import java.util.ArrayList;
import java.util.List;

public class GlobalModelThread {
	public GlobalModelThread(){
		uniqueList.set(new ArrayList<String>());
	}

	 private static ThreadLocal<List<String>> uniqueList = new ThreadLocal<List<String>>() {
         protected synchronized List<String> initialValue() {
             return new ArrayList<String>();
         }
     };


    public static List<String> getGlobalList() {
        return uniqueList.get();
    }

}
