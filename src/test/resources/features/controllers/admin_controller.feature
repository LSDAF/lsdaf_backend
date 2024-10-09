Feature: Admin Controller tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present


  Scenario: A User tries to request one of the admin endpoints without a token
    Given the following users
      | id                                   | name       | email               | password | roles | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER  | true     | true    |

    When the user requests the admin endpoint to get the global info with no token

    Then the response status code should be 401

  Scenario: A non-admin user tries to request one of the admin endpoints
    Given the following users
      | id                                   | name       | email               | password | roles | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER  | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the global info

    Then the response status code should be 403

  Scenario: An admin user requests the global info
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
      | 8ea5a501-2429-4e89-881c-f19aa191cabb | Paul AITTE | paul.aitte@test.com | toto1234 | USER       | true     | true    |
      | 08a446bb-7a12-48fd-b4bf-b01d0b60b5c0 | Paul HISSE | paul.hisse@test.com | toto1234 | USER       | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | currentStage | maxStage | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10       | 10           | 10     |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 8ea5a501-2429-4e89-881c-f19aa191cabb | 100  | 100     | 100     | 100      | 100          | 100      | 100          | 100    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 08a446bb-7a12-48fd-b4bf-b01d0b60b5c0 | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000         | 1000   |
    And the time clock set to the following value 2020-01-01T00:00:00Z


    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the global info

    Then the response status code should be 200

    And the response should have the following GlobalInfo
      | userCounter | gameSaveCounter | now                  |
      | 3           | 3               | 2020-01-01T00:00:00Z |

  Scenario: An admin user requests all the users
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
      | 8ea5a501-2429-4e89-881c-f19aa191cabb | Paul AITTE | paul.aitte@test.com | toto1234 | USER       | true     | true    |
      | 08a446bb-7a12-48fd-b4bf-b01d0b60b5c0 | Paul HISSE | paul.hisse@test.com | toto1234 | USER       | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get all the users ordered by NAME

    Then the response should have the following Users in exact order
      | id                                   | name       | email               | provider | roles      | verified | enabled |
      | 8ea5a501-2429-4e89-881c-f19aa191cabb | Paul AITTE | paul.aitte@test.com | toto1234 | USER       | true     | true    |
      | 08a446bb-7a12-48fd-b4bf-b01d0b60b5c0 | Paul HISSE | paul.hisse@test.com | toto1234 | USER       | true     | true    |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

  Scenario: An admin user requests all the game saves without cache
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10           | 10     | 10      | 10      | 10       | 10       | 10           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100          | 100    | 100     | 100     | 100      | 100      | 100          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000         | 1000   | 1000    | 1000    | 1000     | 1000     | 1000         |


    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get all the game saves ordered by ID_DESC

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | 1000     | 1000         |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100  | 100     | 100     | 100      | 100          | 100    | 100      | 100          |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 10   | 10      | 10      | 10       | 10           | 10     | 10       | 10           |

  Scenario: An admin user requests all the game saves with cache
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10           | 10     | 10      | 10      | 10       | 10       | 10           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100          | 100    | 100     | 100     | 100      | 100      | 100          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000         | 1000   | 1000    | 1000    | 1000     | 1000     | 1000         |

    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100  | 100     | 100     | 100      |

    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 1000         | 1000     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get all the game saves ordered by ID_DESC

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | 1000     | 1000         |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100  | 100     | 100     | 100      | 100          | 100    | 1000     | 1000         |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 100  | 100     | 100     | 100      | 10           | 10     | 10       | 10           |


  Scenario: An admin user requests a user's details by non-existing id
    Given the following users
      | id                                   | name        | email                | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true     | true    |
      | 10d4a23a-de77-4ea8-8ed2-8299cdcf7854 | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER       | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get details of the user with id ae1e58ad-6da5-48f9-a83f-7fbeb647cbd6

    Then the response status code should be 404

  Scenario: An admin user requests a user's details by existing id
    Given the following users
      | id                                   | name        | email                | password | roles      | provider | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | LOCAL    | true    | true     |
      | 10d4a23a-de77-4ea8-8ed2-8299cdcf7854 | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER       | LOCAL    | true    | true     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get details of the user with id 10d4a23a-de77-4ea8-8ed2-8299cdcf7854

    Then the response status code should be 200

    And the response should have the following UserAdminDetails
      | id                                   | name        | email                | password | roles | provider | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER  | LOCAL    | true    | true     |

  Scenario: An admin user requests a user's details by non-existing email
    Given the following users
      | id                                   | name        | email                | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true     | true    |
      | 10d4a23a-de77-4ea8-8ed2-8299cdcf7854 | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER       | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get details of the user with email paul.itesse@test.com

    Then the response status code should be 404

  Scenario: An admin user requests a user's details by existing email
    Given the following users
      | id                                   | name        | email                | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true     | true    |
      | 10d4a23a-de77-4ea8-8ed2-8299cdcf7854 | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER       | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get details of the user with email paul.emploi@test.com

    Then the response status code should be 200

    And the response should have the following UserAdminDetails
      | id                                   | name        | email                | password | roles | provider | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul EMPLOI | paul.emploi@test.com | tutu1234 | USER  | LOCAL    | true    | true     |

  Scenario: An admin user requests the deletion of a non-existing user
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the user with id ae1e58ad-6da5-48f9-a83f-7fbeb647cbd6

    Then the response status code should be 404

  Scenario: An admin user requests the creation of a new user
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest
      | name        | userId                               | email                | password | socialProvider | roles      | enabled | verified |
      | Paul ITESSE | 5951e4ae-e151-4374-a262-4bf97b63e042 | paul.itesse@test.com | toto1234 | LOCAL          | USER,ADMIN | false   | true     |

    Then the response status code should be 200

    And the response should have the following Users
      | name        | id                                   | email                | enabled | password | roles      | socialProvider | verified |
      | Paul ITESSE | 5951e4ae-e151-4374-a262-4bf97b63e042 | paul.itesse@test.com | false   | toto1234 | USER,ADMIN | LOCAL          | true     |

  Scenario: An admin user requests the creation of a new user with invalid data
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest
      | name        | userId                               | email            | password | socialProvider | roles      | enabled | verified |
      | Paul ITESSE | 5951e4ae-e151-4374-a262-4bf97b63e042 | paul.itesse@test | toto     | LOCAL          | USER,ADMIN | true    | true     |

    Then the response status code should be 400

  Scenario: An admin user requests the update of a non-existing user
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id ae1e58ad-6da5-48f9-a83f-7fbeb647cbd6 with the following AdminUserUpdateRequest
      | name        | email            | password | socialProvider | roles      | enabled | verified |
      | Paul ITESSE | paul.itesse@test | toto1234 | LOCAL          | USER,ADMIN | true    | true     |

    Then the response status code should be 404

  Scenario: An admin user requests the update of an existing user
    Given the following users
      | id                                   | name        | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 0fe9d435-fdd0-4128-8f2d-f5f8e196aea5 | Paul EMPLOI | paul.emploi@test.com | toto1234 | USER,ADMIN | false   | false    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id 0fe9d435-fdd0-4128-8f2d-f5f8e196aea5 with the following AdminUserUpdateRequest
      | name        | password     | enabled | roles | email                | socialProvider | verified |
      | Paul ITESSE | toto12345678 | true    | USER  | paul.itesse@test.com | LOCAL          | true     |

    Then the response status code should be 200

    And the response should have the following Users
      | id                                   | name        | email                | roles | enabled | password     | verified |
      | 0fe9d435-fdd0-4128-8f2d-f5f8e196aea5 | Paul ITESSE | paul.itesse@test.com | USER  | true    | toto12345678 | true     |

  Scenario: An admin user requests the update of an existing user with invalid data
    Given the following users
      | id                                   | name        | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 0fe9d435-fdd0-4128-8f2d-f5f8e196aea5 | Paul EMPLOI | paul.emploi@test.com | toto1234 | USER,ADMIN | false   | true     |
      | 8ea5a501-2429-4e89-881c-f19aa191cabb | Paul AITTE  | paul.aitte@test.com  | toto1234 | USER       | true    | true     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the user with id 0fe9d435-fdd0-4128-8f2d-f5f8e196aea5 with the following AdminUserUpdateRequest
      | name        | password | enabled | roles | email               |
      | Paul ITESSE | tutu     | true    | USER  | paul.aitte@test.com |

    Then the response status code should be 400

  Scenario: An admin user requests the deletion of an existing user
    Given the following users
      | id                                   | name        | email                | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | true     | true    |
      | ae1e58ad-6da5-48f9-a83f-7fbeb647cbd6 | Paul ITESSE | paul.itesse@test.com | toto5678 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the user with id ae1e58ad-6da5-48f9-a83f-7fbeb647cbd6

    Then the response status code should be 200

  Scenario: An admin user requests the deletion of a non-existing game save
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | currentStage | maxStage | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10       | 10           | 10     |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 100          | 100      | 100          | 100    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000         | 1000   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the game save with id 80c4387f-3bd9-4f43-afac-ea1980f0ee64

    Then the response status code should be 404

  Scenario: An admin user requests the deletion of an existing game save
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | currentStage | maxStage | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10       | 10           | 10     |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 100          | 100      | 100          | 100    |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | 1000         | 1000   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to delete the game save with id cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b

    Then the response status code should be 200

  Scenario: An admin user requests the creation of a new game save
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | userEmail           | gold | healthPoints | attack | diamond | emerald | amethyst | nickname                             | currentStage | maxStage |
      | paul.ochon@test.com | 10   | 10           | 10     | 100     | 1000    | 10000    | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 300          | 400      |

    Then the response status code should be 200

  Scenario: An admin user requests the creation of a new game save with invalid data
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | userEmail           | gold | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | paul.ochon@test.com | -15  | 10           | 10     | 100     | 1000    | 10000    | 100          | 120      |

    Then the response status code should be 400

  Scenario: An admin user requests the creation of a new game save with invalid stage values
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest
      | userEmail           | gold | healthPoints | attack | diamond | emerald | amethyst | currentStage | maxStage |
      | paul.ochon@test.com | 10   | 10           | 10     | 10      | 10      | 10       | 100          | 99       |

    Then the response status code should be 400

  Scenario: An admin user requests the update of a non-existing game save
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10       | 10           |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 80c4387f-3bd9-4f43-afac-ea1980f0ee64 with the following AdminGameSaveUpdateRequest
      | healthPoints | attack |
      | 10000        | 10000  |

    Then the response status code should be 404

  Scenario: An admin user requests the update of an existing game save
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | player1  | 1            | 1        |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 10000        | 10000  | player3  |

    Then the response status code should be 200

    And the response should have the following GameSave
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | nickname | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10000        | 10000  | paul.ochon@test.com | player3  | 1        | 1            |

  Scenario: An admin user requests the update of an existing game save with a custom nickname
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | nickname | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | player1  | 1        | 1            |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname   |
      | 10000        | 10000  | Play3r-0n3 |
    Then the response status code should be 200

    And the response should have the following GameSave
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | nickname   | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10000        | 10000  | paul.ochon@test.com | Play3r-0n3 | 1        | 1            |

  Scenario: An admin user requests the update of an existing game save with an existing nickname
    Given the following users
      | id                                   | name       | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul OCHON | paul.ochon2@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | maxStage | currentStage | attack | userEmail            | nickname |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10       | 10           | 10     | paul.ochon@test.com  | player1  |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 10   | 10      | 10      | 10       | 10           | 10       | 10           | 10     | paul.ochon2@test.com | player2  |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 10           | 10000  | player2  |

    Then the response status code should be 400

  Scenario: An admin user requests the update of an existing game save with a nickname too long
    Given the following users
      | id                                   | name       | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul OCHON | paul.ochon2@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com  | player1  | 1            | 1        |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon2@test.com | player2  | 1            | 1        |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname           |
      | 10000        | 10000  | 012345678901234567 |

    Then the response status code should be 400

  Scenario: An admin user requests the update of an existing game save with a nickname too short
    Given the following users
      | id                                   | name       | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul OCHON | paul.ochon2@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com  | player1  | 1            | 1        |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon2@test.com | player2  | 1            | 1        |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 10000        | 10000  | 01       |

    Then the response status code should be 400

  Scenario: An admin user requests the update of an existing game save with a nickname containing spaces
    Given the following users
      | id                                   | name       | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul OCHON | paul.ochon2@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com  | player1  | 1            | 1        |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon2@test.com | player2  | 1            | 1        |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname    |
      | 10000        | 10000  | 01234 56789 |

    Then the response status code should be 400

  Scenario: An admin user requests the update of an existing game save with a nickname containing invalid characters
    Given the following users
      | id                                   | name       | email                | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com  | toto1234 | USER,ADMIN | true    | true     |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | Paul OCHON | paul.ochon2@test.com | toto1234 | USER,ADMIN | true    | true     |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | nickname | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com  | player1  | 1            | 1        |
      | 0530e1fe-3428-4edd-bb32-cb563419d0be | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1e | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon2@test.com | player2  | 1            | 1        |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following AdminGameSaveUpdateRequest
      | healthPoints | attack | nickname |
      | 10000        | 10000  | \@azeeza |

    Then the response status code should be 400

  Scenario: An admin user requests the update of an existing game save with invalid data
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the game save with id 80c4387f-3bd9-4f43-afac-ea1980f0ee64 with the following AdminGameSaveUpdateRequest
      | healthPoints | attack |
      | -100         | 10000  |

    Then the response status code should be 400

  Scenario: An admin user searches for users with valid SearchRequest
    Given the following users
      | id                                   | name               | email                     | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON         | paul.ochon@test.com       | toto1234 | USER,ADMIN | true     | true    |
      | bca528fd-9642-4591-995f-ddb759599c02 | Camille COMBALE    | camille.combale@test.com  | toto2345 | USER,ADMIN | true     | true    |
      | 25577cc5-fbc1-4c45-a77a-61d0a97795df | Arthuro BRACHETTI  | arturo.brachetti@test.com | toto3456 | USER,ADMIN | true     | true    |
      | 171b53be-282a-4029-8aac-ae2b08a49722 | Jean DUJARDIN      | jean.dujardin@test.com    | toto4567 | USER,ADMIN | true     | true    |
      | c05a869a-31e8-4618-ae46-b6dd05a53de5 | Michael JACKSON    | michael.jackson@test.com  | toto5678 | USER,ADMIN | true     | true    |
      | 5380d13b-450f-4023-8563-59ea4d7ceb3f | Don Diego DELAVEGA | diego_delavega@test.com   | toto6789 | USER,ADMIN | true     | true    |
      | fd8e97ca-e9b4-4366-b6c5-d0f3c4088557 | Harry POPPERS      | harry.poppers@test.com    | toto7890 | USER,ADMIN | true     | true    |
      | e3ad66e8-28ba-4269-91dc-78603d5043b0 | Mac DOUGLAS        | mac.douglas@test.com      | toto8901 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NAME with the following SearchRequest
      | key        | value |
      | user_roles | USER  |

    Then the response status code should be 200

    And the response should have the following Users in exact order
      | id                                   | name               | email                     | roles      | verified | enabled |
      | 25577cc5-fbc1-4c45-a77a-61d0a97795df | Arthuro BRACHETTI  | arturo.brachetti@test.com | USER,ADMIN | true     | true    |
      | bca528fd-9642-4591-995f-ddb759599c02 | Camille COMBALE    | camille.combale@test.com  | USER,ADMIN | true     | true    |
      | 5380d13b-450f-4023-8563-59ea4d7ceb3f | Don Diego DELAVEGA | diego_delavega@test.com   | USER,ADMIN | true     | true    |
      | fd8e97ca-e9b4-4366-b6c5-d0f3c4088557 | Harry POPPERS      | harry.poppers@test.com    | USER,ADMIN | true     | true    |
      | 171b53be-282a-4029-8aac-ae2b08a49722 | Jean DUJARDIN      | jean.dujardin@test.com    | USER,ADMIN | true     | true    |
      | e3ad66e8-28ba-4269-91dc-78603d5043b0 | Mac DOUGLAS        | mac.douglas@test.com      | USER,ADMIN | true     | true    |
      | c05a869a-31e8-4618-ae46-b6dd05a53de5 | Michael JACKSON    | michael.jackson@test.com  | USER,ADMIN | true     | true    |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON         | paul.ochon@test.com       | USER,ADMIN | true     | true    |

  Scenario: An admin user searches for users with invalid SearchRequest
    Given the following users
      | id                                   | name               | email                     | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON         | paul.ochon@test.com       | toto1234 | USER,ADMIN | true     | true    |
      | bca528fd-9642-4591-995f-ddb759599c02 | Camille COMBALE    | camille.combale@test.com  | toto2345 | USER,ADMIN | true     | true    |
      | 25577cc5-fbc1-4c45-a77a-61d0a97795df | Arthuro BRACHETTI  | arturo.brachetti@test.com | toto3456 | USER,ADMIN | true     | true    |
      | 171b53be-282a-4029-8aac-ae2b08a49722 | Jean DUJARDIN      | jean.dujardin@test.com    | toto4567 | USER,ADMIN | true     | true    |
      | c05a869a-31e8-4618-ae46-b6dd05a53de5 | Michael JACKSON    | michael.jackson@test.com  | toto5678 | USER,ADMIN | true     | true    |
      | 5380d13b-450f-4023-8563-59ea4d7ceb3f | Don Diego DELAVEGA | diego_delavega@test.com   | toto6789 | USER,ADMIN | true     | true    |
      | fd8e97ca-e9b4-4366-b6c5-d0f3c4088557 | Harry POPPERS      | harry.poppers@test.com    | toto7890 | USER,ADMIN | true     | true    |
      | e3ad66e8-28ba-4269-91dc-78603d5043b0 | Mac DOUGLAS        | mac.douglas@test.com      | toto8901 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search users ordered by NAME with the following SearchRequest
      | key  | value         |
      | toto | Jean DUJARDIN |

    Then the response status code should be 400

  Scenario: An admin user searches for game saves with valid SearchRequest
    Given the following users
      | id                                   | name        | email                | password | roles      | provider | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER,ADMIN | LOCAL    | true     | true    |
      | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | Paul EMPLOI | paul.emploi@test.com | toto5678 | USER       | LOCAL    | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | maxStage | currentStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com  | 10       | 10           |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 100          | 100    | paul.ochon@test.com  | 100      | 100          |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | paul.emploi@test.com | 1000     | 1000         |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by NONE with the following SearchRequest
      | key     | value                                |
      | user_id | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f |

    Then the response status code should be 200

    And the response should have the following GameSaves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail            | maxStage | currentStage |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | paul.emploi@test.com | 1000     | 1000         |

  Scenario: An admin user searches for game saves with invalid SearchRequest
    Given the following users
      | id                                   | name            | email                    | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON      | paul.ochon@test.com      | toto1234 | USER,ADMIN | true     | true    |
      | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | Camille COMBALE | camille.combale@test.com | toto2345 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail                | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com      | 10           | 10       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 100          | 100    | paul.ochon@test.com      | 100          | 100      |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | camille.combale@test.com | 1000         | 1000     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to search game saves ordered by ID_DESC with the following SearchRequest
      | key  | value                                |
      | toto | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f |

    Then the response status code should be 400

  Scenario: An admin user flushes and clears the cache
    Given the following users
      | id                                   | name            | email                    | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON      | paul.ochon@test.com      | toto1234 | USER,ADMIN | true     | true    |
      | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | Camille COMBALE | camille.combale@test.com | toto2345 | USER,ADMIN | true     | true    |

    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail                | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com      | 10           | 10       |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 100          | 100    | paul.ochon@test.com      | 100          | 100      |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | 1000 | 1000    | 1000    | 1000     | 1000         | 1000   | camille.combale@test.com | 1000         | 1000     |
    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 5000 | 5000    | 5000    | 5000     |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | 6000 | 6000    | 6000    | 6000     |
    And the following game_save_ownership entries in cache
      | gameSaveId                           | userEmail           |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com |
      | 3bb1a064-79cc-4279-920a-fd0760663ca5 | paul.ochon@test.com |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to flush and clear the cache

    Then the response status code should be 200
    And the currency cache should be empty
    And the currency_histo cache should be empty
    And the game_save_ownership cache should be empty

  Scenario: An admin user requests the status of the cache
    Given the following users
      | id                                   | name            | email                    | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON      | paul.ochon@test.com      | toto1234 | USER,ADMIN | true     | true    |
      | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | Camille COMBALE | camille.combale@test.com | toto2345 | USER,ADMIN | true     | true    |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to get the cache status

    Then the response status code should be 200

    And the response should have the following Boolean true

  Scenario: An admin user requests the toggle of the cache
    Given the following users
      | id                                   | name            | email                    | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON      | paul.ochon@test.com      | toto1234 | USER,ADMIN | true     | true    |
      | 97d5a418-7cc6-44e8-b66f-20ac32e47e1f | Camille COMBALE | camille.combale@test.com | toto2345 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to toggle the cache status

    Then the response status code should be 200

    And the redis cache should be disabled

  Scenario: An admin user requests the update of the currencies of a game save with cache enabled
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |
    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100  | 100     | 100     | 100      |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the currencies of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following CurrencyRequest
      | gold | diamond | emerald | amethyst |
      | 1000 | 1000    | 1000    | 1000     |

    Then the response status code should be 200

    And the currency cache should contain the following values
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 1000 | 1000    | 1000    | 1000     |

    And I should have the following game saves in DB
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |

  Scenario: An admin user requests the update of the currencies of a game save with cache disabled
    Given the cache is disabled
    And the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the currencies of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following CurrencyRequest
      | gold | diamond | emerald | amethyst |
      | 1000 | 1000    | 1000    | 1000     |

    Then the response status code should be 200

    And I should have the following game saves in DB
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 1000    | 1000    | 1000     | 10           | 10     | paul.ochon@test.com | 10           | 10       |

  Scenario: An admin user requests the update of the stages of a game save with cache enabled
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100          | 100      |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the stages of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following StageRequest
      | currentStage | maxStage |
      | 499          | 500      |
    Then the response status code should be 200

    And the stage cache should contain the following values
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 499          | 500      |

    And I should have the following game saves in DB
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |

  Scenario: An admin user requests the update of the stages of a game save with invalid data
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |
    And the following stage entries in cache
      | gameSaveId                           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 100          | 100      |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the stages of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following StageRequest
      | currentStage | maxStage |
      | 500          | 400      |
    Then the response status code should be 400

  Scenario: An admin user requests the update of the stages of a game save with cache disabled
    Given the cache is disabled
    And the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 10           | 10       |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the admin endpoint to update the stages of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following StageRequest
      | currentStage | maxStage |
      | 499          | 500      |

    Then the response status code should be 200

    And I should have the following game saves in DB
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | userEmail           | currentStage | maxStage |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 10   | 10      | 10      | 10       | 10           | 10     | paul.ochon@test.com | 499          | 500      |