package mokilop.paleolithic.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class RockEntity extends SnowballEntity {
    private static final float VEL_DAMGAGE_MULTIPLIER = 1.6f;

    public RockEntity(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    public RockEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    public RockEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), (float) (this.getVelocity().length() * VEL_DAMGAGE_MULTIPLIER));
    }
}
