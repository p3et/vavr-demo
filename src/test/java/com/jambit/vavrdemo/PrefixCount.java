package com.jambit.vavrdemo;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import org.junit.Test;


public class PrefixCount {

    @Test
    public void test() {
        final var input = List.of("foo", "bar", "baz");

        final var root = Tree.of(Tuple.of("", 0), List.empty());

        final var prefixCounts = input.foldLeft(root, (node, word) -> updateNode(node, word));

        System.out.println(prefixCounts);
    }

    private Tree.Node<Tuple2<String, Integer>> updateNode(final Tree.Node<Tuple2<String, Integer>> node, final String word) {
        return hasPrefix(word, node) ? incrementNode(node, word)
                                     : node;
    }

    private Tree.Node<Tuple2<String, Integer>> incrementNode(final Tree.Node<Tuple2<String, Integer>> node, final String word) {
        final var count = node.getValue().map2(i -> i + 1);
        final var updatedNode = Tree.of(count, node.getChildren());

        final var maybeChildWithPrefix = node.getChildren()
                                             .find(child -> hasPrefix(word, child));

        return maybeChildWithPrefix.map(child -> updateChild(updatedNode, child, word))
                                   .getOrElse(() -> addChild(updatedNode, word));
    }

    private Tree.Node<Tuple2<String, Integer>> updateChild(final Tree.Node<Tuple2<String, Integer>> parent,
                                                           final Tree.Node<Tuple2<String, Integer>> child,
                                                           final String word) {
        final var children = parent.getChildren()
                                   .filter(sibling -> !sibling.equals(child))
                                   .prepend(updateNode(child, word));

        return Tree.of(parent.getValue(), children);
    }

    private Tree.Node<Tuple2<String, Integer>> addChild(final Tree.Node<Tuple2<String, Integer>> parent, final String word) {

        final var level = parent.getValue()._1.length();

        if (level >= word.length()) {
            return parent;
        }

        final var prefix = word.substring(0, level + 1);
        final var child = addChild(Tree.of(Tuple.of(prefix, 1)), word);

        final var children = parent.getChildren()
                                   .prepend(child);

        return Tree.of(parent.getValue(), children);
    }

    private boolean hasPrefix(final String word, final Tree.Node<Tuple2<String, Integer>> node) {
        return word.startsWith(node.getValue()._1);
    }
}
