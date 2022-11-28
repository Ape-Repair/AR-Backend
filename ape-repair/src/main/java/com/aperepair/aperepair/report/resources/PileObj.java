package com.aperepair.aperepair.report.resources;

public class PileObj<T> {


    // 01) Atributtes
    private T[] pile;
    private int top;

    // 02) Constructor
    public PileObj(int capacity) {
        pile = (T[]) new Object[capacity];
        top = -1;
    }

    // 03) Methods

    public Boolean isEmpty() {
        return top == -1;
    }

    public Boolean isFull() {
        return top == pile.length -1;
    }

    public void push(T object) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        else {
            pile[++top] = object;
        }
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        return pile[top--];
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return pile[top];
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("Empty stack");
        }
        else {
            for (int i = top; i >= 0; i--) {
                System.out.println(pile[i]);
            }
        }
    }

    public void setPile(T[] pile) {
        this.pile = pile;
    }

    public void setTop(int top) {
        this.top = top;
    }

    //Getters - do not withdraw
    public int getTop() {
        return top;
    }

    public T[] getPile() {
        return pile;
    }
}