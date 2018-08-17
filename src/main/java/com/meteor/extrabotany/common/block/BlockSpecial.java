package com.meteor.extrabotany.common.block;

import javax.annotation.Nonnull;

import com.meteor.extrabotany.ExtraBotany;
import com.meteor.extrabotany.common.core.handler.ConfigHandler;
import com.meteor.extrabotany.common.lib.LibBlocksName;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class BlockSpecial extends BlockSpecialFlower{

	static {
		if(ConfigHandler.ENABLE_BE)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_BLOODYENCHANTRESS);
		if(ConfigHandler.ENABLE_SB)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_SUNBLESS);
		if(ConfigHandler.ENABLE_MB)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_MOONBLESS);
		if(ConfigHandler.ENABLE_OV)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_OMINIVIOLET);
		if(ConfigHandler.ENABLE_SS)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_STONESIA);
		if(ConfigHandler.ENABLE_TK)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_TINKLE);
		if(ConfigHandler.ENABLE_BF)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_BELLFLOWER);
		if(ConfigHandler.ENABLE_AF)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_ANNOYINGFLOWER);
		if(ConfigHandler.ENABLE_SL)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_STARDUSTLOTUS);
		if(ConfigHandler.ENABLE_ML)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_MANALINKIUM);
		if(ConfigHandler.ENABLE_RL)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_REIKARLILY);
		if(ConfigHandler.ENABLE_EO)
			ExtraBotany.subtilesForCreativeMenu.add(LibBlocksName.SUBTILE_ENCHANTEDORCHID);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		for(String s : ExtraBotany.subtilesForCreativeMenu) {
			stacks.add(ItemBlockSpecialFlower.ofType(s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				stacks.add(ItemBlockSpecialFlower.ofType(BotaniaAPI.miniFlowers.get(s)));
		}
	}
	
}
