package com.coinmetrics;

import com.coinmetrics.datastructures.Blockchain;
import com.coinmetrics.datastructures.Coin;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class BlockchainProcessorTest {

    @Test
    public void coinMetricsExampleInboundVolumeAddress() throws IOException {
        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample1");

        Assert.assertNull(BlockchainProcessor.findMaximumInboundVolumeAddress(b, 0, 1));
        Assert.assertNull(BlockchainProcessor.findMaximumInboundVolumeAddress(b, 0, 1609459199));
        Assert.assertNull(BlockchainProcessor.findMaximumInboundVolumeAddress(b, 1609459201, 199999999));

        Assert.assertEquals(BlockchainProcessor.findMaximumInboundVolumeAddress(
                b, 1609459200, 1609459200), "Erick4");

        Assert.assertEquals(BlockchainProcessor.findMaximumInboundVolumeAddress(
                b, 1609459200, 1609459201), "Erick4");

        Assert.assertEquals(BlockchainProcessor.findMaximumInboundVolumeAddress(
                b, 1609459199, 1609459200), "Erick4");
    }

    @Test
    public void coinMetricsExampleCoinbaseAncestors() throws IOException {
        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample1");

        final Coin c3 = b.getBlocks().get(0).getTransactions().get(1).getOutputs().get(0);
        final Coin c4 = b.getBlocks().get(0).getTransactions().get(2).getOutputs().get(0);

        final List<Coin> c3Ancestors = BlockchainProcessor.findCoinbaseAncestors(c3);
        final List<Coin> c4Ancestors = BlockchainProcessor.findCoinbaseAncestors(c4);

        Assert.assertEquals(c3Ancestors.size(), 1);
        Assert.assertEquals(c3Ancestors.get(0).getAddress(), "Erick1");

        Assert.assertEquals(c4Ancestors.size(), 2);
        Assert.assertEquals(c4Ancestors.get(0).getAddress(), "Erick0");
        Assert.assertEquals(c4Ancestors.get(1).getAddress(), "Erick1");
    }

    @Test
    public void sample2MaxInboundVolumeAddress() throws IOException {

        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample2");

        final String address1 = BlockchainProcessor.findMaximumInboundVolumeAddress(b,
                getEpochSecond("2021-01-01 00:00:00"), getEpochSecond("2021-01-01 00:00:15"));

        Assert.assertEquals(address1, "Erick4");

        final String address2 = BlockchainProcessor.findMaximumInboundVolumeAddress(b,
                getEpochSecond("2021-01-01 00:00:13"), getEpochSecond("2021-01-01 00:00:15"));
        Assert.assertEquals(address2, "Erick5");

        final Coin c5 = b.getBlocks().get(3).getTransactions().get(0).getOutputs().get(0);
        final List<Coin> c5Ancestors = BlockchainProcessor.findCoinbaseAncestors(c5);
        Assert.assertEquals(c5Ancestors.size(), 1);
    }

    @Test
    public void sample3MaxInboundVolumeAddress() throws IOException {

        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample3");

        final String address1 = BlockchainProcessor.findMaximumInboundVolumeAddress(b,
                getEpochSecond("2021-01-01 00:00:00"), getEpochSecond("2021-01-01 00:00:15"));

        Assert.assertEquals(address1, "Erick1");

        final String address2 = BlockchainProcessor.findMaximumInboundVolumeAddress(b,
                getEpochSecond("2021-01-01 00:00:00"), getEpochSecond("2021-01-01 00:00:03"));
        Assert.assertEquals(address2, "Erick0");
    }

    @Test
    public void sample4CoinbaseAncestors() throws IOException {

        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample4");

        final Coin c5 = b.getBlocks().get(1).getTransactions().get(1).getOutputs().get(0);

        final List<Coin> c5Ancestors = BlockchainProcessor.findCoinbaseAncestors(c5);

        Assert.assertEquals(c5Ancestors.size(), 1);
        Assert.assertEquals(c5Ancestors.get(0).getAddress(), "Erick4");
    }

    @Test
    public void sample5CoinbaseAncestors() throws IOException {

        final Blockchain b = Blockchain.load(
                new File("").getAbsolutePath() + "/src/test/resources/sample5");

        final Coin c1 = b.getBlocks().get(0).getTransactions().get(1).getOutputs().get(0);
        final Coin c4 = b.getBlocks().get(1).getTransactions().get(1).getOutputs().get(0);
        final Coin c5 = b.getBlocks().get(1).getTransactions().get(1).getOutputs().get(1);

        final List<Coin> c1Ancestors = BlockchainProcessor.findCoinbaseAncestors(c1);
        final List<Coin> c4Ancestors = BlockchainProcessor.findCoinbaseAncestors(c4);
        final List<Coin> c5Ancestors = BlockchainProcessor.findCoinbaseAncestors(c5);

        Assert.assertEquals(c1Ancestors.size(), 1);
        Assert.assertEquals(c1Ancestors.get(0).getAddress(), "Erick0");

        Assert.assertEquals(c4Ancestors.size(), 2);
        Assert.assertEquals(c4Ancestors.get(0).getAddress(), "Erick3");
        Assert.assertEquals(c4Ancestors.get(1).getAddress(), "Erick0");

        Assert.assertEquals(c5Ancestors.size(), 2);
        Assert.assertEquals(c5Ancestors.get(0).getAddress(), "Erick3");
        Assert.assertEquals(c5Ancestors.get(1).getAddress(), "Erick0");
    }

    private long getEpochSecond(final String dt) {
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return df.parse(dt).toInstant().getEpochSecond();
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
