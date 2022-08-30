package macstevins.soundsplus.fabric.client.gui.widget.option;

import com.google.common.collect.ImmutableList;
import macstevins.core.util.StringUtil;
import macstevins.soundsplus.fabric.ModUtil;
import macstevins.soundsplus.fabric.client.config.SoundsPlusConfig;
import macstevins.soundsplus.fabric.client.config.SoundsPlusConfigManager;
import macstevins.soundsplus.fabric.client.gui.widget.PlayButtonWidget;
import macstevins.soundsplus.fabric.client.gui.widget.SoundsPlusSliderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SoundVolumeListWidget extends ElementListWidget<SoundVolumeListWidget.Entry> {

	private final Language language;

	private final Screen parent;

	private final SoundManager soundManager;

	private final SoundsPlusConfigManager configManager;

	private final TextRenderer textRenderer;

	private final Thread entriesLoader;

	private final String[] progressStages = new String[] {"oooooo", "Oooooo", "oOoooo", "ooOooo", "oooOoo", "ooooOo", "oooooO"};

	private boolean isEntriesLoaded = false;

	private float sideGap = -6;

	private int longestLength = 0;

	public SoundVolumeListWidget(Screen parent) {
		
		super(MinecraftClient.getInstance(), parent.width, parent.height, 38, parent.height - 36, 20);
		
		this.parent = parent;
		language = Language.getInstance();
		soundManager = client.getSoundManager();
		configManager = SoundsPlusConfigManager.getInstance();
		textRenderer = client.textRenderer;
		
		SoundsPlusConfig config = SoundsPlusConfigManager.getConfig();
		
		entriesLoader = new Thread(() -> {
			
			synchronized(client.gameRenderer) {
			
				try {
					
					Thread.sleep(500);
					
					List<Identifier> keys = new ArrayList<>(soundManager.getKeys());
					List<String> namespaces = new ArrayList<>();
					keys.sort(Comparator.comparing(Identifier::toString));
					
					for(Identifier id : keys) {
						
						if(!namespaces.contains(id.getNamespace())) {
							
							String categoryKey = ModUtil.MOD_ID + ".namespace." + id.getNamespace();
							
							addEntry(new CategoryEntry(new LiteralText(language.hasTranslation(categoryKey) ?
									language.get(categoryKey) :
									StringUtil.capitalizeEachWord(id.getNamespace()))));
							
							namespaces.add(id.getNamespace());
							configManager.createNamespaceConfig(id.getNamespace());
						
						}
						
						addEntry(new VolumeEntry(new LiteralText(translateKeyBites(id)), id));
						
						if(config.isSlowMode())
							Thread.sleep(0, 1);
					
					}
					
					addEntry(new EmptyEntry());
					isEntriesLoaded = true;
					
					System.gc();
				
				}
				catch(Exception e) { ModUtil.logError(e); }
			
			}
		
		}, this.getClass().getSimpleName());
	
	}

	private String translateKeyBites(Identifier id) {
		
		String uniqueKey = ModUtil.MOD_ID + ".key.unique." + id.getNamespace();
		String[] keySplit = id.getPath().split("[.]");
		StringBuilder builder = new StringBuilder();
		int a = 0;
		
		for(String keyBite : keySplit) {
			
			String commonKey = ModUtil.MOD_ID + ".key." + id.getNamespace() + "." + keyBite;
			
			builder.append(language.hasTranslation(uniqueKey += "." + keyBite) ?
								language.get(uniqueKey) :
								(language.hasTranslation(commonKey) ?
										language.get(commonKey) :
										StringUtil.capitalizeEachWord(keyBite.replaceAll("_", " "))));
			
			if(++a < keySplit.length)
				builder.append(" | ");
		
		}
		
		return builder.toString().trim();
	
	}

	@Override
	public int getRowWidth() { return client.getWindow().getScaledWidth() - (int) (sideGap * 2) - 14; }

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		
		if(!isEntriesLoaded) {
			
			drawCenteredText(matrices, textRenderer, progressStages[(int) (Util.getMeasuringTimeMs() / 150 % progressStages.length)], width / 2, height / 2, 0xFFFFFF);
			
			if(!entriesLoader.isAlive())
				entriesLoader.start();
			
			return;
		
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	
	}

	@Override
	protected int getScrollbarPositionX() { return client.getWindow().getScaledWidth() - (int) sideGap - 6; }

	private static class EmptyEntry extends Entry {}

	private class CategoryEntry extends Entry {
	
		private final Text text;
	
		private final int textWidth;
	
		private CategoryEntry(Text text) {
			
			this.text = text;
			this.textWidth = textRenderer.getWidth(text);
		
		}
	
		@Override
		public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) { textRenderer.draw(matrices, this.text, (float) (width / 2 - textWidth / 2), (float) (y + entryHeight - client.textRenderer.fontHeight - 1), 0xFFFFFF); }
	
	}

	private class VolumeEntry extends Entry {
	
		private final Identifier soundID;
	
		private final PlayButtonWidget playButton;
	
		private final SoundsPlusSliderWidget sliderVolume;
	
		private final Text text;
	
		private VolumeEntry(Text text, Identifier soundID) {
			
			this.text = text;
			this.soundID = soundID;
			
			int textWidth = textRenderer.getWidth(text);
			if(textWidth > longestLength)
				longestLength = textWidth;
			
			sliderVolume = new SoundsPlusSliderWidget(150, 20, soundID, configManager.getNamespaceValue(soundID));
			playButton = new PlayButtonWidget(button -> {
				
				soundManager.stopAll();
				soundManager.play(new PositionedSoundInstance(soundID, SoundCategory.MASTER, 1, 1, false, 0, SoundInstance.AttenuationType.NONE, 0, 0, 0, true));
			
			});
		
		}
	
		@Override
		public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			
			float clientWidthCenter = client.getWindow().getScaledWidth() / 2f;
			float textY = (float) (y + (sliderVolume.getHeight() / 2) - (textRenderer.fontHeight / 2));
			int gap = 8;
			
			if(sideGap == -6) {
				
				sideGap = Math.max(clientWidthCenter - gap - longestLength, 10);
				getScrollbarPositionX();
			
			}
			
			textRenderer.draw(matrices, text, sideGap, textY, 0xFFFFFF);
			
			playButton.x = getScrollbarPositionX() - playButton.getWidth() - gap;
			playButton.y = (y + (sliderVolume.getHeight() / 2) - (playButton.getHeight() / 2));
			
			sliderVolume.x = (int) clientWidthCenter + gap + (playButton.x - (int) (clientWidthCenter + gap) - sliderVolume.getWidth() - 4);
			sliderVolume.y = y;
			
			sliderVolume.render(matrices, mouseX, mouseY, tickDelta);
			playButton.render(matrices, mouseX, mouseY, tickDelta);
			
			if(mouseX >= sideGap - 4
			&& mouseY >= textY - 4
			&& mouseX < sideGap + textRenderer.getWidth(text) + 4
			&& mouseY < textY + textRenderer.fontHeight + 4) {
				
				Text idText = new LiteralText(soundID.toString());
				
//				List<OrderedText> tooltipText = new ArrayList<>(textRenderer.wrapLines(idText, textRenderer.getWidth(idText)));
//				tooltipText.add(new LiteralText("").asOrderedText());
//				tooltipText.add(new LiteralText("Volume:").formatted(Formatting.BOLD).asOrderedText());
				
//				parent.renderOrderedTooltip(matrices, tooltipText, mouseX, mouseY);
				
				parent.renderOrderedTooltip(matrices, textRenderer.wrapLines(idText, textRenderer.getWidth(idText)), mouseX, mouseY);
			
			}
		
		}
	
		@Override
		public List<? extends Element> children() { return ImmutableList.of(sliderVolume, playButton); }
	
		@Override
		public List<? extends Selectable> selectableChildren() { return ImmutableList.of(sliderVolume, playButton); }
	
	}

	abstract static class Entry extends ElementListWidget.Entry<Entry> {
	
		@Override
		public List<? extends Element> children() { return Collections.emptyList(); }
	
		@Override
		public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {}
	
		@Override
		public List<? extends Selectable> selectableChildren() { return Collections.emptyList(); }
	
	}

}
