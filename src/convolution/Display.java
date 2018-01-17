package convolution;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Display extends JFrame{
    
    ImageIcon icon;
    JLabel label = new JLabel();
   
    Display(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        add(label); 
    }
    
    void display(BufferedImage img){
        setSize(img.getWidth(), img.getHeight());
        icon = new ImageIcon(img);
        label.setIcon(icon);
        repaint();
        setVisible(true);        
    }
    
}
