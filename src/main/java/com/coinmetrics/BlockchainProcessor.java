package com.coinmetrics;

import com.coinmetrics.datastructures.Block;
import com.coinmetrics.datastructures.Blockchain;
import com.coinmetrics.datastructures.Coin;
import com.coinmetrics.datastructures.Transaction;

import java.util.*;

public class BlockchainProcessor {

    /**
     * To find maximum inbound address volume we only consider blocks within the
     * given interval. Interval start and end are represented as epoch seconds as
     * it's easier to understand and is not locale dependent. This could be represented
     * as date or some other data type.
     *
     * Once a block is within range we process all transactions and keep a map tracking
     * what the total amount outbound is and to which addresses it's going, as those outputs
     * are inbound to the provided addresses.
     *
     * Finally we take the top address by value. If there are two with the same value the
     * first address present when sorted is taken.
     *
     * @param blockchain to process
     * @param intervalStart epoch seconds to start looking at
     * @param intervalEnd epoch seconds to end looking at
     * @return maximum inbound volume address
     */
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

    /**
     * To find ancestors we use a BFS style traversal taking all inputs to the transaction
     * and traversing it up until we reach Coins where the parent is a coinbase.
     *
     * @param coin to start traversal at
     * @return list of all traversable coins where parent is Coinbase transaction
     */
    public static List<Coin> findCoinbaseAncestors(final Coin coin) {
        final List<Transaction> visited = new ArrayList<>();

        final List<Coin> ancestors = new ArrayList<>();
        final Queue<Coin> queue = new LinkedList<>();
        queue.add(coin);

        while (!queue.isEmpty()) {
            final Coin c = queue.poll();

            if (c.getCreatorTransaction().isCoinbase()) {
                ancestors.add(c);
            } else {
                if (visited.stream().noneMatch(t -> t == c.getCreatorTransaction())) {
                    visited.add(c.getCreatorTransaction());
                    queue.addAll(c.getCreatorTransaction().getInputs());
                }
            }
        }

        return ancestors;
    }
}
