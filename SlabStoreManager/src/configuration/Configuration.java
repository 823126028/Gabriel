package configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	public static Properties p;
	public static enum ConfigElement{
		RELEASE_STRATEGY_NUM_LOW("slabManager.release.strategy.low"),
		RELEASE_STRATEGY_NUM_HIGH("slabManager.release.strategy.high"),
		RELEASE_STRATEGY_DOUBLE_SIZE_THRESHOLD("slabManager.release.strategy.double.size"),
		RELEASE_STRATEGY_REDUCE_SIZE_THRESHOLD("slabManager.release.strategy.reduce.size"),
		PRE_INIT_NUM("slabManager.pre.init.num");
		private String key;
		ConfigElement(String key){
			this.key = key;
		}
		public String getKey() {
			return key;
		}
	}
	
	public static String getConfiguration(ConfigElement element){
		return p.getProperty(element.getKey());
	}
	
	public static void loadConfiguration() throws IOException{
		File f = new File(Configuration.class.getClass().getResource("/").getPath() + "/configuration.properties");
		InputStream in = new FileInputStream(f);
		p = new Properties();
		p.load(in);
	}	
}
