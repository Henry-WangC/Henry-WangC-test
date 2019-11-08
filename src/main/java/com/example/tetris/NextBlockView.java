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
import java.util.List;

public class NextBlockView extends View {
    /**
     * 网格开始坐标值，横纵坐标的开始值都是此值
     */
    public static final int beginPoint = 10;
    /**
     * 俄罗斯方块的最大坐标
     */
    private static int max_x, max_y;
    private List<BlockUnit> blockUnits = new ArrayList<BlockUnit>();
    /**
     * 背景墙画笔
     */
    private static Paint paintWall = null;
    private static final int BOUND_WIDTH_OF_WALL = 2;
    private static Paint paintBlock = null;
    private int div_x = 0;
    // 俄罗斯方块颜色数组
    private static final int color[] = {Color.parseColor("#FF6600"), Color.BLUE, Color.RED, Color.GREEN, Color.GRAY};

    public NextBlockView(Context context) {
        super(context, null);
    }

    public NextBlockView(Context context, @Nullable AttributeSet attrs) {
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
    }

    public void setBlockUnits(List<BlockUnit> blockUnits, int div_x) {
        this.blockUnits = blockUnits;
        this.div_x = div_x;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rel;
        int len = blockUnits.size();
        for (int i = 0; i < len; i++) {
            int x = blockUnits.get(i).x - div_x + BlockUnit.UNIT_SIZE * 2;
            int y = blockUnits.get(i).y + BlockUnit.UNIT_SIZE * 2;
            rel = new RectF(x + BOUND_WIDTH_OF_WALL, y + BOUND_WIDTH_OF_WALL, x + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL, y + BlockUnit.UNIT_SIZE - BOUND_WIDTH_OF_WALL);
            canvas.drawRoundRect(rel, 8, 8, paintWall);
            rel = new RectF(x, y, x + BlockUnit.UNIT_SIZE, y + BlockUnit.UNIT_SIZE);
            canvas.drawRoundRect(rel, 8, 8, paintBlock);
        }
    }

    public void stopGame(){
        blockUnits.clear();
        invalidate();
    }

}
