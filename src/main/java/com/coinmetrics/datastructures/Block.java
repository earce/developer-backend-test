package com.coinmetrics.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private final long blockTime;

    private final List<Transaction> transactions;

    private Block(long blockTime) {
        this.blockTime = blockTime;
        this.transactions = new ArrayList<>();
    }

    public long getBlockTime() {
        return blockTime;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    public static class Builder {

        private long blockTime;

        public Builder setBlockTime(final long blockTime) {
            this.blockTime = blockTime;
            return this;
        }

        public Block build() {
            return new Block(blockTime);
        }
    }
}
