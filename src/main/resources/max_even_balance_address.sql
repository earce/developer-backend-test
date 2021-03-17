SELECT
	coin_address
FROM (
	SELECT
		SUM(coin_amount) AS balance,
		coin_address
	FROM
		coins
	WHERE
		coin_spender_tx_hash IS NULL
	GROUP BY coin_address
) AS subq
WHERE
	subq.balance % 2 = 0
ORDER BY
	balance DESC
LIMIT 1