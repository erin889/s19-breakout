import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


public class Ball extends View {

    private int vecX;
    private int vecY;
    private Dimension d;

    public Ball() {
        super(Config.BALL_X, Config.BALL_Y);
        this.width = Config.BALL_SIZE;
        this.height = Config.BALL_SIZE;

        int posRandom = ThreadLocalRandom.current().nextInt(0, 51);
        if (posRandom < 25)  this.vecX = -1;
        else this.vecX = 1;
        this.vecY = -1;

        try {
            this.img = new ImageIcon(ImageIO.read(new FileInputStream("resources/ball.png"))).getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.img.getScaledInstance(width, height, this.img.SCALE_DEFAULT);
        this.type = ViewType.BALL;
    }

    public void setVectPos(int x, int y, boolean flipX, boolean flipY) {
        this.vecX = x;
        this.vecY = y;
        if (flipX) this.vecX *= -1;
        if (flipY) this.vecY *= -1;
    }

    @Override
    public void notifyView(Dimension d) { // to do
        this.d = d;
        int boundW = Config.WINDOW_W - width;
        int boundH = Config.WINDOW_H - height;

        if (posX >= boundW || posX <= 0) setVectPos(vecX, vecY, true, false); // hit left and right walls
        else if (posY < 0) setVectPos(vecX, vecY, false, true); // hit ceiling
        else if (posY >= boundH) { // ball dropped
            display = false;
            vecY = 1;
        }
        posX += vecX; // update to current position
        posY += vecY;
    }

    public void hit(View v) { // to do

        double W = (width + v.getWidth())* (v.getBoundedRect().getCenterX() - this.getBoundedRect().getCenterX());
        double H = (height + v.getHeight())* (v.getBoundedRect().getCenterY() - this.getBoundedRect().getCenterY());

        if (H >= W) {
            if (-1*H >= W) setVectPos(vecX, vecY, false, true);
            else {
                setVectPos(vecX, vecY, true, false);
                if (v.getPosX() - width >= 0) {
                    posX = v.getPosX()- width;
                } else {
                    setVectPos(vecX, vecY, true, false);
                    posY = v.getPosY() + v.getHeight();
                }
            }
        } else {
            if (-1*H >= W) {
                setVectPos(vecX, vecY, true, false);
                if (v.getPosX() + v.getWidth() + width <= Config.WINDOW_W - width) {
                    posX = v.getPosX() + v.getWidth() - width;
                } else {
                    setVectPos(vecX, vecY, true, false);
                    posY = v.getPosY() + v.getHeight();
                }
            } else setVectPos(vecX, vecY, false, true);

        }
        this.notifyView(d);
    }
}
