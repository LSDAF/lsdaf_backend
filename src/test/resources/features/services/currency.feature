Feature: Currency Service tests

  Background:
    Given the BDD engine is ready
    And the expiration seconds properties set to -1
    And a clean database
    And the time clock set to the present

  Scenario: Get Currency with cache
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | false    |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    And the following currency entries in cache
      | gameSaveId                           | gold   | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 | 3000    | 4000    | 2000     |
    When we want to get the currencies for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
    Then the currency should be the following
      | gold   | diamond | emerald | amethyst |
      | 600000 | 3000    | 4000    | 2000     |

  Scenario: Get Currency without cache
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    And the following currency entries in cache
      | gameSaveId | gold | diamond | emerald | amethyst |
    When we want to get the currencies for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
    Then the currency should be the following
      | gold    | diamond | emerald | amethyst |
      | 5630280 | 0       | 0       | 0        |

  Scenario: Get Currency from non-existing game save
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    When we want to get the currencies for the game save with id ade63373-fdc9-4fdc-aea9-6100d6f36dae
    Then I should throw a NotFoundException

  Scenario: Set Currency with cache
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 500  |         |         |          |
    When we want to set the following currencies for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 with toCache to true
      | gold | diamond | emerald | amethyst |
      | 200  | 300     |         |          |
    Then I should have the following currency entries in cache
      | gameSaveId                           | gold | diamond |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 200  | 300     |
    And I should have the following game saves in DB
      | id                                   | userId                               | gold    | diamond | emerald | amethyst |  | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        |  | 500          | 1072   |

  Scenario: Set Currency without cache
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    When we want to set the following currencies for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 with toCache to false
      | gold | diamond |
      | 200  | 300     |
    Then I should have the following currency entries in cache
      | gameSaveId | gold | diamond |

    And I should have the following game saves in DB
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 200  | 300     | 0       | 0        | 500          | 1072   |

  Scenario: Set Currency for non-existing game save
    Given the following users
      | id                                   | name       | email               | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 0       | 0       | 0        | 500          | 1072   |
    When we want to set the following currencies for the game save with id ade63373-fdc9-4fdc-aea9-6100d6f36dae with toCache to false
      | gold | diamond |
      | 200  | 300     |
    Then I should throw a NotFoundException
