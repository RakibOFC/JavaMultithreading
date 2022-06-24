import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDatabaseImplementation {

    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {

        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {

            while (true) {

                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();

        for (int readeIndex = 0; readeIndex < numberOfReaderThreads; readeIndex++) {

            Thread reader = new Thread(() -> {

                for (int i = 0; i < 100000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });

            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        long endReadingTime = System.currentTimeMillis();

        System.out.printf("Reading took %d ms%n", endReadingTime - startReadingTime);
    }

    public static class InventoryDatabase {

        private TreeMap<Integer, Integer> priceTopCountMap = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock();
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {

            //lock.lock();
            readLock.lock();

            try {

                Integer fromKey = priceTopCountMap.ceilingKey(lowerBound);
                Integer toKey = priceTopCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangePrice = priceTopCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;

                for (int numberOfItemForPrice : rangePrice.values()) {
                    sum += numberOfItemForPrice;
                }
                return sum;

            } finally {
                // lock.unlock();
                readLock.unlock();
            }
        }

        public void addItem(int price) {

            // lock.lock();
            writeLock.lock();

            try {
                Integer numberOfItemsForPrice = priceTopCountMap.get(price);

                if (numberOfItemsForPrice == null) {
                    priceTopCountMap.put(price, 1);
                } else {
                    priceTopCountMap.put(price, numberOfItemsForPrice + 1);
                }
            } finally {
                // lock.unlock();
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            // lock.lock();
            writeLock.lock();

            try {
                Integer numberOfItemsForPrice = priceTopCountMap.get(price);

                if (numberOfItemsForPrice == null) {
                    priceTopCountMap.remove(price);
                } else {
                    priceTopCountMap.put(price, numberOfItemsForPrice - 1);
                }
            } finally {
                // lock.unlock();
                writeLock.unlock();
            }
        }
    }
}
