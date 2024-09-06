@active
Feature: Client testing CRUD

  @smoke
  Scenario: Change the phone number of the first Client named Laura
    Given there are at least ten registered clients:
      | name   | lastName | country  | city       | email                   | phone        | id  |
      |--------|----------|----------|------------|-------------------------|--------------|-----|
      | Laura  | Munoz    | Colombia | Bogota     | lauramunoz@gmail.com    | 3229993210   | 100000000   |
      | John   | Doe      | USA      | New York   | johndoe@example.com     | 5551234567   | 200000000   |
      | Maria  | Lopez    | Spain    | Madrid     | marialopez@example.com  | 611223344    | 300000000   |
      | Ahmed  | Khan     | Pakistan | Karachi    | ahmedkhan@example.com   | 03001234567  | 400000000   |
      | Yuki   | Tanaka   | Japan    | Tokyo      | yukitanaka@example.com  | 08012345678  | 500000000   |
      | Priya  | Sharma   | India    | Mumbai     | priyasharma@example.com | 9876543210   | 600000000   |
      | Hans   | Müller   | Germany  | Berlin     | hansmuller@example.com  | 01761234567  | 700000000   |
      | Li     | Wei      | China    | Beijing    | liwei@example.com       | 13800138000  | 800000000   |
      | Anna   | Ivanova  | Russia   | Moscow     | annaivanova@example.com | 89161234567  | 900000000   |
      | Carlos | Gomez    | Mexico   | Mexico City| carlosgomez@example.com | 5512345678   | 1000000000  |
    And I find the first client named Laura
    When I update her phone number
    Then I validate her new phone number is different
    And the response should have an HTTP status code of 200
    And the response body schema should be correct
    And I delete all the registered clients