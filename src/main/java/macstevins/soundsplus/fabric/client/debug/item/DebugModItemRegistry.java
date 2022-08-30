package macstevins.soundsplus.fabric.client.debug.item;

import macstevins.soundsplus.fabric.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class DebugModItemRegistry {

	public static Item SOUNDSPLUS_MENU = registerItem(ModUtil.MOD_ID + "_menu", new SoundPlusMenuItem(new FabricItemSettings().group(ItemGroup.MISC)
																																	.maxCount(1)
																																	.rarity(Rarity.EPIC)));

	private DebugModItemRegistry() {}

	public static void registerModItems() { if(ModUtil.DEBUG_MODE) ModUtil.logInfo("Registering Debug Mod Items"); }

	private static Item registerItem(String name, Item item) { return ModUtil.DEBUG_MODE ? Registry.register(Registry.ITEM, new Identifier(ModUtil.MOD_ID, name), item) : null; }

}
