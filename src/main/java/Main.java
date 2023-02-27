import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    protected static String string1 = "";
    protected static String string2 = "";
    protected static String string3 = "";

    protected static long i1 = 0;
    protected static long i2 = 0;
    protected static long i3 = 0;

    protected static long time = 10;
    protected static TimeUnit timeUnit = TimeUnit.SECONDS;

    static ArrayBlockingQueue arr1 = new ArrayBlockingQueue(100);
    static ArrayBlockingQueue arr2 = new ArrayBlockingQueue(100);
    static ArrayBlockingQueue arr3 = new ArrayBlockingQueue(100);

    public static void main(String[] args) {
        ThreadGroup mainGroup = new ThreadGroup("mainGroup");

        final Thread thread = new Thread(mainGroup, () -> {
            Random random = new Random();
            for (int i = 0; i < 10_000; i++) {
                String s = generateText("abc", 100_000 + random.nextInt(3));
                try {
                    arr1.offer(s, time, timeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    arr2.offer(s, time, timeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    arr3.offer(s, time, timeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Поставнока задач окончена");
        });
        thread.start();

        final Thread thread1 = new Thread(mainGroup, () -> {
            try {
            while (true) {
                String es = (String) arr1.poll(time, timeUnit);

                if (es == null) break;

                long i = es.chars()
                        .filter(c -> c == "a".charAt(0))
                        .count();
                if (i > i1) {
                    i1 = i;
                    string1 = es;
                }
            }
            } catch (InterruptedException e) {e.printStackTrace();}});
        thread1.start();

        final Thread thread2 = new Thread(mainGroup, () -> {
            try {
                while (true) {
                    String es = (String) arr2.poll(time, timeUnit);

                    if (es == null) break;

                    long i = es.chars()
                            .filter(c -> c == "b".charAt(0))
                            .count();
                    if (i > i2) {
                        i2 = i;
                        string2 = es;
                    }
                }
            } catch (InterruptedException e) {e.printStackTrace();}});
        thread2.start();

        final Thread thread3 = new Thread(mainGroup, () -> {
            try {
                while (true) {
                    String es = (String) arr3.poll(time, timeUnit);

                    if (es == null) break;

                    long i = es.chars()
                            .filter(c -> c == "c".charAt(0))
                            .count();
                    if (i > i3) {
                        i3 = i;
                        string3 = es;
                    }
                }
            } catch (InterruptedException e) {e.printStackTrace();}});
        thread3.start();

        while (mainGroup.activeCount() !=0) {

        }

        System.out.println("У самой длинной с а " + i1);
        System.out.println("У самой длинной с b " + i2);
        System.out.println("У самой длинной с c " + i3);
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
