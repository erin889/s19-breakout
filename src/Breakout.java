import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import java.awt.*;

public class Breakout extends JFrame {

    static private int fps = 50;
    static private int ballSpeed = 3;
    private Model model;

    Breakout(int fps, int ballSpeed) {
        super();
        model = new Model(fps, ballSpeed);
        this.add(model);

        this.setTitle("Breakout");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);
        this.setSize(Config.WINDOW_W, Config.WINDOW_H);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    public static void main(String[] args) {

        if (args.length > 0) {
            fps = Integer.valueOf(args[0]);
            ballSpeed = Integer.valueOf(args[1]);
        }
        if (fps > 50 || fps < 25 || ballSpeed < 1 || ballSpeed > 3){
            System.out.println("Invalid arguments, please follow the range: 25 <= fps <= 50, 1 <= ball speed <= 3 ");
            System.exit(0);

        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Breakout b = new Breakout(fps, ballSpeed);
            }
        });

    }
}
