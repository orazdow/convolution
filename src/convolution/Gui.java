package convolution;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JFrame{
    
    File f;
    BufferedImage img;
    Display display = new Display();
    ConvProc conv = new ConvProc();
    
    Gui(){
        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout(new FlowLayout());
        
        final JFileChooser chooser = new JFileChooser();

        JPanel panel = new JPanel(new FlowLayout());
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(panel);
        
        JButton open = new JButton("open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int sel = chooser.showOpenDialog(rootPane);
               if(sel == JFileChooser.APPROVE_OPTION){
                   f = chooser.getSelectedFile();
                    try {
                        img = ImageIO.read(f);
                        display.display(img);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
               }
                
            }
        });    
        panel.add(open);
        
        JButton process = new JButton("process");
        process.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conv.process(img, conv.blur);
                conv.process(img, conv.blur);
                conv.process(img, conv.edge);
                display.display(img);
            }
        });
        panel.add(process);
        setVisible(true);
    }
}
