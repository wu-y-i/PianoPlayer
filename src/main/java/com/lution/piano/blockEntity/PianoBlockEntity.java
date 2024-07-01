package com.lution.piano.blockEntity;

import com.lution.piano.PianoPlayer;
import com.lution.piano.block.ModBlocks;
import com.lution.piano.block.PianoBlock;
import com.lution.piano.gui.PianoSetting;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;


public class PianoBlockEntity extends BlockEntity
{
    private int tone = 0;
    private int volume = PianoSetting.default_volume;
    private int timer = PianoSetting.default_timer;
    private int level = 0;
    private boolean isMid = false;

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("tone", tone);
        nbt.putInt("volume", volume);
        nbt.putInt("level", level);
        nbt.putInt("timer", timer);
        nbt.putBoolean("isMid", isMid);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        tone = nbt.getInt("tone");
        volume = nbt.getInt("volume");
        level = nbt.getInt("level");
        timer = nbt.getInt("timer");
        isMid = nbt.getBoolean("isMid");
    }

    public PianoBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIANO_BLOCK_ENTITY, pos, state);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }


    /**
     * getter、setter
     **********************/

    public PianoSetting getSettings(){
        return new PianoSetting(this.tone,this.volume,this.timer,this.level,this.isMid);
    }

    public void setTone(int tone) {
        if(tone>7){
            tone = 1;
        }
        this.tone = tone;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMid(boolean mid) {
        isMid = mid;
    }
}