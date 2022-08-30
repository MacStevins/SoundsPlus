package macstevins.soundsplus.fabric.client;

import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.config.SoundsPlusConfigManager;
import macstevins.soundsplus.fabric.client.debug.item.DebugModItemRegistry;
import macstevins.soundsplus.fabric.client.debug.resource.DebugModResourceLoader;
import macstevins.soundsplus.fabric.client.debug.sound.DebugModSoundEventRegistry;
import macstevins.soundsplus.fabric.client.resource.ModResourceListener;
import net.fabricmc.api.ClientModInitializer;

public class SoundsPlusModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		
		SoundsPlusConfigManager.restartInstance();
		
		ModUtil.logInfo("Hello, Minecraft Fabric " + ModUtil.FABRIC_VERSION);
		ModUtil.logInfo("This is " + ModUtil.MOD_NAME + " " + ModUtil.MOD_VERSION);
		
		if(ModUtil.DEBUG_MODE) {
		
			DebugModResourceLoader.registerModResources();
			DebugModItemRegistry.registerModItems();
			DebugModSoundEventRegistry.registerModSoundEvents();
		
		}
		
		ModResourceListener.registerModResourcesListener();
	
	}

}
