package es.frnd.tree;

import java.util.List;

public class Tree<T> extends Node<T> {

	protected Tree(List<T> elements, List<Resolver<T, ?>> resolvers) {
		super(null, elements, resolvers);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ROOT\n");
		for (Node<T> node : getChildren()) {
			buffer.append(node.toString(1));
		}
		return buffer.toString();
	}

}
