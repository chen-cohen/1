import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chen2 {

	public static void main(String[] args) throws IOException
	{
		System.out.println("Hello World!");

		Connection connection = Jsoup.connect("http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js");
		Document htmlDocument = connection.get();

//		String regex = "\"http://[^\\s]*\"";
		String regex = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(htmlDocument.toString());
		while (m.find()) {
			System.out.println(m.group());
		}
	}

//	Pattern p = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
//	Matcher matcher = p.matcher(htmlDocument.toString());
//	while (matcher.find()) {
//		System.out.println(matcher.group());
//	}


}