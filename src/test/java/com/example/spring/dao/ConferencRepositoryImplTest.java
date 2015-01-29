package com.example.spring.dao;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.spring.model.Address;
import com.example.spring.model.Conference;
import com.example.spring.model.QConference;
//import com.example.spring.model.QSignup;
import com.example.spring.model.Signup;
import com.example.spring.model.Status;
import com.example.spring.mongo.ConferenceRepository;
import com.example.spring.mongo.SignupRepository;
//import com.mysema.query.mongodb.JoinBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/example/spring/config/applicationContext-mongo.xml")
public class ConferencRepositoryImplTest {
	private static final Logger log = LoggerFactory
			.getLogger(ConferencRepositoryImplTest.class);

	@Autowired
	private ConferenceRepository conferenceRepository;

	@Autowired
	private SignupRepository signupRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	private Conference newConference() {
		Conference conf = new Conference();
		conf.setName("JUD2013");
		conf.setSlug("jud-2013");
		conf.setDescription("JBoss User Developer Conference 2013 Boston");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 30);

		Date startedDate = cal.getTime();

		conf.setStartedDate(startedDate);

		cal.add(Calendar.DAY_OF_YEAR, 7);

		Date endedDate = cal.getTime();
		conf.setEndedDate(endedDate);

		conf.setAddress(newAddress());

