package com.company;

class Node<T>
{
    T data;
    Node<T> next;

    Node(T n)
    {
        data = n;
    }
}

public class LinkedList<T>
{
    Node<T> head;

    public void insert(T d)
    {
        Node<T> n = new Node(d);
        if(head == null)
        {
            head = n;
        }
        else
        {
            Node<T> temp = head;
            while (temp.next != null)
            {
                temp = temp.next;
            }
            temp.next = n;
        }
    }

    @Override
    public String toString()
    {
        String str = "";
        Node temp = head;
        while (temp != null)
        {
            str = str + temp.data + " ";
            temp = temp.next;
        }
        return str;
    }
}
