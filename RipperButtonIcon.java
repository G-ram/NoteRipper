import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
public class RipperButtonIcon implements Icon{
	public RipperButtonIcon(String aLabel, int aWidth, int aHeight,Color aColor){
		label = aLabel;
		width = aWidth;
		height = aHeight;
		color = aColor;
	}
	public int getIconWidth(){
		return width;
	}
	public int getIconHeight(){
		return height;
	}
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2 = (Graphics2D)g;
		Rectangle2D.Double background = new Rectangle2D.Double(0,0,width,height);
		g2.setColor(color);
		g2.fill(background);
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
		Font labelFont = new Font("Helvetica",Font.BOLD,(int)(height*0.65));
		int textWidth = (int)(labelFont.getStringBounds(label, frc).getWidth());
		int textHeight = (int)(labelFont.getStringBounds(label, frc).getHeight());
		g2.setColor(Color.WHITE);
		g2.setFont(labelFont);
		g.drawString(label, (int)(width/2-textWidth/2), (int)(height-textHeight/2.5));
	}
	private String label;
	private Color color;
	private int width;
	private int height;
}