import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        System.out.println(finalI + " >> " + Thread.currentThread().getName());
                        s.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();
    }
}

