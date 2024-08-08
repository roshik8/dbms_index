class BTreeInnerNode<Key extends Comparable<Key>> extends BTreeNode<Key> {
    final BTreeNode<Key>[] children;

    @SuppressWarnings("unchecked")
    public BTreeInnerNode(int order) {
        this.order = order;
        keys = (Key[]) new Comparable[order + 1];
        children = new BTreeNode[order + 2];
    }

    public BTreeNode<Key> getChild(int index) {
        return this.children[index];
    }

    public void setChild(int index, BTreeNode<Key> child) {
        this.children[index] = child;
        if (child != null)
            child.setParent(this);
    }

    @Override
    public TreeNodeType getNodeType() {
        return TreeNodeType.InnerNode;
    }

    @Override
    public int search(Key key) {
        int index = 0;
        for (index = 0; index < getKeyCount(); index++) {
            int cmp = getKey(index).compareTo(key);
            if (cmp == 0) {
                return index + 1;
            }
            else if (cmp > 0) {
                return index;
            }
        }

        return index;
    }
    private void insertAt(int index, Key key, BTreeNode<Key> leftChild, BTreeNode<Key> rightChild) {
        for (int i = getKeyCount() + 1; i > index; i--) {
            setChild(i, getChild(i - 1));
        }
        for (int i = getKeyCount(); i > index; i--) {
            setKey(i, getKey(i - 1));
        }
        setKey(index, key);
        setChild(index, leftChild);
        setChild(index + 1, rightChild);
        this.keyCount += 1;
    }
    @Override
    protected BTreeNode<Key> split() {
        int midIndex = getKeyCount() / 2;

        BTreeInnerNode<Key> newRNode = new BTreeInnerNode<>(order);
        for (int i = midIndex + 1; i < getKeyCount(); i++) {
            newRNode.setKey(i - midIndex - 1, getKey(i));
            setKey(i, null);
        }
        for (int i = midIndex + 1; i <= getKeyCount(); i++) {
            newRNode.setChild(i - midIndex - 1, getChild(i));
            newRNode.getChild(i - midIndex - 1).setParent(newRNode);
            setChild(i, null);
        }
        setKey(midIndex, null);
        newRNode.keyCount = getKeyCount() - midIndex - 1;
        this.keyCount = midIndex;

        return newRNode;
    }

    @Override
    protected BTreeNode<Key> upKey(Key key, BTreeNode<Key> leftChild, BTreeNode<Key> rightNode) {
        int index = search(key);
        insertAt(index, key, leftChild, rightNode);
        if (isFull()) {
            return getNewNodeInsert();
        }
        else {
            return getParent() == null ? this : null;
        }
    }
    private void deleteAt(int index) {
        int i = 0;
        for (i = index; i < getKeyCount() - 1; i++) {
            setKey(i, getKey(i + 1));
            setChild(i + 1, getChild(i + 2));
        }
        setKey(i, null);
        setChild(i + 1, null);
        this.keyCount--;
    }

    @Override
    protected void borrowFromSiblings(BTreeNode<Key> borrower, BTreeNode<Key> lender, int borrowIndex) {
        int borrowerChildIndex = 0;
        while (borrowerChildIndex < getKeyCount() + 1 && getChild(borrowerChildIndex) != borrower)
            borrowerChildIndex++;

        if (borrowIndex == 0) {
            Key upKey = borrower.transferFromSibling(getKey(borrowerChildIndex), lender, borrowIndex);
            setKey(borrowerChildIndex, upKey);
        }
        else {
            Key upKey = borrower.transferFromSibling(getKey(borrowerChildIndex - 1), lender, borrowIndex);
            setKey(borrowerChildIndex - 1, upKey);
        }
    }

    @Override
    protected BTreeNode<Key> mergeChildren(BTreeNode<Key> leftChild, BTreeNode<Key> rightChild) {
        int index = 0;
        while (index < getKeyCount() && getChild(index) != leftChild)
            index++;
        Key sinkKey = getKey(index);
        leftChild.mergeSiblings(sinkKey, rightChild);
        deleteAt(index);
        if (isUnderflow()) {
            if (getParent() == null) {
                if (getKeyCount() == 0) {
                    leftChild.setParent(null);
                    return leftChild;
                }
                else {
                    return null;
                }
            }

            return getNewNodeDelete();
        }

        return null;
    }

    @Override
    protected void mergeSiblings(Key sinkKey, BTreeNode<Key> rightSibling) {
        BTreeInnerNode<Key> rightSiblingNode = (BTreeInnerNode<Key>)rightSibling;

        int j = getKeyCount();
        setKey(j++, sinkKey);

        for (int i = 0; i < rightSiblingNode.getKeyCount(); i++) {
            setKey(j + i, rightSiblingNode.getKey(i));
        }
        for (int i = 0; i < rightSiblingNode.getKeyCount() + 1; i++) {
            setChild(j + i, rightSiblingNode.getChild(i));
        }
        this.keyCount += 1 + rightSiblingNode.getKeyCount();

        setRightSibling(rightSiblingNode.rightSibling);
        if (rightSiblingNode.rightSibling != null)
            rightSiblingNode.rightSibling.setLeftSibling(this);
    }

    @Override
    protected Key transferFromSibling(Key sinkKey, BTreeNode<Key> sibling, int borrowIndex) {
        BTreeInnerNode<Key> siblingNode = (BTreeInnerNode<Key>)sibling;

        Key upKey = null;
        if (borrowIndex == 0) {
            int index = getKeyCount();
            setKey(index, sinkKey);
            setChild(index + 1, siblingNode.getChild(borrowIndex));
            this.keyCount += 1;

            upKey = siblingNode.getKey(0);
            siblingNode.deleteAt(borrowIndex);
        }
        else {
            insertAt(0, sinkKey, siblingNode.getChild(borrowIndex + 1), getChild(0));
            upKey = siblingNode.getKey(borrowIndex);
            siblingNode.deleteAt(borrowIndex);
        }

        return upKey;
    }
}