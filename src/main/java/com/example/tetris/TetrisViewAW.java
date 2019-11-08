package com.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

public class TetrisViewAW extends View {
    /**
     * 网格开始坐标值，横纵坐标的开始值都是此值
     */
    public static final int beginPoint = 10;
    /**
     * 俄罗斯方块的最大坐标
     */
    private static int max_x, max_y;
    /**
     * 行数和列数
     */
    private static int num_x = 0, num_y = 0;
    /**
     * 背景墙画笔
     */
    private static Paint paintWall = null;
    /**
     * 俄罗斯方块的单元块画笔
     */
    private static Paint paintBlock = null;
    private static final int BOUND_WIDTH_OF_WALL = 2;
    /**
     * 当前正在下落的方块
     */
    private List<BlockUnit> blockUnits = new ArrayList<BlockUnit>();
    /**
     * 下一个要显示的方块
     */
    private List<BlockUnit> blockUnitBufs = new ArrayList<BlockUnit>();
    /**
     * 下一个要显示的方块
     */
    private List<BlockUnit> routeBlockUnitBufs = new ArrayList<BlockUnit>();
    /**
     * 全部的方块allBlockUnits
     */
    private List<BlockUnit> allBlockUnits = new ArrayList<BlockUnit>();
    /**
     * 调用此对象的Activity对象
     */
    private TetrisActivityAW father = null;
    private int[] map = new int[100]; // 保存每行网格中包含俄罗斯方块单元的个数
    /**
     * 游戏的主线程
     */
    private Thread mainThread = null;
    // 游戏的几种状态
    /**
     * 标识游戏是开始还是停止
     */
    private boolean gameStatus = false;
    /**
     * 标识游戏是暂停还是运行
     */
    private boolean runningStatus = false;
    private boolean speedDown = false;
    /**
     * 俄罗斯方块颜色数组
     */
    private static final int color[] = {Color.parseColor("#FF6600"), Color.BLUE, Color.RED, Color.GREEN, Color.GRAY};
    /**
     * 方块的中心方块单元的坐标,
     */
    private int xx, yy;
    /**
     * 方块,用户随机获取各种形状的方块
     */
    private TetrisBlock tetrisBlock;
    /**
     * 分数
     */
    private int score = 0;
    /**
     * 当前方块的类型
     */
    private int blockType = 0;

    public TetrisViewAW(Context context) {
        super(context);
    }

    public TetrisViewAW(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (paintWall == null) {
            paintWall = new Paint();
            paintWall.setColor(Color.GRAY);
            paintWall.setStyle(Paint.Style.STROKE);
            paintWall.setStrokeWidth(BOUND_WIDTH_OF_WALL + 1);
        }
        if (paintBlock == null) {
            paintBlock = new Paint();
            paintBlock.setColor(Color.parseColor("#FF6600"));
        }
        tetrisBlock = new TetrisBlock();
        routeBlockUnitBufs = tetrisBlock.getUnits(beginPoint, beginPoint);
        Arrays.fill(map, 0);
    }

    public void setFather(TetrisActivityAW father) {
        this.father = father;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        max_x = getWidth();
        max_y = getHeight();
        RectF rel;
        num_x = 0;
        num_y = 0;
        for (int i = beginPoint; i < max_x - BlockUnit.UNIT_SIZE; i += BlockUnit.UNIT_SIZE) {
            for (int j = beginPoint; j < max_y - BlockUnit.UNIT_SIZE; j += BlockUnit.UNIT_SIZE) {
                rel = new RectF(i, j, i + BlockUnit.UNIT_SIZE, j + BlockUnit.UNIT_SIZE);
                canvas.drawRoundRect(rel, 8, 8, paintWall);
                num_y++;
            }
            num_y = 0;
            num_x++;
        }
        int len = blockUnits.size();
        for (int i = 0; i < len; i++) {
            int x = blockUnits.get(i).x;
            int y = blockUnits.get(i).y;
            paintBlock.setColor(color[blockUnits.get(i).color]);
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL, x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
        }
        len = allBlockUnits.size();
        for (int i = 0; i < len; i++) {
            int x = allBlockUnits.get(i).x;
            int y = allBlockUnits.get(i).y;
            paintBlock.setColor(color[allBlockUnits.get(i).color]);
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL, x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
        }
    }

    public void startGame() {
        gameStatus = true;
        runningStatus = true;
        if (mainThread == null || !mainThread.isAlive()) {
            getNewBlock();
            mainThread = new Thread(new MainThread());
            mainThread.start();
        }
    }

    public void pauseGame() {
        runningStatus = false;
    }

    public void continueGame() {
        runningStatus = true;
    }

    public void stopGame() {
        runningStatus = false;
        gameStatus = false;
        if (mainThread != null) {
            mainThread.interrupt();
        }else {
            exit(0);
        }
        blockUnits.clear();
        allBlockUnits.clear();
        Arrays.fill(map, 0);
        score = 0;
        invalidate();
    }

    public void speedDown() {
        speedDown = true;
    }

