package com.lution.piano.Packet;

import com.lution.piano.PianoPlayer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessage
{
    public static final Identifier PIANO_ENTITY_ID = new Identifier(PianoPlayer.MOD_ID,"update_piano_entity");
    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(PIANO_ENTITY_ID,PianoEntityC2SPacket::receive);
    }

}
