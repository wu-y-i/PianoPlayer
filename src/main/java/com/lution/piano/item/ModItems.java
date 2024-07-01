package com.lution.piano.item;


import com.lution.piano.PianoPlayer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    //创建一个物品并注册
//    public static final Item ICE_ETHER =
//            registerItems("ice_ether",
//                    new Item(new FabricItemSettings()));
//    public static final Item RAW_ICE_ETHER =
//            registerItems("raw_ice_ether",
//                    new Item(new FabricItemSettings()));
//
//    private static void add_items_to_redstone_group(FabricItemGroupEntries fabricItemGroupEntries){
//        fabricItemGroupEntries.add(ICE_ETHER);
//        fabricItemGroupEntries.add(RAW_ICE_ETHER);
//    }
//    /**
//     * 物品的注册方法
//     * @param name 物品名称
//     * @param item 物品引用
//     * @return 返回已注册好的该物品
//     */
//    private static Item registerItems(String name,Item item){
//        return Registry.register(Registries.ITEM,new Identifier(PianoPlayer.MOD_ID,name),item);
//    }
    //初始化方法
    public static void registerModItems(){
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
//                .register(ModItems::add_items_to_redstone_group);
    }
}
