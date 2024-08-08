import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BTreeIndex<Key extends Comparable<Key>, Value> {
    private final BPlusTree<Key, Value> bPlusTree;
    public BTreeIndex(BPlusTree<Key, Value> bPlusTree) {
        this.bPlusTree = bPlusTree;
    }

    @SuppressWarnings("unchecked")
    public void createIndex(List<Value> table, Class<Value> classTable, String columnIndex) {
        Field field = null;
        try {
            field = classTable.getDeclaredField(columnIndex);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        for (var t : table) {
            try {
                bPlusTree.insert((Key) field.get(t), t);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void createReverseIndex(List<Value> table, Class<Value> classTable, String columnIndex) {
        Field field = null;
        try {
            field = classTable.getDeclaredField(columnIndex);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        for (var t : table) {
            Integer reverseKey = null;
            try {
                reverseKey = Integer.reverse((Integer) field.get(t));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            bPlusTree.insert((Key) reverseKey, t);
        }
    }

    public void insert(Key key, Value value) {
        bPlusTree.insert(key, value);
    }

    public void delete(Key key) {
        bPlusTree.delete(key);
    }

    public Value indexScan(Key key) {
        return bPlusTree.searchFirstValue(key);
    }

    public List<Value> fullScan() {
        return Arrays.stream(bPlusTree.fullScan()).toList();
    }

    public List<Value> rangeScan(Key fromKey, Key toKey) {
        return bPlusTree.rangeScan(fromKey, toKey);
    }

    public Value getMin() {
        return bPlusTree.getMin();
    }

    public Value getMax() {
        return bPlusTree.getMax();
    }

    public int getBlevel() {
        return bPlusTree.getBlevel();
    }

    public int getCnt() {
        return bPlusTree.getCnt();
    }

    public int getClusterFactor() {
        return bPlusTree.getClusterFactor();
    }
}
