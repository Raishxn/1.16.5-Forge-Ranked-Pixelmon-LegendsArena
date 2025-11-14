package com.raishxn.legendsarena.events;

import com.raishxn.legendsarena.queue.QueueManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {

    private static int tickCounter = 0;
    private static final int MATCHMAKING_TICK_INTERVAL = 20 * 5; // 20 ticks = 1 segundo. 5 segundos.

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;

            // Roda o matchmaking a cada 5 segundos (ou o intervalo que você preferir)
            if (tickCounter >= MATCHMAKING_TICK_INTERVAL) {
                tickCounter = 0; // Reseta o contador

                // Chama a lógica de matchmaking
                QueueManager.processQueues();
            }
        }
    }
}