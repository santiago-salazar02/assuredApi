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