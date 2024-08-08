import java.util.List;

public class Main {

    public static void main(String[] args) {
        int rows_on_block = 5;
        var songs = CsvUtils.getSongs();
        long start = System.currentTimeMillis();
        SpotifySong s = songs.stream().filter(spotifySong -> spotifySong.id.equals(789455)).toList().get(0);
        System.out.println(s);
        System.out.println("Время поиска без индекса: " + (System.currentTimeMillis() - start));

        BPlusTree<Integer, SpotifySong> bPlusTree = new BPlusTree<>(rows_on_block);
        BTreeIndex<Integer, SpotifySong> index = new BTreeIndex<>(bPlusTree);
        index.createIndex(songs, SpotifySong.class, "id");
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

    }
}