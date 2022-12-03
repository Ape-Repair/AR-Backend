package com.aperepair.aperepair.report.resources;

public class QueueObj<T> {
    // 01) Atributos
    private int size;
    private T[] queue   ;

    // 02) Construtor
    public QueueObj(int capacity) {
        size = 0;
        queue = (T[]) new Object[capacity];
    }

    // 03) Métodos

    public Boolean isEmpty() {
        return size == 0;
    }

    public Boolean isFull() {
        return size == queue.length;
    }

    public void insert(T info) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        else {
            queue[size++] = info;
        }
    }

    public T peek() {
        return queue[0];
    }

    public T poll() {
        T primeiro = peek(); // salva o primeiro elemento da fila

        if (!isEmpty()) { // se a fila não está vazia
            // faz a fila andar
            for (int i = 0; i < size -1; i++) {
                queue[i] = queue[i+1];
            }
            queue[size -1] = null;    // limpa o último cara da fila
            size--;                 // decrementa tamanho
        }

        return primeiro;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("A fila está vazia");
        }
        else {
            System.out.println("\nElementos da fila:");
            for (int i = 0; i < size; i++) {
                System.out.println(queue[i]);
            }
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setQueue(T[] queue) {
        this.queue = queue;
    }

    //Getters - Não retirar
    public T[] getQueue() {
        return queue;
    }

    public int getSize() {
        return size;
    }
}