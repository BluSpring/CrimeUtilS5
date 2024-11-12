package xyz.bluspring.crimeutils5.mixin.tweaks.howl;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WolfModel.class)
public interface WolfModelAccessor {
    @Accessor
    ModelPart getHead();

    @Accessor
    ModelPart getRealHead();

    @Accessor
    ModelPart getBody();

    @Accessor
    ModelPart getRightHindLeg();

    @Accessor
    ModelPart getLeftHindLeg();

    @Accessor
    ModelPart getRightFrontLeg();

    @Accessor
    ModelPart getLeftFrontLeg();

    @Accessor
    ModelPart getTail();

    @Accessor
    ModelPart getRealTail();

    @Accessor
    ModelPart getUpperBody();
}