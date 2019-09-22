import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Model extends JComponent {
    private int score = 0;
    private List<Block> blocks = new ArrayList<>();
    private List<View> views = new ArrayList<>();
    private Ball ball = new Ball();
    private Paddle paddle = new Paddle();

    private int curX;
    private int fps, ballSpeed;
    private Timer fpsTimer = new Timer();
    private Timer ballTimer = new Timer();
    private Timer paddleTimer = new Timer();;

    private Status where;
    private Dimension d;
    private double multiplierW, multiplierH;

    public enum Status {
        READY,
        RUNNING,
        PAUSED,
        OVER
    }

    Model(int fps, int ballSpeed) {
        this.fps = fps;
        this.ballSpeed = ballSpeed;

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                d = e.getComponent().getBounds().getSize();
                multiplierW = (double) d.width / Config.WINDOW_W;
                multiplierH = (double) d.height / Config.WINDOW_H;
            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (where == Status.RUNNING)  curX = e.getX();
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (where == Status.RUNNING) {
                    paddle.setPosition(paddle.getPosX() - curX + e.getX(), paddle.getPosY());
                    curX = e.getX();
                    hitTest();
                    repaint();
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ENTER) {
                    if (where == Status.READY || where == Status.PAUSED) where = Status.RUNNING;
                    if (where == Status.OVER) {
                        bootstrap();
                        where = Status.RUNNING;
                    }
                } else if (code == KeyEvent.VK_E) {
                    for (int i = 0; i < views.size(); i++) views.get(i).setEasyMode();
                } else if (code == KeyEvent.VK_Q) {
                    System.exit(0);
                } else if (code == KeyEvent.VK_P) where = Status.PAUSED;
                return true;
            }
        });

        fpsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, 1000 / this.fps);

        ballTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (where == Status.RUNNING) repaintBall();
            }
        }, 0, 10 / this.ballSpeed);

        paddleTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paddle.easyMode && paddle.getWidth() > Config.PADDLE_W) {
                    double newPaddleWidth = (int)paddle.getWidth()/1.5;
                    paddle.setPaddleWidth((int)newPaddleWidth);
                }
            }
        }, 0, 30000);

        this.bootstrap();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(multiplierW, multiplierH);

        ImageIcon bgImgIcon = null;
        try {
            bgImgIcon = new ImageIcon(ImageIO.read(new FileInputStream("resources/background.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image bgImg = bgImgIcon.getImage();
        bgImg.getScaledInstance(Config.WINDOW_W, Config.WINDOW_H, bgImg.SCALE_DEFAULT);
        g2.drawImage(bgImg, 1, 1, Config.WINDOW_W, Config.WINDOW_H, this);

        /* Code inspired by https://www.programcreek.com/java-api-examples/java.awt.RenderingHints */
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (where == Status.READY || where == Status.PAUSED) ready(g2);
        if (where != Status.READY) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Courier", Font.BOLD, 15));
            g2.drawString("Ball Speed: " + ballSpeed,20, 20);
            g2.drawString("Current FPS: " + fps,20, 40);
            g2.drawString("Current Score: " + score,20, 60);
            for (int i = 0; i < views.size(); i++) {
                View v = views.get(i);
                g2.drawImage(v.getImage(), v.getPosX(), v.getPosY(), v.getWidth(), v.getHeight(), this);
            }
            if (where == Status.OVER) over(g2);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void repaintBall() {
        if (!ball.isDisplay()) where = Status.OVER;
        this.hitTest();
        ball.notifyView(d);
    }

    private void over(Graphics2D g2) {
        g2.setColor(Color.RED);
        Font ft = new Font("Courier", Font.PLAIN, 20);
        g2.setFont(ft);
        g2.drawString("----------------------------------",100,(Config.WINDOW_H)/2-80);
        String s = "Game Over";
        int w = this.getFontMetrics(ft).stringWidth(s);
        g2.drawString(s, (Config.WINDOW_W - w)/2, (Config.WINDOW_H)/2-30);
        s = "Total Score: " + score;
        w = this.getFontMetrics(ft).stringWidth(s);
        g2.drawString(s, (Config.WINDOW_W - w)/2, (Config.WINDOW_H)/2+30);
        ft = new Font("Courier", Font.PLAIN, 15);
        g2.setFont(ft);
        g2.drawString( "Click ENTER to try again", (Config.WINDOW_W -200)/2, (Config.WINDOW_H)/2+100);
        ft = new Font("Courier", Font.PLAIN, 20);
        g2.setFont(ft);
        g2.drawString("----------------------------------", 100,(Config.WINDOW_H)/2+150);

    }

    private void ready(Graphics2D g2) {
        Font font = new Font("Courier", Font.PLAIN, 20);
        g2.setColor(Color.RED);
        g2.setFont(font);
        if (where == Status.READY) g2.drawString("Chenyi Yang 20660449",200,300);
        g2.drawString("----------------------------------",100,350);
        if (where == Status.READY) g2.drawString("ENTER : Start",220,400);
        if (where == Status.PAUSED) g2.drawString("ENTER : Resume",220,400);
        g2.drawString("P : Pause",250,450);
        g2.drawString("E : Easy mode",250,500);
        g2.drawString("Q : Quit",250,550);
        g2.drawString("Drag mouse to move the paddle",150,600);
        g2.drawString("----------------------------------",100,650);
    }

    private void hitTest() {
        boolean hitted = false;

        if (blocks.size() == 0) where = Status.OVER;

        // Hit test between ball and paddle
        if (ball.getBoundedRect().intersects(paddle.getBoundedRect())
            || paddle.getBoundedRect().intersects(ball.getBoundedRect())) {
            ball.hit(paddle);
            ball.notifyView(d);
            hitted = true;
        }

        // Hit test between ball and blocks
        if (blocks.size() > 0) {
            for (int i = 0; i < blocks.size(); i++) {
                if (ball.getBoundedRect().intersects(blocks.get(i).getBoundedRect())
                && blocks.get(i).isDisplay() && !hitted) {
                    if (!blocks.get(i).isCleared()) {
                        hitted = true;
                        ball.hit(blocks.get(i));
                        blocks.get(i).hit(ball);
                        if (blocks.get(i).isPremium()) score += 100;
                    }
                    if (blocks.get(i).getFirmness() == 0) {
                        views.remove(blocks.get(i));
                        if (blocks.get(i).isPremium()) {
                            double extended = paddle.getWidth()*1.5;
                            paddle.setPaddleWidth((int)extended);
                            score += 100;
                        }
                        blocks.remove(i);
                        score += 50;
                    }

                }
            }
        }
    }

    private void bootstrap() {
        this.score = 0;
        this.where = Status.READY;
        setDoubleBuffered(true);

        views.clear();
        blocks.clear();
        paddle = new Paddle();
        ball = new Ball();

        views.add(ball);
        views.add(paddle);

        int index = -1;
        int firmness = 1;
        int rand1 = ThreadLocalRandom.current().nextInt(0, Config.TOTAL_BLOCKS_DEFAULT/2);
        int rand2 = ThreadLocalRandom.current().nextInt(Config.TOTAL_BLOCKS_DEFAULT/2, Config.TOTAL_BLOCKS_DEFAULT);

        for (int row = 0; row < Config.BLOCK_ROWS; row++){
            for (int col = 0; col < Config.BLOCK_COLS; col++) {
                ++index;
                boolean premium = false;
                if (rand1 == index || rand2 == index) premium = true;
                if ((row == 2 || row == 3 || row == 4) && (col == 2 || col == 3 || col == 4)) firmness = 2;
                blocks.add(new Block(col * Config.BLOCK_W + Config.DIST_TO_VWALL,
                        row * Config.BLOCK_H + Config.DIST_TO_HWALL, firmness, premium));
                views.add(blocks.get(index));
            }
        }

    }

}
