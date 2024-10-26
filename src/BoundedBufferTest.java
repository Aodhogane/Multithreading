import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BoundedBufferTest {

    public static void main(String[] args) throws InterruptedException {
        //задаём размер буфера
        int bufferSize = 10;
        //задаём количество производителей и потребителей
        int сount = 3;
        //задаём количество элементов, которые каждый производитель будет добавлять
        int itemsPerProducer = 5;

        //создаем ограничение для буффера
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(bufferSize);

        //создаём потоки
        ExecutorService executor = Executors.newFixedThreadPool(сount + сount);

        //запуск производителей
        for (int i = 0; i < сount; i++) {
            int producerId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        int item = producerId * itemsPerProducer + j;
                        buffer.put(item);
                        System.out.println("Производитель " + producerId + " отдал: " + item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        //запуск потребителей
        for (int i = 0; i < сount; i++) {
            int consumerId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < (сount * itemsPerProducer) / сount; j++) {
                        Integer item = buffer.take();
                        System.out.println("Потребитель " + consumerId + " взял: " + item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        //заверщение поток
        executor.shutdown();
        //ждём, когда поток закончится
        executor.awaitTermination(4, TimeUnit.MINUTES);
        System.out.println("\n");
        System.out.println("Поотоки завершили свою работу");
    }
}
