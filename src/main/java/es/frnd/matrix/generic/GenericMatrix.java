package es.frnd.matrix.generic;

import es.frnd.matrix.AbstractMatrix;
import es.frnd.matrix.Matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sorted by the natural sorting of the row and column headers.
 *
 * @param <T>
 * @param <V>
 * @param <R>
 * @param <C>
 * @author Fernando Gonzalez
 */
public class GenericMatrix<R, C, T, V> extends AbstractMatrix<R, C, T, V> implements
        Matrix<R, C, T, V> {

    private final Resolver<T, R> rowResolver;
    private final Resolver<T, C> colResolver;
    private final Accumulator<T, V> accumulator;

    public GenericMatrix(Resolver<T, R> rowResolver, Resolver<T, C> colResolver,
                         Accumulator<T, V> accumulator) {
        super(new HashMap<R, Map<C, Matrix.Cell<T, V>>>(), new ArrayList<T>(), new HashMap<C, Matrix.Cell<T, V>>(),
                new HashMap<R, Matrix.Cell<T, V>>(), new Cell<T, V>(accumulator));
        this.rowResolver = rowResolver;
        this.colResolver = colResolver;
        this.accumulator = accumulator;
    }

    static class Cell<T, V> implements Matrix.Cell<T, V> {

        private final Accumulator<T, V> accumulator;
        private V value;
        private List<T> items;

        public Cell(Accumulator<T, V> accumulator) {
            this.accumulator = accumulator;
            items = new ArrayList<T>();
        }

        public void add(T item) {
            items.add(item);
            accumulate();
        }

        /*
         * This method is for internal use only. Will add an item without
         * calling to Cell.accumulate()
         */
        void addInternal(T item) {
            items.add(item);
        }

        public void accumulate() {
            value = accumulator.accumulate(items);
        }

        /**
         * @return the value
         */
        public V getValue() {
            return value;
        }

        /**
         * @return the items
         */
        public List<T> getItems() {
            return items;
        }

        @Override
        public void clear() {
            items.clear();
            value = null;
        }
    }

    @Override
    protected es.frnd.matrix.Matrix.Resolver<T, R> getRowResolver() {
        return rowResolver;
    }

    @Override
    protected es.frnd.matrix.Matrix.Resolver<T, C> getColResolver() {
        return colResolver;
    }

    @Override
    protected Cell<T, V> createCell() {
        return new Cell<T, V>(accumulator);
    }

    @Override
    protected Map<C, Matrix.Cell<T, V>> createBackingMap() {
        return new HashMap<C, Matrix.Cell<T, V>>();
    }
}
