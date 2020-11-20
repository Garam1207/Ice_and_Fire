package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.util.IAFMath;

import net.minecraft.util.math.MathHelper;

public class IceAndFireTabulaModelAnimator {

    protected TabulaModel baseModel;

    public IceAndFireTabulaModelAnimator(TabulaModel baseModel) {
        this.baseModel = baseModel;
    }

    public void setRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += limbSwingAmount * distance(model.rotateAngleX, x);
        model.rotateAngleY += limbSwingAmount * distance(model.rotateAngleY, y);
        model.rotateAngleZ += limbSwingAmount * distance(model.rotateAngleZ, z);
    }

    public void addToRotateAngle(AdvancedModelBox model, float limbSwingAmount, float x, float y, float z) {
        model.rotateAngleX += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationX, x);
        model.rotateAngleY += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationY, y);
        model.rotateAngleZ += Math.min(limbSwingAmount * 2, 1) * distance(model.defaultRotationZ, z);
    }

    public boolean isPartEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.defaultRotationX && pose.rotateAngleY == original.defaultRotationY && pose.rotateAngleZ == original.defaultRotationZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.defaultPositionX && pose.rotationPointY == original.defaultPositionY && pose.rotationPointZ == original.defaultPositionZ;
    }

    public void transitionTo(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime, boolean oldFashioned) {
        if (oldFashioned) {
            from.rotateAngleX += ((to.rotateAngleX - from.rotateAngleX) / maxTime) * timer;
            from.rotateAngleY += ((to.rotateAngleY - from.rotateAngleY) / maxTime) * timer;
            from.rotateAngleZ += ((to.rotateAngleZ - from.rotateAngleZ) / maxTime) * timer;
        } else {
            transitionAngles(from, to, timer, maxTime);
        }
        from.rotationPointX += ((to.rotationPointX - from.rotationPointX) / maxTime) * timer;
        from.rotationPointY += ((to.rotationPointY - from.rotationPointY) / maxTime) * timer;
        from.rotationPointZ += ((to.rotationPointZ - from.rotationPointZ) / maxTime) * timer;

        //from.offsetX += ((to.offsetX - from.offsetX) / maxTime) * timer;
        //from.offsetY += ((to.offsetY - from.offsetY) / maxTime) * timer;
        //from.offsetZ += ((to.offsetZ - from.offsetZ) / maxTime) * timer;
    }

    public void transitionAngles(AdvancedModelBox from, AdvancedModelBox to, float timer, float maxTime) {
        from.rotateAngleX += ((distance(from.rotateAngleX, to.rotateAngleX)) / maxTime) * timer;
        from.rotateAngleY += ((distance(from.rotateAngleY, to.rotateAngleY)) / maxTime) * timer;
        from.rotateAngleZ += ((distance(from.rotateAngleZ, to.rotateAngleZ)) / maxTime) * timer;
    }

    public float distance(float rotateAngleFrom, float rotateAngleTo) {
        return (float) IAFMath.atan2_accurate(MathHelper.sin(rotateAngleTo - rotateAngleFrom), MathHelper.cos(rotateAngleTo - rotateAngleFrom));
    }

    public void rotate(ModelAnimator animator, AdvancedModelBox model, float x, float y, float z) {
        animator.rotate(model, (float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
    }

    public void moveToPose(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            if (!isPartEqual(baseModel.getCube(cube.boxName), modelTo.getCube(cube.boxName))) {
                float toX = modelTo.getCube(cube.boxName).rotateAngleX;
                float toY = modelTo.getCube(cube.boxName).rotateAngleY;
                float toZ = modelTo.getCube(cube.boxName).rotateAngleZ;
                model.llibAnimator.rotate(cube, distance(cube.rotateAngleX, toX), distance(cube.rotateAngleY, toY), distance(cube.rotateAngleZ, toZ));
            }
            if (!isPositionEqual(baseModel.getCube(cube.boxName), modelTo.getCube(cube.boxName))) {
                float toX = modelTo.getCube(cube.boxName).rotationPointX;
                float toY = modelTo.getCube(cube.boxName).rotationPointY;
                float toZ = modelTo.getCube(cube.boxName).rotationPointZ;
                model.llibAnimator.move(cube, distance(cube.rotationPointX, toX), distance(cube.rotationPointY, toY), distance(cube.rotationPointZ, toZ));
            }
        }
    }

}
