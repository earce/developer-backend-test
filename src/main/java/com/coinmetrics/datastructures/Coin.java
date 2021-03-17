package com.coinmetrics.datastructures;

public class Coin {

    private Transaction creatorTransaction;

    private Transaction spenderTransaction;

    private final String address;

    private final long amount;

    private Coin(final String address, long amount) {
        this.address = address;
        this.amount = amount;
    }

    public Transaction getCreatorTransaction() {
        return creatorTransaction;
    }

    public Transaction getSpenderTransaction() {
        return spenderTransaction;
    }

    public String getAddress() {
        return address;
    }

    public long getAmount() {
        return amount;
    }

    public void setCreatorTransaction(final Transaction creatorTransaction) {
        this.creatorTransaction = creatorTransaction;
    }

    public void setSpenderTransaction(final Transaction spenderTransaction) {
        this.spenderTransaction = spenderTransaction;
    }

    public static class Builder {

        private String address;

        private long amount;

        public Builder setAddress(final String address) {
            this.address = address;
            return this;
        }

        public Builder setAmount(final long amount) {
            this.amount = amount;
            return this;
        }

        public Coin build() {
            return new Coin(address, amount);
        }
    }
}
