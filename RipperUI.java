import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
public class RipperUI{
	public RipperUI(){}
	public static JButton makeRipperButton(int width, int height, String label,Color color){
		JButton aButton = new JButton(new RipperButtonIcon(label,width,height,color));
		aButton.setBorderPainted(false);
		aButton.setFocusPainted(false);
		aButton.setContentAreaFilled(false);
        aButton.setPressedIcon(new RipperButtonIcon(label,width,height,new Color((int)(color.getRed()*0.75),(int)(color.getGreen()*0.75),(int)(color.getBlue()*0.75))));
        aButton.setDisabledIcon(new RipperButtonIcon(label,width,height,new Color(color.getRed(),color.getGreen(),color.getBlue(),80)));
        aButton.setPreferredSize(new Dimension(width, height));
		return aButton;
	}
	public static JTextField makeRipperTextField(int fontSize, int width, String placeholder){
		JTextField aTextField = new JTextField(placeholder);
		aTextField.setFont(new Font("Helvetica",Font.PLAIN,fontSize));
        Border fieldBorder = BorderFactory.createLineBorder(new Color(53,185,236), 2);
        aTextField.setBorder(fieldBorder);
		aTextField.setPreferredSize(new Dimension(width,50));
		return aTextField;
	}
	public static JLabel makeRipperLabel(String label, int fontSize){
		JLabel aLabel = new JLabel(label);
		aLabel.setFont(new Font("Helvetica",Font.BOLD,fontSize));
		return aLabel;
	}
	public static RipperProgressBar makerRipperProgressBar(int width, int height){
		RipperProgressBar aProgressbar = new RipperProgressBar(width,height,100,0);
		return aProgressbar;
	}
}