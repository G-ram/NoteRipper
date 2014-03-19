import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.concurrent.*;
public class SummaryProcessor implements Runnable{
	public SummaryProcessor(){}
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
		summary = "";
		for(Element link : links) {
  			String linkHref = link.attr("href");
  			if(linkHref.startsWith("section")){
				summary += followSectionLink(url+linkHref);
			}
		}
		countDownLatch.countDown();
	}
	private String followSectionLink(String url){
		String sectionSummary = "";
		try{
			Document doc = Jsoup.connect(url).get();
			String sectionTitle = doc.title();
			sectionTitle = sectionTitle.substring(sectionTitle.indexOf(":")+2);
			sectionTitle = sectionTitle.substring(sectionTitle.indexOf(":")+2);
			sectionSummary += "<p style='font-size:14px;'><strong>"+sectionTitle+"</strong></p>";
			Elements content = doc.getElementsByClass("studyGuideText");
			String contentString = content.get(0).toString();
			Document simplifiedDoc = Jsoup.parse(contentString);
			simplifiedDoc.select(".floatingad").remove();
			simplifiedDoc.select(".attribution").remove();
			simplifiedDoc.select("a").remove();
			simplifiedDoc.select("h4").attr("style", "font-size:12px;font-weight:bold;");
			simplifiedDoc.select("p").attr("style", "font-size:11px;");
			simplifiedDoc.select("h4").tagName("p");
			simplifiedDoc.select("blockquote").attr("style", "font-size:11px;font-style:italic");
			String htmlString = simplifiedDoc.html();
			sectionSummary+=htmlString.substring(htmlString.indexOf("class=\"studyGuideText\">")+23,htmlString.indexOf("</div>"));
		}catch(IOException exception){
			System.out.println(exception);
		}
		return sectionSummary;
	}
	public String getSummary(){
		return summary;
	}
	private CountDownLatch countDownLatch;
	private Elements links;
	private String summary;
	private String url;
}