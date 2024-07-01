package com.lution.piano.soundEvent;

import com.lution.piano.PianoPlayer;
import net.minecraft.client.sound.Sound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;

public class ModSoundEvent {
    public static Map<String,SoundEvent> SOUND_EVENTS = new HashMap<>();
    private static void registerSoundEvent(String soundName){
        Identifier id = new Identifier(PianoPlayer.MOD_ID, soundName);
        SoundEvent soundEvent = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, soundEvent);
        SOUND_EVENTS.put(soundName,soundEvent);
    }
    public static void registerModSoundEvent(){
        String[] strings = {"n.","a.","aa.","aaa.","d.","dd.","ddd."};
        for (String string : strings){
            for(int i=1;i<=7;i++){
                String name = string + i;
                registerSoundEvent(name);
            }
            registerSoundEvent(string+"1_");
            registerSoundEvent(string+"2_");
            registerSoundEvent(string+"4_");
            registerSoundEvent(string+"5_");
            registerSoundEvent(string+"6_");
        }
        registerSoundEvent("aaaa.1");
        registerSoundEvent("dddd.6");
        registerSoundEvent("dddd.6_");
        registerSoundEvent("dddd.7");

    }
}
