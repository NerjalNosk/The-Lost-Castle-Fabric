package net.teamremastered.tlc.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerAdvancementLoader.class)
public abstract class ServerAdvancementLoaderMixin {
    @WrapOperation(method = "method_20723", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/Advancement$Builder;fromJson(Lcom/google/gson/JsonObject;Lnet/minecraft/predicate/entity/AdvancementEntityPredicateDeserializer;)Lnet/minecraft/advancement/Advancement$Builder;"))
    private Advancement.Builder tlc$buildFromJsonWrapper(JsonObject obj, AdvancementEntityPredicateDeserializer predicateDeserializer, Operation<Advancement.Builder> original, @Local(argsOnly = true) Identifier id) {
        Advancement.Builder result = original.call(obj, predicateDeserializer);
        if (id.equals(Identifier.tryParse("follow_ender_eye"))) {
            String s = "in_castle";
            CriterionConditions c = TickCriterion.Conditions.createLocation(LocationPredicate.feature(StructureKeys.STRONGHOLD));
            AdvancementCriterion criterion = new AdvancementCriterion(c);
            result.criterion(s, criterion);
            result.requirements(new String[][]{{s}});
        }
        return result;
    }
}
