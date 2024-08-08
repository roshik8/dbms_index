import java.util.ArrayList;
import java.util.List;

public class BPlusTree<Key extends Comparable<Key>, Value> {
    private BTreeNode<Key> root;
    private int cnt = 0;

    public BPlusTree(int order) {
        this.root = new BTreeLeafNode<Key, Value>(order);
    }

    public void insert(Key key, Value value) {
        BTreeLeafNode<Key, Value> leaf = this.findLeaf(key);
        leaf.insertKey(key, value);

        if (leaf.isFull()) {
            BTreeNode<Key> newRoot = leaf.getNewNodeInsert();
            if (newRoot != null) {
                this.root = newRoot;
            }
        }
        cnt++;
    }

    public Value searchFirstValue(Key key) {
        BTreeLeafNode<Key, Value> leaf = this.findLeaf(key);

        int index = leaf.search(key);
        return (index == -1) ? null : leaf.getValue(index);
    }

    public List<Value> search(Key key) {
        return rangeScan(key, key);
    }

    public void delete(Key key) {
        BTreeLeafNode<Key, Value> leaf = this.findLeaf(key);
        boolean isDelete = leaf.delete(key);
        if (isDelete) cnt--;
        if (isDelete && leaf.isUnderflow()) {
            BTreeNode<Key> newRoot = leaf.getNewNodeDelete();
            if (newRoot != null)
                this.root = newRoot;
        }
    }

    public Value getMin() {
        BTreeNode<Key> node = root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            node = ((BTreeInnerNode<Key>) node).getChild(0);
        }
        if (node.keyCount == 0) return null;
        BTreeLeafNode<Key, Value> leaf = (BTreeLeafNode<Key, Value>) node;
        return leaf.values[0];
    }

    public Value getMax() {
        BTreeNode<Key> node = root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            node = ((BTreeInnerNode<Key>) node).children[node.keyCount];
        }
        if (node.keyCount == 0) return null;
        BTreeLeafNode<Key, Value> leaf = (BTreeLeafNode<Key, Value>) node;
        return leaf.values[leaf.keyCount - 1];
    }

    public List<Value> rangeScan(Key fromKey, Key toKey) {
        BTreeLeafNode<Key, Value> leaf = this.findLeaf(fromKey);
        ArrayList<Value> values = new ArrayList<>();
        while (leaf != null) {
            for (int i = 0; i < leaf.getKeyCount(); i++) {
                if (leaf.getKey(i) == null) { break; }
                if (fromKey.compareTo(leaf.getKey(i)) <= 0 && toKey.compareTo(leaf.getKey(i)) >= 0) {
                    values.add(leaf.getValue(i));
                }
            }
            leaf = (BTreeLeafNode<Key, Value>) leaf.rightSibling;

        }
        return values;
    }

    public Value[] fullScan() {
        BTreeNode<Key> node = root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            node = ((BTreeInnerNode<Key>) node).getChild(0);
        }
        if (node.keyCount == 0) return null;

        Value[] arr = (Value[]) new Object[cnt];
        BTreeLeafNode<Key, Value> leaf = (BTreeLeafNode<Key, Value>) node;
        int cntNewArr = 0;
        while (leaf != null) {
            System.arraycopy(leaf.values, 0, arr, cntNewArr, leaf.keyCount);
            cntNewArr = cntNewArr + leaf.keyCount;
            leaf = (BTreeLeafNode<Key, Value>) leaf.rightSibling;
        }

        return arr;
    }

    public int getBlevel() {
        int height = 0;
        BTreeNode<Key> node = root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            node = ((BTreeInnerNode<Key>) node).getChild(0);
            height++;
        }
        return height;
    }

    public int getCnt() {
        return cnt;
    }

    public int getClusterFactor() {
        int clusterFactor = 0;
        int prevBlock = 0;
        int curBlock = 0;
        Object[] arr;
        arr = fullScan();

        for (Object o : arr) {

            int kRowid = (Integer) o;

            curBlock = kRowid / root.order;

            if (prevBlock != curBlock) {
                clusterFactor++;
            }
            prevBlock = curBlock;
        }

        return clusterFactor;
    }
    private BTreeLeafNode<Key, Value> findLeaf(Key key) {
        BTreeNode<Key> node = this.root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            int idx = node.search(key);
            node = ((BTreeInnerNode<Key>) node).getChild(idx);
        }

        return (BTreeLeafNode<Key, Value>) node;
    }
}
