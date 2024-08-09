import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int rows_on_block = 100;
        var songs = CsvUtils.getSongs();


        BPlusTree<Integer, SpotifySong> bPlusTree = new BPlusTree<>(rows_on_block);
        BTreeIndex<Integer, SpotifySong> index = new BTreeIndex<>(bPlusTree);
        index.createIndex(songs, SpotifySong.class, "id");

        long start = System.currentTimeMillis();
        SpotifySong s = songs.stream().filter(spotifySong -> spotifySong.id.equals(789455)).toList().get(0);
        System.out.println(s);
        System.out.println("Время поиска без индекса: " + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        s = index.indexScan(789455);
        System.out.println(s);
        System.out.println("Время поиска c индексом: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<SpotifySong> ss = songs.stream().filter(spotifySong -> spotifySong.id.compareTo(13589) >= 0 && spotifySong.id.compareTo(25589) <= 0).toList();
        System.out.println(ss.size());
        System.out.println("Время поиска диапазона без индекса: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        ss = index.rangeScan(13589, 25589);
        System.out.println(ss.size());
        System.out.println("Время поиска диапазона c индексом: " + (System.currentTimeMillis() - start));

        System.out.println("Полное сканирование индекса: " + index.fullScan().size());
        System.out.println("Уровень индекса: " + index.getBlevel());
        System.out.println("Количество элементов в индексе: " + index.getCnt());
        System.out.println("Максимальный элемент в индексе: " + index.getMax());
        System.out.println("Минимальный элемент в индексе: " + index.getMin());

        index.delete(1473395);
        System.out.println("Максимальный элемент в индексе: " + index.getMax());

        randomSearch(5000, songs);
        randomSearchIndex(5000, index);

    }

    private static void randomSearchIndex(int n, BTreeIndex<Integer, SpotifySong> index) {
        Random random = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            var s = index.indexScan(random.nextInt(1000000));
            //System.out.println(s);
        }
        System.out.println("Время поиска случайных " + n + " элементов по индексу " + (System.currentTimeMillis() - start));
    }

    private static void randomSearch(int n, List<SpotifySong> table) {
        Random random = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            var s = table.stream().filter(spotifySong -> spotifySong.id.equals(random.nextInt(1000000))).toList();
            //System.out.println(s);
        }
        System.out.println("Время поиска случайных " + n + " элементов без индекса " + (System.currentTimeMillis() - start));
    }
}