Feature: Admin Service Features

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: Admin: Get all users
    Given the following users
      | id                                   | name          | email                  | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto |
    When an admin gets all the users ordered by NONE
    Then I should return the following users
      | id                                   | name          | email                  | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto |

  Scenario: Admin: Get a user by id
    Given the following users
      | id                                   | name          | email                  | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto |

    When an admin gets the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should return the following UserAdminDetails
      | id                                   | name       | email               | password | enabled | userRoles | socialProvider | providerUserId |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | USER      | LOCAL          |                |

  Scenario: Admin: Get user by email
    Given the following users
      | id                                   | name          | email                  | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto |

    When an admin gets the user with email paul.hisse@test.com

    Then I should return the following UserAdminDetails
      | id                                   | name       | email               | password | enabled | userRoles | socialProvider | providerUserId |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE | paul.hisse@test.com | 5678toto | true    | USER      | LOCAL          |                |

  Scenario: Admin: Get non-existing user by email
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin gets the user with email paul.hitique@test.com

    Then I should throw a NotFoundException

  Scenario: Admin: Get non-existing user by id
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin gets the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should throw a NotFoundException

  Scenario: Admin: Create a new User
    Given the following users
      | id | name | email | password |

    When an admin creates a new user with the following AdminUserCreationRequest
      | name       | email               | password | socialProvider | providerUserId | userRoles  |
      | Paul HISSE | paul.hisse@test.com | toto1234 | LOCAL          |                | USER,ADMIN |

    Then I should return the following users
      | name       | email               | password | enabled | userRoles | socialProvider | providerUserId |
      | Paul HISSE | paul.hisse@test.com | toto1234 | true    | USER      | LOCAL          |                |

  Scenario: Admin: Update a user
    Given the following users
      | id                                   | name       | email               | password | enabled | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | USER  |

    When an admin updates a user with id a5c17332-cd02-40b8-ac45-322baef4b70a with the following AdminUserUpdateRequest
      | name        | enabled |
      | Paul Emploi | false   |

    Then I should return the following users
      | id                                   | name        | email               | password | enabled | userRoles | socialProvider | providerUserId |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul Emploi | paul.ochon@test.com | toto1234 | false   | USER      | LOCAL          |                |

  Scenario: Admin: Update a non-existing User
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin updates a user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d with the following AdminUserUpdateRequest
      | name        |
      | Paul Emploi |

    Then I should throw a NotFoundException

  Scenario: Admin: Delete a user
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin deletes a user with the following id a5c17332-cd02-40b8-ac45-322baef4b70a

    Then I should have no user entries in DB

  Scenario: Admin: Delete a non-existing user
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin deletes a user with the following id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should throw a NotFoundException

  Scenario: Admin: Delete a user by email
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin deletes a user with the following email paul.ochon@test.com

    Then I should have no user entries in DB

  Scenario: Admin: Delete a non-existing user by email
    Given the following users
      | id                                   | name       | email               | password |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 |

    When an admin deletes a user with the following email paul.itesse@test.com

    Then I should throw a NotFoundException

  Scenario: Admin: Get GlobalInfo
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 124          | 1072       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin gets the global info

    Then I should return the following GlobalInfo
      | userCounter | gameSaveCounter |
      | 2           | 3               |

  Scenario: Admin: Get all game saves
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 124          | 1072       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin gets all the game saves ordered by NONE

    Then I should return the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     | userEmail            |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 124          | 1072       | paul.ochon@test.com  |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        | paul.ochon@test.com  |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 | paul.itesse@test.com |

  Scenario: Admin: Get a save game by id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 124          | 1072       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin gets the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should return the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | userEmail           |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   | paul.ochon@test.com |

  Scenario: Admin: Get a non-existing save game by id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin gets the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should throw a NotFoundException

  Scenario: Admin: Update a save game with a non-existing id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin updates the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateAdminRequest
      | gold | healthPoints | attack |
      | 500  | 11289        | 5000   |

    Then I should throw a NotFoundException

  Scenario: Admin: Update a save game with an existing id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin updates the game save with id 7be1f95f-fd42-4f0e-863c-093a6b4eeeca with the following GameSaveUpdateAdminRequest
      | gold | healthPoints | attack |
      | 500  | 11289        | 5000   |

    Then I should return the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | userEmail            |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 500  | 11289        | 5000   | paul.itesse@test.com |

  Scenario: Admin: Delete a save game with an existing id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin deletes the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should have no game save entries in DB

  Scenario: Admin: Delete a save game with a non-existing id
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin deletes the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should throw a NotFoundException

  Scenario: Admin: Create a new save game with valid AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id | userId | gold | healthPoints | attack |

    When an admin creates a new game save with the following AdminGameSaveCreationRequest
      | gold | healthPoints | attack | userEmail           | id                                   |
      | 666  | 666          | 666    | paul.ochon@test.com | f98b0292-1ed9-4f3b-b7aa-72846916beb9 |

    Then I should return the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | userEmail           |
      | f98b0292-1ed9-4f3b-b7aa-72846916beb9 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 666  | 666          | 666    | paul.ochon@test.com |

  Scenario: Admin: Create a new save game with invalid AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin creates a new game save with the following AdminGameSaveCreationRequest
      | gold | healthPoints | attack | userEmail            |
      | 666  | 666          | 666    | paul.emploi@test.com |

    Then I should throw a NotFoundException

  Scenario: Admin: Search for users - 1
    Given the following users
      | id                                   | name        | email                | provider |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | local    |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | local    |

    When an admin searches for users ordered by NONE with the following SearchRequest
      | key  | value       |
      | name | Paul ITESSE |

    Then I should return the following users
      | id                                   | name        | email                | provider |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | local    |

  Scenario: Admin: Search for users - 2
    Given the following users
      | id                                   | name          | email                  | password | provider |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    |

    When an admin searches for users ordered by NAME_DESC with the following SearchRequest
      | key | value |

    Then I should return the following users in exact order
      | id                                   | name          | email                  | password | provider |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    |

  Scenario: Admin: Search for users - 3
    Given the following users
      | id                                   | name          | email                  | password | provider |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    |

    When an admin searches for users ordered by ID with the following SearchRequest
      | key   | value                  |
      | email | jean.dujardin@test.com |

    Then I should return the following users
      | id                                   | name          | email                  | password | provider |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    |

  Scenario: Admin: Search for game saves - 1
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |

    When an admin searches for game saves ordered by NONE with the following SearchRequest
      | key     | value                                |
      | user_id | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d |

    Then I should return the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | userEmail           |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272 | 12999        | 666    | paul.ochon@test.com |

  Scenario: Admin: Search for game saves - 2
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 12           | 1          |

    When an admin searches for game saves ordered by ID_DESC with the following SearchRequest
      | key | value |

    Then I should return the following game saves in exact order
      | id                                   | userId                               | gold        | healthPoints | attack     | userEmail            |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        | paul.ochon@test.com  |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 12           | 1          | paul.itesse@test.com |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 | paul.itesse@test.com |

  Scenario: Admin: Search for game saves - 3
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 12           | 1          |

    When an admin searches for game saves ordered by GOLD with the following SearchRequest
      | key | value |

    Then I should return the following game saves
      | id                                   | userId                               | gold        | healthPoints | attack     | userEmail            |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 12           | 1          | paul.itesse@test.com |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 12999        | 666        | paul.ochon@test.com  |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 12           | 1223378989 | paul.itesse@test.com |
