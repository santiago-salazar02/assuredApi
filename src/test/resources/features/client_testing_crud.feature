@active
Feature: Client testing CRUD

  @smoke
  Scenario: Change the phone number of the first Client named Laura
    Given there are at least 10 registered clients:
      | name   | lastName | country  | city       | email                   | phone |
      | Laura  | Buenavista    | Colombia | Medellin     | laurita@gmail.com    | 3229994324 |
      | John   | Doe      | USA      | New York   | johndoe@example.com     | 5551234567     |
      | Maria  | Lopez    | Spain    | Madrid     | marialopez@example.com  | 611223344      |
      | Ahmed  | Khan     | Pakistan | Karachi    | ahmedkhan@example.com   | 03001234567    |
      | Yuki   | Tanaka   | Japan    | Tokyo      | yukitanaka@example.com  | 08012345678    |
      | Priya  | Sharma   | India    | Mumbai     | priyasharma@example.com | 9876543210     |
      | Hans   | Müller   | Germany  | Berlin     | hansmuller@example.com  | 01761234567    |
      | Li     | Wei      | China    | Beijing    | liwei@example.com       | 13800138000    |
      | Anna   | Ivanova  | Russia   | Moscow     | annaivanova@example.com | 89161234567    |
      | Carlos | Gomez    | Mexico   | Mexico City| carlosgomez@example.com | 5512345678     |
    And I find the first client named Laura
    When I update her phone number
    Then I validate her new phone number is different
    And the response should have an HTTP status code of 200
    And the response client body schema should be correct
    And I delete all the registered clients

  @smoke
  Scenario: Update and delete a new client
    Given a new client is created:
      | name   | lastName | country  | city       | email                   | phone |
      | Moises  | Buenavista    | Colombia | Medellin     | moisesowo@gmail.com    | 3229994324 |
    When I find the new client
    And I update any parameter of the new client
    Then the response should have an HTTP status code of 200
    And the response client body schema should be correct
    And the response body data should reflect the updated parameter
    And I delete the created client