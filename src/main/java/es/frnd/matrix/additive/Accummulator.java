package es.frnd.matrix.additive;

public abstract class Accummulator<T, V> implements es.frnd.matrix.Matrix.Accummulator<T, V>{

	PropertyResolver<T, V> propertyResolver;
	
	public Accummulator(PropertyResolver<T, V> propertyResolver) {
		this.propertyResolver = propertyResolver;
	}
	
}
