package com.example.tetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class TetrisActivityAW extends Activity {
    private NextBlockView nextBlockView;
    private TetrisViewAW tetrisViewAW;
    private TextView gameStatusTip;
    public TextView score;
    private ImageButton imageButton;
    private SharedPreferences sp;
    private TextView max_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris_aw);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Intent intent = new Intent(TetrisActivityAW.this, MusicService.class);
        nextBlockView = findViewById(R.id.nextBlockView1);
        tetrisViewAW = findViewById(R.id.tetrisViewAW1);
        tetrisViewAW.setFather(this);
        gameStatusTip = findViewById(R.id.game_staus_tip);
        score = findViewById(R.id.score);
        max_score  = findViewById(R.id.max_score);
        imageButton = findViewById(R.id.btn1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicService.isplay == false) {
                    startService(intent);
                    imageButton.setBackground(getResources().getDrawable(R.drawable.on));
                } else {
                    stopService(intent);
                    imageButton.setBackground(getResources().getDrawable(R.drawable.close));
                }
            }
        });
    }

    public void setNextBlockView(List<BlockUnit> blockUnits, int div_x) {
        nextBlockView.setBlockUnits(blockUnits, div_x);
    }

    /**
     * 开始游戏
     *
     * @param view
     */
    public void startGame(View view) {
        tetrisViewAW.startGame();
        gameStatusTip.setText("游戏运行中");
        sp = getSharedPreferences("max_score", MODE_PRIVATE);
        String maxScore = sp.getString("maxScore", null);
        if (maxScore == null || maxScore.length() == 0) {
            max_score.setText("0");
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("maxScore", "0");
            editor.commit();
        } else {
            max_score.setText(maxScore);
        }
    }

    /**
     * 暂停游戏
     */
    public void pauseGame(View view) {
        tetrisViewAW.pauseGame();
        gameStatusTip.setText("游戏已暂停");
    }

    /**
     * 继续游戏
     */
    public void continueGame(View view) {
        tetrisViewAW.continueGame();
        gameStatusTip.setText("游戏运行中");
    }

    /**
     * 停止游戏
     */
    public void stopGame(View view) throws IOException {
        tetrisViewAW.stopGame();
        nextBlockView.stopGame();
        gameStatusTip.setText("游戏已停止");
        sp = getSharedPreferences("max_score", MODE_PRIVATE);
        String maxScore = sp.getString("maxScore", null);
        if (Integer.parseInt(score.getText().toString()) > Integer.parseInt(maxScore)) {
            maxScore = score.getText().toString();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("maxScore",maxScore);
            editor.commit();
        }
        score.setText("" + 0);
        max_score.setText("0");
    }

    public void speedDown(View view) {
        tetrisViewAW.speedDown();
    }

    /**
     * 向左滑动
     */
    public void toLeft(View view) {
        tetrisViewAW.toLeft();
    }

    /**
     * 向右滑动
     */
    public void toRight(View view) {
        tetrisViewAW.toRight();
    }

    /**
     * 向右滑动
     */
    public void toRoute(View view) {
        tetrisViewAW.route();
    }

    @Override
    protected void onStart() {
        startService(new Intent(TetrisActivityAW.this, MusicService.class));
        super.onStart();
    }
}
