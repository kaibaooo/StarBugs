import java.awt.*;
import javax.swing.*;


public class Main {
    static JFrame frame = new JFrame("Frame With Panel");
    public static void main(String[] args) {
        frame.setSize(1600,900);
        JLayeredPane contentPane = frame.getLayeredPane();
	    fuck layer1 = new fuck();
        fuck layer2 = new fuck();
        layer1.setBackground(Color.blue);
        layer2.setBackground(Color.darkGray);
        layer1.setBounds(50,50,500,500);
        layer2.setBounds(0,0,200,200);
        layer1.setVisible(true);
        layer2.setVisible(true);
        contentPane.add(layer1, 1);
        contentPane.add(layer2, 2);
        frame.setVisible(true);
    }
    static class fuck extends JPanel{
        public fuck(){

        }

    }
}
