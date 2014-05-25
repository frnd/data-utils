/*
 * The MIT License
 *
 * Copyright 2013 Fernando Gonz�lez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package es.frnd.matrix;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 
 * @author fernando
 *
 * @param <R> The row index type
 * @param <C> The column index type
 * @param <T> The type of the elements that matrix will contain.
 * @param <V> The type of the value. Is the result of accumulation this cell constituents.
 */
public abstract class AbstractMatrix<R, C, T, V> implements Matrix<R, C, T, V> {

	final Map<R, Map<C, Cell<T, V>>>	backingMap;
	final List<T>						allItems;
	final Map<C, Cell<T, V>>			totalRow;
	final Map<R, Cell<T, V>>			totalColumn;
	final Cell<T, V>					total;

	/**
	 * Returns the row resolver.
	 * 
	 * @return
	 */
	protected abstract Resolver<T, R> getRowResolver();

	/**
	 * Returns the col resolver.
	 * 
	 * @return
	 */
	protected abstract Resolver<T, C> getColResolver();

	/**
	 * 
	 * @return
	 */
	protected abstract Map<C, Cell<T, V>> createBackingMap();

	/**
	 * Returns a new and empty cell.
	 * 
	 * @return
	 */
	protected abstract Cell<T, V> createCell();

	public AbstractMatrix(Map<R, Map<C, Matrix.Cell<T, V>>> backingMap, List<T> allItems,
			Map<C, Cell<T, V>> totalRow, Map<R, Cell<T, V>> totalColumn, Cell<T, V> total) {
		super();
		this.backingMap = backingMap;
		this.totalRow = totalRow;
		this.totalColumn = totalColumn;
		this.total = total;
		this.allItems = allItems;
	}

