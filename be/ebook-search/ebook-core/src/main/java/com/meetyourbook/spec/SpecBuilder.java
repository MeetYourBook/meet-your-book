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

        private List<Specification<T>> specs = new ArrayList<>();

        private void addSpec(Specification<T> spec) {
            if (spec != null) {
                specs.add(spec);
            }
        }

        public Builder<T> and(Specification<T> spec) {
            addSpec(spec);
            return this;
        }

        public <V> Builder<T> ifNotNull(V value, Function<V, Specification<T>> specSupplier) {
            if (value != null) {
                addSpec(specSupplier.apply(value));
            }
            return this;
        }

        public Specification<T> toSpec() {
            Specification<T> spec = Specification.where(null);
            for (Specification<T> s : specs) {
                spec = spec.and(s);
            }
            return spec;
        }
    }
}