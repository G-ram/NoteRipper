import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
public class RipperProgressBar extends JComponent{
	public RipperProgressBar(int aWidth,int aHeight, int aMax, int aCurrentValue){
		width = aWidth;
		height = aHeight;
		max = aMax;
		currentValue = aCurrentValue;
		nextValue = aCurrentValue;
		currentWidth = (int)((double)width * ((double)currentValue/(double)max));
		animationTimer = new Timer(5,null);
	}
	public void incrementBarBy(int increment){
		nextValue+=increment;
		animateBar();
	}
	private void animateBar(){
   		for( ActionListener aL : animationTimer.getActionListeners()){
        	animationTimer.removeActionListener(aL);
    	}
		animationTimer.addActionListener(new
        	ActionListener(){
            	public void actionPerformed(ActionEvent event){
            		if(nextValue > currentValue){
						currentValue+=1;
						currentWidth = (int)((double)width * ((double)currentValue/(double)max));
						if(currentValue >= nextValue){
							animationTimer.stop();
							nextValue = currentValue;
						}
					}else{
						currentValue-=1;
						currentWidth = (int)((double)width * ((double)currentValue/(double)max));
						if(currentValue <= nextValue){
							animationTimer.stop();
							nextValue = currentValue;
						}
					}
					repaint();
				}
        });
   		animationTimer.start();
	}
    public Dimension getPreferredSize(){return new Dimension(width,height);}
    public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(53,185,236));
		g2.setStroke(new BasicStroke(4));
		Rectangle2D.Double borderRect = new Rectangle2D.Double(0,0,width,height);
		g2.draw(borderRect);
		g2.setStroke(new BasicStroke(0));
		Rectangle2D.Double barRect = new Rectangle2D.Double(0,0,currentWidth,height);
    	g2.draw(barRect);
    	g2.fill(barRect);
    }
	private Timer animationTimer;
	private int max;
	private int currentValue;
	private int nextValue;
	private int currentWidth;
	private int width;
	private int height;
}