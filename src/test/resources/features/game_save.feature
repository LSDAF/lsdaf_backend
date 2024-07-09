Feature: Game Save Features

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: Create a new GameSave

    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    When the user with email paul.ochon@test.com creates a new game save
    Then I should return the following game save
      | userId                               | gold | healthPoints | attack |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 0    | 10           | 1      |


  Scenario: Get an existing GameSave
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When the user with email paul.ochon@test.com gets the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should return the following game save
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |


  Scenario: Get a non-existing GameSave
    Given the following users
      | id                                   | name       | email               |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When the user with email paul.ochon@test.com gets the game save with id 21524a69-dd46-475e-a152-9032477df41e

    Then I should throw a NotFoundException
#
#  Scenario: Get a GameSave with non-owner user
#    Given the following users
#  When
#  Then
#
#  Scenario: Update an existing GameSave
#    Given A clean database
#    And the following users
#  When
#  Then
#
#  Scenario: Update a non-existing GameSave
#    Given A clean database
#    And the following users
#  When
#  Then
#
#  Scenario: Update a GameSave with non-owner user
#    Given A clean database
#    And the following users
#  When
#  Then
#
#  Scenario: Delete an existing GameSave
#    Given A clean database
#    And the following users
#  When
#  Then
#
#  Scenario: Delete a non-existing GameSave
#    Given A clean database
#    And the following users
#  When
#  Then
#
#  Scenario: Delete a GameSave with non-owner user
#    Given A clean database
#    And the following users
#  When
#  Then