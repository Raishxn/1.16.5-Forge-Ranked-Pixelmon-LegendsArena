package com.raishxn.legendsarena.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class BattleEvents {

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        try {
            // Tipo corrigido para ServerPlayerEntity
            List<ServerPlayerEntity> players = event.getPlayers();
            if (players == null || players.size() == 0) return;

            // Tipo corrigido para ServerPlayerEntity
            for (ServerPlayerEntity sp : players) {
                try {
                    Optional<?> resOpt = event.getResult(sp);
                    if (resOpt == null || !resOpt.isPresent()) continue;
                    Object resultsObj = resOpt.get();

                    boolean playerWon = false;

                    // tentativa 1: método isWin()
                    try {
                        Method isWin = resultsObj.getClass().getMethod("isWin");
                        Object val = isWin.invoke(resultsObj);
                        if (val instanceof Boolean) playerWon = (Boolean) val;
                    } catch (NoSuchMethodException ignored) { }

                    // tentativa 2: getResult().isWin()
                    if (!playerWon) {
                        try {
                            Method getResult = resultsObj.getClass().getMethod("getResult");
                            Object inner = getResult.invoke(resultsObj);
                            if (inner != null) {
                                try {
                                    Method innerIsWin = inner.getClass().getMethod("isWin");
                                    Object v = innerIsWin.invoke(inner);
                                    if (v instanceof Boolean) playerWon = (Boolean) v;
                                } catch (NoSuchMethodException ignored2) { }
                            }
                        } catch (NoSuchMethodException ignored3) { }
                    }

                    // tentativa 3: campo 'result'
                    if (!playerWon) {
                        try {
                            Field f = resultsObj.getClass().getDeclaredField("result");
                            f.setAccessible(true);
                            Object inner = f.get(resultsObj);
                            if (inner != null) {
                                try {
                                    Method innerIsWin = inner.getClass().getMethod("isWin");
                                    Object v = innerIsWin.invoke(inner);
                                    if (v instanceof Boolean) playerWon = (Boolean) v;
                                } catch (NoSuchMethodException ignored4) { }
                            }
                        } catch (NoSuchFieldException ignored5) { }
                    }

                    // tentativa 4: getWinner()
                    if (!playerWon) {
                        try {
                            Method getWinner = resultsObj.getClass().getMethod("getWinner");
                            Object winner = getWinner.invoke(resultsObj);
                            if (winner instanceof BattleParticipant) {
                                BattleParticipant bp = (BattleParticipant) winner;
                                try {
                                    Method getUniqueId = bp.getClass().getMethod("getUniqueId");
                                    Object uid = getUniqueId.invoke(bp);
                                    if (uid != null && uid.equals(sp.getUUID())) playerWon = true;
                                } catch (Exception ignore) { }
                            }
                        } catch (NoSuchMethodException ignored6) { }
                    }

                    // Aqui processe vitória/derrota: substitua estas chamadas pelas suas funções de atualização de stats
                    if (playerWon) {
                        // updateStatsWin(sp);
                        System.out.println("[Ranked] Player won: " + sp.getName().getString());
                    } else {
                        // updateStatsLoss(sp);
                        System.out.println("[Ranked] Player lost: " + sp.getName().getString());
                    }

                } catch (Exception exInner) {
                    exInner.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
// --- FIM DA CORREÇÃO ---