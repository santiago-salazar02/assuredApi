package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.requests.ResourceRequest;
import com.testing.api.utils.Constants;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Step definitions for resource-related Cucumber scenarios.
 */
public class ResourceStep {
    private static final Logger logger = LogManager.getLogger(ResourceStep.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;
    private List<Resource> activeResources = new ArrayList<Resource>();

    /**
     * Ensures that there are at least the specified number of active resources.
     *
     * @param amountResources the minimum number of active resources required
     * @param resourceData    the data table containing resource information
     */
    @Given("there are at least {int} active resources:")
    public void thereAreAtLeastXActiveResources(int amountResources, DataTable resourceData) {
        String path = "schemas/resourceListSchema.json";
        response = resourceRequest.getResources();
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        List<Resource> createResources = new ArrayList<Resource>();
        int activeResources = 0;
        for (Resource resource : resources) {
            if (resource.isActive()) {
                activeResources++;
            }
        }
        amountResources -= resources.size();
        Map<String, String> resourceDataMap;
        for (int i = 0; i < amountResources; i++) {
            resourceDataMap = resourceData.asMaps().get(i);
            resource = Resource.builder()
                    .name(resourceDataMap.get("name"))
                    .trademark(resourceDataMap.get("trademark"))
                    .stock(Long.parseLong(resourceDataMap.get("stock")))
                    .price(Double.parseDouble(resourceDataMap.get("price")))
                    .description(resourceDataMap.get("description"))
                    .tags(resourceDataMap.get("tags"))
                    .active(Boolean.parseBoolean(resourceDataMap.get("active")))
                    .build();
            createResources.add(resource);
        }
        response = resourceRequest.createResources(createResources);
        Assert.assertEquals(response.statusCode(), 201);
        logger.info("New resources created");
    }

    /**
     * Finds all active resources.
     */
    @When("I find all active resources")
    public void iFindAllActiveResources() {
        String path = "schemas/resourceListSchema.json";
        response = resourceRequest.getResources();
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
        Assert.assertEquals(response.statusCode(), 200);
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        for (Resource resource : resources) {
            if (resource.isActive()) {
                this.activeResources.add(resource);
            }
        }
        logger.info("All active resources found");
    }

    /**
     * Updates all found active resources to inactive.
     */
    @Then("I update all found resources to inactive")
    public void iUpdateAllFoundResourcesToInactive() {
        String path = "schemas/resourceSchema.json";
        for (Resource resource : activeResources) {
            resource.setActive(false);
            response = resourceRequest.updateResource(resource, resource.getId());
            Assert.assertEquals(200, response.statusCode());

            Assert.assertTrue(resourceRequest.validateSchema(response, path));
            logger.info("Successfully Validated schema from Client object");
        }
        logger.info("Active resources to inactive");
    }

    /**
     * Ensures that there are at least the specified number of resources.
     *
     * @param amountResources the minimum number of resources required
     * @param resourceData    the data table containing resource information
     */
    @Given("there are at least {int} resources:")
    public void thereAreAtLeast15Resources(int amountResources, DataTable resourceData) {
        String path = "schemas/resourceListSchema.json";
        response = resourceRequest.getResources();
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        List<Resource> createResources = new ArrayList<Resource>();
        amountResources -= resources.size();
        Map<String, String> resourceDataMap;
        for (int i = 0; i < amountResources; i++) {
            resourceDataMap = resourceData.asMaps().get(i);
            resource = Resource.builder()
                    .name(resourceDataMap.get("name"))
                    .trademark(resourceDataMap.get("trademark"))
                    .stock(Long.parseLong(resourceDataMap.get("stock")))
                    .price(Double.parseDouble(resourceDataMap.get("price")))
                    .description(resourceDataMap.get("description"))
                    .tags(resourceDataMap.get("tags"))
                    .active(Boolean.parseBoolean(resourceDataMap.get("active")))
                    .build();
            createResources.add(resource);
        }
        response = resourceRequest.createResources(createResources);
        Assert.assertEquals(response.statusCode(), 201);
        logger.info("New resources created");
    }

    /**
     * Finds the latest created resource.
     */
    @When("I find the latest created resource")
    public void iFindTheLatestCreatedResource() {
        response = resourceRequest.getResources();
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        resource = resources.get(resources.size() - 1);
    }

    /**
     * Updates all parameters of the latest created resource.
     */
    @When("I update all the parameters of this resource")
    public void iUpdateAllTheParametersOfThisResource() {
        resource.setName(Constants.FAKER.name().name());
        resource.setTrademark(Constants.FAKER.name().name());
        resource.setStock(Constants.FAKER.number().randomDigit());
        resource.setPrice(Constants.FAKER.number().randomDouble(3, 1, 30));
        resource.setDescription(Constants.FAKER.artist().name());
        resource.setTags(Constants.FAKER.address().country());
        resource.setActive(true);
        response = resourceRequest.updateResource(resource, resource.getId());
    }

    /**
     * Validates the response status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the status code should be {int}")
    public void theStatusCodeShouldBex(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    /**
     * Validates the schema of the response body.
     */
    @Then("the response body schema should be valid")
    public void theResponseBodySchemaShouldBeValid() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    /**
     * Validates that the response body data reflects the updates.
     */
    @Then("the response body data should reflect the updates")
    public void theResponseBodyDataShouldReflectTheUpdates() {
        response = resourceRequest.getResource(resource.getId());
        Resource updatedResource = resourceRequest.getResourceEntity(response);

        Assert.assertEquals(updatedResource.getName(), this.resource.getName());
        Assert.assertEquals(updatedResource.getTrademark(), this.resource.getTrademark());
        Assert.assertEquals(updatedResource.getStock(), this.resource.getStock());
        Assert.assertEquals(updatedResource.getPrice(), this.resource.getPrice(), 0.01);
        Assert.assertEquals(updatedResource.getDescription(), this.resource.getDescription());
        Assert.assertEquals(updatedResource.getTags(), this.resource.getTags());
        Assert.assertEquals(updatedResource.isActive(), this.resource.isActive());

        logger.info("Response body data validated to reflect updates");
    }
}