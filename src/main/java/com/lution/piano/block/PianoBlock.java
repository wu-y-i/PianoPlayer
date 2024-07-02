package com.lution.piano.block;

import com.lution.piano.blockEntity.PianoBlockEntity;
import com.lution.piano.gui.PianoSetting;
import com.lution.piano.gui.screen.PianoScreen;
import com.lution.piano.soundEvent.ModSoundEvent;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.client.MinecraftClient;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;


public class PianoBlock extends AbstractRedstoneGateBlock implements BlockEntityProvider
{
    //        public static IntProperty TONE = IntProperty.of("tone", 1, 7);
//    public static IntProperty LEVEL = IntProperty.of("level", -4,4);
//    public static IntProperty VOLUME = IntProperty.of("volume",0,100);

//
    public static final BooleanProperty LOCKED;

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    public static final BooleanProperty SOUNDED;



    public PianoBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(POWERED,false)
                .with(LOCKED,false)
                .with(FACING,Direction.NORTH)
                .with(SOUNDED,false));
    }



    protected int getUpdateDelayInternal(World world,BlockPos pos) {
        return getPBE(world,pos).getSettings().timer;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PianoBlockEntity(pos, state);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, LOCKED, POWERED}).add(SOUNDED);
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
        //从反编译文件中复制而来
        if (state.canPlaceAt(world, pos)) {
            this.updatePowered(world, pos, state);
        } else {
            BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropStacks(state, world, pos, blockEntity);
            world.removeBlock(pos, false);
            Direction[] directions = Direction.values();

            for (Direction direction : directions) {
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

        }
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        PianoBlockEntity pbe = getPBE(world,pos);
        // 将(1-7) * 3  + random{0,1,2} 以按音调映射给颜色
        world.addParticle(ParticleTypes.NOTE,
                (double)pos.getX() + 0.5, (double)pos.getY() + 1.2, (double)pos.getZ() + 0.5,
                ((double)pbe.getSettings().tone * 3 + (int)Math.round(Math.random()*2)) / 24.0, 0.0, 0.0);
        return true;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getUpdateDelayInternal(BlockState state) {
        return 0;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        PianoBlockEntity pbe = getPBE(world,pos);
        if (!this.isLocked(world, pos, state)) {
            boolean powered = (Boolean)state.get(POWERED);
            boolean powering = this.hasPower(world, pos, state);
            if (powered && !powering) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, false), 2);
            } else if (!powered) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, true), 2);
                if (!powering) {
                    world.scheduleBlockTick(pos, this, getUpdateDelayInternal(world,pos), TickPriority.VERY_HIGH);
                }
            }
        }
    }
    @Override
    protected void updatePowered(World world, BlockPos pos, BlockState state) {
        if (!this.isLocked(world, pos, state)) {
            boolean bl = (Boolean)state.get(POWERED);
            boolean bl2 = this.hasPower(world, pos, state);
            if (bl != bl2 && !world.getBlockTickScheduler().isTicking(pos, this)) {
                TickPriority tickPriority = TickPriority.HIGH;
                if (this.isTargetNotAligned(world, pos, state)) {
                    tickPriority = TickPriority.EXTREMELY_HIGH;
                } else if (bl) {
                    tickPriority = TickPriority.VERY_HIGH;
                }
                world.scheduleBlockTick(pos, this, this.getUpdateDelayInternal(world,pos), tickPriority);
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
        world.addSyncedBlockEvent(pos,this,0,0);
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

    static {
        LOCKED = Properties.LOCKED;
        SOUNDED = BooleanProperty.of("sounded");
    }
}
