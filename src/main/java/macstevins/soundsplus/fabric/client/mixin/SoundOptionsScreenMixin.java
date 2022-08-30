package macstevins.soundsplus.fabric.client.mixin;

import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.gui.screen.option.SoundsPlusOptionsScreen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SoundSliderWidget;
import net.minecraft.client.option.Option;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends GameOptionsScreen {

	public SoundOptionsScreenMixin() { super(null, null, null); }

	@Inject(method = "init()V", at = @At(value = "HEAD"), cancellable = true)
	public void redrawSoundOptions(CallbackInfo ci) {
		
		int xPos = 0;
		int yPos = height / 6 - 12;
		
		for(SoundCategory category : SoundCategory.values()) {
			
			if(category != SoundCategory.MASTER)
				addDrawableChild(new SoundSliderWidget(client, width / 2 - 155 + xPos % 2 * 160, yPos + 22 * (xPos++ >> 1), category, 150));
			
			else {
				
				addDrawableChild(new SoundSliderWidget(client, width / 2 - 155 + xPos % 2 * 160, yPos + 22 * (xPos >> 1), category, 310));
				xPos += 2;
			
			}
		
		}
		
		addDrawableChild(new ButtonWidget(width / 2 + 5, yPos + 22 * (xPos >> 1), 150, 20, new TranslatableText(ModUtil.MOD_ID + ".option.button.advanced").append("..."), button -> client.setScreen(new SoundsPlusOptionsScreen(this))));
		
		if(xPos % 2 == 1)
			++xPos;
		
		addDrawableChild(Option.AUDIO_DEVICE.createButton(gameOptions, width / 2 - 155, yPos + 22 * (xPos >> 1), 310));
		addDrawableChild(Option.SUBTITLES.createButton(gameOptions, width / 2 - 75, yPos + 22 * ((xPos += 2) >> 1), 150));
		addDrawableChild(new ButtonWidget(width / 2 - 100, yPos + 22 * ((xPos += 2) >> 1), 200, 20, ScreenTexts.DONE, button -> client.setScreen(parent)));
		
		ci.cancel();
	
	}

}
