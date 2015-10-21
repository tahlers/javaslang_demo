package de.bi.jug;

import javaslang.Function1;
import javaslang.Function2;
import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.Map.Entry;
import javaslang.collection.Stream;
import javaslang.control.Either;
import javaslang.control.Left;
import javaslang.control.Right;

import java.time.LocalDate;
import java.util.function.Function;

public class Examples {

    public static void main(String[] args) {
        funfun();
    }

    private static void funfun() {
        // sum.apply(1, 3) = 4
        final Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        System.out.println("sum(1, 3) = " + sum.apply(1, 3));

        // add2.apply(1) = 3
        final Function1<Integer, Integer> add2 = sum.curried().apply(2);
        System.out.println("add2(1) = " + add2.apply(1));

        // computes the hash code of an object
        final Function1<Object, Integer> hash = Object::hashCode;

        // computes the hash once, then reads from cache
        final Function<Object, Integer> hashCache =
                Function1.lift(Object::hashCode).memoized();

        // ciao type erasure, hi Integer.class!
        final Class<?> returnType = hash.getType().returnType();
        System.out.println("return Type: " + returnType);

    }

    private static void streamEndless() {
        Stream.from(1)
                .stdout();
    }

    private static void streamfun() {
        Stream.from(1) // 1, 2, 3, ...
                .filter(i -> i % 2 == 0) // 2, 4, 6, ...
                .sliding(4, 2) // (2, 4, 6, 8), (6, 8, 10, 12), (10, 12, 14, 16), ...
                .take(4)
                .stdout();
    }

    private static void streamfun2() {
        Stream.gen(Math::random)
                .take(1000)
                .zipWithIndex()
                .stdout();
    }

    private static void mapfun() {
        HashMap<String, String> slangmap = HashMap.of(
                Entry.of("a", "hallo"),
                Entry.of("b", "weltuntergang"));
        slangmap.stdout();
        Map<String, Integer> mapLength = slangmap.toMap(
                e -> Tuple.of(
                        e.key(),
                        e.value().length()
                ));
        mapLength.stdout();
    }

    private static void eitherfun() {
        Either<String, LocalDate> dateOrErrorMessages = new Right<>(LocalDate.now());
        Either<String, LocalDate> dateOrErrorMessages2 = new Right<>(LocalDate.now().minusYears(1));
        Either<String, LocalDate> dateOrErrorMessages3 = new Left<>("an error occured");

        System.out.println(dateOrErrorMessages);
        System.out.println(dateOrErrorMessages2);
        System.out.println(dateOrErrorMessages3);

        List.of(dateOrErrorMessages, dateOrErrorMessages2, dateOrErrorMessages3)
                .flatMap(Either::right)
                .map(date -> "date result: " + date)
                .stdout();
    }

}
