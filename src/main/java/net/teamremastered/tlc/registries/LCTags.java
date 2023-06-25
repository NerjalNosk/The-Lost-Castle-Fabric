package net.teamremastered.tlc.registries;

import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.teamremastered.tlc.TheLostCastle;

public class LCTags {

    //useless init but I like knowing in my main class that the tags are registered
    public static void init() {}

    public static TagKey<Structure> LOST_CASTLE_MAP = TagKey.of(RegistryKeys.STRUCTURE, new Identifier(TheLostCastle.MODID, "lost_castle_map"));
}
