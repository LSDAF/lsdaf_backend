Feature: Game Save Features

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: Create a new GameSave from userEmail
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |

    When we want to create a new game save for the user with email paul.ochon@test.com
    Then I should return the following game saves
      | userId                               | gold | healthPoints | attack |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 0    | 10           | 1      |

  Scenario: Create a new GameSave from AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    When we want to create a new game save with the following AdminGameSaveCreationRequest
      | userEmail            | gold | healthPoints | attack |
      | paul.itesse@test.com | 500  | 11289        | 5000   |

    Then I should return the following game saves
      | userEmail            | gold | healthPoints | attack |
      | paul.itesse@test.com | 500  | 11289        | 5000   |


  Scenario: Get an existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to get the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should return the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |


  Scenario: Get a non-existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to get the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0be

    Then I should throw a NotFoundException


  Scenario: Update an existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | gold | healthPoints | attack |
      | 0    | 200          | 3000   |
    Then I should return the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 0    | 200          | 3000   |


  Scenario: Update a non-existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0be with the following GameSaveUpdateRequest
      | id                                   | userId                               | gold      | healthPoints | attack |
      | 21524a69-dd46-475e-a152-9032477df41d | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 420420420 | 69420        | 69000  |
    Then I should throw a NotFoundException

  Scenario: Get all game saves
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
      | d90b8f0f-68da-44ca-9d79-f564a0a33c59 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3       | 2            | 11111  |
    When we want to get all game saves

  Scenario: Delete an existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to delete the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should have no game save entries in DB


  Scenario: Delete a non-existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we want to delete the game save with id 21524a69-dd46-475e-a152-9032477df41e

    Then I should throw a NotFoundException

  Scenario: Check GameSave Ownership for owner
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 806c8d75-6d20-4980-973b-6c5185d41eb0 | Paul ITESSE | paul.itesse@test.com |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.ochon@test.com

    Then I should throw no Exception

  Scenario: Check GameSave Ownership for non-owner
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.itesse@test.com
    Then I should throw a ForbiddenException

  Scenario: Check GameSave Ownership for non-existing GameSave
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.itesse@test.com
    Then I should throw a NotFoundException