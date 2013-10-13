package es.frnd.tree;

public interface Resolver<T, R extends Comparable<R>> {
	R resolve(T item);
}
