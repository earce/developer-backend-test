package com.coinmetrics.datastructures;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BlockchainTest {

    /**
     * Asserting assumptions about Coin Metrics provided example
     */
    @Test
    public void loader() throws IOException {
        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample1");

        final Block blk = b.getBlocks().get(0);

        Assert.assertEquals(b.getBlocks().size(), 1);


        Assert.assertEquals(blk.getBlockTime(), 1609459200);
        Assert.assertEquals(blk.getTransactions().size(), 3);

        Assert.assertEquals(blk.getTransactions().get(0).getInputs().size(), 0);
        Assert.assertEquals(blk.getTransactions().get(0).getOutputs().size(), 2);
        Assert.assertEquals(blk.getTransactions().get(0).getOutputs().get(0).getAddress(), "Erick0");
        Assert.assertEquals(blk.getTransactions().get(0).getOutputs().get(0).getAmount(), 100);
        Assert.assertEquals(blk.getTransactions().get(0).getOutputs().get(1).getAddress(), "Erick1");
        Assert.assertEquals(blk.getTransactions().get(0).getOutputs().get(1).getAmount(), 110);

        Assert.assertTrue(blk.getTransactions().get(0).getOutputs().get(0).getCreatorTransaction().isCoinbase());

        final Coin c1 = blk.getTransactions().get(0).getOutputs().get(1);
        final Coin c2 = blk.getTransactions().get(2).getInputs().get(1);

        Assert.assertSame(c1.getSpenderTransaction(), c2.getCreatorTransaction());

        Assert.assertTrue(blk.getTransactions().get(0).isCoinbase());
    }

    /**
     * Asserting a blockchain input vs output is sane
     */
    @Test
    public void validSums() throws IOException {
        assertAll(Blockchain.load(new File("").getAbsolutePath() + "/src/test/resources/sample1"));
        assertAll(Blockchain.load(new File("").getAbsolutePath() + "/src/test/resources/sample2"));
        assertAll(Blockchain.load(new File("").getAbsolutePath() + "/src/test/resources/sample3"));
        assertAll(Blockchain.load(new File("").getAbsolutePath() + "/src/test/resources/sample4"));
        assertAll(Blockchain.load(new File("").getAbsolutePath() + "/src/test/resources/sample5"));

    }

    private void assertAll(final Blockchain b) {
        b.getBlocks().forEach(blk ->
                blk.getTransactions().forEach(tx -> {
                    if (tx.isCoinbase()) {
                        return;
                    }
                    long totalInput = tx.getInputs().stream().mapToLong(Coin::getAmount).sum();
                    long totalOutput = tx.getOutputs().stream().mapToLong(Coin::getAmount).sum();

                    Assert.assertTrue(
                            "totalOutput: " + totalOutput + " cannot be greater then totalInput: " + totalInput,
                            totalOutput <= totalInput);
        }));
    }
}
