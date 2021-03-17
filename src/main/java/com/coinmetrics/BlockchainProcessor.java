package com.coinmetrics;

import com.coinmetrics.datastructures.Block;
import com.coinmetrics.datastructures.Blockchain;
import com.coinmetrics.datastructures.Coin;
import com.coinmetrics.datastructures.Transaction;

import java.util.*;

public class BlockchainProcessor {

    public static String findMaximumInboundVolumeAddress(final Blockchain blockchain,
                                                  long intervalStart,
                                                  long intervalEnd) {

        final Map<String, Long> totalInputs = new HashMap<>();
        for (Block b : blockchain.getBlocks()) {
            if (b.getBlockTime() < intervalStart) {
                continue;
            }
            if (b.getBlockTime() > intervalEnd) {
                break;
            }
            for (Transaction t : b.getTransactions()) {
                for (Coin c : t.getOutputs()) {
                    final String address = c.getAddress();
                    final long totalAmount = totalInputs.getOrDefault(address, 0L);
                    totalInputs.put(address, totalAmount + c.getAmount());
                }
            }
        }
        if (totalInputs.isEmpty()) {
            return null;
        }
        return totalInputs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    public static List<Coin> findCoinbaseAncestors(final Coin coin) {
        final List<Coin> ancestors = new ArrayList<>();
        final Queue<Coin> queue = new LinkedList<>();
        queue.add(coin);

        while (!queue.isEmpty()) {
            final Coin c = queue.poll();
            ancestors.add(c);
            if (!c.getCreatorTransaction().isCoinbase()) {
                queue.addAll(c.getCreatorTransaction().getInputs());
            }
        }

        ancestors.remove(0); // remove starting coin
        return ancestors;
    }
}
