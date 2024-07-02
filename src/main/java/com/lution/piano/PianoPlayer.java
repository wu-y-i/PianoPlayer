package com.lution.piano;

import com.lution.piano.Packet.ModMessage;
import com.lution.piano.block.ModBlocks;
import com.lution.piano.blockEntity.ModBlockEntities;
import com.lution.piano.item.ModItemGroup;
import com.lution.piano.item.ModItems;
import com.lution.piano.soundEvent.ModSoundEvent;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PianoPlayer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "pianoplayer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//初始化

		LOGGER.info("Hello Fabric world!");

		//初始化物品
		LOGGER.info("Start ModItems Initial");
		ModItems.registerModItems();
		LOGGER.info("Finish ModItems Initial");

		//初始化物品栏
		LOGGER.info("Start ModItemGroup Initial");
		ModItemGroup.registerModItemGroup();
		LOGGER.info("Finish ModItemGroup Initial");

		//初始化方块
		LOGGER.info("Start ModBlocks Initial");
		ModBlocks.registerModBlocks();
		LOGGER.info("Finish ModBlocks Initial");

		//初始化音频
		LOGGER.info("Start ModBlocks Initial");
		ModSoundEvent.registerModSoundEvent();
		LOGGER.info("Finish ModBlocks Initial");

		//初始化方块实体
		LOGGER.info("Start ModBlocks Initial");
		ModBlockEntities.registerModBlockEntities();
		LOGGER.info("Finish ModBlocks Initial");

		LOGGER.info("Start ModNetworkMessage Initial");
		ModMessage.registerC2SPackets();
		LOGGER.info("Finish ModNetworkMessage Initial");
	}
}