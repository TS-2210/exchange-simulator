**Electronic Trading Exchange**

A Java-based electronic trading exchange that simulates the core infrastructure of a financial market, including price-time priority order books, order matching, trade execution and multi-asset markets.
The project is being developed with an emphasis on object-oriented design, data structures, algorithms, concurrency and performance engineering.

**Order Types**

Currently supported:
Limit orders execute only at an acceptable price or better and may remain in the order book.
Market orders execute immediately against available liquidity.

**Matching Algorithm**

The matching engine uses price-time priority.
For buy orders:
Highest bid -> highest priority

For sell orders:
Lowest ask -> highest priority

Orders at the same price are processed FIFO.
