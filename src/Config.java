public class Config {

    public static final int WINDOW_W = 600;
    public static final int WINDOW_H = 900;
    public static final int PADDLE_W = 140;
    public static final int PADDLE_X = (WINDOW_W - PADDLE_W) / 2;
    public static final int PADDLE_Y = WINDOW_H - 80;
    public static final int BALL_SIZE = 25;
    public static final int BALL_X = PADDLE_X + (PADDLE_W - BALL_SIZE) / 2;
    public static final int BALL_Y = PADDLE_Y - WINDOW_H / 12;
    public static final int TOTAL_BLOCKS_DEFAULT = 30;
    public static final int BLOCK_ROWS = 6;
    public static final int BLOCK_COLS = 5;
    public static final int BLOCK_W = 80;
    public static final int BLOCK_H = 30;
    public static final int DIST_TO_VWALL = (WINDOW_W - BLOCK_W*BLOCK_COLS + BLOCK_COLS) / 2;
    public static final int DIST_TO_HWALL = 110;
}
