package es.frnd.tree;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Test;

public class TreeTest {

	@Test
	public void test() throws ParseException {

		List<Person> persons;
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd-MM-yyyy");

		persons = new ArrayList<Person>();
		persons.add(new Person("Fernando", formatter.parse("11-04-1979"), Sex.MALE));
		persons.add(new Person("Larisa", formatter.parse("30-10-2011"), Sex.FEMALE));
		persons.add(new Person("Isabel", formatter.parse("18-12-1985"), Sex.FEMALE));
		persons.add(new Person("Lolo", formatter.parse("18-12-1980"), null));
		persons.add(new Person("Lolailo", null, Sex.MALE));
		persons.add(new Person("Lolailo", null, null));

		Resolver<Person, Sex> firstLevel = new Resolver<Person, Sex>() {

			@Override
			public Sex resolve(Person item) {
				return item.sex;
			}
		};

		Resolver<Person, Integer> secondLevel = new Resolver<Person, Integer>() {

			@Override
			public Integer resolve(Person item) {
				if(item.dob == null){
					return null;
				}
				DateTime today = DateTime.now();
				Years yearsBetween = Years.yearsBetween(new DateTime(item.dob.getTime()), today);
				return yearsBetween.getYears();
			}
		};

		List<Resolver<Person, ?>> resolvers = new ArrayList<Resolver<Person, ?>>();
		resolvers.add(firstLevel);
		resolvers.add(secondLevel);
		Tree<Person> tree = new Tree<Person>(persons, resolvers);
		
		System.out.println(tree.toString());

		assertEquals(3, tree.childCount()); // First level
		Node<Person> node;
		node = tree.getChildren().get(0); // Male elements
		assertEquals(2, node.childCount());

		node = tree.getChildren().get(1); // Female elements
		assertEquals(2, node.childCount());
		
		node = tree.getChildren().get(2); // null elements
		assertEquals(2, node.childCount());
	}

	/*
	 * Bean to categorize
	 */
	class Person {

		String	name;
		Date	dob;
		Sex		sex;

		public Person(String name, Date dob, Sex sex) {
			this.name = name;
			this.dob = dob;
			this.sex = sex;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", dob=" + dob + ", sex=" + sex + "]";
		}
		
		
	}

	/*
	 * First Level
	 */
	enum Sex {
		MALE, FEMALE
	}

	/*
	 * Second Level
	 */
	enum LegalDOBStatus {
		CHILDHOOD, ADULTHOOD
	}
}
