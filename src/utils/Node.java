package utils;

public class Node<E> {
    int index;
    E item;

    public Node(int index, E element) {
        this.index = index;
        this.item = element;
    }

    public E getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }
}
