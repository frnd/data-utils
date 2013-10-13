package es.frnd.matrix.additive;

public interface PropertyResolver<T, V> {
	V getValue(T bean);
}
