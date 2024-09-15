Feature: Cache Service tests

  Background:
    Given the BDD engine is ready
    And the expiration seconds properties set to -1
    And a clean database

  Scenario: Flush Cache Service
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 2       | 26           | 770    |
    And the following currency entries in cache
      | gameSaveId                           | gold   |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 80000  |
    When the gold cache is flushed
    And I should have the following game saves in DB
      | id                                   | userId                               | gold   | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 600000 | 500          | 1072   |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 80000  | 26           | 770    |


  Scenario: Key Expiration
    Given the expiration seconds properties set to 10
    And the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    And the following currency entries in cache
      | gameSaveId                           | gold   |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 600000 |
    When a currency cache entry is expired

    Then the currency cache should be empty

    And the currency_histo cache should be empty

    And I should have the following game saves in DB
      | id                                   | userId                               | gold   | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 600000 | 500          | 1072   |