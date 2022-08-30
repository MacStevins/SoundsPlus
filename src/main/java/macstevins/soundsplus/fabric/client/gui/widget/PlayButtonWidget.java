package macstevins.soundsplus.fabric.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import macstevins.soundsplus.fabric.ModUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class PlayButtonWidget extends ButtonWidget {

	public PlayButtonWidget(PressAction onPress) { super(0, 0, 16, 16, new LiteralText(""), onPress); }

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		
		if(!this.active && !this.visible)
			return false;
		
		if(this.isValidClickButton(button) && this.clicked(mouseX, mouseY)) {
			
			this.onClick(mouseX, mouseY);
			return true;
		
		}
		
		return false;
	
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		
		if(!visible)
			return;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, new Identifier(ModUtil.MOD_ID, "textures/gui/play_button.png"));
		
		hovered  = mouseX >= x
				&& mouseY >= y
				&& mouseX < x + width
				&& mouseY < y + height;
		
		drawTexture(matrices, this.x, this.y, 0, isHovered() ? 16: 0, width, height, 16, 32);
	
	}

}
