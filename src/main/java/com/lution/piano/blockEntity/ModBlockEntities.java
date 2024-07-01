package com.lution.piano.blockEntity;

import com.lution.piano.PianoPlayer;
import com.lution.piano.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author
 * @Description:
 * @date 2024/6/29
 */
public class ModBlockEntities {
    public static final BlockEntityType<PianoBlockEntity> PIANO_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(PianoPlayer.MOD_ID,"piano_block_entity"),
            FabricBlockEntityTypeBuilder.create(
                    PianoBlockEntity::new, ModBlocks.PIANO_BLOCK).build());

    public static void registerModBlockEntities(){

    }

}
