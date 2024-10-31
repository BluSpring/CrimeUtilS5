package xyz.bluspring.crimeutils5.howl

import net.minecraft.client.model.WolfModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.util.Mth
import net.minecraft.world.entity.animal.Wolf
import xyz.bluspring.crimeutils5.mixin.howl.WolfModelAccessor


class HowlModel<T : Wolf>(root: ModelPart) : WolfModel<T>(root) {
    private val middleBody = root.getChild("middle_body")

    override fun bodyParts(): MutableIterable<ModelPart> {
        return super.bodyParts().toMutableList().apply {
            this.add(this@HowlModel.middleBody)
        }
    }

    override fun prepareMobModel(wolf: T, f: Float, g: Float, h: Float) {
        val model = this as WolfModelAccessor

        if (wolf.isAngry) {
            model.tail.yRot = 0.0F
        } else {
            model.tail.yRot = Mth.cos(f * 0.6662F) * 1.4F * g
        }

        if (wolf.isInSittingPose) {
            model.head.setPos(-1.0f, 3.0f, -13.0f)

            model.upperBody.setPos(0.0f, 7.0f, -7.5f)
            model.upperBody.xRot = -18 * Mth.DEG_TO_RAD
            model.upperBody.yRot = 0.0F

            this.middleBody.setPos(0.0f, 7.5f, -8.0f)
            this.middleBody.xRot = -31 * Mth.DEG_TO_RAD

            model.body.setPos(0.0f, 14.0f, 2.5f)
            model.body.xRot = -45 * Mth.DEG_TO_RAD

            model.tail.setPos( 0.0f, 14.25f, 13.75f)
            model.tail.zRot = 65 * Mth.DEG_TO_RAD

            model.rightHindLeg.setPos(-4.5f, 20.5f, 8.5f)
            model.rightHindLeg.xRot = 270F * Mth.DEG_TO_RAD

            model.leftHindLeg.setPos(4.5f, 20.5f, 8.5f)
            model.leftHindLeg.xRot = 270 * Mth.DEG_TO_RAD

            model.rightFrontLeg.xRot = -3 * Mth.DEG_TO_RAD
            model.rightFrontLeg.setPos(-5.5f, 8.0f, -13.25f)

            model.leftFrontLeg.xRot = -3 * Mth.DEG_TO_RAD
            model.leftFrontLeg.setPos(5.5f, 8.0f, -13.25f)
        } else {
            model.head.setPos(-1.0f, 6.0f, -13.0f)

            model.body.setPos(0.0f, 7.0f, 6.5f)
            model.body.xRot = 0F

            this.middleBody.setPos(0.0f, 7.5f, -8.0f)
            this.middleBody.xRot = model.body.xRot

            model.upperBody.setPos(0.0f, 8.0f, -7.5f)
            model.upperBody.xRot = model.body.xRot

            model.tail.setPos(0.0f, 2.25f, 15.75f)
            model.tail.zRot = 0F

            model.rightHindLeg.setPos(-4.5f, 7.5f, 12.5f)
            model.leftHindLeg.setPos(4.5f, 7.5f, 12.5f)

            model.rightFrontLeg.setPos(-5.5f, 8.0f, -13.25f)
            model.leftFrontLeg.setPos(5.5f, 8.0f, -13.25f)

            model.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g
            model.leftHindLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g

            model.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + 3.1415927F) * 1.4F * g
            model.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g
        }

