package com.meteor.extrabotany.client.render.tile;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.meteor.extrabotany.common.block.ModBlocks;
import com.meteor.extrabotany.common.block.tile.TileElfJar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;

@SideOnly(Side.CLIENT)
public class RenderElfJar extends TileEntitySpecialRenderer<TileElfJar>
{
	public static int forceManaNumber = -1;

	@Override
	public void render(@Nonnull TileElfJar pool, double d0, double d1, double d2, float f, int digProgress, float unused) {
		if(pool != null && (!pool.getWorld().isBlockLoaded(pool.getPos(), false)
				|| pool.getWorld().getBlockState(pool.getPos()).getBlock() != ModBlocks.elfjar))
			return;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableRescaleNormal();
		float a = 1F;

		GlStateManager.color(1F, 1F, 1F, a);
		if (pool == null) { // A null pool means we are calling the TESR without a pool (on a minecart). Adjust accordingly
			GlStateManager.translate(0, 0, -1);
		} else {
			GlStateManager.translate(d0, d1, d2);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int color = 0xFFFFFF;

		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.color(1, 1, 1, a);
		GlStateManager.enableRescaleNormal();

		int mana = pool.fluidTank.getFluid() == null ? 0 : pool.fluidTank.getFluid().amount;
		int cap = pool.fluidTank.getCapacity();

		float waterLevel = (float) mana / (float) cap * 0.8F;

		float s = 1F / 16F;
		float v = 1F / 8F;
		float w = -v * 3.5F;

		if(waterLevel > 0 && pool.fluidTank.getFluidAmount() > 0) {
			s = 1F / 256F * 14F;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlpha();
			GlStateManager.color(1F, 1F, 1F, a);
			GlStateManager.translate(w, -1F - (0.43F - waterLevel), w);
			GlStateManager.rotate(90F, 1F, 0F, 0F);
			GlStateManager.scale(s, s, s);
			renderIcon(0, 0, getFluidTexture(pool.fluidTank.getFluid()), 16, 16, 240);
			
			GlStateManager.enableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		forceManaNumber = -1;
	}
	
	public static TextureAtlasSprite getFluidTexture(FluidStack fluid) {
		final ResourceLocation textureLocation = fluid.getFluid().getStill(fluid);
		return getTextureAtlasLocation(textureLocation);
	}

	public static TextureAtlasSprite getFluidTexture(Fluid fluid) {
		final ResourceLocation textureLocation = fluid.getStill();
		return getTextureAtlasLocation(textureLocation);
	}

	public static TextureAtlasSprite getTextureAtlasLocation(final ResourceLocation textureLocation) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textureLocation.toString());
	}

	public void renderIcon(int par1, int par2, TextureAtlasSprite par3Icon, int par4, int par5, int brightness) {
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, ClientProxy.POSITION_TEX_LMAP);
		tessellator.getBuffer().pos(par1 + 0, par2 + par5, 0).tex(par3Icon.getMinU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + par5, 0).tex(par3Icon.getMaxU(), par3Icon.getMaxV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + par4, par2 + 0, 0).tex(par3Icon.getMaxU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.getBuffer().pos(par1 + 0, par2 + 0, 0).tex(par3Icon.getMinU(), par3Icon.getMinV()).lightmap(brightness, brightness).endVertex();
		tessellator.draw();
	}

}
