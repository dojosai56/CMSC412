package SairamS;

public class HW3 {


    public static void main(String[] args) {

        Concurrency thread1 = new Concurrency(5);
        Concurrency thread2 = new Concurrency(5);
        Concurrency thread3 = new Concurrency(5);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class Concurrency extends Thread {

    private int loopNum;

    Concurrency(int loopNum) {
        this.loopNum = loopNum;
    }

    @Override
    public void run() {

        for (byte counter = 1; counter <= 5; counter++) {

            // sleep .5 seconds for demonstration purposes
            try {
                Thread.sleep(500);
            } catch(InterruptedException ex) {
                ex.printStackTrace(); }

            System.out.println(Concurrency.currentThread().getName() + ", Iteration number: " + counter);
        }
    }
}