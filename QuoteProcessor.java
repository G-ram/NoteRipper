import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.concurrent.*;
public class QuoteProcessor implements Runnable{
	public QuoteProcessor(){}
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
		quotes = "";
		for(Element link : links) {
  			String linkHref = link.attr("href");
  			if(linkHref.startsWith("quotes")){
				quotes += followQuotesLink(url+linkHref);
			}
		}
		countDownLatch.countDown();
	}
	private String followQuotesLink(String url){
		String quoteSummary = "";
		try{
			Document doc = Jsoup.connect(url).get();
			Elements content = doc.getElementsByClass("studyGuideText");
			String contentString = content.get(0).toString();
			Document simplifiedDoc = Jsoup.parse(contentString);
			simplifiedDoc.select("a").remove();
			simplifiedDoc.select("span").remove();
			simplifiedDoc.select("blockquote").attr("style", "font-size:11px;font-style:italic");
			simplifiedDoc.select("p").attr("style", "font-size:11px;");
			String htmlString = simplifiedDoc.html();
			quoteSummary+=htmlString.substring(htmlString.indexOf("class=\"studyGuideText\">")+23,htmlString.indexOf("</body>")-8);
		}catch(IOException exception){
			System.out.println(exception);
		}
		return quoteSummary;
	}
	public String getQuotes(){
		return quotes;
	}
	private CountDownLatch countDownLatch;
	private Elements links;
	private String quotes;
	private String url;
}