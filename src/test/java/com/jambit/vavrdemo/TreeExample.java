package com.jambit.vavrdemo;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import io.vavr.control.Option;
import org.junit.Test;


public class TreeExample {

    @Test
    public void test() {
        final Tree.Node<Tuple2<String, Integer>> root = Tree.of(Tuple.of("", 0), List.empty());
        final List<String> input = List.of("foo", "bar", "baz");

        final Tree<Tuple2<String, Integer>> prefixCounts = input.foldLeft(root, (node, word) -> update(node, word));

        System.out.println(prefixCounts);
    }

    private Tree.Node<Tuple2<String, Integer>> update(final Tree.Node<Tuple2<String, Integer>> node, final String word) {
        return hasPrefix(word, node) ? increment(node, word)
                                     : node;
    }

    private Tree.Node<Tuple2<String, Integer>> increment(final Tree.Node<Tuple2<String, Integer>> node, final String word) {
        final Tree.Node<Tuple2<String, Integer>> incremented = Tree.of(node.getValue()
                                                                           .map2(i -> i + 1),
                                                                       node.getChildren());


        final Option<Tree.Node<Tuple2<String, Integer>>> maybeMatchingChild = node.getChildren()
                                                                                  .find(child -> hasPrefix(word, child));


        return maybeMatchingChild.map(child -> updateChild(incremented, child, word))
                                 .getOrElse(() -> addChild(incremented, word));
    }

    private Tree.Node<Tuple2<String, Integer>> addChild(final Tree.Node<Tuple2<String, Integer>> node, final String word) {

        final int level = node.getValue()._1.length();

        if (level >= word.length()) {
            return node;
        }

        final String prefix = word.substring(0, level + 1);

        final Tree.Node<Tuple2<String, Integer>> newChild = Tree.of(Tuple.of(prefix, 1));

        final List<Tree.Node<Tuple2<String, Integer>>> children = node.getChildren()
                                                                      .prepend(addChild(newChild, word));

        return Tree.of(node.getValue(), children);
    }

    private Tree.Node<Tuple2<String, Integer>> updateChild(final Tree.Node<Tuple2<String, Integer>> node, final Tree.Node<Tuple2<String, Integer>> matchingNode, final String word) {
        final List<Tree.Node<Tuple2<String, Integer>>> children = node.getChildren()
                                                                      .filter(child -> !child.equals(matchingNode))
                                                                      .prepend(update(matchingNode, word));

        return Tree.of(node.getValue(), children);
    }

    private boolean hasPrefix(final String word, final Tree.Node<Tuple2<String, Integer>> node) {
        return word.startsWith(node.getValue()._1);
    }
}
