package es.frnd.matrix.impl.matrix;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import es.frnd.matrix.Matrix;
import es.frnd.matrix.Matrix.Cell;
import es.frnd.matrix.Matrix.Resolver;
import es.frnd.matrix.generic.GenericMatrix;

public class SimpleMatrixTest {

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
			return "Person [" + name + "]";
		}
		
		
	}

	/*
	 * Rows
	 */
	enum Sex {
		MALE, FEMALE
	}

	/*
	 * Column
	 */
	enum LegalDOBStatus {
		CHILDHOOD, ADULTHOOD
	}

	/*
	 * Column resolver.
	 */
	class SexResolver implements Resolver<Person, Sex> {

		public Sex resolve(Person person) {
			return person.sex;
		}
	}

	/*
	 * Row resolver.
	 */
	class DateResolver implements Resolver<Person, LegalDOBStatus> {

		private static final int	AMOUNTOFYEARS	= 18;

		public LegalDOBStatus resolve(Person person) {
			if(person.dob == null) {
				return null;
			}
			Calendar today = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(person.dob);
			calendar.add(Calendar.YEAR, AMOUNTOFYEARS);
			return calendar.after(today) ? LegalDOBStatus.CHILDHOOD : LegalDOBStatus.ADULTHOOD;
		}

	}

	class CountAcummulator implements Matrix.Accummulator<Person, Integer> {

		public Integer cumulate(List<Person> items) {
			return items != null ? items.size() : 0;
		}
	}

	@Test
	public void simpleMatrix() throws ParseException {
		List<Person> persons;
		Matrix<Sex, LegalDOBStatus, Person, Integer> matrix;

		DateFormat formatter;
		formatter = new SimpleDateFormat("dd-MM-yyyy");

		persons = new ArrayList<SimpleMatrixTest.Person>();
		persons.add(new Person("Fernando", formatter.parse("11-04-1979"), Sex.MALE));
		persons.add(new Person("Larisa", formatter.parse("30-10-2011"), Sex.FEMALE));
		persons.add(new Person("Isabel", formatter.parse("18-12-1985"), Sex.FEMALE));
		persons.add(new Person("Lolo", formatter.parse("18-12-1980"), null));
		persons.add(new Person("Lolailo", null, Sex.MALE));
		persons.add(new Person("LoLola", null, null));

		matrix = new GenericMatrix<Sex, LegalDOBStatus, Person, Integer>(new SexResolver(),
				new DateResolver(), new CountAcummulator());

		matrix.putAll(persons);
		
		System.out.println(matrix);

		Cell<Person, Integer> cell;
		cell = matrix.get(Sex.MALE, LegalDOBStatus.CHILDHOOD);
		assertNotNull(cell);
		assertNull(cell.getValue());

		cell = matrix.get(Sex.MALE, LegalDOBStatus.ADULTHOOD);
		assertNotNull(cell);
		assertEquals(new Integer(1), cell.getValue());

		cell = matrix.get(Sex.FEMALE, LegalDOBStatus.ADULTHOOD);
		assertNotNull(cell);
		assertEquals(new Integer(1), cell.getValue());

		cell = matrix.get(Sex.FEMALE, LegalDOBStatus.CHILDHOOD);
		assertNotNull(cell);
		assertEquals(new Integer(1), cell.getValue());
		
		Collection<Person> matrixItems = matrix.getItems();
		System.out.println(matrixItems);
		assertNotNull(matrixItems);
		assertEquals(persons.size(), matrixItems.size());
		assertArrayEquals(persons.toArray(), matrixItems.toArray());
		
		Set<Cell<Person,Integer>> cellSet = matrix.cellSet();
		assertEquals(9, cellSet.size());
		for (Cell<Person, Integer> c : cellSet) {
			System.out.println(c.getValue());
		}
		
		
	}
	
	@Test
	public void simpleMatrixCellSet() throws ParseException {
		List<Person> persons;
		Matrix<Sex, LegalDOBStatus, Person, Integer> matrix;

		DateFormat formatter;
		formatter = new SimpleDateFormat("dd-MM-yyyy");

		persons = new ArrayList<SimpleMatrixTest.Person>();
		persons.add(new Person("Fernando", formatter.parse("11-04-1979"), Sex.MALE));
		persons.add(new Person("Pepe", formatter.parse("11-04-1978"), Sex.MALE));

		matrix = new GenericMatrix<Sex, LegalDOBStatus, Person, Integer>(new SexResolver(),
				new DateResolver(), new CountAcummulator());

		matrix.putAll(persons);
		
		Set<Cell<Person,Integer>> cellSet = matrix.cellSet();
		assertEquals(1, cellSet.size());
		for (Cell<Person, Integer> c : cellSet) {
			System.out.println(c.getValue());
		}
	}
}
