package sf.example.data;

import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import sf.example.TestConfiguration;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration(classes = TestConfiguration.class)
public class JobTestStepDefs {
	@Autowired
	private JobRepository jobRepository;

	private Job createdJob;
	private Job searchedJob;

	@Given("^there is a Job that pays between (\\d+) and (\\d+) called (.*)$")
	public void thereIsAJob(int minPay, int maxPay, String title) {
		createdJob = jobRepository.save(new Job(title, minPay, maxPay));
	}

	@When("^I search for a Job with a title of (.*)$")
	public void searchForAJob(String title) {
		searchedJob = jobRepository.findByTitle(title).get(0);
	}

	@Then("^the Jobs should be the same$")
	public void theJobsShouldBeTheSame() {
		assertReflectionEquals(createdJob, searchedJob);
	}

	@Then("^the Job should pay at least (\\d+)$")
	public void minPay(int minPay) {
		assertEquals(minPay, searchedJob.getMinPay(), 0);
	}

	@Then("^the Job should pay at most (\\d+)$")
	public void maxPay(int maxPay) {
		assertEquals(maxPay, searchedJob.getMaxPay(), 0);
	}

	@Then("^the Job title should be (.*)$")
	public void title(String title) {
		assertEquals(title, searchedJob.getTitle());
	}

	@Then("^delete the Job$")
	public void delete() {
		jobRepository.delete(searchedJob);
	}

	@Then("^the Job should be deleted$")
	public void jobShouldBeDeleted() {
		assertEquals(null, jobRepository.findOne(searchedJob.getId()));
	}

	@Then("^change the (minimum|maximum) pay of the Job to (\\d+)$")
	public void changePay(String minMax, int newPay) {
		if ("minimum".equals(minMax)) {
			searchedJob.setMinPay(newPay);
		} else {
			searchedJob.setMaxPay(newPay);
		}
	}
	
	@Then("^change the title of the Job to (.*)$")
	public void changeTitle(String title) {
		searchedJob.setTitle(title);
	}

	@After
	public void cleanup() {
		jobRepository.delete(jobRepository.findAll());
	}
}
