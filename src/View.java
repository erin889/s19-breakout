import java.awt.*;
import java.io.IOException;


public abstract class View {

    public int posX;
    public int posY;
    public int width;
    public int height;
    public boolean display;
    public ViewType type;
    public Image img;
    public Dimension d;
    public boolean easyMode;

    enum ViewType {
        BALL,
        BLOCK,
        PADDLE
    }

    public View(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.display = true;
        this.d = new Dimension(Config.WINDOW_W, Config.WINDOW_H);
        this.easyMode = false;
    }

    abstract void notifyView(Dimension d);
    abstract void hit(View v) throws IOException;

    public int getPosX() {
        return this.posX;
    }
    public int getPosY() {
        return this.posY;
    }

    public Rectangle getBoundedRect() {
        Rectangle rect = new Rectangle(posX, posY, width-1, height-1);
        return rect;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }

    public boolean isDisplay() {
        return this.display;
    }

    public void setPosition(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public Image getImage() {
        return this.img;
    }

    public void setEasyMode() {
        this.easyMode = true;
    }
}
