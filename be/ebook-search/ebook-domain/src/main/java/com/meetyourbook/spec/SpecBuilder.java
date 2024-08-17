package com.meetyourbook.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.jpa.domain.Specification;

public class SpecBuilder {

    public static <T> Builder<T> builder(Class<T> type) {
        return new Builder<T>();
    }

    public static class Builder<T> {

        private List<Specification<T>> andSpecs = new ArrayList<>();
        private List<Specification<T>> orSpecs = new ArrayList<>();

        private void addAndSpec(Specification<T> spec) {
            if (spec != null) {
                andSpecs.add(spec);
            }
        }

        private void addOrSpec(Specification<T> spec) {
            if (spec != null) {
                orSpecs.add(spec);
            }
        }

        public <V> Builder<T> ifNotNullAnd(V value, Function<V, Specification<T>> specSupplier) {
            if (value != null) {
                addAndSpec(specSupplier.apply(value));
            }
            return this;
        }

        public <V> Builder<T> ifNotNullOr(V value, Function<V, Specification<T>> specSupplier) {
            if (value != null) {
                addOrSpec(specSupplier.apply(value));
            }
            return this;
        }

        public Specification<T> toSpec() {
            Specification<T> andSpec = Specification.where(null);
            for (Specification<T> s : andSpecs) {
                andSpec = andSpec.and(s);
            }

            Specification<T> orSpec = Specification.where(null);
            for (Specification<T> s : orSpecs) {
                orSpec = orSpec.or(s);
            }

            return andSpec.and(orSpec);
        }
    }
}