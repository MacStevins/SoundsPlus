package macstevins.soundsplus.fabric.client.config;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import macstevins.soundsplus.fabric.ModUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundsPlusConfigManager {

	private static final String CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(ModUtil.MOD_ID).toString();

	private static final File CONFIG_FILE =  new File(CONFIG_FOLDER.concat(".json"));

	private static final File NAMESPACE_FOLDER = new File(CONFIG_FOLDER);

	private static final Gson GSON = new GsonBuilder().serializeNulls().create();

	private static final Map<String, File> NAMESPACE_CONFIG_FILE = new HashMap<>();

	private static SoundsPlusConfig config = new SoundsPlusConfig();

	private static SoundsPlusConfigManager instance;

	private SoundsPlusConfigManager() {
		
		try {
			
			LogManager.getLogger(ModUtil.MOD_ID).info("[Sounds+] Loading Configs");
			
			createNamespaceConfigFolder();
			
			if(CONFIG_FILE.exists()) {
				
				JsonReader reader = createJsonReader(CONFIG_FILE);
				
				config = GSON.fromJson(reader, SoundsPlusConfig.class);
				
				reader.close();
			
			}
		
		}
		catch(Exception e) { e.printStackTrace(); }
	
	}

	public static SoundsPlusConfigManager getInstance() { return instance; }

	public static SoundsPlusConfigManager restartInstance() {
		
		if(instance != null) {
			
			instance = null;
			System.gc();
		
		}
		
		return instance = new SoundsPlusConfigManager();
	
	}

	public static SoundsPlusConfig getConfig() {
		
		if(instance != null)
			restartInstance();
		
		return config;
	
	}

	public double getNamespaceValue(Identifier id) {
		
		double value = 1;
		
		try {
			
			if(checkNamespaceConfig(id.getNamespace()))
				return 1;
			
			JsonReader reader = createJsonReader(NAMESPACE_CONFIG_FILE.get(id.getNamespace()));
			JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
			
			if(!jsonObject.has(id.getPath()))
				return 1;
			
			value = jsonObject.get(id.getPath()).getAsDouble();
			
			reader.close();
		
		}
		catch(Exception e) { ModUtil.logError(e); }
		
		return value;
	
	}

	public void createNamespaceConfig(String namespace) {
		
		try {
			
			if(!NAMESPACE_FOLDER.exists()) {
				
				ModUtil.logWarn("Namespace config folder is missing, returning to default values");
				createNamespaceConfigFolder();
			
			}
			
			File namespaceConfig = NAMESPACE_FOLDER.toPath()
					.resolve(ModUtil.MOD_ID.concat(".")
							.concat(namespace)
							.concat(".json"))
					.toFile();
			
			if(NAMESPACE_CONFIG_FILE.get(namespace) != null && !NAMESPACE_CONFIG_FILE.get(namespace).exists())
				ModUtil.logWarnf("Namespace config file \"%s\" is missing, returning to default values", namespace);
			
			NAMESPACE_CONFIG_FILE.put(namespace, namespaceConfig);
			
			if(namespaceConfig.exists())
				return;
			
			JsonWriter writer = createJsonWriter(namespaceConfig);
			writer.setIndent("\t");
			
			GSON.toJson(new JsonObject(), JsonObject.class, writer);
			writer.close();
		
		}
		catch(Exception e) { ModUtil.logError(e); }
	
	}

	public void setNamespaceValue(Identifier id, double value) {
		
		try {
			
			if(checkNamespaceConfig(id.getNamespace()))
				return;
			
			File namespaceConfig = NAMESPACE_CONFIG_FILE.get(id.getNamespace());
			
			JsonReader reader = createJsonReader(namespaceConfig);
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			
			if(value < 1)
				jsonObject.addProperty(id.getPath(), value);
			else
				jsonObject.remove(id.getPath());
			
			JsonWriter writer = createJsonWriter(namespaceConfig);
			GSON.toJson(jsonObject, writer);
			
			writer.close();
			reader.close();
		
		}
		catch(Exception e) { ModUtil.logError(e); }
	
	}

	private JsonReader createJsonReader(File file) throws IOException { return new JsonReader(new FileReader(file)); }

	private JsonWriter createJsonWriter(File file) throws IOException {
		
		JsonWriter writer = new JsonWriter(new FileWriter(file));
		writer.setIndent("\t");
		
		return writer;
	
	}

	private boolean checkNamespaceConfig(String namespace) {
		
		File file = NAMESPACE_CONFIG_FILE.get(namespace);
		
		if(!file.exists()) {
			
			ModUtil.logWarnf("Namespace config file \"%s\" is missing, returning to default values", namespace);
			createNamespaceConfig(namespace);
			
			return true;
		
		}
		
		return false;
	
	}

	private void createNamespaceConfigFolder() throws IOException {
		
		if(!NAMESPACE_FOLDER.exists()) {
			
			ModUtil.logWarn("Namespace config folder is missing, returning to default values");
			
			if(!NAMESPACE_FOLDER.mkdir())
				throw new IOException("Namespace folder was not able to be created");
		
		}
	
	}

}
