SELECT
    sum(coin_amount) amt, tx_block_hash
FROM
    coins, transactions
WHERE
    coin_spender_tx_hash IS NOT null
    AND coin_spender_tx_hash = tx_hash
GROUP BY tx_block_hash
ORDER BY amt DESC
LIMIT 10