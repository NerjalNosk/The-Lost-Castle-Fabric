package net.teamremastered.tlc.processors;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldView;
import net.teamremastered.tlc.registries.LCProcessors;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Dynamically generates a foundation below the Castle like the Mansion.
 * Credit to YungNickYoung since I used his Leg Processor and tweaked it.
 * Repo: https://github.com/YUNG-GANG/YUNGs-Better-Strongholds/blob/multiloader/1.19/Common/src/main/java/com/yungnickyoung/minecraft/betterstrongholds/world/processor/LegProcessor.java
 */

@MethodsReturnNonnullByDefault
public class FoundationProcessor extends StructureProcessor {

    public static final FoundationProcessor INSTANCE = new FoundationProcessor();
    public static final Codec<FoundationProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world,
                                                        BlockPos jigsawPiecePos,
                                                        BlockPos jigsawPieceBottomCenterPos,
                                                        StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                        StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                        StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.isOf(Blocks.YELLOW_CONCRETE)) {
            if (world instanceof ChunkRegion chunkRegion && !chunkRegion.getCenterPos().equals(new ChunkPos(blockInfoGlobal.pos))) {
                return blockInfoGlobal;
            }

            BlockState[] foundationBlocks = {
                    Blocks.STONE.getDefaultState(),
                    Blocks.STONE_BRICKS.getDefaultState(),
                    Blocks.CRACKED_STONE_BRICKS.getDefaultState(),
                    Blocks.CRACKED_STONE_BRICKS.getDefaultState(),
                    Blocks.MOSSY_STONE_BRICKS.getDefaultState(),
                    Blocks.POLISHED_ANDESITE.getDefaultState(),
                    Blocks.POLISHED_ANDESITE.getDefaultState()
            };

            // Replace the yellow concrete itself
            if (blockInfoGlobal.state.isOf(Blocks.YELLOW_CONCRETE)) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, RandomBlocks(foundationBlocks), blockInfoGlobal.nbt);
            }

            // Reusable mutable
            BlockPos.Mutable mutable = blockInfoGlobal.pos.mutableCopy().move(Direction.DOWN); // Move down since we already processed the first block
            BlockState currBlockState = world.getBlockState(mutable);

            while (mutable.getY() > world.getBottomY()
                    && mutable.getY() < world.getTopY()
                    && (currBlockState.isAir() || !world.getFluidState(mutable).isEmpty())) {
                // Place block in vertical pillar
                world.getChunk(mutable).setBlockState(mutable, RandomBlocks(foundationBlocks), false);

                // Move down
                mutable.move(Direction.DOWN);
                currBlockState = world.getBlockState(mutable);
            }
        }
        return blockInfoGlobal;
    }

    public BlockState RandomBlocks (BlockState[] randomBlocks) {
        int min = 0;
        int max = randomBlocks.length;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max);

        return randomBlocks[randomNum];
    }

    protected StructureProcessorType<?> getType() {
        return LCProcessors.FOUNDATION_PROCESSOR;
    }
}
