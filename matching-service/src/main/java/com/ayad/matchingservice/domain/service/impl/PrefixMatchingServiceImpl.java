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
import java.util.Optional;
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
     * @param input the input string to match against the prefixes of the {@link PrefixEntity} instances.
     * @return a string that contain the longest prefix
     * @throws MatchingException if no matching prefixes are found.
     */
    public String findLongestPrefix(String input) {
        log.debug("root node {}", root);
        TrieNode node = root;
        StringBuilder oldLongestPrefix = new StringBuilder();
        StringBuilder longestPrefix = new StringBuilder();
        if (input.length() == 1) {//input is one Char
            if (node.getChildren().containsKey(input) && node.getChildren().get(input).isEndOfPrefix()) {
                return input;
            } else {
                throw new MatchingException(HttpStatus.BAD_REQUEST, MatchingUtility.CONSTRAINT_VIOLATIONS,
                        MatchingUtility.NO_PREFIX_MATCHING_FOUND);
            }

        } else {//input is more than one Char

            for (char c : input.toCharArray()) {
                if (node.getChildren().containsKey(c)) {
                    node = node.getChildren().get(c);
                    log.debug("tree nodes {} ", node);
                    log.debug("input {} char {} length of his children is {} children are {} ", input, c, node.getChildren().size(), node.getChildren());
                    //longestPrefix += c;
                    longestPrefix.append(c);

                    if (node.isEndOfPrefix()) {//detect a prefix end
                        //oldLongestPrefix = longestPrefix;
                        oldLongestPrefix.setLength(0); // clear old value
                        oldLongestPrefix.append(longestPrefix);
                    }
/*
                if (node.isEndOfPrefix() && node.getChildren().isEmpty()) {// I have reached to the end of the prefix, no other children exist and all the chars were part of the input string
                    return longestPrefix;
                }

 */
                } else {// no match for the input next character and it is not the leaf of the prefix
                    log.debug("input {} latest Match char {} longestPrefix {} oldLongestPrefix {} and the node is {} with children length {}", input, c, longestPrefix, oldLongestPrefix, node, node.getChildren().size());
                    if (node.isEndOfPrefix()) {// I have reached to the end of the  matched prefix, no other children exist and all the chars were part of the input string
                        return longestPrefix.toString();
                    } else {// this mean the latest longestPrefix doesn't match as still have chars in the prefix so I will return the oldLongestMatch if it was have a value otherwise no match is exist
                        if (!oldLongestPrefix.toString().isEmpty()) {
                            return oldLongestPrefix.toString();
                        }
                        throw new MatchingException(HttpStatus.BAD_REQUEST, MatchingUtility.CONSTRAINT_VIOLATIONS,
                                MatchingUtility.NO_PREFIX_MATCHING_FOUND);
                    }

                }
            }
        }
        return longestPrefix.toString();
    }

    /**
     * Finds the longest prefix of the input string that matches a prefix of any {@link PrefixEntity} instance in the database.
     *
     * @param input the input string to match against the prefixes of the {@link PrefixEntity} instances.
     * @return the prefix of the {@link PrefixEntity} instance with the longest matching prefix, or throws a
     * @throws MatchingException if no matching prefixes are found.
     */
    public String findLongestPrefixUsingDB(String input) {
        Optional<List<PrefixEntity>> prefixEntities = prefixRepository.findByMatchingPrefix(input);
        if (prefixEntities.isPresent() && !prefixEntities.get().isEmpty()) {
            List<PrefixEntity> matchedPrefixEntities = prefixEntities.get();
            // process the prefixEntities list
            log.debug("the list of matched prefixes are {}", matchedPrefixEntities);
            return matchedPrefixEntities.get(0).getPrefix();
        } else {
            throw new MatchingException(HttpStatus.BAD_REQUEST, MatchingUtility.CONSTRAINT_VIOLATIONS,
                    MatchingUtility.NO_PREFIX_MATCHING_FOUND);
        }
    }

}

