package com.coinmetrics.datastructures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Blockchain {

    private final List<Block> blocks;

    private Blockchain(final List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Method is given a path where three files are expected to be:
     *      blocks.json
     *      coins.json
     *      transactions.json
     *
     * Based on PDF several fields are circular references to other fields, e.g.
     * Transaction -> Block and Block -> Transaction. In order to properly build
     * these objects we first build the objects without their circular references
     * and then stitch them together based on the unique id provided in the json
     * files.
     *
     * There was some liberty here in deciding how to represent the data as flat
     * files and how to stitch them together.
     *
     */
    public static Blockchain load(final String path) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final JsonNode blocksJson = mapper.readTree(new File((path + "/blocks.json")));
        final JsonNode coinsJson = mapper.readTree(new File((path + "/coins.json")));
        final JsonNode transactionsJson = mapper.readTree(new File(path + "/transactions.json"));

        final Map<String, Block> blocks = new TreeMap<>(Comparator.comparingInt(Integer::parseInt));

        // build all objects without including fields which have circular references

        final Map<String, Set<String>> blockToTxId = new HashMap<>();
        blocksJson.fieldNames().forEachRemaining(blockId -> {
            final JsonNode blockJson = blocksJson.get(blockId);

            blockJson.get("transactions").forEach(txn ->
                    blockToTxId
                            .computeIfAbsent(blockId, s -> new HashSet<>())
                            .add(txn.textValue()));

            blocks.put(blockId, new Block.Builder()
                    .setBlockTime(blockJson.get("blockTime").asLong())
                    .build());
        });

        final Map<String, Coin> coins = new HashMap<>();
        coinsJson.fieldNames().forEachRemaining(coinId ->
                coins.put(coinId, new Coin.Builder()
                        .setAddress(coinsJson.get(coinId).get("address").asText())
                        .setAmount(coinsJson.get(coinId).get("amount").asLong())
                        .build()));

        final Map<String, Transaction> transactions = new HashMap<>();
        final Map<String, Set<String>> txnInputs = new HashMap<>();
        final Map<String, Set<String>> txnOutputs = new HashMap<>();

        transactionsJson.fieldNames().forEachRemaining(transactionId -> {
            final JsonNode transactionBody = transactionsJson.get(transactionId);

            transactionBody.get("inputs").forEach(coin ->
                    txnInputs
                            .computeIfAbsent(transactionId, s -> new HashSet<>())
                            .add(coin.textValue()));

            transactionBody.get("outputs").forEach(coin ->
                    txnOutputs
                            .computeIfAbsent(transactionId, s -> new HashSet<>())
                            .add(coin.textValue()));

            transactions.put(transactionId, new Transaction.Builder()
                    .setIsCoinbase(transactionBody.get("isCoinbase").asBoolean())
                    .build());
        });

        // set all fields which have circular references

        blocks.forEach((blockId, block) ->
                blockToTxId.get(blockId).forEach(txnId ->
                        block.addTransaction(transactions.get(txnId))));

        coins.forEach((coinId, coin) -> {
            final JsonNode coinBody = coinsJson.get(coinId);
            coin.setCreatorTransaction(transactions.get(coinBody.get("creatorTransaction").asText()));
            coin.setSpenderTransaction(transactions.get(coinBody.get("spenderTransaction").asText()));
        });

        transactions.forEach((transactionId, tx) -> {
            tx.setBlock(blocks.get(transactionsJson.get(transactionId).get("block").asText()));

            if (!tx.isCoinbase()) {
                txnInputs.get(transactionId).forEach(inputId -> tx.addInput(coins.get(inputId)));
            }
            txnOutputs.get(transactionId).forEach(outputId -> tx.addOutput(coins.get(outputId)));
        });

        return new Blockchain(new ArrayList<>(blocks.values()));
    }
}
