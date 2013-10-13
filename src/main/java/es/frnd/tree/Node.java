package es.frnd.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node<T> {

	private Object			data;

	private List<Node<T>>	children;

	protected Node(Object data, List<T> elements, List<Resolver<T, ?>> resolvers) {
		children = new ArrayList<Node<T>>();
		this.data = data;

		if (elements.isEmpty()) {
			return;
		}

		if (resolvers.isEmpty()) {
			// the elements are leaf
			for (T t : elements) {
				children.add(new Node<T>(t));
			}
		} else {

			Resolver<T, ?> resolver = resolvers.iterator().next();
			ResolverComparator rc = new ResolverComparator(resolver);
			Collections.sort(elements, rc);

			T current = elements.iterator().next();

			int startIndex = 0;
			int endIndex = 0;
			for (T next : elements) {
				if (rc.compare(current, next) != 0) {
					List<T> sublist = elements.subList(startIndex, endIndex);
					List<Resolver<T, ?>> nextResolvers = resolvers.subList(1, resolvers.size());
					Object childData = resolver.resolve(current);
					children.add(new Node<T>(childData, sublist, nextResolvers));
					current = next;
					startIndex = endIndex;
				}
				endIndex++;
			}
			if(startIndex < elements.size()){
				List<T> sublist = elements.subList(startIndex, endIndex);
				List<Resolver<T, ?>> nextResolvers = resolvers.subList(1, resolvers.size());
				Object childData = resolver.resolve(current);
				children.add(new Node<T>(childData, sublist, nextResolvers));
			}

		}
	}

	protected Node(T data) {
		this.data = data;
	}

	class ResolverComparator implements Comparator<T> {

		private final Resolver<T, ?>	resolver;

		public ResolverComparator(Resolver<T, ?> resolver) {
			this.resolver = resolver;
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public int compare(T o1, T o2) {
			Comparable resolved1 = resolver.resolve(o1);
			Comparable resolved2 = resolver.resolve(o2);
			if (resolved1 == null && resolved2 == null) {
				return 0;
			} else if (resolved1 != null && resolved2 == null) {
				return -1;
			} else if (resolved1 == null && resolved2 != null) {
				return 1;
			}
			return resolved1.compareTo(resolved2);
		}

	}

	/**
	 * Obtain the data contained on this node
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Get children list if this node.
	 * 
	 * @return
	 */
	public List<Node<T>> getChildren() {
		return children;
	}

	/**
	 * Count of elements.
	 * 
	 * @return
	 */
	public int childCount() {
		return getChildren() != null ? getChildren().size() : 0;
	}

	/**
	 * Return true if current node has no children.
	 */
	public boolean isLeaf() {
		return childCount() == 0;
	}

	@Override
	public String toString() {
		return "Node [data=" + data + ", childrenCount=" + childCount() + "]";
	}

	String toString(int tabSize) {
		char[] t = new char[tabSize];
		Arrays.fill(t, '\t');
		StringBuffer buffer = new StringBuffer();

		buffer.append(new String(t) + this.toString() + "\n");
		if (children != null) {
			for (Node<T> c : children) {
				buffer.append(c.toString(tabSize + 1));
			}
		}

		return buffer.toString();
	}

}
