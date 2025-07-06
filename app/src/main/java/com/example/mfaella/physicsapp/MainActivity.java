package com.example.mfaella.physicsapp;


import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.example.mfaella.physicsapp.levels.CowboyScreen;
import com.example.mfaella.physicsapp.levels.Level1;
import com.example.mfaella.physicsapp.levels.MainMenu;

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
