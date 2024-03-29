package com.example.tetris;

import java.util.List;

public class BlockUnit {
    public static final int UNIT_SIZE = 50;
    public static final int BEGIN = 10;
    public int color;
    public int x, y;

    public BlockUnit() {
    }

    public BlockUnit(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * 判断方块是否可以向左移动,1是否在边缘,2是否会与其他方块重合
     *
     * @param blockUnits    当前正在下落的方块
     * @param allBlockUnits 所有的方块
     * @return 能移动true;不能移动false
     */
    public static boolean canMoveToLeft(List<BlockUnit> blockUnits, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x;
            if (x - UNIT_SIZE < BEGIN) {
                return false;
            }
            int y = blockUnit.y;
            if (isSameUnit(x - UNIT_SIZE, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }

    //* @param max_x         游戏主界面X轴的最大值 ,下同
    public static boolean canMoveToRight(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x + UNIT_SIZE;
            if (x > max_x - UNIT_SIZE) {
                return false;
            }
            int y = blockUnit.y;
            if (isSameUnit(x, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canMoveToDown(List<BlockUnit> blockUnits, int max_y, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            int x = blockUnit.x;
            int y = blockUnit.y + UNIT_SIZE;
            if (y > max_y - UNIT_SIZE) {
                return false;
            }
            if (isSameUnit(x, y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canRoute(List<BlockUnit> blockUnits, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            if (isSameUnit(blockUnit.x, blockUnit.y, allBlockUnits)) {
                return false;
            }
        }
        return true;
    }

    public static boolean toLeft(List<BlockUnit> blockUnits, List<BlockUnit> allBlockUnits) {
        if (canMoveToLeft(blockUnits, allBlockUnits)) {
            for (BlockUnit blockUnit : blockUnits) {
                blockUnit.x -= UNIT_SIZE;
            }
            return true;
        }
        return false;
    }

    public static boolean toRight(List<BlockUnit> blockUnits, int max_x, List<BlockUnit> allBlockUnits) {
        if (canMoveToRight(blockUnits, max_x, allBlockUnits)) {
            for (BlockUnit blockUnit : blockUnits) {
                blockUnit.x += UNIT_SIZE;
            }
            return true;
        }
        return false;
    }

    public static void toDown(List<BlockUnit> blockUnits, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : blockUnits) {
            blockUnit.y += UNIT_SIZE;
        }
    }

    public static boolean isSameUnit(int x, int y, List<BlockUnit> allBlockUnits) {
        for (BlockUnit blockUnit : allBlockUnits) {
            if (Math.abs(x - blockUnit.x) < UNIT_SIZE && Math.abs(y - blockUnit.y) < UNIT_SIZE) {
                return true;
            }
        }
        return false;
    }

    public static void remove(List<BlockUnit> allBlockUnits, int j) {
        for (int i = allBlockUnits.size() - 1; i >= 0; i--) {
            if ((int) ((allBlockUnits.get(i).y - BEGIN) / UNIT_SIZE) == j) {
                allBlockUnits.remove(i);
            }
        }
    }
}
