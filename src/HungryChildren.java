/**
 * Created by Roma on 09.09.2016.
 * Simple example about how to use wait/notify/notifyAll from real life about hungry children
 */
public class HungryChildren {
    public static void main(String[] args) {
        Family family = new Family();

        new Mother(family).start();

        new Child(family).start();
        new Child(family).start();
        new Child(family).start();
        new Child(family).start();
        new Child(family).start();

    }
}

class Family {
    private int portionAmount;
    private final int FRIDGE_CAPACITY = 3;

    public synchronized void cookFood() {

        while (portionAmount >= FRIDGE_CAPACITY) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        portionAmount++;
        System.out.println("Mother has cooked one more meal");
        System.out.printf("There is %d portion in the fridge\n\n", portionAmount);

        //give opportunity for children to eat
        notifyAll();
    }

    public synchronized void eatFood() {
        while (portionAmount < 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        portionAmount--;
        System.out.println("Some child has eaten 1 portion");
        System.out.printf("There is %d portions left\n", portionAmount);

        //say mother that maybe it's time to cook again
        notify();
    }
}

class Mother extends Thread {
    private Family family;

    public Mother(Family family) {
        this.family = family;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            family.cookFood();
        }
    }
}

class Child extends Thread {
    private static int counter;
    private int id;
    private Family family;
    private int satietyPercent;

    public Child(Family family) {
        this.family = family;
        id = ++counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            synchronized (family){
                family.eatFood();
                satietyPercent += 50;
                System.out.printf("Child(%d)'s satiety is %d%%\n\n", id, satietyPercent);
            }
        }
    }
}

