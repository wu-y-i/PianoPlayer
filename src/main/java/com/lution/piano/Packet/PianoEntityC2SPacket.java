package com.lution.piano.Packet;

import com.lution.piano.block.PianoBlock;
import com.lution.piano.blockEntity.PianoBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;


public class PianoEntityC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player,
                               ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender){
        BlockPos pos = buf.readBlockPos();
        int tone = buf.readInt();
        int timer = buf.readInt();
        int volume = buf.readInt();
        int level = buf.readInt();
        boolean isMid = buf.readBoolean();

        server.execute(()->{
            BlockEntity blockEntity = player.getServerWorld().getBlockEntity(pos);
            if(blockEntity instanceof PianoBlockEntity pbe){
                player.getServerWorld().setBlockState(pos,pbe.getCachedState().with(PianoBlock.DELAY,timer),3);
                pbe.setTone(tone);
                pbe.setMid(isMid);
                pbe.setTimer(timer);
                pbe.setLevel(level);
                pbe.setVolume(volume);
                pbe.markDirty();
                pbe.sync();
            }
        });
    }
}
