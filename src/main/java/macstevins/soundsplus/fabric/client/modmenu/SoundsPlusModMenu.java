package macstevins.soundsplus.fabric.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import macstevins.soundsplus.fabric.client.gui.screen.option.SoundsPlusOptionsScreen;

public class SoundsPlusModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() { return SoundsPlusOptionsScreen::new; }

}
