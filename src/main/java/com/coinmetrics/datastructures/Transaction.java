package com.coinmetrics.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private Block block;

    private final boolean isCoinbase;

    private List<Coin> inputs;

    private List<Coin> outputs;

    public Transaction(boolean isCoinbase) {
        this.isCoinbase = isCoinbase;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean isCoinbase() {
        return this.isCoinbase;
    }

    public List<Coin> getInputs() {
        return this.inputs;
    }

    public List<Coin> getOutputs() {
        return this.outputs;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }

    public void addInput(final Coin input) {
        this.inputs.add(input);
    }

    public void addOutput(final Coin output) {
        this.outputs.add(output);
    }

    public static class Builder {

        private boolean isCoinbase;

        public Builder setIsCoinbase(boolean isCoinbase) {
            this.isCoinbase = isCoinbase;
            return this;
        }

        public Transaction build() {
            return new Transaction(isCoinbase);
        }
    }
}
