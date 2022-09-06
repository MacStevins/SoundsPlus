package macstevins.soundsplus.fabric.client.config;

public class SoundsPlusConfig {

	protected boolean isDebugMode = false;

	protected boolean isThreaded = true;

	protected boolean runSlowMode = false;

	public boolean isDebugMode() { return isDebugMode; }

	public boolean isThreaded() { return isThreaded; }

	public boolean isSlowMode() { return isDebugMode && runSlowMode; }

}
