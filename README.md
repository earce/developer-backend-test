# Coin Metrics Developer Backend Test

notes: 
 
function findMaximumInboundVolumeAddress, seems like it should probably return a list because there can be multiple addresses that have the same inbound volume

are we making incorrect assumptions about sending money from addresses that arent yours? for non-coinbase?

making the assumption that we can split transaction into two parts for same address, writing code defensively