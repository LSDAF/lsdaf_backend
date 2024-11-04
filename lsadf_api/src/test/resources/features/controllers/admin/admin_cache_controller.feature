Feature: Admin Cache Controller BDD tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: Check if cache is enabled
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to get the cache status

    Then the response status code should be 200

    And the response should have the following Boolean true

  Scenario: Toggle cache status
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to toggle the cache status

    Then the response status code should be 200
    And the response should have the following Boolean false

  Scenario: Flush the cache
    Given the following game saves
      | id                                   | userEmail            | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com  | 1000 | 1000    | 1000    | 1000     | 100          | 10     | 1000         | 1000     |
    And the following currency entries in cache
      | gameSaveId                           | gold     | diamond  | emerald  | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 56302802 | 56302802 | 56302802 | 56302802 |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 99           | 100      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |
    And the user requests the admin endpoint to flush and clear the cache

    Then the response status code should be 200

    And the currency cache should be empty
    And the stage cache should be empty
    And the game_save_ownership cache should be empty