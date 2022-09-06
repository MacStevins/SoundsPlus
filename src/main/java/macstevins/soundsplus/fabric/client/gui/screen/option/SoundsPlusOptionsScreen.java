package macstevins.soundsplus.fabric.client.gui.screen.option;

import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.gui.widget.option.SoundVolumeListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;

public class SoundsPlusOptionsScreen extends Screen {

	private final Screen parent;

	private SoundVolumeListWidget listWidget;

	private boolean fromButton;

	public SoundsPlusOptionsScreen(Screen parent) {
		
		super(new TranslatableText(ModUtil.MOD_ID + ".option.title.advanced"));
		
		this.parent = parent;
	
	}

	@Override
	public void close() {
		
		client.getSoundManager().stopAll();
		
		if(fromButton)
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
		
		client.setScreen(parent);
	
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		
		renderBackgroundTexture(0);
		listWidget.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, textRenderer, title, width / 2, 15, 0xFFFFFF);
		
		super.render(matrices, mouseX, mouseY, delta);
	
	}

	@Override
	protected void init() {
		
		addSelectableChild(listWidget = new SoundVolumeListWidget(this));
		addDrawableChild(new ButtonWidget(width / 2 - 100, height - 28, 200, 20, ScreenTexts.DONE, button -> {
			
			fromButton = true;
			close();
		
		}));
		
		System.gc();
	
	}

}
