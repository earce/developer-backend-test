package com.coinmetrics.datastructures;

import java.util.List;

public class Transaction {

    private Block block;

    private boolean isCoinbase;

    private List<Coin> inputs;

    private List<Coin> outputs;
}
