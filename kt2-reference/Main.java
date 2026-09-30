import java.util.Arrays;
import java.util.Random;

/**
 * КТ2 — эталонная реализация сортировки слиянием и быстрой сортировки.
 *
 * Код совпадает с тем, что разбирали на слайдах:
 *   mergeSort + merge            — сортировка слиянием
 *   quickSort + partition + swap — быстрая сортировка (pivot = последний элемент)
 *
 * Запуск:  javac Main.java  &&  java Main
 */
public class Main {

    // ═══════════════════ СОРТИРОВКА СЛИЯНИЕМ ═══════════════════

    /** Сортирует часть массива a[left..right] включительно. */
    static void mergeSort(int[] a, int left, int right) {
        if (left >= right) return;          // 0 или 1 элемент — уже отсортировано
        int mid = (left + right) / 2;       // середина
        mergeSort(a, left, mid);            // сортируем левую половину
        mergeSort(a, mid + 1, right);       // сортируем правую половину
        merge(a, left, mid, right);         // сливаем две отсортированные половины
    }

    /** Сливает две отсортированные части: a[left..mid] и a[mid+1..right]. */
    static void merge(int[] a, int left, int mid, int right) {
        int[] tmp = new int[right - left + 1];  // временный массив под результат
        int i = left;       // первый элемент левой половины
        int j = mid + 1;    // первый элемент правой половины
        int k = 0;          // куда кладём в tmp

        // пока в обеих половинах есть элементы — берём меньший из первых
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                tmp[k] = a[i];
                i++;
            } else {
                tmp[k] = a[j];
                j++;
            }
            k++;
        }

        // одна половина закончилась — переносим остаток другой
        while (i <= mid) {
            tmp[k] = a[i];
            i++;
            k++;
        }
        while (j <= right) {
            tmp[k] = a[j];
            j++;
            k++;
        }

        // копируем результат обратно в массив, начиная с позиции left
        for (int t = 0; t < tmp.length; t++) {
            a[left + t] = tmp[t];
        }
    }

    // ═══════════════════ БЫСТРАЯ СОРТИРОВКА ═══════════════════

    /** Сортирует часть массива a[low..high] включительно. */
    static void quickSort(int[] a, int low, int high) {
        if (low >= high) return;            // 0 или 1 элемент — уже отсортировано
        int p = partition(a, low, high);    // pivot встал на своё место p
        quickSort(a, low, p - 1);           // сортируем всё, что левее pivot
        quickSort(a, p + 1, high);          // сортируем всё, что правее pivot
    }

    /**
     * Разбиение: pivot = a[high].
     * После вызова слева от pivot только меньшие, справа — большие или равные.
     * Возвращает индекс, на котором оказался pivot.
     */
    static int partition(int[] a, int low, int high) {
        int pivot = a[high];        // опорный — последний элемент
        int i = low - 1;            // граница зоны «меньше pivot» (пока пустая)
        for (int j = low; j < high; j++) {
            if (a[j] < pivot) {
                i++;                // расширяем зону «меньше»
                swap(a, i, j);      // и кладём туда a[j]
            }
        }
        swap(a, i + 1, high);       // ставим pivot сразу за зоной «меньше»
        return i + 1;
    }

    /** Меняет местами a[i] и a[j]. */
    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    // ═══════════════════ ДЕМОНСТРАЦИЯ И ТЕСТЫ ═══════════════════

    public static void main(String[] args) {
        // 1. Демонстрация на массиве со слайдов
        int[] a = {5, 2, 8, 1, 9, 3, 7, 4};
        int[] b = a.clone();                // копия, чтобы quickSort получил НЕотсортированный массив

        System.out.println("До:    " + Arrays.toString(a));
        mergeSort(a, 0, a.length - 1);
        quickSort(b, 0, b.length - 1);
        System.out.println("Merge: " + Arrays.toString(a));
        System.out.println("Quick: " + Arrays.toString(b));
        System.out.println();

        // 2. Тесты из чек-листа
        System.out.println("Тесты:");
        check("Наш массив",        new int[]{5, 2, 8, 1, 9, 3, 7, 4});
        check("Пустой",            new int[]{});
        check("Один элемент",      new int[]{7});
        check("С повторами",       new int[]{3, 1, 3, 1});
        check("Уже отсортирован",  new int[]{1, 2, 3, 4});
        check("Обратный порядок",  new int[]{9, 7, 5, 3, 1});
        check("С отрицательными",  new int[]{0, -5, 8, -1});
        check("Все одинаковые",    new int[]{4, 4, 4, 4, 4});

        // 3. 1000 случайных массивов — сверяем с Arrays.sort
        Random rnd = new Random(42);
        int failed = 0;
        for (int t = 0; t < 1000; t++) {
            int[] data = new int[rnd.nextInt(50)];
            for (int i = 0; i < data.length; i++) data[i] = rnd.nextInt(201) - 100;
            if (!sortsCorrectly(data)) failed++;
        }
        System.out.println();
        System.out.println(failed == 0
                ? "Случайные тесты: 1000 из 1000 — OK"
                : "Случайные тесты: ОШИБОК " + failed + " из 1000");
    }

    /** Печатает результат одного теста. */
    static void check(String name, int[] data) {
        String status = sortsCorrectly(data) ? "OK  " : "FAIL";
        System.out.println("  " + status + "  " + name + ": " + Arrays.toString(data));
    }

    /** Сортирует копии массива обоими способами и сравнивает с Arrays.sort. */
    static boolean sortsCorrectly(int[] data) {
        int[] expected = data.clone();
        Arrays.sort(expected);              // эталон — только для проверки!

        int[] m = data.clone();
        mergeSort(m, 0, m.length - 1);

        int[] q = data.clone();
        quickSort(q, 0, q.length - 1);

        return Arrays.equals(m, expected) && Arrays.equals(q, expected);
    }
}
