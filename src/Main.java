import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger beautifulWordsLength3 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsLength4 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWordsLength5 = new AtomicInteger(0);

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread(() -> countBeautifulWords(texts, Main::isPalindrome));
        Thread sameLetterThread = new Thread(() -> countBeautifulWords(texts, Main::isSameLetter));
        Thread ascendingOrderThread = new Thread(() -> countBeautifulWords(texts, Main::isAscendingOrder));

        palindromeThread.start();
        sameLetterThread.start();
        ascendingOrderThread.start();

        try {
            palindromeThread.join();
            sameLetterThread.join();
            ascendingOrderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Красивых слов с длиной 3: " + beautifulWordsLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + beautifulWordsLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + beautifulWordsLength5.get() + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void countBeautifulWords(String[] texts, WordChecker checker) {
        for (String text : texts) {
            if (checker.check(text)) {
                switch (text.length()) {
                    case 3 -> beautifulWordsLength3.incrementAndGet();
                    case 4 -> beautifulWordsLength4.incrementAndGet();
                    case 5 -> beautifulWordsLength5.incrementAndGet();
                }
            }
        }
    }

    public static boolean isPalindrome(String text) {
        int length = text.length();
        for (int i = 0; i < length / 2; i++) {
            if (text.charAt(i) != text.charAt(length - i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSameLetter(String text) {
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAscendingOrder(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) > text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    interface WordChecker {
        boolean check(String text);
    }
}