package macstevins.soundsplus.fabric;

import macstevins.soundsplus.fabric.client.config.SoundsPlusConfigManager;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class ModUtil {

	/** Debug Mode */

	public static final boolean DEBUG_MODE = (ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0) || SoundsPlusConfigManager.getConfig().isDebugMode();

	/** Mod Details */

	public static final String FABRIC_VERSION = FabricLoaderImpl.VERSION;

	public static final String MOD_ID = "soundsplus";

	public static final String MOD_NAME = "Sounds+" + (DEBUG_MODE ? " (Debug Mode)" : "");

	public static final String MOD_VERSION = "0.5.0-1.18.2";

	/** Mod Utilities */

	private static final Logger logger = LoggerFactory.getLogger(MOD_NAME);

	public static void logError(Exception exception) {
		
		StringBuilder builder = new StringBuilder();
		
		for(StackTraceElement element : exception.getStackTrace())
			builder.append("\tat ").append(element.toString()).append("\n");
		
		logger.error("[" + MOD_NAME + "] " + exception + "\n" + builder);
	
	}

	public static void logInfo(Object message) { logger.info("[" + MOD_NAME + "] " + message); }

	public static void logInfof(@NotNull String format, Object... args) { logger.info("[" + MOD_NAME + "] " + format.formatted(args)); }

	public static void logWarn(Object message) { logger.warn("[" + MOD_NAME + "] " + message); }

	public static void logWarnf(@NotNull String format, Object... args) { logger.warn("[" + MOD_NAME + "] " + format.formatted(args)); }

	private ModUtil() {}

}
