package com.lution.piano.block;

import com.lution.piano.blockEntity.PianoBlockEntity;
import com.lution.piano.gui.PianoSetting;
import com.lution.piano.gui.screen.PianoScreen;
import com.lution.piano.soundEvent.ModSoundEvent;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PianoBlock extends Block implements BlockEntityProvider
{
//    public static IntProperty TONE = IntProperty.of("tone", 1, 7);
//    public static IntProperty LEVEL = IntProperty.of("level", -4,4);
//    public static IntProperty VOLUME = IntProperty.of("volume",0,100);
//    public static BooleanProperty EFFECTING = BooleanProperty.of("effecting");
//

    public PianoBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PianoBlockEntity(pos, state);
    }

    //
//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(TONE)
//                .add(LEVEL);
//    }
//
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity block = world.getBlockEntity(pos);
        if (block instanceof PianoBlockEntity PBE) {
            PianoSetting pianoSetting = PBE.getSettings();
            if(world.isClient){
                MinecraftClient.getInstance().setScreen(new PianoScreen(Text.literal("Piano Gui"),pianoSetting,PBE));
            }
        }
        return ActionResult.SUCCESS;
    }




    private void playMySound(World world,BlockPos pos, PianoSetting pianoSetting) {
        String name = getSoundName(pianoSetting.tone, pianoSetting.level, pianoSetting.isMid);
        float final_volume = (float) pianoSetting.volume / 100;
        world.playSound(null,pos,ModSoundEvent.SOUND_EVENTS.get(name),SoundCategory.BLOCKS, final_volume, 1);
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

}
