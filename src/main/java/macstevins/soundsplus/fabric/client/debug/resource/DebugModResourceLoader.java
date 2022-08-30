package macstevins.soundsplus.fabric.client.debug.resource;

import macstevins.soundsplus.fabric.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DebugModResourceLoader {

	private DebugModResourceLoader() {}

	public static void registerModResources() {
		
		if(!ModUtil.DEBUG_MODE)
			return;
		
		ModUtil.logInfo("Registering Debug Mod Resources");
		
		FabricLoader.getInstance().getModContainer(ModUtil.MOD_ID).ifPresent(container -> {
			
			ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("debug"), container, ModUtil.MOD_NAME, ResourcePackActivationType.ALWAYS_ENABLED);
		
		});
	
	}

}
