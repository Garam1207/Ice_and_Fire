package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;

public class DragonAIEscort extends Goal {
    private final EntityDragonBase dragon;
    private final double movementSpeed;
    private Path path;

    public DragonAIEscort(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute() {
        return this.dragon.canMove() && this.dragon.getAttackTarget() == null && this.dragon.getOwner() != null && this.dragon.getCommand() == 2;
    }

    public void tick() {
        if (this.dragon.getOwner() != null) {
            double dist = this.dragon.getDistance(this.dragon.getOwner());
            if (dist > this.dragon.getBoundingBox().getAverageEdgeLength() && (!this.dragon.isFlying() && !this.dragon.isHovering() || !dragon.isAllowedToTriggerFlight())) {
                this.dragon.getNavigator().tryMoveToEntityLiving(this.dragon.getOwner(), 1.5F);
            }
            if ((dist > 30 || this.dragon.getOwner().getPosY() - this.dragon.getPosY() > 8) && !this.dragon.isFlying() && !this.dragon.isHovering() && dragon.isAllowedToTriggerFlight()) {
                this.dragon.setHovering(true);
                this.dragon.setSleeping(false);
                this.dragon.setSitting(false);
                this.dragon.flyTicks = 0;
            }
        }

    }

    public boolean shouldContinueExecuting() {
        return this.dragon.canMove() && this.dragon.getAttackTarget() == null && this.dragon.getOwner() != null && this.dragon.getOwner().isAlive() && (this.dragon.getDistance(this.dragon.getOwner()) > 15 || !this.dragon.getNavigator().noPath());
    }

}
