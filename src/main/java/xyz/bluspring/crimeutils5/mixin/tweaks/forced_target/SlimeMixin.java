package xyz.bluspring.crimeutils5.mixin.tweaks.forced_target;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.crimeutils5.entity.ForcedTargetEntity;

import java.util.UUID;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob implements ForcedTargetEntity {
    @Unique private UUID cc$assignedTarget = null;

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void forceTargetToAssignedTarget(CallbackInfo ci) {
        if (cc$assignedTarget != null && (this.getTarget() == null || !this.getTarget().getUUID().equals(cc$assignedTarget))) {
            var player = this.level().getPlayerByUUID(cc$assignedTarget);

            if (player == null)
                return;

            this.setTarget(player);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void storeAssignedTargetData(CompoundTag compound, CallbackInfo ci) {
        if (cc$assignedTarget != null)
            compound.putUUID("CCAssignedTarget", cc$assignedTarget);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadAssignedTargetData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("CCAssignedTarget"))
            this.cc$assignedTarget = compound.getUUID("CCAssignedTarget");
    }

    @Override
    public @Nullable UUID getCrimecraft_forcedTarget() {
        return this.cc$assignedTarget;
    }

    @Override
    public void setCrimecraft_forcedTarget(@Nullable UUID uuid) {
        this.cc$assignedTarget = uuid;
    }
}
