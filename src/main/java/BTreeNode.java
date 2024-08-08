abstract class BTreeNode<Key extends Comparable<Key>> {
    protected Key[] keys;
    protected int keyCount;
    protected int order;
    protected BTreeNode<Key> parentNode;
    protected BTreeNode<Key> leftSibling;
    protected BTreeNode<Key> rightSibling;

    protected BTreeNode() {
        this.keyCount = 0;
        this.parentNode = null;
        this.leftSibling = null;
        this.rightSibling = null;
    }

    public int getKeyCount() {
        return this.keyCount;
    }

    public Key getKey(int index) {
        return this.keys[index];
    }

    public void setKey(int index, Key key) {
        this.keys[index] = key;
    }

    public BTreeNode<Key> getParent() {
        return this.parentNode;
    }

    public void setParent(BTreeNode<Key> parent) {
        this.parentNode = parent;
    }

    public abstract TreeNodeType getNodeType();

    public abstract int search(Key key);

    public BTreeNode<Key> getNewNodeInsert() {
        int midIndex = getKeyCount() / 2;
        Key midKey = getKey(midIndex);

        BTreeNode<Key> newRNode = split();

        if (getParent() == null) {
            setParent(new BTreeInnerNode<>(order));
        }
        newRNode.setParent(getParent());

        newRNode.setLeftSibling(this);
        newRNode.setRightSibling(rightSibling);
        if (getRightSibling() != null)
            getRightSibling().setLeftSibling(newRNode);
        setRightSibling(newRNode);
        return getParent().upKey(midKey, this, newRNode);
    }

    protected abstract BTreeNode<Key> split();

    protected abstract BTreeNode<Key> upKey(Key key, BTreeNode<Key> leftChild, BTreeNode<Key> rightNode);

    public BTreeNode<Key> getLeftSibling() {
        if (this.leftSibling != null && this.leftSibling.getParent() == this.getParent())
            return this.leftSibling;
        return null;
    }

    public void setLeftSibling(BTreeNode<Key> sibling) {
        this.leftSibling = sibling;
    }

    public BTreeNode<Key> getRightSibling() {
        if (this.rightSibling != null && this.rightSibling.getParent() == this.getParent())
            return this.rightSibling;
        return null;
    }

    public void setRightSibling(BTreeNode<Key> silbling) {
        this.rightSibling = silbling;
    }

    public boolean isFull() {
        return this.getKeyCount() == this.keys.length;
    }

    public boolean isUnderflow() {
        return this.getKeyCount() < (this.keys.length / 2);
    }

    public boolean isLendable() {
        return this.getKeyCount() > (this.keys.length / 2);
    }

    public BTreeNode<Key> getNewNodeDelete() {
        if (getParent() == null)
            return null;
        BTreeNode<Key> leftSibling = getLeftSibling();
        if (leftSibling != null && leftSibling.isLendable()) {
            getParent().borrowFromSiblings(this, leftSibling, leftSibling.getKeyCount() - 1);
            return null;
        }

        BTreeNode<Key> rightSibling = getRightSibling();
        if (rightSibling != null && rightSibling.isLendable()) {
            getParent().borrowFromSiblings(this, rightSibling, 0);
            return null;
        }

        if (leftSibling != null) {
            return getParent().mergeChildren(leftSibling, this);
        } else {
            return getParent().mergeChildren(this, rightSibling);
        }
    }

    protected abstract void borrowFromSiblings(BTreeNode<Key> borrower, BTreeNode<Key> lender, int borrowIndex);

    protected abstract BTreeNode<Key> mergeChildren(BTreeNode<Key> leftChild, BTreeNode<Key> rightChild);

    protected abstract void mergeSiblings(Key key, BTreeNode<Key> rightSibling);

    protected abstract Key transferFromSibling(Key key, BTreeNode<Key> sibling, int borrowIndex);
}