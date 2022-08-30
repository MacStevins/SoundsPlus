package macstevins.soundsplus.fabric.client.config;

public class SoundsPlusConfig {

	protected boolean isDebugMode = false;

	protected boolean runSlowMode = false;

	public boolean isDebugMode() { return isDebugMode; }

	public boolean isSlowMode() { return isDebugMode && runSlowMode; }

}
