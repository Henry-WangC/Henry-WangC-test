package com.example.tetris;

import java.util.ArrayList;
import java.util.List;

public class TetrisBlock {
    public static final int TYPE_SUM = 7;
    public int blockType, blockDirection;
    private int x, y, color;

    public TetrisBlock() {
    }

    public TetrisBlock(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public List<BlockUnit> getUnits(int x, int y) {
        this.x = x;
        this.y = y;
        return returnUnit();
    }

    private List<BlockUnit> returnUnit() {
        List<BlockUnit> units = new ArrayList<BlockUnit>();
        blockType = (int) (Math.random() * TYPE_SUM) + 1;
        blockDirection = 1;
        color = (int) (Math.random() * 4) + 1;
        units.clear();
        switch (blockType) {
            case 1:
                for (int i = 0; i < 4; i++) {
                    units.add(new BlockUnit(x + (i - 2) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 2:
                units.add(new BlockUnit(x , y - BlockUnit.UNIT_SIZE, color));
                for (int i = 0; i < 3; i++) {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 3:
                for (int i = 0; i < 2; i++) {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y - BlockUnit.UNIT_SIZE, color));
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 4:
                units.add(new BlockUnit(x - BlockUnit.UNIT_SIZE, y - BlockUnit.UNIT_SIZE, color));
                for (int i = 0; i < 3; i++) {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 5:
                units.add(new BlockUnit(x + BlockUnit.UNIT_SIZE, y - BlockUnit.UNIT_SIZE, color));
                for (int i = 0; i < 3; i++) {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 6:
                for (int i = 0; i < 2; i++) {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y - BlockUnit.UNIT_SIZE, color));
                    units.add(new BlockUnit(x + i * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
            case 7:
                for (int i = 0; i < 2; i++) {
                    units.add(new BlockUnit(x + i * BlockUnit.UNIT_SIZE, y - BlockUnit.UNIT_SIZE, color));
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNIT_SIZE, y, color));
                }
                break;
        }
        return units;
    }
}
