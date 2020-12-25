/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vod_ga;

import java.util.NoSuchElementException;

/**
 *  equip single list
 * @author Admin
 */

public class Set<E>{
    private int size = 0;
    
    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    private Node<E> first;
    
     /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    private Node<E> last;
    
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }
    
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }
    
    public void add(E item){
        if (size == 0){
            first = last = new Node<E>(item, null);
            ++size;
            return;
        }
        Node<E> tmp = new Node<E>(item, null);
        last.next = tmp;
        last = tmp;
        ++size;
    }
    
    public void addAll(Set<? extends E> s) {
        if (this == s) {
            throw new RuntimeException("2 List are located same memory");
        }
        if(s.size == 0) return;
        if(this.size == 0) {
            this.first = (Node<E>) s.first;
            this.last = (Node<E>) s.last;
            this.size = s.size;
        }
        Object tmp = s.first;
        if (size == 0) {
            first = (Node<E>) tmp;
        }
        this.last.next = (Node<E>) tmp;
        last = (Node<E>) s.last;
        size = s.size + size;
        return;
    }
    public void show(){
        Node<E> tmp;
        if(size > 0){
            tmp = first;
            while (tmp != null){
                System.out.print(tmp.item.toString()+ " ");
                tmp = tmp.next;
            }
        }
        System.out.println();
        return;
    }
    //Datastructe
    private static class Node<E>{
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }
}
