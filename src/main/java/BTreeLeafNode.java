
class BTreeLeafNode<Key extends Comparable<Key>, Value> extends BTreeNode<Key> {
    final Value[] values;

    @SuppressWarnings("unchecked")
    public BTreeLeafNode(int order) {
        this.order = order;
        this.keys = (Key[]) new Comparable[order + 1];
        this.values = (Value[]) new Object[order + 1];
    }

    public Value getValue(int index) {
        return this.values[index];
    }

    public void setValue(int index, Value value) {
        this.values[index] = value;
    }

    @Override
    public TreeNodeType getNodeType() {
        return TreeNodeType.LeafNode;
    }

    @Override
    public int search(Key key) {
        for (int i = 0; i < getKeyCount(); i++) {
            int cmp = getKey(i).compareTo(key);
            if (cmp == 0) {
                return i;
            }
            else if (cmp > 0) {
                return -1;
            }
        }

        return -1;
    }
    public void insertKey(Key key, Value value) {
        int index = 0;
        while (index < getKeyCount() && getKey(index).compareTo(key) < 0)
            index++;
        insertAt(index, key, value);
    }

    private void insertAt(int index, Key key, Value value) {
        for (int i = getKeyCount() - 1; i >= index; i--) {
            setKey(i + 1, getKey(i));
            setValue(i + 1, getValue(i));
        }
        setKey(index, key);
        setValue(index, value);
        keyCount++;
    }
    @Override
    protected BTreeNode<Key> split() {
        int midIndex = getKeyCount() / 2;

        BTreeLeafNode<Key, Value> newRNode = new BTreeLeafNode<>(order);
        for (int i = midIndex; i < getKeyCount(); i++) {
            newRNode.setKey(i - midIndex, getKey(i));
            newRNode.setValue(i - midIndex, getValue(i));
            setKey(i, null);
            setValue(i, null);
        }
        newRNode.keyCount = getKeyCount() - midIndex;
        keyCount = midIndex;

        return newRNode;
    }

    @Override
    protected BTreeNode<Key> upKey(Key key, BTreeNode<Key> leftChild, BTreeNode<Key> rightNode) {
        throw new UnsupportedOperationException();
    }

    public boolean delete(Key key) {
        int index = search(key);
        if (index == -1)
            return false;

        deleteAt(index);
        return true;
    }

    private void deleteAt(int index) {
        int i = index;
        for (i = index; i < getKeyCount() - 1; i++) {
            setKey(i, getKey(i + 1));
            setValue(i, getValue(i + 1));
        }
        setKey(i, null);
        setValue(i, null);
        keyCount--;
    }

    @Override
    protected void borrowFromSiblings(BTreeNode<Key> borrower, BTreeNode<Key> lender, int borrowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected BTreeNode<Key> mergeChildren(BTreeNode<Key> leftChild, BTreeNode<Key> rightChild) {
        throw new UnsupportedOperationException();
    }
    @Override
    protected void mergeSiblings(Key sinkKey, BTreeNode<Key> rightSibling) {
        BTreeLeafNode<Key, Value> siblingLeaf = (BTreeLeafNode<Key, Value>)rightSibling;

        int j = getKeyCount();
        for (int i = 0; i < siblingLeaf.getKeyCount(); i++) {
            setKey(j + i, siblingLeaf.getKey(i));
            setValue(j + i, siblingLeaf.getValue(i));
        }
        keyCount += siblingLeaf.getKeyCount();

        setRightSibling(siblingLeaf.rightSibling);
        if (siblingLeaf.rightSibling != null)
            siblingLeaf.rightSibling.setLeftSibling(this);
    }

    @Override
    protected Key transferFromSibling(Key sinkKey, BTreeNode<Key> sibling, int borrowIndex) {
        BTreeLeafNode<Key, Value> siblingNode = (BTreeLeafNode<Key, Value>)sibling;

        insertKey(siblingNode.getKey(borrowIndex), siblingNode.getValue(borrowIndex));
        siblingNode.deleteAt(borrowIndex);

        return borrowIndex == 0 ? sibling.getKey(0) : getKey(0);
    }

}