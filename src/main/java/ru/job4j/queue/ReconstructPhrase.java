package ru.job4j.queue;

import java.util.Deque;
import java.util.Iterator;

public class ReconstructPhrase {

    private final Deque<Character> descendingElements;

    private final Deque<Character> evenElements;

    public ReconstructPhrase(Deque<Character> descendingElements, Deque<Character> evenElements) {
        this.descendingElements = descendingElements;
        this.evenElements = evenElements;
    }

    private String getEvenElements() {
        StringBuilder stb = new StringBuilder();
        int deckSize = evenElements.size();
        for (int i = 0; i < deckSize; i++) {
            Character ch = evenElements.pollFirst();
            if ((i % 2) == 0) {
                stb.append(ch);
            }
        }
        return stb.toString();
    }

    private String getDescendingElements() {
        StringBuilder stb = new StringBuilder();
        Iterator<Character> iterator = descendingElements.descendingIterator();
        while (iterator.hasNext()) {
            stb.append(iterator.next());
        }
        return stb.toString();
    }

    public String getReconstructPhrase() {
        return getEvenElements() + getDescendingElements();
    }
}