package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiDragonForge extends ContainerScreen<ContainerDragonForge> {
    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
    private static final ResourceLocation TEXTURE_LIGHTNING = new ResourceLocation("iceandfire:textures/gui/dragonforge_lightning.png");
    private final PlayerInventory playerInventory;
    private ContainerDragonForge tileFurnace;
    private int dragonType;

    public GuiDragonForge(ContainerDragonForge container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container;
        if (tileFurnace instanceof ContainerDragonForge) {
            this.dragonType = tileFurnace.isFire;
        }
    }

    protected void func_230451_b_(MatrixStack stack, int mouseX, int mouseY) {
        FontRenderer font = this.getMinecraft().fontRenderer;
        if (tileFurnace != null) {
            String s = I18n.format("block.iceandfire.dragonforge_" + DragonType.getNameFromInt(dragonType) + "_core");
            font.func_238421_b_(stack, s, this.xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
        }
        font.func_238421_b_(stack, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (dragonType == 0) {
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_FIRE);
        } else if (dragonType == 1) {
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_ICE);
        } else{
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_LIGHTNING);
        }
        int k = (this.field_230708_k_ - this.xSize) / 2;
        int l = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, k, l, 0, 0, this.xSize, this.ySize);
        int i1 = this.func_175381_h(126);
        this.func_238474_b_(matrixStack, k + 12, l + 23, 0, 166, i1, 38);
    }

    private int func_175381_h(int p_175381_1_) {
        TileEntity te = IceAndFire.PROXY.getRefrencedTE();
        int j = 0;
        int maxCookTime = 1000;
        if (te instanceof TileEntityDragonforge) {
            maxCookTime = ((TileEntityDragonforge) te).getMaxCookTime(tileFurnace.getSlot(0).getStack(), tileFurnace.getSlot(1).getStack());
            j = Math.min(((TileEntityDragonforge) te).cookTime, maxCookTime);
        }
        return j != 0 ? j * p_175381_1_ / maxCookTime : 0;
    }

    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(p_230430_1_);
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }
}