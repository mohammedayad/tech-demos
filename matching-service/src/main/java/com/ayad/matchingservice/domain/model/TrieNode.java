package com.ayad.matchingservice.domain.model;

// TrieNode class

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * TrieNode class for Using the Trie data structure:
 * It is used as an efficient information retrieval data structure. It stores the keys in form of a balanced BST..
 */
@Getter
@Setter
@ToString
public class TrieNode {
    private char value;  // The character value of the node
    private boolean isEndOfPrefix; //  boolean flag indicating if the node represents the end of a prefix
    private Map<Character, TrieNode> children; // map of child nodes, where the key is the character value and the value is the corresponding child node

    public TrieNode(char value) {
        this.value = value;
        this.isEndOfPrefix = false;
        this.children = new HashMap<>();
    }

}