        model.realHead.zRot = wolf.getHeadRollAngle(h) + wolf.getBodyRollAngle(h, 0.0F)
        model.upperBody.zRot = wolf.getBodyRollAngle(h, -0.08F)
        this.middleBody.zRot = wolf.getBodyRollAngle(h, -0.12F)
        model.body.zRot = wolf.getBodyRollAngle(h, -0.16F)
        model.realTail.zRot = wolf.getBodyRollAngle(h, -0.2F)
    }

    override fun setupAnim(wolf: T, f: Float, g: Float, h: Float, i: Float, j: Float) {
        super.setupAnim(wolf, f, g, h, i, j)
        val model = this as WolfModelAccessor

        model.tail.xRot = (90F * Mth.DEG_TO_RAD) - h
    }

    companion object {
        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val left_front_leg = partdefinition.addOrReplaceChild(
                "left_front_leg",
                CubeListBuilder.create().texOffs(0, 116)
                    .addBox(-3.0f, 13.0f, -3.75f, 6.0f, 3.0f, 6.0f, CubeDeformation(0.0f))
                    .texOffs(0, 98).addBox(-2.5f, 3.0f, -2.75f, 5.0f, 12.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(5.5f, 8.0f, -13.25f)
            )

            val cube_r1 = left_front_leg.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create().texOffs(0, 78)
                    .addBox(-3.0f, -6.0f, -4.0f, 6.0f, 12.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 2.0f, 0.0f, 0.0873f, 0.0f, 0.0f)
            )

            val right_front_leg = partdefinition.addOrReplaceChild(
                "right_front_leg",
                CubeListBuilder.create().texOffs(0, 98).mirror()
                    .addBox(-2.5f, 3.0f, -2.75f, 5.0f, 12.0f, 6.0f, CubeDeformation(0.0f)).mirror(false)
                    .texOffs(0, 116).mirror().addBox(-3.0f, 13.0f, -3.75f, 6.0f, 3.0f, 6.0f, CubeDeformation(0.0f))
                    .mirror(false),
                PartPose.offset(-5.5f, 8.0f, -13.25f)
            )

            val cube_r2 = right_front_leg.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create().texOffs(0, 78).mirror()
                    .addBox(-3.0f, -6.0f, -4.0f, 6.0f, 12.0f, 8.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 2.0f, 0.0f, 0.0873f, 0.0f, 0.0f)
            )

            val left_hind_leg = partdefinition.addOrReplaceChild(
                "left_hind_leg",
                CubeListBuilder.create().texOffs(56, 118)
                    .addBox(-3.0f, 13.5f, -2.5f, 6.0f, 3.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(4.5f, 7.5f, 12.5f)
            )

            val cube_r3 = left_hind_leg.addOrReplaceChild(
                "cube_r3",
                CubeListBuilder.create().texOffs(33, 99)
                    .addBox(-2.504f, -3.0f, -2.5f, 5.005f, 8.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 4.0f, -1.0f, 0.6807f, 0.0f, 0.0f)
            )

            val cube_r4 = left_hind_leg.addOrReplaceChild(
                "cube_r4",
                CubeListBuilder.create().texOffs(33, 113)
                    .addBox(-2.5f, -5.0f, -2.5f, 5.0f, 10.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 11.25f, 1.25f, -0.0524f, 0.0f, 0.0f)
            )

            val cube_r5 = left_hind_leg.addOrReplaceChild(
                "cube_r5",
                CubeListBuilder.create().texOffs(29, 79)
                    .addBox(-2.5f, -3.0f, -4.5f, 6.0f, 10.0f, 9.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.5f, -2.0f, -0.75f, 0.0698f, 0.0f, 0.0f)
            )

            val right_hind_leg = partdefinition.addOrReplaceChild(
                "right_hind_leg",
                CubeListBuilder.create().texOffs(56, 118).mirror()
                    .addBox(-3.0f, 13.5f, -2.5f, 6.0f, 3.0f, 6.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(-4.5f, 7.5f, 12.5f)
            )

            val cube_r6 = right_hind_leg.addOrReplaceChild(
                "cube_r6",
                CubeListBuilder.create().texOffs(33, 99).mirror()
                    .addBox(-2.501f, -3.0f, -2.5f, 5.005f, 8.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 4.0f, -1.0f, 0.6807f, 0.0f, 0.0f)
            )

            val cube_r7 = right_hind_leg.addOrReplaceChild(
                "cube_r7",
                CubeListBuilder.create().texOffs(33, 113).mirror()
                    .addBox(-2.5f, -5.0f, -2.5f, 5.0f, 10.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 11.25f, 1.25f, -0.0524f, 0.0f, 0.0f)
            )

            val cube_r8 = right_hind_leg.addOrReplaceChild(
                "cube_r8",
                CubeListBuilder.create().texOffs(29, 79).mirror()
                    .addBox(-3.5f, -3.0f, -4.5f, 6.0f, 10.0f, 9.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.5f, -2.0f, -0.75f, 0.0698f, 0.0f, 0.0f)
            )

            val head =
                partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-1.0f, 6.0f, -13.0f))

            val cube_r9 = head.addOrReplaceChild(
                "cube_r9",
                CubeListBuilder.create().texOffs(60, 70)
                    .addBox(-5.0f, -9.0f, -6.0f, 10.0f, 8.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, 3.5f, -8.5f, -0.8029f, 0.0f, 0.0f)
            )

            val cube_r10 = head.addOrReplaceChild(
                "cube_r10",
                CubeListBuilder.create().texOffs(48, 50)
                    .addBox(-6.0f, -9.0f, -6.0f, 10.0f, 8.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, -0.5f, -5.5f, -0.6807f, 0.0f, 0.0f)
            )

            val real_head =
                head.addOrReplaceChild("real_head", CubeListBuilder.create(), PartPose.offset(1.0f, -4.0f, -5.0f))

            val cube_r11 = real_head.addOrReplaceChild(
                "cube_r11",
                CubeListBuilder.create().texOffs(72, 14)
                    .addBox(-7.0f, -9.0f, -3.0f, 5.0f, 2.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.5f, 11.55f, -9.1f, 0.0175f, 0.0f, 0.0f)
            )

            val cube_r12 = real_head.addOrReplaceChild(
                "cube_r12",
                CubeListBuilder.create().texOffs(40, 4)
                    .addBox(-6.0f, -9.0f, -3.0f, 4.0f, 4.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.0f, 7.3f, -9.1f, 0.0873f, 0.0f, 0.0f)
            )

            val cube_r13 = real_head.addOrReplaceChild(
                "cube_r13",
                CubeListBuilder.create().texOffs(68, 0)
                    .addBox(-7.0f, -9.0f, -3.0f, 6.0f, 4.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.0f, 8.05f, -10.1f, -0.0698f, 0.0f, 0.0f)
            )

            val cube_r14 = real_head.addOrReplaceChild(
                "cube_r14",
                CubeListBuilder.create().texOffs(36, 0)
                    .addBox(-6.0f, -9.0f, 1.0f, 4.0f, 2.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.0f, 2.8f, -0.85f, 1.1519f, 0.0f, 0.0f)
            )

            val cube_r15 = real_head.addOrReplaceChild(
                "cube_r15",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-6.0f, -9.0f, -1.0f, 12.0f, 12.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 1.5f, -6.5f, -0.0175f, 0.0f, 0.0f)
            )

            val bone8 =
                real_head.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offset(9.0f, 1.5f, -10.5f))

            val cube_r16 = bone8.addOrReplaceChild(
                "cube_r16",
                CubeListBuilder.create().texOffs(103, 0)
                    .addBox(-6.0f, -9.0f, -1.0f, 4.0f, 8.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.75f, 1.5f, 8.5f, -0.4887f, -0.9425f, 0.4887f)
            )

            val cube_r17 = bone8.addOrReplaceChild(
                "cube_r17",
                CubeListBuilder.create().texOffs(103, 0)
                    .addBox(-2.0f, -4.0f, -1.0f, 4.0f, 8.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.0012f, -4.4797f, 8.3684f, 0.2094f, -1.117f, 0.2793f)
            )

            val bone9 =
                real_head.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offset(-9.0f, 1.5f, -10.5f))

            val cube_r18 = bone9.addOrReplaceChild(
                "cube_r18",
                CubeListBuilder.create().texOffs(103, 0).mirror()
                    .addBox(2.0f, -9.0f, -1.0f, 4.0f, 8.0f, 2.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.75f, 1.5f, 8.5f, -0.4887f, 0.9425f, -0.4887f)
            )

            val cube_r19 = bone9.addOrReplaceChild(
                "cube_r19",
                CubeListBuilder.create().texOffs(103, 0).mirror()
                    .addBox(-2.0f, -4.0f, -1.0f, 4.0f, 8.0f, 2.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(4.0012f, -4.4797f, 8.3684f, 0.2094f, 1.117f, -0.2793f)
            )

            val body = partdefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 54)
                    .addBox(-7.0f, -7.5f, -0.5f, 14.0f, 14.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 7.0f, 6.5f)
            )

            val middle_body = partdefinition.addOrReplaceChild(
                "middle_body",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 7.5f, -8.0f)
            )

            val cube_r20 = middle_body.addOrReplaceChild(
                "cube_r20",
                CubeListBuilder.create().texOffs(56, 22)
                    .addBox(-8.0f, -2.0f, -13.0f, 16.0f, 14.0f, 14.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, -6.0f, 13.0f, 0.0698f, 0.0f, 0.0f)
            )

            val upper_body = partdefinition.addOrReplaceChild(
                "upper_body",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 8.0f, -7.5f)
            )

            val cube_r21 = upper_body.addOrReplaceChild(
                "cube_r21",
                CubeListBuilder.create().texOffs(0, 28)
                    .addBox(-8.0f, -8.0f, -6.0f, 16.0f, 14.0f, 12.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, -5.0f, -0.0873f, 0.0f, 0.0f)
            )

            val tail =
                partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0f, 2.25f, 15.75f))

            val real_tail =
                tail.addOrReplaceChild("real_tail", CubeListBuilder.create(), PartPose.offset(0.0f, 1.0f, -1.0f))

            val cube_r22 = real_tail.addOrReplaceChild(
                "cube_r22",
                CubeListBuilder.create().texOffs(104, 118)
                    .addBox(-8.0f, 3.0f, -15.0f, 6.0f, 4.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(5.0f, 2.0f, 12.0f, 0.7854f, 0.0f, 0.0f)
            )

            val cube_r23 = real_tail.addOrReplaceChild(
                "cube_r23",
                CubeListBuilder.create().texOffs(96, 102)
                    .addBox(-8.0f, -2.0f, -15.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.0f, 3.5f, 14.75f, 0.2269f, 0.0f, 0.0f)
            )

            val cube_r24 = real_tail.addOrReplaceChild(
                "cube_r24",
                CubeListBuilder.create().texOffs(104, 82)
                    .addBox(-8.004f, -2.0f, -15.0f, 6.005f, 14.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(5.0f, -0.25f, 15.5f, 0.2269f, 0.0f, 0.0f)
            )

            val cube_r25 = real_tail.addOrReplaceChild(
                "cube_r25",
                CubeListBuilder.create().texOffs(108, 72)
                    .addBox(-8.0f, -2.0f, -13.0f, 6.0f, 6.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(5.0f, -9.75f, 8.0f, 0.9774f, 0.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 128, 128)
        }
    }
}