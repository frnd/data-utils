package es.frnd.matrix;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Categorization matrix.
 * 
 * @author Fernando Gonzalez
 * @param <R>
 *            the type of the table row keys
 * @param <C>
 *            the type of the table column keys
 * @param <V>
 *            the type of the cell values. The summary of all items in the cell.
 * @param <T>
 *            the type of the items to categorize
 */
public interface Matrix<R, C, T, V> {

	/**
	 * Returns true if the matrix contains the specified element.
	 * 
	 * @param item
	 *            the item to search for
	 * @return
	 */
	public boolean contains(T item);

	/**
	 * Returns {@code true} if the table contains a mapping with the specified
	 * row and column keys.
	 * 
	 * @param rowKey
	 *            key of row to search for
	 * @param columnKey
	 *            key of column to search for
	 */
	boolean contains(R rowKey, C columnKey);

	/**
	 * Returns {@code true} if the table contains a mapping with the specified
	 * row key.
	 * 
	 * @param rowKey
	 *            key of row to search for
	 */
	boolean containsRow(R rowKey);

	/**
	 * Returns {@code true} if the table contains a mapping with the specified
	 * column key.
	 * 
	 * @param columnKey
	 *            key of column to search for
	 */
	boolean containsColumn(C columnKey);

	/**
	 * Return the a matrix cell for the specified row and column keys, or
	 * {@code null} if no such mapping exists.
	 * 
	 * <pre>
	 * for (R row : matrix.rowKeySet()) {
	 * 	for (C col : matrix.ColumnKeySet()) {
	 * 		CellValue value = matrix.getValue(col, row);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param row
	 *            key of row to search for
	 * @param column
	 *            key of column to search for
	 * @return a cellValue
	 */
	Cell<T, V> get(R row, C column);

	/**
	 * Returns the number of row key / column key / value mappings in the table.
	 */
	int size();

	/** Returns {@code true} if the table does not contains any element. */
	boolean isEmpty();

	/** Removes all mappings from the table. */
	void clear();

	/**
	 * Add the specified value to the items in the matrix and categorize
	 * 
	 * @param value
	 *            value to be added
	 * @return the previously cell where the value fall when categorizing, or
	 *         {@code null}
	 */
	Cell<T, V> put(T value);

	/**
	 * Add all the specified value to the items in the matrix and categorize
	 * 
	 * @param values
	 *            the new values to add
	 */
	void putAll(Collection<T> values);

	void remove(T value);

	void removeAll(Collection<T> values);

	/**
	 * Returns a view of all values that have the given row key. For each row
	 * key / column key / value mapping in the table with that row key, the
	 * returned map associates the column key with the value.
	 * 
	 * @param rowKey
	 *            key of row to search for in the table
	 * @return the corresponding map from column keys to values
	 */
	Map<C, Cell<T, V>> row(R rowKey);

	Cell<T, V> totalRow(R rowKey);

	/**
	 * Returns a view of all mappings that have the given column key. For each
	 * row key / column key / value mapping in the table with that column key,
	 * the returned map associates the row key with the value. If no mappings in
	 * the table have the provided column key, an empty map is returned.
	 * 
	 * <p>
	 * Changes to the returned map will update the underlying table, and vice
	 * versa.
	 * 
	 * @param columnKey
	 *            key of column to search for in the table
	 * @return the corresponding map from row keys to values
	 */
	Map<R, Cell<T, V>> column(C columnKey);

	Cell<T, V> totalColumn(C columnKey);

	/**
	 * Returns a view that associates each row key with the corresponding map
	 * from column keys to values. Changes to the returned map will update this
	 * table. The returned map does not support {@code put()} or
	 * {@code putAll()}, or {@code setValue()} on its entries.
	 * 
	 * <p>
	 * In contrast, the maps returned by {@code rowMap().get()} have the same
	 * behavior as those returned by {@link #row}. Those maps may support
	 * {@code setValue()}, {@code put()}, and {@code putAll()}.
	 * 
	 * @return a map view from each row key to a secondary map from column keys
	 *         to values
	 */
	Map<R, Map<C, Cell<T, V>>> rowMap();

	/**
	 * Returns a view that associates each column key with the corresponding map
	 * from row keys to values. Changes to the returned map will update this
	 * table. The returned map does not support {@code put()} or
	 * {@code putAll()}, or {@code setValue()} on its entries.
	 * 
	 * <p>
	 * In contrast, the maps returned by {@code columnMap().get()} have the same
	 * behavior as those returned by {@link #column}. Those maps may support
	 * {@code setValue()}, {@code put()}, and {@code putAll()}.
	 * 
	 * @return a map view from each column key to a secondary map from row keys
	 *         to values
	 */
	Map<C, Map<R, Cell<T, V>>> columnMap();

	/**
	 * Returns a set of all row key / column key / value triplets. Changes to
	 * the returned set will update the underlying table, and vice versa. The
	 * cell set does not support the {@code add} or {@code addAll} methods.
	 * 
	 * @return set of table cells consisting of row key / column key / value
	 *         triplets
	 */
	Set<Cell<T, V>> cellSet();

	/**
	 * Returns a set of row keys that have one or more values in the table.
	 * Changes to the set will update the underlying table, and vice versa.
	 * 
	 * @return set of row keys
	 */
	Set<R> rowKeySet();

	/**
	 * Returns a set of column keys that have one or more values in the table.
	 * Changes to the set will update the underlying table, and vice versa.
	 * 
	 * @return set of column keys
	 */
	Set<C> columnKeySet();

	/**
	 * Return a list of all items where the matrix is made from.
	 * 
	 * @return all items in the matrix
	 */
	Collection<T> getItems();

	/**
	 * A cell is a bundle of items categorized on 2 axis
	 *
	 * @author Fernando Gonzalez
	 * 
	 * @param <V>
	 *            the type of the value. Is the result of accumulation this cell
	 *            constituents.
	 * @param <T>
	 */
	public interface Cell<T, V> {

		/**
		 * Returns the value of this cell as the result of the accumulation of
		 * all items in this cell.
		 */
		public V getValue();

		/**
		 * Return the constituents of the cell.
		 */
		public List<T> getItems();

		/**
		 * Runs accumulation on current cell.
		 */
		void accumulate();

		/**
		 * Clears all information in current cell.
		 */
		void clear();
	}

	public interface Accumulator<T, V> {

		V accumulate(List<T> items);
	}

	public interface Resolver<T, Axis> {

		Axis resolve(T o1);
	}

}
