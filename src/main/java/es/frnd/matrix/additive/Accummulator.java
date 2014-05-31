package es.frnd.matrix.additive;

import es.frnd.matrix.Matrix;

public abstract class Accummulator<T, V> implements Matrix.Accumulator<T, V> {

	PropertyResolver<T, V> propertyResolver;
	
	public Accummulator(PropertyResolver<T, V> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
	
}
