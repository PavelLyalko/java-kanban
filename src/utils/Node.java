package utils;


public class Node<E> {
    int index;
    E item;
    Node<E> next;
    Node<E> prev;

    public Node(int index, Node<E> prev, E element, Node<E> next) {
        this.index = index;
        this.item = element;
        this.next = next;
        this.prev = prev;
    }

    public E getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }
}