		log.debug("new conference object:" + conf);
		return conf;
	}

	private Address newAddress() {
		Address address = new Address();
		address.setAddressLine1("address line 1");
		address.setAddressLine2("address line 2");
		address.setCity("NY");
		address.setCountry("CN");
		address.setZipCode("510000");

		return address;
	}

	private Signup newSignup() {
		Signup signup = new Signup();

		signup.setComment("test comments");
		signup.setCompany("TestCompany");
		signup.setCreatedDate(new Date());
		signup.setEmail("test@test.com");
		signup.setFirstName("Hantsy");
		signup.setLastName("Bai");
		signup.setOccupation("Developer");
		signup.setPhone("123 222 444");
		signup.setStatus(Status.PENDING);

		return signup;
	}

	@BeforeClass
	public static void init() {
		log.debug("==================before class=========================");

	}

	@Before
	public void beforeTestCase() {
		log.debug("==================before test case=========================");
		conferenceRepository.save(newConference());
	}

	@After
	public void afterTestCase() {
		log.debug("==================after test case=========================");
		signupRepository.deleteAll();
		conferenceRepository.deleteAll();
	}

	@Test
	public void retrieveConference() {
		Conference conference = newConference();
		conference.setSlug("test-jud");
		conference.setName("Test JUD");
		conference.getAddress().setCountry("US");
		conference = conferenceRepository.save(conference);

		assertTrue(null != conference.getId());

		conference = conferenceRepository.findBySlug("test-jud");
		assertTrue(null != conference);

		List<Conference> confs = conferenceRepository
				.findByAddressCountry("CN");
		log.debug("findByAddressCountry@" + confs.size());
		assertTrue(1 == confs.size());

		confs = conferenceRepository.searchByDescription("Boston");
		log.debug("searchByDescription@" + confs.size());
		assertTrue(2 == confs.size());

		confs = conferenceRepository.findByDescriptionLike("Boston");
		log.debug("findByDescriptionLike@" + confs.size());
		assertTrue(2 == confs.size());

		confs = conferenceRepository.findByDescriptionRegex(".*Boston.*");
		log.debug("findByDescriptionRegex@" + confs.size());
		assertTrue(2 == confs.size());

		Signup signup = newSignup();
		signup.setConference(conference);

		signupRepository.save(signup);

		assertTrue(null != signup.getId());

		List<Signup> signups = signupRepository.findByConference(conference);
		assertTrue(1 == signups.size());

		Signup signup2 = newSignup();
		signup2.setComment("another comments");
		signup2.setConference(conference);

		signupRepository.save(signup2);

		assertTrue(null != signup2.getId());

		List<Signup> signups2 = signupRepository.findByConference(conference);
		assertTrue(2 == signups2.size());

	}

	@Test
	public void updateConference() {
		Conference conference = newConference();
		conference.setSlug("test-jud");
		conference.setName("Test JUD");
		conference.getAddress().setCountry("US");
		conference = conferenceRepository.save(conference);

		String cid = conference.getId();

		log.debug("saved conference id@" + cid);
		assertTrue(null != cid);

		conference = conferenceRepository.findBySlug("test-jud");
		assertTrue(null != conference);

		conferenceRepository.updateConferenceDescription("MyDesc", cid);

		Conference conf = conferenceRepository.findOne(cid);

		assertTrue("MyDesc".equals(conf.getDescription()));

	}

	@Test
	public void retrieveConferenceByQueryDSL() {
		Conference conference = newConference();
		conference.setSlug("test-jud");
		conference.setName("Test JUD");
		conference.getAddress().setCountry("US");
		conference = conferenceRepository.save(conference);
		log.debug("conference @" + conference);
		String cid = conference.getId();

		log.debug("saved conference id @" + cid);
		assertTrue(null != cid);

		QConference qconf = QConference.conference;
		List<Conference> conferences = (List<Conference>) conferenceRepository
				.findAll(qconf.slug.eq("test-jud"));
		log.debug("conferences.size()@" + conferences.size());
		assertTrue(1 == conferences.size());

		List<Conference> conferences2 = (List<Conference>) conferenceRepository
				.findAll(QConference.conference.address.country.eq("CN"));
		log.debug("conferences2.size()@" + conferences2.size());
		assertTrue(1 == conferences2.size());

		Conference conf = conferenceRepository.findOne(cid);

		Signup signup = newSignup();
		signup.setConference(conf);

		signup = signupRepository.save(signup);
		log.debug("signup @" + signup);

		assertTrue(null != signup.getId());

		List<Signup> signups = mongoTemplate.find(
				Query.query(Criteria.where("conference").is(conf)),
				Signup.class);
		// List<Signup> signups = (List<Signup>) signupRepository
		// .findAll(QSignup.signup.conference.eq(conf));
		//

		log.debug("signups.size()@" + signups.size());
		assertTrue(1 == signups.size());

		Signup signup2 = newSignup();
		signup2.setComment("another comments");
		signup2.setConference(conf);

		signup2 = signupRepository.save(signup2);
		log.debug("signup2 @" + signup2);

		assertTrue(null != signup2.getId());

		// List<Signup> signups2 = (List<Signup>) signupRepository
		// .findAll(QSignup.signup.conference.eq(conf));
		List<Signup> signups2 = mongoTemplate.find(
				Query.query(Criteria.where("conference").is(conf)),
				Signup.class);
		log.debug("signups2.size()@" + signups2.size());
		assertTrue(2 == signups2.size());

	}

	DelegatingSpringDataMongodbQuery<Signup> signupQuery;

	@Test
	public void testMongodbQuery() {

		Conference conference = newConference();
		conference.setSlug("test-jud");
		conference.setName("Test JUD");
		conference.getAddress().setCountry("US");
		conference = conferenceRepository.save(conference);
		log.debug("conference @" + conference);
		String cid = conference.getId();

		log.debug("saved conference id @" + cid);
		assertTrue(null != cid);

		Conference conf = conferenceRepository.findOne(cid);

		Signup signup = newSignup();
		signup.setConference(conf);

		signup = signupRepository.save(signup);
		log.debug("signup @" + signup);

		signupQuery = new DelegatingSpringDataMongodbQuery<Signup>(
				mongoTemplate, Signup.class);

		// TODO does not work as expected.

		// List<Signup> signups=
		// signupQuery.where(QSignup.signup.id.isNotEmpty())
		// .join(QSignup.signup.conference,
		// QConference.conference).on(QConference.conference.eq(conf))
		// .list();
		//
		// log.debug("signups.size()@" + signups.size());
		// assertTrue(1 == signups.size());
	}
}
