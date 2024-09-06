package com.testing.api.stepDefinitions;

import com.github.javafaker.Faker;
import com.testing.api.models.Client;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client client;
    private String pastCellphone;
    private final Faker faker = new Faker();

    // TC 1

    @Given("there are at least ten registered clients:")
    public void thereAreAtLeastTenRegisteredClients(DataTable clientData){
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        if(clientList.size() < 10){
            Map<String, String> clientDataMap;
            for(int i =0;i<10; i++){
                clientDataMap = clientData.asMaps().get(i);
                client = Client.builder()
                        .id(clientDataMap.get("id"))
                        .name(clientDataMap.get("name"))
                        .lastName(clientDataMap.get("lastName"))
                        .country(clientDataMap.get("country"))
                        .city(clientDataMap.get("city"))
                        .email(clientDataMap.get("email"))
                        .phone(clientDataMap.get("phone"))
                        .build();
                response = clientRequest.createClient(client);
                Assert.assertEquals(response.statusCode(),201);
                logger.info("New client created");
            }
            logger.info("Creating 10 previous clients");
        }
        logger.info("10 clients or more inside the server");
    }

    @Given("I find the first client named Laura")
    public void iFindTheFIrstClientNamedLaura(){
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        boolean isLauraInside = false;
        for(Client client: clientList){
            if(Objects.equals(client.getName(), "Laura")){
                isLauraInside = true;
                this.client = client;
                this.pastCellphone = client.getPhone();
                break;
            }
        }
        Assert.assertTrue(isLauraInside);
    }

    @When("I update her phone number")
    public void iUpdateHerPhoneNumber(){
        String phone = faker.phoneNumber().cellPhone();
        client.setPhone(phone);
        response = clientRequest.updateClient(client,client.getId());
        logger.info("Phone updated:{}", phone);
    }

    @Then("I validate her new phone number is different")
    public void iValidateHerPhoneNumberIsDifferent(){
        client = clientRequest.getClientEntity(response);
        Assert.assertNotSame(pastCellphone,client.getPhone());
        logger.info("Phone is different:{}", client.getPhone());
    }

    @Then("the response should have an HTTP status code of {int}")
    public void theResponseShouldHaveAnHttpStatusCodeOf200(int statusCode){
        Assert.assertEquals(statusCode,response.statusCode());
        logger.info("Successfully validated the status code (200)");
    }

    @Then("the response body schema should be correct")
    public void theResponseBodySchemaShouldBeCorrect(){
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("I delete all the registered clients")
    public void iDeleteAllTheRegisteredClients(){
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        for(Client client: clientList){
            response = clientRequest.deleteClient(client.getId());
        }
        response = clientRequest.getClients();
        clientList = clientRequest.getClientsEntity(response);
        Assert.assertEquals(0,clientList.size());
        Assert.assertEquals(response.statusCode(),200);
        logger.info("Successfully deleted all clients");
    }

    // TC 3


    /*

    @Given("there are registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem() {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                            .prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);
        if (clientList.isEmpty()) {
            response = clientRequest.createDefaultClient();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps()
                                                      .get(0);
        client = Client.builder()
                       .name(clientDataMap.get("Name"))
                       .lastName(clientDataMap.get("LastName"))
                       .country(clientDataMap.get("Country"))
                       .city(clientDataMap.get("City"))
                       .build();
        logger.info("Client mapped: " + client);
    }

    @When("I retrieve the details of the client with ID {string}")
    public void sendGETRequest(String clientId) {
        response = clientRequest.getClient(clientId);
        logger.info(response.jsonPath()
                            .prettify());
        logger.info("The status code is: " + response.statusCode());
    }

    @When("I send a GET request to view all the clients")
    public void iSendAGETRequestToViewAllTheClient() {
        response = clientRequest.getClients();
    }

    @When("I send a POST request to create a client")
    public void iSendAPOSTRequestToCreateAClient() {
        response = clientRequest.createClient(client);
    }

    @When("I send a DELETE request to delete the client with ID {string}")
    public void iSendADELETERequestToDeleteTheClientWithID(String clientId) {
        response = clientRequest.deleteClient(clientId);
    }

    @When("I send a PUT request to update the client with ID {string}")
    public void iSendAPUTRequestToUpdateTheClientWithID(String clientId, String requestBody) {
        client = clientRequest.getClientEntity(requestBody);
        response = clientRequest.updateClient(client, clientId);
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                                                          .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("LastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("Country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("City"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("Id"), client.getId());
    }

    @Then("the response should include the details of the created client")
    public void theResponseShouldIncludeTheDetailsOfTheCreatedClient() {
        Client new_client = clientRequest.getClientEntity(response);
        new_client.setId(null);
        Assert.assertEquals(client, new_client);
    }

    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client List object");
    }
    */
}
