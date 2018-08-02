package com.meteor.extrabotany.common.block.subtile.functional;

import com.meteor.extrabotany.common.core.handler.ConfigHandler;
import com.meteor.extrabotany.common.item.ItemBinder;
import com.meteor.extrabotany.common.lexicon.LexiconData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;

public class SubTileStardustLotus extends SubTileFunctional{
	
	private static final int COST = 2000;
	private static final int RANGE = 1;
	
	private static final String TAG_MANA = "consumed";
	private static final String TAG_COST = "cost";
	private static final String TAG_X = "Posx";
	private static final String TAG_Y = "Posy";
	private static final String TAG_Z = "Posz";
	private static final String TAG_DIM = "dim";
	private static final String TAG_PAPER = "haspaper";
	int dim = 0;
	int consumed = 0;
	int shouldCost = 0;
	int x,y,z = 0;
	boolean hasPaper = false;
	
	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		cmp.setInteger(TAG_MANA, consumed);
		cmp.setInteger(TAG_COST, shouldCost);
		cmp.setInteger(TAG_X, x);
		cmp.setInteger(TAG_Y, y);
		cmp.setInteger(TAG_Z, z);
		cmp.setBoolean(TAG_PAPER, hasPaper);
		cmp.setInteger(TAG_DIM, dim);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		consumed = cmp.getInteger(TAG_MANA);
		shouldCost = cmp.getInteger(TAG_COST);
		x = cmp.getInteger(TAG_X);
		y = cmp.getInteger(TAG_Y);
		z = cmp.getInteger(TAG_Z);
		hasPaper = cmp.getBoolean(TAG_PAPER);
		dim = cmp.getInteger(TAG_DIM);
	}
	
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) { 
		if(player.getHeldItem(hand).getItem() instanceof ItemBinder){
			ItemBinder bind = (ItemBinder) player.getHeldItem(hand).getItem();
			x = bind.getPosX(player.getHeldItem(hand));
			y = bind.getPosY(player.getHeldItem(hand));
			z = bind.getPosZ(player.getHeldItem(hand));
			dim = bind.getDim(player.getHeldItem(hand));
		}
		return true; 
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		int posx = this.supertile.getPos().getX();
		int posy = this.supertile.getPos().getY();
		int posz = this.supertile.getPos().getZ();
		
		if(!canTPExist(this.getWorld(), this.getPos()) || redstoneSignal > 0)
			return;
		
		shouldCost = (int) (ConfigHandler.BASECOST + Math.sqrt(Math.pow(x-posx, 2) + Math.pow(y-posy, 2) + Math.pow(z-posz, 2))* ConfigHandler.PERCOST);
		
		if(!hasPaper){
			for(EntityItem item : supertile.getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)))){
				if(item.getItem().getItem() == Items.PAPER && item.getItem().getCount() > 0){
					item.getItem().shrink(1);
					hasPaper = true;
				}
			}
		}
		
		if(hasPaper && consumed < shouldCost && mana > ConfigHandler.CONSUMESPEED){
			mana -=ConfigHandler.CONSUMESPEED;
			consumed +=ConfigHandler.CONSUMESPEED;
		}
		
		if(consumed >= shouldCost){
			Botania.proxy.sparkleFX(posx + 0.5F, posy + 1F, posz + 0.5F, 1F, 0.75F, 0.79F, 5F, 10);
			for(EntityLivingBase living : supertile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(supertile.getPos().add(-RANGE, -RANGE, -RANGE), supertile.getPos().add(RANGE + 1, RANGE + 1, RANGE + 1)))){
				living.setPosition(x,y,z);
				hasPaper = false;
				consumed = 0;
			}
		}
	}
	
	private static final BlockPos[] QUARTZ_LOCATIONS = {
			new BlockPos(2, -1, 2), new BlockPos(2, -1, 1), new BlockPos(2, -1, 0), new BlockPos(2, -1, -1), new BlockPos(2, -1, -2),
			new BlockPos(1, -1, 2), new BlockPos(1, -1, -2),
			new BlockPos(0, -1, 2), new BlockPos(0, -1, -2),
			new BlockPos(-1, -1, 2), new BlockPos(-1, -1, -2),
			new BlockPos(-2, -1, 2), new BlockPos(-2, -1, 1), new BlockPos(-2, -1, 0), new BlockPos(-2, -1, -1), new BlockPos(-2, -1, -2)
	};
	
	private static final BlockPos[] LAMP_LOCATIONS = {
			new BlockPos(2, 2, 2), new BlockPos(-2, 2, 2), new BlockPos(2, 2, -2), new BlockPos(-2, 2, -2)
	};
	
	private static final BlockPos[] PILLAR_LOCATIONS = {
			new BlockPos(2, 1, 2), new BlockPos(-2, 1, 2), new BlockPos(2, 1, -2), new BlockPos(-2, 1, -2),
			new BlockPos(2, 0, 2), new BlockPos(-2, 0, 2), new BlockPos(2, 0, -2), new BlockPos(-2, 0, -2)
	};


	public static MultiblockSet makeMultiblockSet() {
		Multiblock mb = new Multiblock();

		for(BlockPos o : QUARTZ_LOCATIONS)
			mb.addComponent(o.up(), Blocks.QUARTZ_BLOCK.getDefaultState());
		for(BlockPos o : LAMP_LOCATIONS)
			mb.addComponent(o.up(), Blocks.SEA_LANTERN.getDefaultState());
		for(BlockPos o : PILLAR_LOCATIONS)
			mb.addComponent(o.up(), Blocks.LAPIS_BLOCK.getDefaultState());

		return mb.makeSet();
	}
	
	public static boolean canTPExist(World world, BlockPos pos) {
		for(BlockPos o : QUARTZ_LOCATIONS)
			if(world.getBlockState(pos.add(o)).getBlock() != Blocks.QUARTZ_BLOCK)
				return false;
		for(BlockPos o : LAMP_LOCATIONS)
			if(world.getBlockState(pos.add(o)).getBlock() != Blocks.SEA_LANTERN)
				return false;

		for(BlockPos o : PILLAR_LOCATIONS)
			if(world.getBlockState(pos.add(o)).getBlock() != Blocks.LAPIS_BLOCK)
				return false;

		return true;
	}
	
	@Override
	public int getMaxMana() {
		return 100000;
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.stardustlotus;
	}
	
	@Override
	public int getColor() {
		return 0x800080;
	}
}