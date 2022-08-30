package macstevins.soundsplus.fabric.client.debug.sound;

import macstevins.soundsplus.fabric.ModUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class DebugModSoundEventRegistry {

	public static SoundEvent DEBUG_JINGLE = registerSoundEvent("debug.debug_jingle");

	private DebugModSoundEventRegistry() {}
	public static void registerModSoundEvents() { if(ModUtil.DEBUG_MODE) ModUtil.logInfo("Registering Debug Mod Sound Events"); }

	private static SoundEvent registerSoundEvent(String name) {
		
		if(ModUtil.DEBUG_MODE) {
			
			Identifier id = new Identifier(ModUtil.MOD_ID, name);
			return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
		
		}
		return null;
	
	}

}
