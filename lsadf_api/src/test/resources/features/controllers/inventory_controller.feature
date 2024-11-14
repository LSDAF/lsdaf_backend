Feature: Inventory Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A requests its inventory when it is empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd should be empty

    When the user requests the endpoint to get the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

#    And the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd should be empty

# Do the same for a non empty inventory