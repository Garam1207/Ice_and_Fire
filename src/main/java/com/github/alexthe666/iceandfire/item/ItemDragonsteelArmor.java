package com.github.alexthe666.iceandfire.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.IceAndFire;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDragonsteelArmor extends ArmorItem implements IProtectAgainstDragonItem {

    private IArmorMaterial material;

    // NOTE: tweakbsd added ctor
    public ItemDragonsteelArmor(IArmorMaterial material, int renderIndex, EquipmentSlotType slot, String gameName, String name, Item.Properties properties) {

        super(material, slot, properties.group(IceAndFire.TAB_ITEMS));
        this.material = material;
        this.setRegistryName(IceAndFire.MODID, gameName);
    }
    public ItemDragonsteelArmor(IArmorMaterial material, int renderIndex, EquipmentSlotType slot, String gameName, String name) {

        // NOTE: func_234689_a_ -> isImmuneToFire()
        super(material, slot, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.material = material;
        this.setRegistryName(IceAndFire.MODID, gameName);
    }


    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        int legs = 11;
        int armor = 10;
        if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            legs = 13;
            armor = 12;
        }
        if (material == IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL) {
            legs = 21;
            armor = 20;
        }
        return (A) IceAndFire.PROXY.getArmorModel(slot == EquipmentSlotType.LEGS ? legs : armor);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.dragonscales_armor.desc").func_240699_a_(TextFormatting.GRAY));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (material == IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        } else if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        } else {
            return "iceandfire:textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        }
    }


    // NOTE: tweakbsd added knockback resistance modifier
    @Nonnull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
        Multimap<Attribute, AttributeModifier> ret = super.getAttributeModifiers(slot);
        UUID uuid = new UUID(Registry.ITEM.getKey(this).hashCode() + slot.toString().hashCode(), 0);
        if (slot == getEquipmentSlot()) {
            ret = HashMultimap.create(ret);
            int reduction = material.getDamageReductionAmount(slot);

            // NOTE: round to lower number of -> reduction / 175 <- then make it
            double knockbackResistance = (double)((int)Math.ceil(((double)reduction / 150.0F) * 1000.0) / 1000);

            // NOTE: tweakbsd field_233820_c_ aka KNOCKBACK_RESISTANCE
            ret.put(Attributes.field_233820_c_,
                    new AttributeModifier(uuid, "Dragonsteel modifier " + this.slot, knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        return ret;
    }


}
