import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;

public class Block extends View {
    private int firmness;
    private boolean premium;
    private boolean cleared;
    private boolean cracked;

    public Block(int posX, int posY, int firmness, boolean premium) {
        super(posX, posY);
        this.firmness = firmness;
        this.premium = premium;
        this.cleared = false;
        this.cracked = false;

        this.width = Config.BLOCK_W;
        this.height = Config.BLOCK_H;
        this.setBrickDisplay(posY);
        this.type = ViewType.BLOCK;
    }

    @Override
    public void notifyView(Dimension d) {}

    public boolean isPremium() {
        return premium;
    }

    public boolean isCleared() {
        return cleared;
    }

    public int getFirmness() {
        return firmness;
    }


    @Override
    public void hit(View v) {
        firmness -= 1;
        if (firmness == 0) {
            this.display = false;
            this.cleared = true;
            return;
        }
        cracked = true;
        setBrickDisplay(posY);
    }

    public void setBrickDisplay(int posY) {
        String imgSource = "resources";
        int r1 = Config.DIST_TO_HWALL;
        int distance = height;
        int r2 = r1 + distance;
        int r3 = r2 + distance;
        int r4 = r3 + distance;
        int r5 = r4 + distance;

        if (cracked) {
            if (posY <= r1) imgSource += "/lego_red_c";
            if (posY > r1 && posY <= r2) imgSource += "/lego_orange_c";
            if (posY > r2 && posY <= r3) imgSource += "/lego_yellow_c";
            if (posY > r3 && posY <= r4) imgSource += "/lego_green_c";
            if (posY > r4 && posY <= r5) imgSource += "/lego_blue_c";
            if (posY > r5) imgSource += "/lego_purple_c";

        } else {
            if (posY <= r1) imgSource += "/lego_red";
            if (posY > r1 && posY <= r2) imgSource += "/lego_orange";
            if (posY > r2 && posY <= r3) imgSource += "/lego_yellow";
            if (posY > r3 && posY <= r4) imgSource += "/lego_green";
            if (posY > r4 && posY <= r5) imgSource += "/lego_blue";
            if (posY > r5) imgSource += "/lego_purple";
        }

        if (premium) {
            imgSource += "_s.png";
        } else {
            imgSource += ".png";
        }

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new FileInputStream(imgSource)));
            this.img = icon.getImage();
            this.img.getScaledInstance(width, height, this.img.SCALE_DEFAULT);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
