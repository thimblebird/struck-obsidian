package io.thimblebird.struckobsidian;

import io.thimblebird.struckobsidian.config.StruckObsidianConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StruckObsidian implements ModInitializer {
	public static Logger LOGGER = LoggerFactory.getLogger("struckobsidian");
	public static final StruckObsidianConfig CONFIG = StruckObsidianConfig.createAndLoad();

	@Override
	public void onInitialize() {}
}
