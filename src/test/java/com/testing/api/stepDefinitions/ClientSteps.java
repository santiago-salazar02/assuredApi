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
        String phone = Constants.FAKER.phoneNumber().cellPhone();
        client.setPhone(phone);
        response = clientRequest.updateClient(client,client.getId());
        logger.info("Phone updated:{}", phone);
        logger.info(response.jsonPath()
                .prettify());
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

    @Then("the response client body schema should be correct")
    public void theResponseClientBodySchemaShouldBeCorrect(){
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


    @Given("a new client is created:")
    public void aNewClientIsCreated(DataTable clientData){
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
        Assert.assertEquals(response.statusCode(),201);
        client = clientRequest.getClientEntity(response);
        logger.info("New client created");
    }

    @When("I find the new client")
    public void iFindTheNewClient(){
        response = clientRequest.getClient(client.getId());
        Assert.assertEquals(response.statusCode(),200);
        logger.info("Client was found");
    }

    @When("I update any parameter of the new client")
    public void iUpdateAnyParameterOfTheNewClient(){
        String phone = Constants.FAKER.phoneNumber().cellPhone();
        client.setPhone(phone);
        response = clientRequest.updateClient(client,client.getId());
        logger.info("Client updated");
    }

    @Then("the response body data should reflect the updated parameter")
    public void theResponseBodyDataShouldReflectTheUpdatedParameter(){
        String newPhone = this.client.getPhone();
        response = clientRequest.getClient(client.getId());
        Client client = clientRequest.getClientEntity(response);
        Assert.assertEquals(newPhone,client.getPhone());
        logger.info("Updated parameter reflected");
        logger.info(response.jsonPath()
                .prettify());
    }

    @Then("I delete the created client")
    public void iDeleteTheCreatedClient(){
        response = clientRequest.deleteClient(client.getId());
        Assert.assertEquals(200,response.statusCode());
        logger.info("Client deleted");
    }
}
