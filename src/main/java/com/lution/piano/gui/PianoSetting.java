package com.lution.piano.gui;



public class PianoSetting
{
    public int tone;
    public int volume;
    public int timer;
    public int level;
    public boolean isMid;

    public static int default_timer = 0;
    public static int default_volume = 50;

    public PianoSetting(int tone, int volume, int timer, int level, boolean isMid) {
        this.tone = tone;
        this.volume = volume;
        this.level = level;
        this.isMid = isMid;
        this.timer = timer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTone(int tone) {
        this.tone = tone;
    }

    public void setMid(boolean isMid) {
        this.isMid = isMid;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
