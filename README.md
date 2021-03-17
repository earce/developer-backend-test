# Coin Metrics Developer Backend Test

The two functions defined in the test are implemented in [BlockchainProcessor.java](https://github.com/earce/developer-backend-test/blob/main/src/main/java/com/coinmetrics/BlockchainProcessor.java)

## Testing

Code Coverage (Instructions) <kbd>~98%</kbd> 

Running code coverage generator

```bash script
$ git clone https://github.com/earce/developer-backend-test.git
$ cd developer-backend-test
$ chmod +x gradlew
$ ./gradlew build
$ ./gradlew coverageReport
$ cd build/reports/jacoco/test/html
```

Then you should be able to click on index.html to view it as a webpage.

## Assumptions:

- Coinbase transactions can only happen at the start of a block.

- Some liberty was taken in translating the objects present in the PDF from C++ to Java as there are similar structures but they are not perfectly analogous.

```java
public static String findMaximumInboundVolumeAddress(final Blockchain blockchain,
                                                     long intervalStart,
                                                     long intervalEnd) {
```

- This method returns a single address, however it's conceivable that two addresses would end up with the same inbound volume of coins in a window. For this reason we simply pick one address, this signature would make more sense returning a list.