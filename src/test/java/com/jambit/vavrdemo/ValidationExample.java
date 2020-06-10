package com.jambit.vavrdemo;

import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.junit.Test;

/**
 * Code partially copied from https://www.vavr.io/vavr-docs/#_validation
 */
public class ValidationExample {

    @Test
    public void validation() {

        final var persons = List.of(Person.of("Alice", 42),
                                    Person.of("Bob", -1),
                                    Person.of("R2D2", 77),
                                    Person.of("$man", -100));

        final var validator = new PersonValidator();

        final var validations = persons.map(validator::validatePerson);

        final var validPersons = validations.filter(Validation::isValid)
                                            .map(Validation::get);

        System.out.println(validPersons);

        final var errors = validations.filter(Validation::isInvalid)
                                      .map(v -> v.getError())
                                      .map(msgs -> msgs.intersperse(" and ")
                                                       .foldLeft(new StringBuilder(), StringBuilder::append)
                                                       .toString());

        System.out.println(errors);

    }

    private static class Person {
        public final String name;
        public final int age;

        private Person(final String name, final int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person(" + name + ", " + age + ")";
        }

        public static Person of(final String name, final int age) {
            return new Person(name, age);
        }
    }

    private static class PersonValidator {

        private static final String VALID_NAME_CHARS = "[a-zA-Z ]";
        private static final int MIN_AGE = 0;

        public Validation<Seq<String>, Person> validatePerson(final Person person) {
            return Validation.combine(validateName(person.name), validateAge(person.age))
                             .ap(Person::new);
        }

        private Validation<String, String> validateName(final String name) {
            return CharSeq.of(name)
                          .replaceAll(VALID_NAME_CHARS, "")
                          .transform(seq -> seq.isEmpty()
                                            ? Validation.valid(name)
                                            : Validation.invalid("Name contains invalid characters: '" + seq.distinct().sorted() + "'"));
        }

        private Validation<String, Integer> validateAge(final int age) {
            return age < MIN_AGE ? Validation.invalid("Age must be at least " + MIN_AGE)
                                 : Validation.valid(age);
        }

    }
}
