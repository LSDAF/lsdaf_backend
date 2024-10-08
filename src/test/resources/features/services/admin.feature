Feature: Admin Service tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the cache is enabled
    And the time clock set to the present

  Scenario: Admin: Get all users
    Given the following users
      | id                                   | name          | email                  | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     | USER  |
    When an admin gets all the users ordered by NONE
    Then I should return the following users
      | id                                   | name          | email                  | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     | USER  |

  Scenario: Admin: Get a user by id
    Given the following users
      | id                                   | name          | email                  | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     | USER  |

    When an admin gets the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should return the following UserAdminDetails
      | id                                   | name       | email               | password | enabled | userRoles | socialProvider | providerUserId | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | USER      | LOCAL          |                | true     |

  Scenario: Admin: Get user by email
    Given the following users
      | id                                   | name          | email                  | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     | USER  |

    When an admin gets the user with email paul.hisse@test.com

    Then I should return the following UserAdminDetails
      | id                                   | name       | email               | password | enabled | userRoles | socialProvider | providerUserId | verified |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE | paul.hisse@test.com | 5678toto | true    | USER      | LOCAL          |                | true     |

  Scenario: Admin: Get non-existing user by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin gets the user with email paul.hitique@test.com

    Then I should throw a NotFoundException

  Scenario: Admin: Get non-existing user by id
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin gets the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should throw a NotFoundException

  Scenario: Admin: Create a new User
    Given the following users
      | id | name | email | password | enabled | verified |

    When an admin creates a new user with the following AdminUserCreationRequest
      | name       | email               | password | socialProvider | providerUserId | roles      |
      | Paul HISSE | paul.hisse@test.com | toto1234 | LOCAL          |                | USER,ADMIN |

    Then I should return the following users
      | name       | email               | password | enabled | userRoles | socialProvider | providerUserId |
      | Paul HISSE | paul.hisse@test.com | toto1234 | true    | USER      | LOCAL          |                |

  Scenario: Admin: Update a user
    Given the following users
      | id                                   | name       | email               | password | enabled | roles | verified |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | USER  | true     |

    When an admin updates a user with id a5c17332-cd02-40b8-ac45-322baef4b70a with the following AdminUserUpdateRequest
      | name        | enabled |
      | Paul Emploi | false   |

    Then I should return the following users
      | id                                   | name        | email               | password | enabled | userRoles | socialProvider | providerUserId | verified |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul Emploi | paul.ochon@test.com | toto1234 | false   | USER      | LOCAL          |                | true     |

  Scenario: Admin: Update a non-existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin updates a user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d with the following AdminUserUpdateRequest
      | name        |
      | Paul Emploi |

    Then I should throw a NotFoundException

  Scenario: Admin: Delete a user
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin deletes a user with the following id a5c17332-cd02-40b8-ac45-322baef4b70a

    Then I should have no user entries in DB

  Scenario: Admin: Delete a non-existing user
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin deletes a user with the following id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should throw a NotFoundException

  Scenario: Admin: Delete a user by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin deletes a user with the following email paul.ochon@test.com

    Then I should have no user entries in DB

  Scenario: Admin: Delete a non-existing user by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | a5c17332-cd02-40b8-ac45-322baef4b70a | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |

    When an admin deletes a user with the following email paul.itesse@test.com

    Then I should throw a NotFoundException

  Scenario: Admin: Get GlobalInfo
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage    |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 5630280     | 5630280     | 5630280     | 124          | 1072       | 5630280      | 5630280     |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 3272         | 3272        |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 29289027267  | 29289027267 |
    And the time clock set to the following value 2024-01-01T00:00:00Z
    When an admin gets the global info

    Then I should return the following GlobalInfo
      | userCounter | gameSaveCounter | now                  |
      | 2           | 3               | 2024-01-01T00:00:00Z |

  Scenario: Admin: Get all game saves
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 10      | 10      | 10       | 124          | 1072       | 10           | 10       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 100     | 100     | 100      | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 1000    | 1000    | 1000     | 12           | 1223378989 | 10           | 10       |

    When an admin gets all the game saves ordered by NONE

    Then I should return the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | userEmail            | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 10      | 10      | 10       | 124          | 1072       | paul.ochon@test.com  | 10           | 10       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 100     | 100     | 100      | 12999        | 666        | paul.ochon@test.com  | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 1000    | 1000    | 1000     | 12           | 1223378989 | paul.itesse@test.com | 10           | 10       |

  Scenario: Admin: Get a game save by id
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280     | 10      | 10      | 10       | 124          | 1072       | 10           | 10       |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 100     | 100     | 100      | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 1000    | 1000    | 1000     | 12           | 1223378989 | 10           | 10       |

    When an admin gets the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should return the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 10      | 10      | 10       | 124          | 1072   | paul.ochon@test.com | 10           | 10       |

  Scenario: Admin: Get a non-existing game save by id
    Given the following users
      | id                                   | name        | email                | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | true    | true     | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 10           | 10       |

    When an admin gets the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should throw a NotFoundException

  Scenario: Admin: Update a game save with a non-existing id
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 10      | 10      | 10       | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 100     | 100     | 100      | 12           | 1223378989 | 10           | 10       |

    When an admin updates the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateAdminRequest
      | healthPoints | attack |
      | 11289        | 5000   |

    Then I should throw a NotFoundException

  Scenario: Admin: Update a game save with an existing id
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | nickname | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 10      | 10      | 10       | 12999        | 666        | player1  | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 100     | 100     | 100      | 12           | 1223378989 | player2  | 10           | 10       |

    When an admin updates the game save with id 7be1f95f-fd42-4f0e-863c-093a6b4eeeca with the following GameSaveUpdateAdminRequest
      | healthPoints | attack | nickname |
      | 11289        | 5000   | player3  |

    Then I should return the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack | userEmail            | nickname | currentStage | maxStage |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 100     | 100     | 100      | 11289        | 5000   | paul.itesse@test.com | player3  | 10           | 10       |

  Scenario: Admin: Delete a game save with an existing id
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272 | 10      | 10      | 10       | 12999        | 666    | 10           | 10       |

    When an admin deletes the game save with id 9fb0c57c-2488-44c9-8b8f-6d595fa44937

    Then I should have no game save entries in DB

    And I should have no currency entries in DB

  Scenario: Admin: Flush and clear the cache
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 10      | 10      | 10       | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 10      | 10      | 10       | 12           | 1223378989 | 10           | 10       |
    And the following currency entries in cache
      | gameSaveId                           | gold       | diamond | emerald | amethyst |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 6666666666 | 2000    | 3000    | 4000     |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 505          | 600      |
    When an admin flushes and clears the cache

    Then I should have the following game saves in DB
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 6666666666  | 2000    | 3000    | 4000     | 12999        | 666        | 505          | 600      |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 10      | 10      | 10       | 12           | 1223378989 | 10           | 10       |
    And the currency cache should be empty
    And the currency_histo cache should be empty
    And the stage cache should be empty
    And the stage_histo cache should be empty
    And the game_save_ownership cache should be empty

  Scenario: Admin: Get the cache status
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 10           | 10       |

    When an admin gets the cache status

    Then I should return true

  Scenario: Admin: Toggle the cache enabling
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 10           | 10       |

    When an admin toggles the cache status
    And an admin gets the cache status

    Then I should return false


  Scenario: Admin: Delete a game save with a non-existing id
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 10           | 10       |

    When an admin deletes the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then I should throw a NotFoundException

  Scenario: Admin: Create a new game save with valid AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id | userId | gold | healthPoints | attack | diamond | emerald | amethyst |

    When an admin creates a new game save with the following AdminGameSaveCreationRequest
      | diamond | emerald | amethyst | gold | healthPoints | attack | userEmail           | id                                   | currentStage | maxStage |
      | 666     | 666     | 666      | 666  | 666          | 666    | paul.ochon@test.com | f98b0292-1ed9-4f3b-b7aa-72846916beb9 | 666          | 666      |

    Then I should return the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | f98b0292-1ed9-4f3b-b7aa-72846916beb9 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 666  | 666     | 666     | 666      | 666          | 666    | paul.ochon@test.com | 666          | 666      |

  Scenario: Admin: Create a new game save with invalid AdminGameSaveCreationRequest
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond     | emerald     | amethyst    | healthPoints | attack     | currentStage | maxStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 3272        | 3272        | 3272        | 12999        | 666        | 10           | 10       |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 29289027267 | 29289027267 | 29289027267 | 12           | 1223378989 | 10           | 10       |

    When an admin creates a new game save with the following AdminGameSaveCreationRequest
      | diamond | emerald | amethyst | gold | healthPoints | attack | userEmail            | currentStage | maxStage |
      | 666     | 666     | 666      | 666  | 666          | 666    | paul.emploi@test.com | 666          | 666      |

    Then I should throw a NotFoundException

  Scenario: Admin: Search for users - 1
    Given the following users
      | id                                   | name        | email                | provider | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | local    | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | local    | USER  |

    When an admin searches for users ordered by NONE with the following SearchRequest
      | key  | value       |
      | name | Paul ITESSE |

    Then I should return the following users
      | id                                   | name        | email                | provider | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | local    | USER  |

  Scenario: Admin: Search for users - 2
    Given the following users
      | id                                   | name          | email                  | password | provider | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    | USER  |

    When an admin searches for users ordered by NAME_DESC with the following SearchRequest
      | key | value |

    Then I should return the following users in exact order
      | id                                   | name          | email                  | password | provider | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    | USER  |

  Scenario: Admin: Search for users - 3
    Given the following users
      | id                                   | name          | email                  | password | provider | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | local    | USER  |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    | USER  |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | local    | USER  |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | local    | USER  |

    When an admin searches for users ordered by ID with the following SearchRequest
      | key   | value                  |
      | email | jean.dujardin@test.com |

    Then I should return the following users
      | id                                   | name          | email                  | password | provider | roles |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | local    | USER  |

  Scenario: Admin: Search for game saves - 1
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | maxStage | currentStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 100     | 1000    | 10000    | 12999        | 666        | 100      | 100          |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 10      | 5       | 1        | 12           | 1223378989 | 100      | 100          |

    When an admin searches for game saves ordered by NONE with the following SearchRequest
      | key     | value                                |
      | user_id | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d |

    Then I should return the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | maxStage | currentStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272 | 100     | 1000    | 10000    | 12999        | 666    | paul.ochon@test.com | 100      | 100          |

  Scenario: Admin: Search for game saves - 2
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | maxStage | currentStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 5       | 10      | 15       | 12999        | 666        | 100      | 99           |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 20      | 25      | 30       | 12           | 1223378989 | 10       | 9            |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 35      | 40      | 45       | 12           | 1          | 1000     | 999          |

    When an admin searches for game saves ordered by ID_DESC with the following SearchRequest
      | key | value |

    Then I should return the following game saves in exact order
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | userEmail            | maxStage | currentStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 5       | 10      | 15       | 12999        | 666        | paul.ochon@test.com  | 100      | 99           |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 35      | 40      | 45       | 12           | 1          | paul.itesse@test.com | 1000     | 999          |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 20      | 25      | 30       | 12           | 1223378989 | paul.itesse@test.com | 10       | 9            |

  Scenario: Admin: Search for game saves - 3
    Given the following users
      | id                                   | name        | email                | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | USER  |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul ITESSE | paul.itesse@test.com | USER  |
    And the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | maxStage | currentStage |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 5       | 10      | 15       | 12999        | 666        | 1        | 1            |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 20      | 25      | 30       | 12           | 1223378989 | 10       | 9            |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 35      | 40      | 45       | 12           | 1          | 100      | 8            |

    When an admin searches for game saves ordered by GOLD with the following SearchRequest
      | key | value |

    Then I should return the following game saves
      | id                                   | userId                               | gold        | diamond | emerald | amethyst | healthPoints | attack     | userEmail            | maxStage | currentStage |
      | 8be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 3           | 35      | 40      | 45       | 12           | 1          | paul.itesse@test.com | 100      | 8            |
      | 9fb0c57c-2488-44c9-8b8f-6d595fa44937 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 3272        | 5       | 10      | 15       | 12999        | 666        | paul.ochon@test.com  | 1        | 1            |
      | 7be1f95f-fd42-4f0e-863c-093a6b4eeeca | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 29289027267 | 20      | 25      | 30       | 12           | 1223378989 | paul.itesse@test.com | 10       | 9            |
