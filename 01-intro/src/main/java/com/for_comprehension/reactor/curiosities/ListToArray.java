package com.for_comprehension.reactor.curiosities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

class ListToArray {

    public static void main(String[] args) {
        List<String> strings = Stream.generate(() -> UUID.randomUUID().toString())
          .limit(1000_000)
          .toList();

        // https://shipilev.net/blog/2016/arrays-wisdom-ancients/
        String[] array = strings.toArray(new String[0]);
        String[] array1 = strings.toArray(String[]::new);
    }
}
