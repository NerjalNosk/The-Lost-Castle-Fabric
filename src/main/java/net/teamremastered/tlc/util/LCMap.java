package net.teamremastered.tlc.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.teamremastered.tlc.TheLostCastle;
import net.teamremastered.tlc.registries.LCTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class LCMap {

    public static boolean nullCheck = false;

    public static ItemStack createMap(ServerWorld serverLevel, BlockPos playerPosition) {
        // Get position of marker
        BlockPos structurePos = serverLevel.locateStructure(LCTags.LOST_CASTLE_MAP, playerPosition, 100, false);
        ItemStack stack;

        // Create map
        if (structurePos == null) {
            stack = FilledMapItem.createMap(serverLevel, 0, 0, (byte) 2 , true, true);
            MapState.addDecorationsNbt(stack, new BlockPos(0, 0, 0), "+", MapIcon.Type.TARGET_X);
            TheLostCastle.LOGGER.error("Something went wrong with The Lost Castle");
            nullCheck = true;
        }
        else {
            stack = FilledMapItem.createMap(serverLevel, structurePos.getX(), structurePos.getZ(), (byte) 2 , true, true);
            MapState.addDecorationsNbt(stack, structurePos, "+", MapIcon.Type.TARGET_X);
        }
        FilledMapItem.fillExplorationMap(serverLevel, stack);

        // Set the name of the map
        stack.setCustomName(Text.of("Lost Castle Map"));

        return stack;
    }
    //Create The Trade
    public static class LCMapTrade implements TradeOffers.Factory {

        @Override
        public TradeOffer create(@NotNull Entity entity, @NotNull Random random){
            int xp = 10;
            int min = 15;
            int max = 25;
            int priceEmeralds = ThreadLocalRandom.current().nextInt(min, max + 1);
            if (!entity.getWorld().isClient && entity.getWorld().getRegistryKey() == World.OVERWORLD) {
                ItemStack map = createMap((ServerWorld) entity.getWorld(), entity.getBlockPos());
                return new TradeOffer(new ItemStack(Items.EMERALD, priceEmeralds), new ItemStack(Items.COMPASS), map, 12, xp, 0.2F);
            }
            return null;
        }
    }

    public static void registerVillagerTrades() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3, factories -> {
            factories.add(new LCMapTrade());
        });
    }

}
