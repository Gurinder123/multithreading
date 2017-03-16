package example;

/**
 * Created by gurinder on 16/3/17.
 */
public class ProducerConsumer {
    public static void main(String[] args) {
        Shareable s = new Shareable();
        Producer p = new Producer(s);
        Consumer c = new Consumer(s);
        p.start();
        c.start();
    }
}

class Shareable {

    private boolean available;
    private int count;

    synchronized public void produce(int count) throws InterruptedException {
        while (available) {
           wait();
        }
        this.count = count;
        available = true;
        notifyAll();
    }

    synchronized int consume() throws InterruptedException {
        while (!available) {
            wait();
        }
        available = false;
        notifyAll();
        return count--;
    }
}

class Producer extends Thread {

    public Producer(Shareable s) {
        super(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    System.out.println("Producing :" + i);
                    s.produce(i);
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class Consumer extends Thread {

    public Consumer(Shareable s) {
        super(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    int count = s.consume();
                    System.out.println("Consuming: "+(count));
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
