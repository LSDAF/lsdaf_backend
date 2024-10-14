Feature: Game Save Service tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: Create a new GameSave from userEmail
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    When we want to create a new game save for the user with email paul.ochon@test.com

    Then I should return the following game save entities
      | userId                               | gold | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 0    | 10           | 1      | 0       | 0       | 0        | 1        | 1            |

  Scenario: Create a new GameSave from AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    When we want to create a new game save with the following AdminGameSaveCreationRequest
      | userEmail            | gold | healthPoints | attack | id                                   | diamond | emerald | amethyst | currentStage | maxStage |
      | paul.itesse@test.com | 500  | 11289        | 5000   | 3e97cebd-3ab0-43d7-8fb4-961f19a4ce61 | 600     | 700     | 800      | 100          | 100      |

    Then I should return the following game save entities
      | id                                   | userEmail            | gold | healthPoints | attack | diamond | emerald | amethyst | nickname | currentStage | maxStage |
      | 3e97cebd-3ab0-43d7-8fb4-961f19a4ce61 | paul.itesse@test.com | 500  | 11289        | 5000   | 600     | 700     | 800      | Player   | 100          | 100      |

    And I should have the following game saves in DB
      | id                                   | userId                               | userEmail            | gold | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | 3e97cebd-3ab0-43d7-8fb4-961f19a4ce61 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | paul.itesse@test.com | 500  | 11289        | 5000   | 600     | 700     | 800      | 100      | 100          |


  Scenario: Get an existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we want to get the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should return the following game save entities
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |


  Scenario: Get a non-existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we want to get the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0be

    Then I should throw a NotFoundException


  Scenario: Update an existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | nickname | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | player1  | 10      | 10      | 10       | 100          | 100      |

    When we want to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateNicknameRequest
      | nickname |
      | player2  |
    Then I should return the following game save entities
      | id                                   | userId                               | gold    | healthPoints | attack | nickname | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | player2  | 10      | 10      | 10       | 100          | 100      |


  Scenario: Update a non-existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we want to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0be with the following GameSaveUpdateNicknameRequest
      | nickname |
      | player2  |
    Then I should throw a NotFoundException

  Scenario: Get all game saves
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
      | d90b8f0f-68da-44ca-9d79-f564a0a33c59 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3       | 2            | 11111  | 100     | 100     | 100      | 100          | 100      |
    When we want to get all game saves

    Then I should return the following game save entities
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
      | d90b8f0f-68da-44ca-9d79-f564a0a33c59 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3       | 2            | 11111  | 100     | 100     | 100      | 100          | 100      |

  Scenario: Delete an existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 100     | 100     | 100      | 100      | 100          |
    When we want to delete the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should have no game save entries in DB


  Scenario: Delete a non-existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we want to delete the game save with id 21524a69-dd46-475e-a152-9032477df41e

    Then I should throw a NotFoundException

  Scenario: Check GameSave Ownership for owner
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 806c8d75-6d20-4980-973b-6c5185d41eb0 | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.ochon@test.com

    Then I should throw no Exception

  Scenario: Check GameSave Ownership for non-owner
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.itesse@test.com
    Then I should throw a ForbiddenException

  Scenario: Check GameSave Ownership for non-existing GameSave
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   | 10      | 10      | 10       | 100          | 100      |
    When we check the game save ownership with id 0530e1fe-3428-4edd-bb32-cb563419d0bd for the user with email paul.itesse@test.com
    Then I should throw a NotFoundException

  Scenario: Get all game saves for a user
    Given the following users
      | id                                   | name       | email               | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     | USER  |

    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   | 10      | 10      | 10       | 100          | 100      |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 2       | 26           | 770    | 10      | 10      | 10       | 100          | 100      |
    When we want to get all game saves for the user with email paul.ochon@test.com
    Then I should return the following game save entities
      | id                                   | userId                               | gold    | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   | 10      | 10      | 10       | 100          | 100      |
      | 0203c658-7c54-4154-b94f-8ad81d3dde1a | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 2       | 26           | 770    | 10      | 10      | 10       | 100          | 100      |