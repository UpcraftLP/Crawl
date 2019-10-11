package ru.fewizz.crawl;

import com.chocohead.mm.api.ClassTinkerers;

import net.fabricmc.loader.api.FabricLoader;

public class EntityPoseHack implements Runnable {
	static final String CRAWLING = "CRAWLING";

	@Override
	public void run() {
		String target = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_4050"); //Remap to whatever the Yarn name is if loading in dev
		ClassTinkerers.enumBuilder(target, new Class[0]).addEnum(CRAWLING).build();
	}
}