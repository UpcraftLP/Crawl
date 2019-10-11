package ru.fewizz.crawl.mixin.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.fewizz.crawl.CrawlMod;
import ru.fewizz.crawl.CrawlModClient;

@Mixin(KeyboardInput.class)
abstract class MixinKeyboardInput extends Input {
	@Inject(method="tick", at=@At("HEAD"))
	void onTickBegin(CallbackInfo ci) {
		// Why it's here? ah, ok, nevermind..
		PlayerEntity player = MinecraftClient.getInstance().player;
		
		boolean newCrawlState = CrawlModClient.keyCrawl.isPressed();
		boolean oldCrawlState = player.getPose() == CrawlMod.CRAWLING;
		
		if(newCrawlState != oldCrawlState) {
		    PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
		    byteBuf.writeBoolean(newCrawlState);
            ClientSidePacketRegistry.INSTANCE.sendToServer(CrawlMod.CRAWL_PACKET_ID, byteBuf);
			player.getDataTracker().set(CrawlMod.CRAWLING_REQUEST, newCrawlState);
		}
		
	}
	
	@Inject(method="tick", at=@At("RETURN"))
	void onTickEnd(CallbackInfo ci) {
		if(MinecraftClient.getInstance().player.getPose() == CrawlMod.CRAWLING) {
            movementForward *= 0.3;
            movementSideways *= 0.3;
            sneaking = false;
        }
	}
}