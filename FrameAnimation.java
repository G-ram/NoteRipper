import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FrameAnimation{
	public FrameAnimation(JFrame aFrame){
		frame = aFrame;
		animationTimer = new javax.swing.Timer(1,null);
	}
	public void animateToHeight(final int height){
		final int startFrameHeight = frame.getHeight();
		for( ActionListener aL : animationTimer.getActionListeners()){
        	animationTimer.removeActionListener(aL);
    	}
		animationTimer.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event){
        		if(height > startFrameHeight){
					frame.setSize(frame.getWidth(),frame.getHeight()+10);
					if(frame.getHeight() >= height){
						animationTimer.stop();
					}
				}else{
					frame.setSize(frame.getWidth(),frame.getHeight()-10);
					if(frame.getHeight() <= height){
						animationTimer.stop();
					}
				}
			}
        });
        animationTimer.start();
	}
	private static JFrame frame;
	private javax.swing.Timer animationTimer;
}