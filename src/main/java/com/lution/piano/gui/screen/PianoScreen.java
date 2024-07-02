package com.lution.piano.gui.screen;

import com.lution.piano.Packet.ModMessage;
import com.lution.piano.blockEntity.PianoBlockEntity;
import com.lution.piano.gui.PianoSetting;
import com.lution.piano.gui.widget.PianoKey;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;


import java.util.ArrayList;


public class PianoScreen extends Screen
{
    private PianoSetting pianoSetting;
    private String level_show = "";
    private final String[] level_map = {"----", "---", "--", "-", "0", "+", "++", "+++", "++++"};
    private int baseX;
    private TimerField timerField;
    private VolumeController volumeController;
    private boolean cancel = false;
    public PianoBlockEntity blockEntity;

    private BlockState state;

    public PianoScreen(Text title, PianoSetting pianoSetting, PianoBlockEntity blockEntity, BlockState blockState) {
        super(title);
        this.pianoSetting = pianoSetting;
        this.blockEntity = blockEntity;
        state = blockState;
    }

    @Override
    protected void init() {
        initKey();
        initLevel();
        initVolume();
        initTimer();
        initSettingButtons();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        Text text = Text.translatable("gui.piano.key.name");
        Text name1 = Text.translatable("gui.piano.key.name1");
        Text name2 = Text.translatable("gui.piano.key.name2");
        Text name3 = Text.translatable("gui.piano.key.name3");
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, width / 2, 20, 0xFFFFFFFF);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, level_show, baseX + 30, 75, 0xFFFFFFFF);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,name1, baseX+35,50,0xFFFFFFFF);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,name2, baseX+130,50,0xFFFFFFFF);
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,name3, baseX+225,50,0xFFFFFFFF);
    }

    private void initKey() {
        baseX = width / 2 - 270 / 2;
        PianoKey pianoC = new PianoKey(baseX, 100, 30, 100, pianoSetting, 1, false);
        PianoKey pianoD = new PianoKey(baseX + 40, 100, 30, 100, pianoSetting, 2, false);
        PianoKey pianoE = new PianoKey(baseX + 80, 100, 30, 100, pianoSetting, 3, false);
        PianoKey pianoF = new PianoKey(baseX + 120, 100, 30, 100, pianoSetting, 4, false);
        PianoKey pianoG = new PianoKey(baseX + 160, 100, 30, 100, pianoSetting, 5, false);
        PianoKey pianoA = new PianoKey(baseX + 200, 100, 30, 100, pianoSetting, 6, false);
        PianoKey pianoB = new PianoKey(baseX + 240, 100, 30, 100, pianoSetting, 7, false);
        PianoKey pianoC_ = new PianoKey(baseX + 20, 100, 30, 60, pianoSetting, 1, true);
        PianoKey pianoD_ = new PianoKey(baseX + 60, 100, 30, 60, pianoSetting, 2, true);
        PianoKey pianoF_ = new PianoKey(baseX + 140, 100, 30, 60, pianoSetting, 4, true);
        PianoKey pianoG_ = new PianoKey(baseX + 180, 100, 30, 60, pianoSetting, 5, true);
        PianoKey pianoA_ = new PianoKey(baseX + 220, 100, 30, 60, pianoSetting, 6, true);
        addDrawableChild(pianoC);
        addDrawableChild(pianoD);
        addDrawableChild(pianoE);
        addDrawableChild(pianoF);
        addDrawableChild(pianoG);
        addDrawableChild(pianoA);
        addDrawableChild(pianoB);
        addDrawableChild(pianoC_);
        addDrawableChild(pianoD_);
        addDrawableChild(pianoF_);
        addDrawableChild(pianoG_);
        addDrawableChild(pianoA_);

    }

    private void initLevel() {
        baseX = width / 2 - 270 / 2;
        int baseY = 70;
        ButtonWidget buttonWidget1 = ButtonWidget.builder(
                        Text.literal("-"), button -> {
                            updateLevel(-1);
                        })
                .dimensions(baseX, baseY, 24, 18)
                .build();

        ButtonWidget buttonWidget2 = ButtonWidget.builder(
                        Text.literal("+"), button -> {
                            updateLevel(1);
                        })
                .dimensions(baseX + 56, baseY, 24, 18)
                .build();

        addDrawableChild(buttonWidget1);
        addDrawableChild(buttonWidget2);
        updateLevel(0);
    }

    private void initVolume() {
        baseX = width / 2 - 270 / 2;
        int baseY = 70;
        volumeController = new VolumeController(baseX + 95, baseY, 80, 20, Text.literal(pianoSetting.volume + "%"), pianoSetting.volume);
        volumeController.setMessage(Text.of(pianoSetting.volume + "%"));
        addDrawableChild(volumeController);
    }

    private void updateLevel(int op) {
        if (op == 1) {
            pianoSetting.setLevel(pianoSetting.level + 1);
            if (pianoSetting.level > 3) {
                pianoSetting.setLevel(3);
            }
        }
        else if (op == -1) {
            pianoSetting.setLevel(pianoSetting.level - 1);
            if (pianoSetting.level < -3) {
                pianoSetting.setLevel(-3);
            }
        }
        level_show = level_map[pianoSetting.level + 4];
    }

    private void initTimer() {
        baseX = width / 2 - 270 / 2;
        int baseY = 70;
        timerField = new TimerField(textRenderer, baseX + 190, baseY, 80, 20, Text.of("" + pianoSetting.timer));
        timerField.setMaxLength(3);
        timerField.setMessage(Text.of("" + pianoSetting.timer));
        addDrawableChild(timerField);
    }

    private void initSettingButtons() {
        baseX = width / 2 - 270 / 2;
        ButtonWidget buttonWidget1 = ButtonWidget.builder(Text.translatable("gui.piano.key.setting_button1"), button -> {
                    applyDefault();
                })
                .tooltip(Tooltip.of(Text.translatable("gui.piano.key.default_tooltip")))
                .dimensions(baseX + 280, 40, 60, 20)
                .build();

        ButtonWidget buttonWidget2 = ButtonWidget.builder(Text.translatable("gui.piano.key.setting_button2"), button -> {
                    clear();
                })
                .dimensions(baseX + 280, 70, 60, 20)
                .build();

        ButtonWidget buttonWidget3 = ButtonWidget.builder(Text.translatable("gui.piano.key.setting_button3"), button -> {
                    close();
                })
                .tooltip(Tooltip.of(Text.translatable("gui.piano.key.save_tooltip")))
                .dimensions(baseX + 280, 100, 60, 20)
                .build();

        ButtonWidget buttonWidget4 = ButtonWidget.builder(Text.translatable("gui.piano.key.setting_button4"), button -> {
                    cancel=true;
                    close();
                })
                .dimensions(baseX + 280, 130, 60, 20)
                .build();

        addDrawableChild(buttonWidget1);
        addDrawableChild(buttonWidget2);
        addDrawableChild(buttonWidget3);
        addDrawableChild(buttonWidget4);

    }

    private void applyDefault() {
        applyTimer();
        PianoSetting.default_volume = pianoSetting.volume;
        PianoSetting.default_timer = pianoSetting.timer;
    }
    private void applyTimer(){
        if (!timerField.getText().isEmpty()) {
            pianoSetting.setTimer(Integer.parseInt(timerField.getText()));
            timerField.setText(pianoSetting.timer+"");
            if(pianoSetting.timer>120){
                pianoSetting.setTimer(120);
                timerField.setText("120");
            }
        }
    }
    private void clear() {
        pianoSetting.setTone(0);
        pianoSetting.setTimer(0);
        pianoSetting.setLevel(0);
        pianoSetting.setVolume(50);
        pianoSetting.setMid(false);
        volumeController.setValue(pianoSetting.volume);
        updateLevel(0);
        timerField.setText("");
        timerField.setPlaceholder(Text.of(pianoSetting.timer + ""));
        volumeController.setMessage(Text.of(pianoSetting.volume + "%"));
    }

    private void save(){
        applyTimer();
        blockEntity.setTone(pianoSetting.tone);
        blockEntity.setLevel(pianoSetting.level);
        blockEntity.setVolume(pianoSetting.volume);
        blockEntity.setMid(pianoSetting.isMid);
        blockEntity.setTimer(pianoSetting.timer);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(blockEntity.getPos());
        buf.writeInt(pianoSetting.tone);
        buf.writeInt(pianoSetting.timer);
        buf.writeInt(pianoSetting.volume);
        buf.writeInt(pianoSetting.level);
        buf.writeBoolean(pianoSetting.isMid);
        ClientPlayNetworking.send(ModMessage.PIANO_ENTITY_ID,buf);

    }

    @Override
    public void close() {
        if(!cancel){
            save();
        }
        super.close();
    }

    private class VolumeController extends SliderWidget
    {
        public VolumeController(int x, int y, int width, int height, Text text, double value) {
            super(x, y, width, height, text, value);
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.of(pianoSetting.volume + "%"));
        }

        @Override
        protected void applyValue() {
            pianoSetting.setVolume((int) (value * 100));
        }

        public void setValue(double value) {
            this.value = value;
        }


    }

    private class TimerField extends TextFieldWidget
    {
        private final ArrayList<Text> Tooltip;

        public TimerField(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
            super(textRenderer, x, y, width, height, text);
            Tooltip = new ArrayList<>();
            Tooltip.add(Text.translatable("gui.piano.key.timer_tooltip1"));
            Tooltip.add(Text.translatable("gui.piano.key.timer_tooltip2"));
            Tooltip.add(Text.translatable("gui.piano.key.timer_tooltip3"));
            setPlaceholder(Text.of(pianoSetting.timer + ""));
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            if (Character.isDigit(chr)) {
                return super.charTyped(chr, modifiers);
            }
            return false;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            if (isHovered()) {
                context.drawTooltip(textRenderer, Tooltip, mouseX, mouseY);
            }
        }


    }

}
