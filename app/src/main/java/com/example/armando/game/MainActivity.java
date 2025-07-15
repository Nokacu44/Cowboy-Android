package com.example.armando.game;


import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.example.armando.game.levels.MainMenu;

public class MainActivity extends AndroidGame {

    static {
        try {
            System.loadLibrary("liquidfun");
            System.loadLibrary("liquidfun_jni");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("JLiquidFun load error: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public Screen getStartScreen() {
        return new MainMenu(this);
    }
}
