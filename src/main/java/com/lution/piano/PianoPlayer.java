package com.lution.piano;

import com.lution.piano.item.ModItemGroup;
import com.lution.piano.item.ModItems;
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


		LOGGER.info("Start ModItems Initial");
		ModItems.registerModItems();
		LOGGER.info("Finish ModItems Initial");

		LOGGER.info("Start ModItemGroup Initial");
		ModItemGroup.registerModItemGroup();
		LOGGER.info("Finish ModItemGroup Initial");
	}
}