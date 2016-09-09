/**
 * Created by Roma on 09.09.2016.
 * Simple example about how to use wait/notify from real life about sexual relationship
 */
public class NotReadyWife {
    public static void main(String[] args) {
        State state = new State(false);
        Object syncObj = new Object();

        Thread thread1 = new Thread(new Wife(syncObj, state));
        Thread thread2 = new Thread(new Husband(syncObj, state));

        thread1.start();
        thread2.start();
    }

    static class State{
        boolean wifeState;

        State(boolean wifeState) {
            this.wifeState = wifeState;
        }
    }

    static class Wife implements Runnable{
        private Object syncObj;
        private State state;

        Wife(Object syncObj, State state) {
            this.syncObj = syncObj;
            this.state = state;
        }

        @Override
        public void run() {
            synchronized (syncObj){
                System.out.println("Wife needs some time to prepare");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                state.wifeState = true;
                syncObj.notify();
                System.out.println("Wife - \"Ok I'm ready\"");
            }
        }
    }

    static class Husband implements Runnable{
        private Object syncObj;
        private State state;

        Husband(Object syncObj, State state) {
            this.syncObj = syncObj;
            this.state = state;
        }

        @Override
        public void run() {
            synchronized (syncObj){
                System.out.println("Try to hug wife...");
                if (!state.wifeState){
                    try {
                        System.out.println("Husband is helping his wife to be prepared...");
                        syncObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Hurray! Husband has hugged his wife successfully! :)) ");
            }
        }
    }
}

