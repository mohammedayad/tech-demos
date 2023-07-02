package com.ayad.matchingservice.domain.service.impl;

import com.ayad.matchingservice.common.exception.MatchingException;
import com.ayad.matchingservice.common.utils.MatchingUtility;
import com.ayad.matchingservice.domain.model.PrefixEntity;
import com.ayad.matchingservice.domain.model.TrieNode;
import com.ayad.matchingservice.domain.repository.ifc.PrefixRepository;
import com.ayad.matchingservice.domain.service.ifc.PrefixMatchingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the PrefixMatchingService interface.
 */
@Service
@Slf4j
public class PrefixMatchingServiceImpl implements PrefixMatchingService {

    private final PrefixRepository prefixRepository;
    private TrieNode root;

    public PrefixMatchingServiceImpl(PrefixRepository prefixRepository) {
        this.prefixRepository = prefixRepository;
        this.root = new TrieNode('\0'); // Create the root node
    }

    /**
     * Retrieves all the prefix values.
     *
     * @return a list of prefix values
     */
    @Override
    public List<String> getAllPrefixes() {
        List<PrefixEntity> prefixEntities = prefixRepository.findAll();
        return prefixEntities.stream().map(PrefixEntity::getPrefix).collect(Collectors.toList());
    }

    /**
     * Builds the Trie by inserting each prefix from the provided list of prefixes.
     */
    public void buildTrie(List<String> prefixes) {
        prefixes.forEach(this::insertPrefix);

    }

    /**
     * Inserts a single prefix into the Trie.
     */
    private void insertPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node.getChildren().putIfAbsent(c, new TrieNode(c));
            node = node.getChildren().get(c);
        }
        node.setEndOfPrefix(true);
    }

    /**
     * Traverses the Trie character by character, comparing each character with the child nodes.
     * If a match is found, it continues traversing.
     * If there is no match, it returns the longest prefix found so far.
     *
     * @return a string that contain the longest prefix
     */
    public String findLongestPrefix(String input) {
        log.debug("root node {}", root);
        TrieNode node = root;
        String longestPrefix = "";

        for (char c : input.toCharArray()) {
            if (node.getChildren().containsKey(c)) {
                node = node.getChildren().get(c);
                log.debug("input {} char {} length of his children is {} children are {} ", input, c, node.getChildren().size(), node.getChildren());
                longestPrefix += c;

                if (node.isEndOfPrefix()) {// I have reached to the end of the prefix and all the chars were part of the input string
                    return longestPrefix;
                }
            } else {// no match for the input next character and it is not the leaf of the prefix
                log.debug("longestPrefix for {} is {} and the node is {} with children length {}", input, longestPrefix, node, node.getChildren().size());
                throw new MatchingException(HttpStatus.BAD_REQUEST, MatchingUtility.CONSTRAINT_VIOLATIONS,
                        MatchingUtility.NO_PREFIX_MATCHING_FOUND);
            }
        }
        return longestPrefix;
    }

}

