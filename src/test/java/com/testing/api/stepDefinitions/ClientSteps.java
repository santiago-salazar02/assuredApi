package com.testing.api.stepDefinitions;

import com.testing.api.models.Client;
import com.testing.api.utils.Constants;
import com.testing.api.requests.ClientRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Step definitions for client-related Cucumber scenarios.
 */
public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);

    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
    private Client client;
    private String pastCellphone;

    /**
     * Ensures that there are at least the specified number of registered clients.
     *
     * @param quantityClients the minimum number of clients required
     * @param clientData      the data table containing client information
     */
    @Given("there are at least {int} registered clients:")
    public void thereAreAtLeastTenRegisteredClients(int quantityClients, DataTable clientData) {
        response = clientRequest.getClients();

        String path = "schemas/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");

        // Create until 10 clients inside API
        List<Client> clientList = clientRequest.getClientsEntity(response);
        quantityClients -= clientList.size();

        Map<String, String> clientDataMap;

        for (int i = 0; i < quantityClients; i++) {
            clientDataMap = clientData.asMaps().get(i);
            client = Client.builder()
                    .name(clientDataMap.get("name"))
                    .lastName(clientDataMap.get("lastName"))
                    .country(clientDataMap.get("country"))
                    .city(clientDataMap.get("city"))
                    .email(clientDataMap.get("email"))
                    .phone(clientDataMap.get("phone"))
                    .build();

            response = clientRequest.createClient(client);
            Assert.assertEquals(response.statusCode(), 201);
            logger.info("New clients created");
        }

        logger.info("10 clients or more inside the server");
    }

    /**
     * Finds the first client named Laura.
     */
    @Given("I find the first client named Laura")
    public void iFindTheFIrstClientNamedLaura() {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        boolean isLauraInside = false;
        for (Client client : clientList) {
            if (Objects.equals(client.getName(), "Laura")) {
                isLauraInside = true;
                this.client = client;
                this.pastCellphone = client.getPhone();
                break;
            }
        }
        Assert.assertTrue(isLauraInside);
        logger.info("Laura is inside");
    }

    /**
     * Updates the phone number of the client.
     */
    @When("I update her phone number")
    public void iUpdateHerPhoneNumber() {
        String phone = Constants.FAKER.phoneNumber().cellPhone();
        client.setPhone(phone);
        response = clientRequest.updateClient(client, client.getId());
        logger.info("Phone updated:{}", phone);
        logger.info(response.jsonPath().prettify());
    }

    /**
     * Validates that the new phone number is different from the old one.
     */
    @Then("I validate her new phone number is different")
    public void iValidateHerPhoneNumberIsDifferent() {
        client = clientRequest.getClientEntity(response);
        Assert.assertNotSame(pastCellphone, client.getPhone());
        logger.info("Phone is different:{}", client.getPhone());
    }

    /**
     * Validates the response status code.
     *
     * @param statusCode the expected status code
     */
    @Then("the response should have an HTTP status code of {int}")
    public void theResponseShouldHaveAnHttpStatusCodeOf200(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
        logger.info("Successfully validated the status code (200)");
    }

    /**
     * Validates the schema of the response client body.
     */
    @Then("the response client body schema should be correct")
    public void theResponseClientBodySchemaShouldBeCorrect() {
        String path = "schemas/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    /**
     * Deletes all the registered clients.
     */
    @Then("I delete all the registered clients")
    public void iDeleteAllTheRegisteredClients() {
        response = clientRequest.getClients();
        List<Client> clientList = clientRequest.getClientsEntity(response);
        for (Client client : clientList) {
            response = clientRequest.deleteClient(client.getId());
        }
        response = clientRequest.getClients();
        clientList = clientRequest.getClientsEntity(response);
        Assert.assertEquals(0, clientList.size());
        Assert.assertEquals(response.statusCode(), 200);
        logger.info("Successfully deleted all clients");
    }

    /**
     * Creates a new client.
     *
     * @param clientData the data table containing client information
     */
    @Given("a new client is created:")
    public void aNewClientIsCreated(@NotNull DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps().get(0);
        client = Client.builder()
                .name(clientDataMap.get("name"))
                .lastName(clientDataMap.get("lastName"))
                .country(clientDataMap.get("country"))
                .city(clientDataMap.get("city"))
                .email(clientDataMap.get("email"))
                .phone(clientDataMap.get("phone"))
                .build();
        response = clientRequest.createClient(client);
        Assert.assertEquals(response.statusCode(), 201);
        client = clientRequest.getClientEntity(response);
        logger.info("New client created");
    }

    /**
     * Finds the new client.
     */
    @When("I find the new client")
    public void iFindTheNewClient() {
        response = clientRequest.getClient(client.getId());
        Assert.assertEquals(response.statusCode(), 200);
        logger.info("Client was found");
    }

    /**
     * Updates any parameter of the new client.
     */
    @When("I update any parameter of the new client")
    public void iUpdateAnyParameterOfTheNewClient() {
        String phone = Constants.FAKER.phoneNumber().cellPhone();
        client.setPhone(phone);
        response = clientRequest.updateClient(client, client.getId());
        logger.info("Client updated");
    }

    /**
     * Validates that the response body data reflects the updated parameter.
     */
    @Then("the response body data should reflect the updated parameter")
    public void theResponseBodyDataShouldReflectTheUpdatedParameter() {
        String newPhone = this.client.getPhone();
        response = clientRequest.getClient(client.getId());
        Client client = clientRequest.getClientEntity(response);
        Assert.assertEquals(newPhone, client.getPhone());
        logger.info("Updated parameter reflected");
        logger.info(response.jsonPath().prettify());
    }

    /**
     * Deletes the created client.
     */
    @Then("I delete the created client")
    public void iDeleteTheCreatedClient() {
        response = clientRequest.deleteClient(client.getId());
        Assert.assertEquals(200, response.statusCode());
        logger.info("Client deleted");
    }
}