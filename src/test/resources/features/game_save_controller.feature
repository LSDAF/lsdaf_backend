Feature: GameSaveController tests

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: A User creates a new GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
