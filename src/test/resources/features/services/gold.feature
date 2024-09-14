Feature: Gold Service tests

  Background:
    Given the BDD engine is ready
    And the expiration seconds properties set to -1
    And a clean database

  Scenario: Get Gold with cache
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    And the following gold entries in cache
      | gameSaveId                           | gold   |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 |
    When we want to get the gold for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
    Then the gold amount should be 600000

  Scenario: Get Gold without cache
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we want to get the gold for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
    Then the gold amount should be 5630280

  Scenario: Get gold from non-existing game save
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we want to get the gold for the game save with id ade63373-fdc9-4fdc-aea9-6100d6f36dae
    Then I should throw a NotFoundException

  Scenario: Set gold for non-existing game save
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we want to set the gold for the game save with id ade63373-fdc9-4fdc-aea9-6100d6f36dae to 1000 without cache
    Then I should throw a NotFoundException

  Scenario: Set gold with cache
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we want to set the gold for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 to 1000 with cache
    Then I should have the following gold entries in cache
      | gameSaveId                           | gold |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1000 |
    And I should have the following game saves in DB
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |

  Scenario: Set gold without cache
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we want to set the gold for the game save with id f81b710d-3e02-4871-a86f-390377798dd1 to 1000 without cache
    Then I should have the following gold entries in cache
      | gameSaveId | gold |
    And I should have the following game saves in DB
      | id                                   | userId                               | gold | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 500          | 1072   |