import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.concurrent.*;
public class ThemeProcessor implements Runnable{
	public ThemeProcessor(){}
	public void setLatch(CountDownLatch aCountDownLatch){
		countDownLatch = aCountDownLatch;
	}
	public void setURL(String aURL){
		url = aURL;
	}
	public void setLinks(Elements aLinks){
		links = aLinks;
	}
	public void run(){
		themes = "";
		for(Element link : links) {
  			String linkHref = link.attr("href");
  			if(linkHref.startsWith("themes")){
	  			themes += followThemesLink(url+linkHref);
	  		}
		}
		countDownLatch.countDown();
	}
	private String followThemesLink(String url){
		String themeSummary = "";
		try{
			Document doc = Jsoup.connect(url).get();
			Elements content = doc.getElementsByClass("studyGuideText");
			String contentString = content.get(0).toString();
			Document simplifiedDoc = Jsoup.parse(contentString);
			simplifiedDoc.select(".quotation").remove();
			simplifiedDoc.select(".floatingad").remove();
			simplifiedDoc.select("p").attr("style", "font-size:11px;");
			simplifiedDoc.select("h4").attr("style", "font-size:16px;font-weight:bold;");
			simplifiedDoc.select("h5").attr("style", "font-size:14px;font-weight:bold;");
			simplifiedDoc.select("h4").tagName("p");
			simplifiedDoc.select("h5").tagName("p");
			String htmlString = simplifiedDoc.html();
			themeSummary+=htmlString.substring(htmlString.indexOf("class=\"studyGuideText\">")+23,htmlString.indexOf("</div>"));
		}catch(IOException exception){
			System.out.println(exception);
		}
		return themeSummary;
	}
	public String getThemes(){
		return themes;
	}
	private CountDownLatch countDownLatch;
	private Elements links;
	private String themes;
	private String url;
}