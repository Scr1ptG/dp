import java.util.Arrays;

/**
 * КТ5 — эталонная реализация: динамическое программирование и жадные алгоритмы.
 *
 *   ways            — задача 1: лестница (ДП)                    2 балла
 *   minCoins        — задача 2: минимум монет (ДП)               3 балла
 *   maxActivities   — задача 3: заявки на аудиторию (жадный)     3 балла
 *   maxNonAdjacent  — задача 4 со звёздочкой (ДП)               +2 балла
 *   main            — вызовы всех методов и вывод                2 балла
 *
 * Запуск:  javac Main.java  &&  java Main
 */
public class Main {

    // ═══════════ ЗАДАЧА 1: ЛЕСТНИЦА ═══════════

    /** Сколько способов подняться на n ступенек шагами 1 или 2. */
    static int ways(int n) {
        if (n <= 1) return 1;            // 0 или 1 ступенька — один способ

        int[] dp = new int[n + 1];       // нужен индекс n, поэтому n + 1
        dp[0] = 1;                       // база: стоять на месте
        dp[1] = 1;                       // база: одна ступенька

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];   // последний шаг был на 1 или на 2
        }
        return dp[n];
    }

    // ═══════════ ЗАДАЧА 2: МИНИМУМ МОНЕТ ═══════════

    /** Минимальное число монет для суммы sum, или -1, если собрать нельзя. */
    static int minCoins(int[] coins, int sum) {
        final int INF = 1000000;

        int[] dp = new int[sum + 1];
        Arrays.fill(dp, INF);            // пока все суммы недостижимы
        dp[0] = 0;                       // база: ноль монет на сумму 0

        for (int s = 1; s <= sum; s++) {
            for (int c : coins) {
                if (c <= s && dp[s - c] != INF) {
                    dp[s] = Math.min(dp[s], dp[s - c] + 1);
                }
            }
        }
        return dp[sum] >= INF ? -1 : dp[sum];
    }

    // ═══════════ ЗАДАЧА 3: ЗАЯВКИ (ЖАДНЫЙ) ═══════════

    /**
     * Максимум непересекающихся заявок.
     * Массивы start и end уже отсортированы по времени окончания.
     */
    static int maxActivities(int[] start, int[] end) {
        int count = 0;
        int lastEnd = 0;                 // когда освободилась аудитория

        for (int i = 0; i < start.length; i++) {
            if (start[i] >= lastEnd) {   // заявка не мешает уже выбранным
                count++;
                lastEnd = end[i];
            }
        }
        return count;
    }

    // ═══════════ ЗАДАЧА 4 СО ЗВЁЗДОЧКОЙ ═══════════

    /** Максимальная сумма чисел массива без двух соседних. */
    static int maxNonAdjacent(int[] a) {
        if (a.length == 0) return 0;
        if (a.length == 1) return a[0];

        int[] dp = new int[a.length];
        dp[0] = a[0];
        dp[1] = Math.max(a[0], a[1]);

        for (int i = 2; i < a.length; i++) {
            dp[i] = Math.max(dp[i - 1],          // пропускаем a[i]
                             dp[i - 2] + a[i]);  // берём a[i]
        }
        return dp[a.length - 1];
    }

    // ═══════════ ДЕМОНСТРАЦИЯ И ТЕСТЫ ═══════════

    public static void main(String[] args) {
        int[] coins = {1, 3, 4};
        int[] start = {1, 3, 0, 5, 8, 5};
        int[] end   = {2, 4, 6, 7, 9, 9};

        System.out.println("Задача 1 — лестница");
        System.out.println("  ways(1)  = " + ways(1));
        System.out.println("  ways(4)  = " + ways(4));
        System.out.println("  ways(10) = " + ways(10));

        System.out.println("Задача 2 — монеты");
        System.out.println("  minCoins({1,3,4}, 6)  = " + minCoins(coins, 6));
        System.out.println("  minCoins({1,3,4}, 11) = " + minCoins(coins, 11));
        System.out.println("  minCoins({2}, 3)      = " + minCoins(new int[]{2}, 3));

        System.out.println("Задача 3 — заявки");
        System.out.println("  maxActivities = " + maxActivities(start, end));

        System.out.println("Задача 4 — со звёздочкой");
        System.out.println("  maxNonAdjacent({3,2,7,10}) = " + maxNonAdjacent(new int[]{3, 2, 7, 10}));

        System.out.println();
        System.out.println("Тесты:");
        check("ways(1)",            ways(1), 1);
        check("ways(2)",            ways(2), 2);
        check("ways(4)",            ways(4), 5);
        check("ways(10)",           ways(10), 89);
        check("minCoins {1,3,4} 6", minCoins(coins, 6), 2);
        check("minCoins {1,3,4} 11",minCoins(coins, 11), 3);
        check("minCoins {1,3,4} 0", minCoins(coins, 0), 0);
        check("minCoins {2} 3",     minCoins(new int[]{2}, 3), -1);
        check("minCoins {1,5,10} 30", minCoins(new int[]{1, 5, 10}, 30), 3);
        check("maxActivities",      maxActivities(start, end), 4);
        check("maxActivities пустой", maxActivities(new int[]{}, new int[]{}), 0);
        check("maxNonAdjacent {3,2,7,10}",   maxNonAdjacent(new int[]{3, 2, 7, 10}), 13);
        check("maxNonAdjacent {3,2,5,10,7}", maxNonAdjacent(new int[]{3, 2, 5, 10, 7}), 15);
        check("maxNonAdjacent {5}",          maxNonAdjacent(new int[]{5}), 5);
        check("maxNonAdjacent пустой",       maxNonAdjacent(new int[]{}), 0);
    }

    /** Печатает OK или FAIL для одного теста. */
    static void check(String name, int got, int expected) {
        String status = got == expected ? "OK  " : "FAIL";
        System.out.println("  " + status + "  " + name + ": получили " + got + ", ожидали " + expected);
    }
}