	@Override
	public boolean contains(T item) {
		if (item == null)
			return true;
		for (Map<C, Matrix.Cell<T, V>> row : backingMap.values()) {
			for (Matrix.Cell<T, V> cell : row.values()) {
				if (cell.getItems().contains(item)) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public boolean contains(R rowKey, C columnKey) {
		Map<C, ?> map = row(rowKey);
		return map != null ? map.containsKey(columnKey) : false;
	}

	@Override
	public boolean containsRow(R rowKey) {
		return rowMap().containsKey(rowKey);
	}

	@Override
	public boolean containsColumn(C columnKey) {
		return columnMap().containsKey(columnKey);
	}

	@Override
	public Cell<T, V> get(R row, C column) {
		Map<C, Cell<T, V>> rowMap = row(row);
		return rowMap != null && rowMap.get(column) != null ? rowMap.get(column) : getEmptyCell();
	}
	
	private es.frnd.matrix.Matrix.Cell<T, V> getEmptyCell() {
		return new Cell<T, V>() {

			@Override
			public V getValue() {
				return null;
			}

			@Override
			public List<T> getItems() {
				return Collections.emptyList();
			}

			@Override
			public void accumulate() {
			}

			@Override
			public void clear() {
			}
		};
	}

	@Override
	public int size() {
		return  allItems.size(); //rowMap().size() * columnMap().size();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Map<R, Cell<T, V>> column(C columnKey) {
		return new Column(columnKey);
	}

	private class Column extends InnerMap<R> {

		final C	columnKey;

		public Column(C rowKey) {
			super();
			this.columnKey = rowKey;
		}

		@Override
		public Cell<T, V> get(Object rowKey) {
			Map<C, Cell<T, V>> map = backingMap.get(rowKey);
			return map != null ? map.get(columnKey) : null;
		}

		@Override
		public boolean containsKey(Object rowKey) {
			return backingMap.containsKey(rowKey);
		}

		@Override
		public Set<Map.Entry<R, Matrix.Cell<T, V>>> entrySet() {
			return new EntrySet();
		}

		class EntrySet extends InnerMap<R>.EntrySet {

			@Override
			public int size() {
				// TODO(frnd) Auto-generated method stub
				return 0;
			}

			@Override
			public Iterator iterator() {
				return new Iterator<Matrix.Cell<T, V>>() {

					@Override
					public boolean hasNext() {
						// TODO(frnd) Auto-generated method stub
						return false;
					}

					@Override
					public Matrix.Cell<T, V> next() {
						// TODO(frnd) Auto-generated method stub
						return null;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public Object[] toArray() {
				// TODO(frnd) Auto-generated method stub
				throw new UnsupportedOperationException("Still not implemented. Help yourself");
			}

			@Override
			public Object[] toArray(Object[] a) {
				// TODO(frnd) Auto-generated method stub
				throw new UnsupportedOperationException("Still not implemented. Help yourself");
			}

			@Override
			public boolean retainAll(Collection c) {
				// TODO(frnd) Auto-generated method stub
				throw new UnsupportedOperationException("Still not implemented. Help yourself");
			}

		}

		@Override
		public boolean isEmpty() {
			return size() == 0;
		}

		@Override
		public Set<R> keySet() {
			return backingMap.keySet();
		}

		@Override
		public int size() {
			int size = 0;
			for (Entry<R, Map<C, Cell<T, V>>> entry : backingMap.entrySet()) {
				if (entry.getValue().containsKey(columnKey)) {
					size++;
				}
			}
			return size;
		}

		@Override
		public Collection<Cell<T, V>> values() {
			// TODO(frnd) avoid generation of a new list.
			List<Cell<T, V>> result = new ArrayList<Matrix.Cell<T, V>>();
			for (Map<C, Matrix.Cell<T, V>> value : backingMap.values()) {
				result.add(value.get(columnKey));
			}
			return result;
		}
	}

	@Override
	public Cell<T, V> totalColumn(C columnKey) {
		return totalRow.get(columnKey);
	}

	@Override
	public Map<C, Matrix.Cell<T, V>> row(R rowKey) {
		return new Row(rowKey);
	}

	private class Row extends InnerMap<C> {
		R	rowKey;

		public Row(R rowKey) {
			this.rowKey = rowKey;
		}

		@Override
		public int size() {
			return backingMap.get(rowKey).size();
		}

		@Override
		public boolean containsKey(Object key) {
			return backingMap.get(rowKey).containsKey(key);
		}

		@Override
		public Cell<T, V> get(Object key) {
			return backingMap.get(rowKey).get(key);
		}

		@Override
		public Set<C> keySet() {
			return backingMap.get(rowKey).keySet();
		}

		@Override
		public Collection<Cell<T, V>> values() {
			return backingMap.get(rowKey).values();
		}

		@Override
		public Set<Map.Entry<C, Matrix.Cell<T, V>>> entrySet() {
			return new EntrySet();
		}

		class EntrySet extends InnerMap<C>.EntrySet {

			@Override
			public int size() {
				return backingMap.get(rowKey).size();
			}

			@Override
			public Iterator<Map.Entry<C, Matrix.Cell<T, V>>> iterator() {
				return backingMap.get(rowKey).entrySet().iterator();
			}

			@Override
			public Object[] toArray() {
				return backingMap.get(rowKey).entrySet().toArray();
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return backingMap.get(rowKey).entrySet().toArray(a);
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				throw new UnsupportedOperationException();
			}

		}

	}

	@Override
	public Cell<T, V> totalRow(R rowKey) {
		return totalColumn.get(rowKey);
	}

	/**
	 * Generic map that throws {@link UnsupportedOperationException} for
	 * operations that are not available.
	 */
	private abstract class InnerMap<A> implements java.util.Map<A, Cell<T, V>> {

		abstract class EntrySet implements Set<Map.Entry<A, Cell<T, V>>> {

			@Override
			public boolean isEmpty() {
				return size() == 0;
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean add(Map.Entry<A, Cell<T, V>> arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean addAll(java.util.Collection<? extends Map.Entry<A, Matrix.Cell<T, V>>> c) {
				throw new UnsupportedOperationException();
			};

			/**
			 * This operation is not available.
			 */
			@Override
			public void clear() {
				throw new UnsupportedOperationException();
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean contains(Object arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean containsAll(Collection<?> arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean remove(Object arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * This operation is not available.
			 */
			@Override
			public boolean removeAll(Collection<?> arg0) {
				throw new UnsupportedOperationException();
			}

		}

		/**
		 * This operation is not available.
		 */
		@Override
		public boolean containsValue(Object arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEmpty() {
			return size() == 0;
		}

		/**
		 * This operation is not available.
		 */
		@Override
		public Cell<T, V> put(A key, Cell<T, V> value) {
			throw new UnsupportedOperationException();
		}

		/**
		 * This operation is not available.
		 */
		@Override
		public Cell<T, V> remove(Object key) {
			throw new UnsupportedOperationException();
		}

		/**
		 * This operation is not available.
		 */
		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}

		/**
		 * This operation is not available.
		 */
		@Override
		public void putAll(Map<? extends A, ? extends Cell<T, V>> arg0) {
			throw new UnsupportedOperationException();
		}

	}

	@Override
	public void clear() {
		backingMap.clear();
		allItems.clear();
		totalColumn.clear();
		totalRow.clear();
		total.clear();
	}

	@Override
	public Cell<T, V> put(T value) {
		// TODO (frnd) failure tolerance. 
		// If one step fails, roll-back the other.
		Cell<T, V> cell;
		R row;
		C column;

		row = getRowResolver().resolve(value);
		column = getColResolver().resolve(value);

		// Add the element to the specific total row
		cell = safeTotalRow(column);
		cell.getItems().add(value);
		cell.accumulate();

		// Add the element to the specific total column
		cell = safeTotalColumn(row);
		cell.getItems().add(value);
		cell.accumulate();

		// Add the element to the cell
		cell = safeGet(row, column);
		cell.getItems().add(value);
		cell.accumulate();
		
		allItems.add(value);

		return cell;
	}

	private Cell<T, V> safeGet(R rowKey, C columnKey) {
		Map<C, Cell<T, V>> row;
		Cell<T, V> cell;
		row = backingMap.get(rowKey);
		if (row == null) {
			row = createBackingMap();
			backingMap.put(rowKey, row);
		}
		cell = row.get(columnKey);
		if (cell == null) {
			cell = createCell();
			row.put(columnKey, cell);
		}

		return cell;
	}

	private Cell<T, V> safeTotalColumn(R columnKey) {
		Cell<T, V> total = totalColumn.get(columnKey);
		if (total == null) {
			total = createCell();
			totalColumn.put(columnKey, total);
		}
		return total;
	}

	private Cell<T, V> safeTotalRow(C columnKey) {
		Cell<T, V> total = totalRow.get(columnKey);
		if (total == null) {
			total = createCell();
			totalRow.put(columnKey, total);
		}
		return total;
	}

	@Override
	public void putAll(Collection<T> values) {
		// TODO (frnd) improve performance. 
		// Some items can fall in the same cell.
		for (T value : values) {
			put(value);
		}
	}

	@Override
	public void remove(T value) {
		// TODO (frnd) failure tolerance. 
		// If one step fails, roll-back the other.
		Cell<T, V> cell;
		R row;
		C column;

		if (!contains(value)) {
			return;
		}

		row = getRowResolver().resolve(value);
		column = getColResolver().resolve(value);

		cell = safeTotalRow(column);
		cell.getItems().remove(value);
		cell.accumulate();

		cell = safeTotalColumn(row);
		cell.getItems().remove(value);
		cell.accumulate();

		cell = get(row, column);
		cell.getItems().remove(value);
		cell.accumulate();
		
		allItems.remove(value);
	}

	@Override
	public void removeAll(Collection<T> values) {
		// TODO (frnd) improve performance. 
		// Some items can fall in the same cell.
		for (T value : values) {
			remove(value);
		}
	}

	@Override
	public Map<R, Map<C, Matrix.Cell<T, V>>> rowMap() {
		return backingMap;
	}

	@Override
	public Map<C, Map<R, Matrix.Cell<T, V>>> columnMap() {
		// TODO(frnd) Auto-generated method stub
		throw new UnsupportedOperationException("Still not implemented. Help yourself");
	}

	@Override
	public Set<Matrix.Cell<T, V>> cellSet() {
		return new AbstractSet<Matrix.Cell<T,V>>() {

			@Override
			public Iterator<es.frnd.matrix.Matrix.Cell<T, V>> iterator() {
				return new Itr();
			}
			
			class Itr implements Iterator<Matrix.Cell<T,V>> {
				// Will iterate over rows * columns
				Iterator<R> rowIterator = rowKeySet().iterator();
				Iterator<C> colIterator = columnKeySet().iterator();
				R row;
				C col;
				
				public Itr(){
					row = rowIterator.next();
				}
				
				@Override
				public boolean hasNext() {
					return rowIterator.hasNext() || colIterator.hasNext();
				}

				@Override
				public es.frnd.matrix.Matrix.Cell<T, V> next() {
					if(!hasNext()){
						throw new NoSuchElementException();
					} 
					if (!colIterator.hasNext()){
						row = rowIterator.next();
						colIterator = columnKeySet().iterator();
					}
					col = colIterator.next();
					return get(row, col);
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("You can not remove a cell of the table.");
				}
			};

			@Override
			public int size() {
				return rowKeySet().size() * columnKeySet().size();
			}
		};
	}

	@Override
	public Set<R> rowKeySet() {
		return backingMap.keySet();
	}

	@Override
	public Set<C> columnKeySet() {
		// TODO (frnd) better implementation.
		Set<C> keySet = new HashSet<C>();
		Collection<Map<C, Cell<T, V>>> values = backingMap.values();
		for (Map<C, Cell<T, V>> map : values) {
			keySet.addAll(map.keySet());
		}
		return keySet;
	}

	@Override
	public Collection<T> getItems() {
		return Collections.unmodifiableList(allItems);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("%10s |", ""));
		for (C column : columnKeySet()) {
			buffer.append(String.format("%10s |", column));
		}
		buffer.append("\n");
		for (R row : rowKeySet()) {
			buffer.append(String.format("%10s |", row));
			for (C column : columnKeySet()) {
				if (contains(row, column)) {
					buffer.append(String.format("%10s |", get(row, column).getValue().toString()));
				} else {
					buffer.append(String.format("%10s |", ""));
				}
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
