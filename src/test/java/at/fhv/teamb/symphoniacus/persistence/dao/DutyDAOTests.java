package at.fhv.teamb.symphoniacus.persistence.dao;

import at.fhv.teamb.symphoniacus.persistence.model.Duty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DutyDAOTests {

	private DutyDAO _dao;

	@BeforeAll
	public void init() {
		EntityManagerFactory fact = Persistence.createEntityManagerFactory("mysqldb");
		_dao = new DutyDAO(fact);
	}

	@Test
	public void testFindAllDuties() {
		List<Duty> list = _dao.findAll();
		Assertions.assertTrue(list != null);
	}
	@Test
	public void testFindAllDutiesForWeek() {
		List<Duty> list = _dao.findAllInWeek(LocalDate.of(2020, 03,30), LocalDate.of(2020,04,05));

		Assertions.assertTrue(list != null);
	}
}
