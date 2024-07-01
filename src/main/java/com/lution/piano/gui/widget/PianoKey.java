package com.lution.piano.gui.widget;


import com.lution.piano.gui.PianoSetting;
import com.lution.piano.gui.screen.PianoScreen;
import com.lution.piano.soundEvent.ModSoundEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Map;


public class PianoKey extends ClickableWidget
{
    private PianoSetting pianoSetting;
    private final int myTone;
    private final boolean isMid;
    private String name;
    private boolean isChoose = false;

    public PianoKey(int x, int y, int width, int height, PianoSetting pianoSetting, int myTone, boolean isMid) {
        super(x, y, width, height, Text.empty());
        this.pianoSetting = pianoSetting;
        this.isMid = isMid;
        this.myTone = myTone;
        name = getSoundName(myTone, pianoSetting.level, isMid);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (checkOverlap(mouseX, mouseY)) {
            this.hovered = false;
        }
        isChoose = pianoSetting.tone == myTone && pianoSetting.isMid == isMid;

        if(isChoose){
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFFFF0000);
            return;
        }
        if (!isMid) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFFFFFFFF);
        }
        else {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF000000);
        }
        if (isHovered()) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF0000FF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(button)) {
                boolean bl = this.clicked(mouseX, mouseY);
                if (bl) {
                    if (!checkOverlap(mouseX, mouseY)) {
                        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                        this.onClick(mouseX, mouseY);
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }
        else {
            return false;
        }
    }


    @Override
    public void onClick(double mouseX, double mouseY) {
        pianoSetting.setTone(myTone);
        pianoSetting.setMid(isMid);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        name = getSoundName(myTone, pianoSetting.level, isMid);
        soundManager.play(PositionedSoundInstance.master(ModSoundEvent.SOUND_EVENTS.get(name), 1.0f,(float) pianoSetting.volume/100));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    private String getSoundName(int tone, int level, Boolean isMid) {
        String name = "";
        name = processLevel(name, level);
        name = processTone(name, tone, isMid);
        return name;
    }

    private String processLevel(String ori, int level) {
        StringBuilder res = new StringBuilder(ori);
        if (level == 0) {
            res.append('n');
        }
        else if (level > 0) {
            res.append("a".repeat(level));
        }
        else {
            res.append("d".repeat(Math.abs(level)));
        }
        res.append('.');
        return res.toString();
    }

    private String processTone(String ori, int tone, Boolean isMid) {
        StringBuilder res = new StringBuilder(ori);
        res.append(tone);
        if (isMid) {
            res.append('_');
        }
        return res.toString();
    }

    private boolean checkOverlap(double mouseX, double mouseY) {
        if (!isMid) {
            if ((myTone == 1 || myTone == 4) && (mouseX >= getX() + 20 && mouseX <= getX() + width) && (mouseY >= getY() && mouseY <= getY() + 60)) {
                return true;
            }
            else if ((myTone == 3 || myTone == 7) && (mouseX >= getX() && mouseX <= getX() + 10) && (mouseY >= getY() && mouseY <= getY() + 60)) {
                return true;
            }
            else if ((myTone == 2 || myTone == 5 || myTone == 6) && (((mouseX >= getX() + 20 && mouseX <= getX() + width) && (mouseY >= getY() && mouseY <= getY() + 60)) || ((mouseX >= getX() && mouseX <= getX() + 10) && (mouseY >= getY() && mouseY <= getY() + 60)))) {
                return true;
            }
        }
        return false;
    }


}
