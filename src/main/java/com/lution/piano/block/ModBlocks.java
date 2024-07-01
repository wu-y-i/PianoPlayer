package com.lution.piano.block;

import com.lution.piano.PianoPlayer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block PIANO_BLOCK = registerBlocks("piano_block",
            new PianoBlock(FabricBlockSettings.copyOf(Blocks.NOTE_BLOCK)));
    /**
     * 注册方块
     * @param name 方块名
     * @param block 方块引用
     * @return Block
     */
    public static Block registerBlocks(String name, Block block){
        registerBlockItems(name,block);
        return Registry.register(Registries.BLOCK,new Identifier(PianoPlayer.MOD_ID,name),
                block);
    }
    /**
     * 注册方块物品
     * @param name 方块名称
     * @param block 方块引用
     * @return Item
     */
    private static Item registerBlockItems(String name, Block block){
        return Registry.register(Registries.ITEM,new Identifier(PianoPlayer.MOD_ID , name),
                new BlockItem(block,new FabricItemSettings()));
    }

    private static void add_blocks_to_redstone_group(FabricItemGroupEntries fabricItemGroupEntries){
        fabricItemGroupEntries.add(PIANO_BLOCK);
    }
    public static void registerModBlocks(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(ModBlocks::add_blocks_to_redstone_group);
    }
}
