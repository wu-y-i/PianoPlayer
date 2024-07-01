package com.lution.piano.block;

import com.lution.piano.blockEntity.PianoBlockEntity;
import com.lution.piano.gui.PianoSetting;
import com.lution.piano.gui.screen.PianoScreen;
import com.lution.piano.soundEvent.ModSoundEvent;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;


public class PianoBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider
{
    //        public static IntProperty TONE = IntProperty.of("tone", 1, 7);
//    public static IntProperty LEVEL = IntProperty.of("level", -4,4);
//    public static IntProperty VOLUME = IntProperty.of("volume",0,100);

//
    public static final BooleanProperty LOCKED;
    public static final IntProperty DELAY;

    public static final BooleanProperty SOUNDED;


    public PianoBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(POWERED,false)
                .with(LOCKED,false)
                .with(DELAY,0)
                .with(FACING,Direction.NORTH)
                .with(SOUNDED,false));
    }

    protected int getUpdateDelayInternal(BlockState state) {
        return state.get(DELAY);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PianoBlockEntity(pos, state);
    }

    static {
        LOCKED = Properties.LOCKED;
        DELAY =  IntProperty.of("delay", 0, 120);
        SOUNDED = BooleanProperty.of("sounded");
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, DELAY, LOCKED, POWERED}).add(SOUNDED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            PianoBlockEntity pbe = getPBE(world,pos);
            PianoSetting pianoSetting = pbe.getSettings();
            MinecraftClient.getInstance().setScreen(new PianoScreen(Text.literal("Piano Gui"), pianoSetting, pbe,state));
        }
        return ActionResult.SUCCESS;
    }
    private PianoBlockEntity getPBE(World world,BlockPos pos){
        return (PianoBlockEntity) world.getBlockEntity(pos);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean sounded = state.get(SOUNDED);
            boolean powering = this.hasPower(world,pos,state);
            if (powered&&!sounded&&powering) {
                PianoBlockEntity pbe = getPBE(world,pos);
                playMySound(world,pos,pbe.getSettings());
                world.setBlockState(pos,state.with(SOUNDED,true));
            }
            else if (!powered) {
                world.setBlockState(pos,state.with(SOUNDED,false));
            }
        }

        if (state.canPlaceAt(world, pos)) {
            this.updatePowered(world, pos, state);
        } else {
            BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropStacks(state, world, pos, blockEntity);
            world.removeBlock(pos, false);
            Direction[] var8 = Direction.values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Direction direction = var8[var10];
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }


    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        PianoBlockEntity pbe = getPBE(world,pos);
        if (!this.isLocked(world, pos, state)) {
            boolean bl = (Boolean)state.get(POWERED);
            boolean bl2 = this.hasPower(world, pos, state);
            if (bl && !bl2) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, false), 2);
            } else if (!bl) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, true), 2);
                if (!bl2) {
                    world.scheduleBlockTick(pos, this, getUpdateDelayInternal(state), TickPriority.VERY_HIGH);
                }
            }

        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    private void playMySound(World world, BlockPos pos, PianoSetting pianoSetting) {
        if (pianoSetting.tone == 0) {
            return;
        }
        String name = getSoundName(pianoSetting.tone, pianoSetting.level, pianoSetting.isMid);
        float final_volume = (float) pianoSetting.volume / 100;
        world.playSound(null, pos, ModSoundEvent.SOUND_EVENTS.get(name), SoundCategory.BLOCKS, final_volume, 1);
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
