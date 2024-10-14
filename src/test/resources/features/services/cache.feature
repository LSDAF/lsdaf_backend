Feature: Cache Service tests

  Background:
    Given the BDD engine is ready
    And the expiration seconds properties set to -1
    And a clean database
    And the time clock set to the present

  Scenario: Flush Cache Service
    Given the following users
      | id                                   | name       | email               | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | emerald | amethyst | diamond | currentStage | maxStage | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 100     | 200      | 300     | 520          | 520      | 500          | 1072   |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 2       | 200     | 300      | 400     | 69800        | 69800    | 26           | 770    |
    And the following currency entries in cache
      | gameSaveId                           | gold   | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 | 200     | 300     | 400      |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 80000  | 300     | 400     | 500      |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 520          | 530      |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 69885        | 70000    |
    When the cache is flushed
    And I should have the following game saves in DB
      | id                                   | userId                               | gold   | emerald | amethyst | diamond | currentStage | maxStage | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 600000 | 300     | 400      | 200     | 520          | 530      | 500          | 1072   |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 80000  | 400     | 500      | 300     | 69885        | 70000    | 26           | 770    |


  Scenario: Currency Key Expiration
    Given the expiration seconds properties set to 5
    And the following users
      | id                                   | name       | email               | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | maxStage | currentStage | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 1       | 3       | 5        | 7        | 5            | 500          | 1072   |
    And the following currency entries in cache
      | gameSaveId                           | gold   | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 | 600000  | 600000  | 600000   |
    When a currency cache entry is expired

    Then the currency cache should be empty

    And the currency_histo cache should be empty

    And I should have the following game saves in DB
      | id                                   | userId                               | gold   | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 600000 | 600000  | 600000  | 600000   | 500          | 1072   | 5            | 7        |

  Scenario: Stage Key Expiration
    Given the expiration seconds properties set to 5
    And the following users
      | id                                   | name       | email               | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | currentStage | maxStage | healthPoints | attack | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280      | 5630280  | 500          | 1072   | 100  | 100     | 100     | 100      |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 6000000      | 6000000  |
    When a stage cache entry is expired

    Then the stage cache should be empty

    And the stage_histo cache should be empty

    And I should have the following game saves in DB
      | id                                   | userId                               | currentStage | maxStage | healthPoints | attack | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 6000000      | 6000000  | 500          | 1072   | 100  | 100     | 100     | 100      |