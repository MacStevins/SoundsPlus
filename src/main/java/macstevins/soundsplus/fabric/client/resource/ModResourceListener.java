package macstevins.soundsplus.fabric.client.resource;

import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.config.SoundsPlusConfigManager;
import macstevins.soundsplus.fabric.client.gui.widget.SoundsPlusSliderWidget;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModResourceListener {

	private ModResourceListener() {}

	public static void registerModResourcesListener() {
		
		ModUtil.logInfo("Registering Mod Resources Listener");
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
		
			@Override
			public Identifier getFabricId() { return new Identifier(ModUtil.MOD_ID, "resource_listener"); }
		
			@Override
			public void reload(ResourceManager manager) {
				
				ModUtil.logInfo("Reloading Sound Volume Values");
				
				SoundsPlusConfigManager configManager = SoundsPlusConfigManager.getInstance();
				List<Identifier> keys = new ArrayList<>(MinecraftClient.getInstance().getSoundManager().getKeys());
				List<String> namespaces = new ArrayList<>();
				keys.sort(Comparator.comparing(Identifier::toString));
				
				for(Identifier id : keys) {
					
					if(!namespaces.contains(id.getNamespace())) {
						
						namespaces.add(id.getNamespace());
						configManager.createNamespaceConfig(id.getNamespace());
					
					}
					
					double value = configManager.getNamespaceValue(id);
					
					if(value < 1)
						new SoundsPlusSliderWidget(0, 0, id, value);
				
				}
				
			}
		
		});
	
	}

}
