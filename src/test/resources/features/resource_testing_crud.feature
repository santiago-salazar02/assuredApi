Feature: Resource testing CRUD

  @smoke
  Scenario: Find and update all active resources to inactive
    Given there are at least 5 active resources:
      | name | trademark | stock | price | description | tags | active |
      | a    | a         | 200   | 200   | waa         | a    | true   |
      | b    | b         | 150   | 300   | wbb         | b    | true   |
      | c    | c         | 100   | 250   | wcc         | c    | true   |
      | d    | d         | 50    | 400   | wdd         | d    | true   |
      | e    | e         | 75    | 350   | wee         | e    | true   |
    When I find all active resources
    Then I update all found resources to inactive

  @smoke
  Scenario: Update the latest created resource
    Given there are at least 15 resources:
      | name | trademark | stock | price | description | tags | active |
      | a    | a         | 200   | 200   | waa         | a    | true   |
      | b    | b         | 150   | 300   | wbb         | b    | true   |
      | c    | c         | 100   | 250   | wcc         | c    | false  |
      | d    | d         | 50    | 400   | wdd         | d    | true   |
      | e    | e         | 75    | 350   | wee         | e    | false  |
      | f    | f         | 60    | 180   | wff         | f    | true   |
      | g    | g         | 90    | 220   | wgg         | g    | true   |
      | h    | h         | 120   | 270   | whh         | h    | false  |
      | i    | i         | 110   | 190   | wii         | i    | true   |
      | j    | j         | 130   | 210   | wjj         | j    | true   |
      | k    | k         | 80    | 240   | wkk         | k    | false  |
      | l    | l         | 140   | 310   | wll         | l    | true   |
      | m    | m         | 70    | 230   | wmm         | m    | true   |
      | n    | n         | 65    | 260   | wnn         | n    | false  |
      | o    | o         | 85    | 280   | woo         | o    | true   |
    When I find the latest created resource
    And I update all the parameters of this resource
    Then the status code should be 200
    And the response body schema should be valid
    And the response body data should reflect the updates