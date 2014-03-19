import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
public class LinkProcessor{
	public LinkProcessor(){}
	public void setURL(String aURL){
		url = aURL;
		header = "";
	}
	public void processLinks(){
		try{
			Document doc = Jsoup.connect(url).get();
			String title = doc.title();
			title = title.substring(12);
			header += "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'><HTML><HEAD>";
			header += "<TITLE>"+title+"</TITLE></HEAD><BODY>";
			header += "<h1>"+title+"</h1>";
			Elements authorElements = doc.getElementsByClass("author");
			String author =authorElements.get(0).text();
			header += "<p style='font-size:11px;'><strong>"+author+"</strong></p>";
			Elements content = doc.getElementsByClass("study-guide-TOC");
			links = content.get(0).getElementsByTag("a"); 
		}catch(IOException exception){
			System.out.println(exception);
		}
	}
	public Elements getLinks(){
		return links;
	}
	public String getHeader(){
		return header;
	}
	private Elements links;
	private String header;
	private String url;
}