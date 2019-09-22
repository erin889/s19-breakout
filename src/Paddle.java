import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class Paddle extends View {

    public Paddle () {
        super(Config.PADDLE_X, Config.PADDLE_Y);
        this.width = Config.PADDLE_W;
        this.height = 20;
        try {
            this.img = new ImageIcon(ImageIO.read(new FileInputStream("resources/paddle.png"))).getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.img.getScaledInstance(width, height, this.img.SCALE_DEFAULT);
        this.type = ViewType.PADDLE;
    }

    @Override
    public void setPosition(int x, int y) {
        int left = 0;
        int right = Config.WINDOW_W - width - 1;
        if (x > left && x < right) {
            this.posX = x;
            this.posY = y;
        } else if (x <= left) {
            this.posX = left;
        } else {
            this.posX = right;
        }
    }

    @Override
    public void notifyView(Dimension d) {}

    @Override
    public void hit(View v) {}

    public void setPaddleWidth(int width) {
        this.width = width;
        this.img.getScaledInstance(width, height, this.img.SCALE_DEFAULT);
    }

    @Override
    public void setEasyMode() {
        super.setEasyMode();
        setPaddleWidth(600);
    }
}
