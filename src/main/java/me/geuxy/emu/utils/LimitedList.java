package me.geuxy.emu.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

@Getter @RequiredArgsConstructor
public class LimitedList<T> extends LinkedList<T> {

    private final int maxSize;

    public boolean add(T t) {
        if(size() >= maxSize) {
            this.removeFirst();
        }
        return super.add(t);
    }

    public boolean isFull() {
        return size() >= maxSize;
    }

}
