import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.*;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import javax.swing.border.Border;
public class Ripper{
	public static void main(String[] args){
		finishedHTML = "";
		final LinkProcessor linkProcessor = new LinkProcessor();
		final SummaryProcessor summaryProcessor = new SummaryProcessor();
		final ThemeProcessor themeProcessor = new ThemeProcessor();
		final QuoteProcessor quoteProcessor = new QuoteProcessor();
		RipperUI ripperUI = new RipperUI();
		final JFrame ripperFrame = new JFrame();
		ripperFrame.setLayout(new FlowLayout());
	    ripperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ripperFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		JLabel logoLabel = new JLabel(new ImageIcon("sparknotes_logo.png"));
		JLabel ripperTitle = ripperUI.makeRipperLabel("NoteRipper",32);
		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new FlowLayout());
		logoPanel.add(logoLabel);
		logoPanel.add(ripperTitle);
		final JTextField urlField = ripperUI.makeRipperTextField(28,776,"http://www.sparknotes.com/lit/fellowship/");
		final JButton generateButton = ripperUI.makeRipperButton(188, 50, "Generate",new Color(53,185,236));
		final JButton saveButton = ripperUI.makeRipperButton(188, 50, "Save",new Color(53,185,236));
		saveButton.setEnabled(false);
		final JButton printerButton = ripperUI.makeRipperButton(188, 50, "Print",new Color(53,185,236));
		printerButton.setEnabled(false);
		final JButton resetButton = ripperUI.makeRipperButton(188, 50, "Reset",Color.RED);
		resetButton.setEnabled(false);
		final FrameAnimation frameAnimation = new FrameAnimation(ripperFrame);
		JPanel buttonPanel = new JPanel();
			buttonPanel.add(generateButton);
			buttonPanel.add(saveButton);
			buttonPanel.add(printerButton);
			buttonPanel.add(resetButton);
		progressBar = ripperUI.makerRipperProgressBar(776,30);
		preview = new JEditorPane();
		preview.setEditable(false);
		preview.setContentType("text/html");
		final JScrollPane previewPane = new JScrollPane(preview);
			previewPane.setPreferredSize(new Dimension(776,500));
			Border previewBorder = BorderFactory.createLineBorder(new Color(53,185,236), 2);
        	previewPane.setBorder(previewBorder);
	    JPanel ripperToolPanel = new JPanel();
	    	ripperToolPanel.setLayout(new GridBagLayout());
	    	c = new GridBagConstraints();
	    	c.anchor = GridBagConstraints.NORTHWEST;
	    	c.gridwidth = 1;
			c.gridheight = 1;
			c.gridx = 0;
			c.gridy = 0;
	    	ripperToolPanel.add(logoPanel,c);
			c.gridwidth = 3;
			c.gridheight = 1;
			c.gridx = 0;
			c.gridy = 1;
	    	ripperToolPanel.add(urlField,c);
			c.gridwidth = 3;
			c.gridheight = 1;
			c.gridx = 0;
			c.gridy = 2;
	    	ripperToolPanel.add(buttonPanel,c);
		ripperFrame.add(ripperToolPanel);
		ripperFrame.add(progressBar);
		ripperFrame.add(previewPane);
		ripperFrame.setVisible(true);
				generateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	String url = urlField.getText();
            	resetButton.setEnabled(true);
            	frameAnimation.animateToHeight(221);
            	linkProcessor.setURL(url);
            	Thread summaryThread = new Thread(summaryProcessor);
            	Thread themeThread = new Thread(themeProcessor);
            	Thread quoteThread = new Thread(quoteProcessor);
        		linkProcessor.processLinks();
        		CountDownLatch countDownLatch = new CountDownLatch(3);
				summaryProcessor.setLatch(countDownLatch);
        		themeProcessor.setLatch(countDownLatch);
        		quoteProcessor.setLatch(countDownLatch);

        		summaryProcessor.setURL(url);
        		themeProcessor.setURL(url);
        		quoteProcessor.setURL(url);

        		summaryProcessor.setLinks(linkProcessor.getLinks());
        		themeProcessor.setLinks(linkProcessor.getLinks());
        		quoteProcessor.setLinks(linkProcessor.getLinks());

	        	summaryThread.start();
	        	themeThread.start();
	        	quoteThread.start();
	        	try{
	        		countDownLatch.await();
	        	}catch(InterruptedException exception){
	        		System.out.println(exception);
	        	}
	        	progressBar.incrementBarBy(100);
            	frameAnimation.animateToHeight(750);
            	finishedHTML = linkProcessor.getHeader();
            	finishedHTML += "<p style='font-weight:bold;font-size:18px;'>Summary</p>";
            	finishedHTML += summaryProcessor.getSummary().replace("null","");
            	finishedHTML += "<p style='font-weight:bold;font-size:18px;'>Themes</p>";
            	finishedHTML += themeProcessor.getThemes().replace("null","");
            	finishedHTML += "<p style='font-weight:bold;font-size:18px;'>Quotes</p>";
            	finishedHTML += quoteProcessor.getQuotes().replace("null","");
            	finishedHTML += "<p style='font-size:12px;font-weight:bold;'>Courtesy of Snazzy Studios and ADI</p>";
       			preview.setText(finishedHTML+"</body></html>");
       			saveButton.setEnabled(true);
       			printerButton.setEnabled(true);
       			generateButton.setEnabled(false);
            }
        });
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
        		JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showSaveDialog(ripperFrame);
				if (option == JFileChooser.APPROVE_OPTION){
					try{
						System.out.println(System.getProperty("os.name"));
						String outputString = "";
						if(System.getProperty("os.name").toLowerCase().contains("mac")){
							outputString = fileChooser.getCurrentDirectory().toString()+"/"+fileChooser.getSelectedFile().getName()+".html";
						}else{
							outputString = fileChooser.getCurrentDirectory().toString()+"\\"+fileChooser.getSelectedFile().getName()+".html";
						}
						FileWriter out = new FileWriter(outputString);
            			out.write(finishedHTML);
            			out.close();
    				}catch(Exception exception){System.out.println(exception);}
				}else if(option == JFileChooser.CANCEL_OPTION){}
            }
        });
        printerButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	PrinterJob printerJob = PrinterJob.getPrinterJob();
        		if(printerJob.printDialog()){
		            printerJob.setPrintable(preview.getPrintable(head, foot));
		            try{printerJob.print();}
		            catch(Exception exception){System.out.println(exception);}
        		}
            }
        });
        resetButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
            	finishedHTML = "";
            	progressBar.incrementBarBy(-100);
            	generateButton.setEnabled(true);
            	resetButton.setEnabled(false);
            	printerButton.setEnabled(false);
            	saveButton.setEnabled(false);
            	frameAnimation.animateToHeight(190);
            }
        });
        urlField.addFocusListener(new FocusListener(){
	        public void focusGained(FocusEvent e){
	            //urlField.setText("");
	        }
	        public void focusLost(FocusEvent e){
	        	if(urlField.getText().equals("")){
	        		urlField.setText("http://www.sparknotes.com/lit/fellowship/");
	        	}
	        }
    	});
   }
   private static String finishedHTML;
   private static MessageFormat head = new MessageFormat("");
   private static MessageFormat foot = new MessageFormat("");
   private static JEditorPane preview;
   private static GridBagConstraints c;
   private static RipperProgressBar progressBar;
   private static final int FRAME_WIDTH = 800;
   private static final int FRAME_HEIGHT = 190;
}