package macstevins.soundsplus.fabric.client.debug.item;

import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.debug.sound.DebugModSoundEventRegistry;
import macstevins.soundsplus.fabric.client.gui.screen.option.SoundsPlusOptionsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
class SoundPlusMenuItem extends Item {

	protected SoundPlusMenuItem(Settings settings) { super(settings); }

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		
		if(world.getServer() != null && user.isCreative()) {
			
			MinecraftClient client = MinecraftClient.getInstance();
			world.playSound(user, user.getBlockPos(), DebugModSoundEventRegistry.DEBUG_JINGLE, SoundCategory.PLAYERS, 1f, 1f);
			client.setScreen(new SoundsPlusOptionsScreen(client.currentScreen));
		
		}
		
		return super.use(world, user, hand);
	
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		
		ModUtil.logInfo("world.getServer()" + world.getServer());
		
		if((entity.isPlayer() && !((PlayerEntity) entity).isCreative()))
			stack.setCount(0);
	
	}

}
