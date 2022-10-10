package net.teamremastered.tlc.registries;

import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamremastered.tlc.TheLostCastle;
import net.teamremastered.tlc.processors.FoundationProcessor;

public class LCProcessors {

    public static StructureProcessorType<FoundationProcessor> FOUNDATION_PROCESSOR = () -> FoundationProcessor.CODEC;

    public static void init() {
        registerProcessors();
    }

    private static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(TheLostCastle.MODID, "foundation_processor"), FOUNDATION_PROCESSOR);
    }
}
