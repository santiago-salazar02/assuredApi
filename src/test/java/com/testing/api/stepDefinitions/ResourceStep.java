package com.testing.api.stepDefinitions;

import com.testing.api.models.Resource;
import com.testing.api.utils.Constants;
import com.testing.api.requests.ResourceRequest;
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
import java.util.Objects;

public class ResourceStep {
    private static final Logger logger = LogManager.getLogger(ResourceStep.class);

    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;
    private List<Resource> activeResources = new ArrayList<Resource>();

    @Given("there are at least {int} active resources:")
    public void thereAreAtLeastXActiveResources(int amountResources,DataTable resourceData){
        String path = "schemas/resourceListSchema.json";
        response = resourceRequest.getResources();
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        List<Resource> createResources = new ArrayList<Resource>();
        int activeResources = 0;
        for(Resource resource: resources){
            if(resource.isActive()){
                activeResources++;
            }
        }
        amountResources -= resources.size();
        Map<String, String> resourceDataMap;
        for(int i = 0; i < amountResources; i++){
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
        Assert.assertEquals(response.statusCode(),201);
        logger.info("New resources created");
    }

    @When("I find all active resources")
    public void iFindAllActiveResources(){
        String path = "schemas/resourceListSchema.json";
        response = resourceRequest.getResources();
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
        Assert.assertEquals(response.statusCode(),200);
        List<Resource> resources = resourceRequest.getResourcesEntity(response);
        for(Resource resource: resources){
            if(resource.isActive()){
                this.activeResources.add(resource);
            }
        }
        logger.info("All active resources found");
    }

    @Then("I update all found resources to inactive")
    public void iUpdateAllFoundResourcesToInactive(){
        String path = "schemas/resourceSchema.json";
        for(Resource resource: activeResources){
            resource.setActive(false);
            response = resourceRequest.updateResource(resource,resource.getId());
            Assert.assertEquals(200,response.statusCode());

            Assert.assertTrue(resourceRequest.validateSchema(response, path));
            logger.info("Successfully Validated schema from Client object");
        }
        logger.info("Active resources to inactive");
    }



}
