package macstevins.soundsplus.fabric.client.gui.widget;

import macstevins.soundsplus.fabric.client.config.SoundsPlusConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoundsPlusSliderWidget extends SliderWidget {

	private static final List<Float> soundVolume = new ArrayList<>();

	private final Identifier soundId;

	private final SoundsPlusConfigManager configManager;

	private final WeightedSoundSet soundSet;

	public SoundsPlusSliderWidget(int width, int height, Identifier id, double value) {
		
		super(0, 0, width, height, LiteralText.EMPTY, value);
		
		configManager = SoundsPlusConfigManager.getInstance();
		soundSet = Objects.requireNonNull(MinecraftClient.getInstance().getSoundManager().get(soundId = id));
		
		for(SoundContainer<Sound> sound : soundSet.sounds)
			soundVolume.add(sound.getSound().volume);
		
		setSoundVolume();
		updateMessage();
	
	}

	private void setSoundVolume() {
		
		for(int a = 0; a < soundSet.sounds.size(); a++)
			soundSet.sounds.get(a).getSound().volume = soundVolume.get(a) * (float) value;
	
	}

	@Override
	protected void applyValue() {
		
		setSoundVolume();
		
		configManager.setNamespaceValue(soundId, value);
	
	}

	@Override
	protected void updateMessage() {
		
		Text text = (float) value == (float) getYImage(false) ? ScreenTexts.OFF : new LiteralText((int) (this.value * 100.0) + "%");
		this.setMessage(new TranslatableText("soundsplus.option.slider.volume").append(": ").append(text));
	
	}

}
