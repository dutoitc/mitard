package ch.mno.talend.mitard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xsicdt on 29/06/15.
 */
public class Test {

	public static void main(String[] args) {
		String dataProperties="<author href=\"../../../talend.project#_Pe8X8t0CEeSVPZ1SNn4Jfw\"/>";
		//author.*?talend.project.(.*?)"
		Matcher matcherItem = Pattern.compile("author.*?talend.project.(.*?)\"").matcher(dataProperties);
		System.out.println(matcherItem.find());
		System.out.println(matcherItem.group(1));
	}
}