    public void toLeft() {
        if (BlockUnit.toLeft(blockUnits, allBlockUnits)) {
            xx -= BlockUnit.UNIT_SIZE;
        }
        invalidate();
    }

    public void toRight() {
        if (BlockUnit.toRight(blockUnits, max_x, allBlockUnits)) {
            xx += BlockUnit.UNIT_SIZE;
        }
        invalidate();
    }

    public void route() {
        if (blockType == 3) {
            return;
        }
        if (routeBlockUnitBufs.size() != blockUnits.size()) {
            routeBlockUnitBufs = tetrisBlock.getUnits(xx, yy);
        }
        for (int i = 0; i < blockUnits.size(); i++) {
            routeBlockUnitBufs.get(i).x = blockUnits.get(i).x;
            routeBlockUnitBufs.get(i).y = blockUnits.get(i).y;
        }
        for (BlockUnit blockUnit : routeBlockUnitBufs) {
            int tx = blockUnit.x;
            int ty = blockUnit.y;
            blockUnit.x = -(ty - yy) + xx;
            blockUnit.y = tx - xx + yy;
        }
        routeTran(routeBlockUnitBufs);
        if (!BlockUnit.canRoute(routeBlockUnitBufs, allBlockUnits)) {
            return;
        }
        for (BlockUnit blockUnit : blockUnits) {
            int tx = blockUnit.x;
            int ty = blockUnit.y;
            blockUnit.x = -(ty - yy) + xx;
            blockUnit.y = tx - xx + yy;
        }
    }

    private void routeTran(List<BlockUnit> blockUnitBufs) {
        boolean needLeftTran = false;
        boolean needRightTran = false;
        for (BlockUnit blockUnit : blockUnitBufs) {
            if (blockUnit.x < beginPoint) {
                needRightTran = true;
            }
            if (blockUnit.x > max_x - BlockUnit.UNIT_SIZE) {
                needLeftTran = true;
            }
        }
        if (needLeftTran || needRightTran) {
            for (BlockUnit blockUnit : blockUnitBufs) {
                if (needLeftTran) {
                    blockUnit.x -= BlockUnit.UNIT_SIZE;
                    xx -= BlockUnit.UNIT_SIZE;
                }
                if (needRightTran) {
                    blockUnit.x += BlockUnit.UNIT_SIZE;
                    xx += BlockUnit.UNIT_SIZE;
                }
            }
            routeTran(blockUnitBufs);
        } else {
            return;
        }
    }

    private void getNewBlock() {
        this.xx = beginPoint + (num_x / 2) * BlockUnit.UNIT_SIZE;
        this.yy = beginPoint;
        if (blockUnitBufs.size() == 0) {
            blockUnitBufs = tetrisBlock.getUnits(xx, yy);
        }
        blockUnits = blockUnitBufs;
        blockType = tetrisBlock.blockType;
        blockUnitBufs = tetrisBlock.getUnits(xx, yy);
        if (father != null) {
            father.setNextBlockView(blockUnitBufs, (num_x / 2) * BlockUnit.UNIT_SIZE);
        }
    }

    private class MainThread implements Runnable {

        @Override
        public void run() {
            while (gameStatus) {
                while (runningStatus) {
                    if (BlockUnit.canMoveToDown(blockUnits, max_y, allBlockUnits)) {
                        BlockUnit.toDown(blockUnits, allBlockUnits);
                        yy += BlockUnit.UNIT_SIZE;
                    } else {
                        for (BlockUnit blockUnit : blockUnits) {
                            allBlockUnits.add(blockUnit);
                        }
                        for (BlockUnit blockUnit : blockUnits) {
                            int index = (int) ((blockUnit.y - beginPoint) / BlockUnit.UNIT_SIZE);
                            map[index]++;
                        }
                        int end = (int) ((max_y - 50 - beginPoint) / BlockUnit.UNIT_SIZE);
                        int full = (int) ((max_x - beginPoint) / BlockUnit.UNIT_SIZE);
                        try {
                            Thread.sleep(GameConfig.SPEED);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i <= end; i++) {
                            if (map[i] >= full) {
                                BlockUnit.remove(allBlockUnits, i);
                                score += 100;
                                map[i] = 0;
                                for (int j = i; j > 0; j--) {
                                    map[j] = map[j - 1];
                                }
                                map[0] = 0;
                                for (BlockUnit blockUnit : allBlockUnits) {
                                    if (blockUnit.y < ((i * BlockUnit.UNIT_SIZE) + beginPoint)) {
                                        blockUnit.y += BlockUnit.UNIT_SIZE;
                                    }
                                }
                            }
                        }
                        father.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                father.score.setText("" + score);
                                invalidate();
                            }
                        });
                        try {
                            Thread.sleep(GameConfig.SPEED * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        father.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                score += 10;
                                getNewBlock();
                                father.score.setText("" + score);
                            }
                        });
                        speedDown = false;
                    }
                    father.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    try {
                        if (speedDown) {
                            Thread.sleep(80);
                        } else {
                            Thread.sleep(GameConfig.SPEED);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
