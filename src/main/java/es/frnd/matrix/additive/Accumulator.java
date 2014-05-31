package es.frnd.matrix.additive;

import es.frnd.matrix.Matrix;

public abstract class Accumulator<T, V> implements Matrix.Accumulator<T, V> {

	PropertyResolver<T, V> propertyResolver;
	
	public Accumulator(PropertyResolver<T, V> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
	
}
