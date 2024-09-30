Feature: User Service tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present


  Scenario: Create a new User
    When we want to create a new user with the following data
      | email               | name       | password  |
      | paul.ochon@test.com | Paul OCHON | paulOchon |

    Then I should return the following user entities
      | name       | email               | enabled | password  | provider | roles | verified |
      | Paul OCHON | paul.ochon@test.com | true    | paulOchon | LOCAL    | USER  | false    |


  Scenario: Check existence of an existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to check the existence of the user with email paul.ochon@test.com

    Then I should return true

  Scenario: Check existence of a non-existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to check the existence of the user with email paul.itesse@test.com

    Then I should return false

  Scenario: Get Users
    Given the following users
      | id                                   | name          | email                  | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     |

    When we want to get all the users

    Then I should return the following user entities
      | id                                   | name          | email                  | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234 | true    | true     |
      | cf0f3d45-18c0-41f8-8007-41c5ea6d3e0b | Jean DUJARDIN | jean.dujardin@test.com | 1234toto | true    | true     |
      | 71b56656-2116-4085-b4e2-f86ce068282a | Paul ITESSE   | paul.itesse@test.com   | toto5678 | true    | true     |
      | 7bbcab56-588e-4e70-bc3a-a582e5a0ede1 | Paul HISSE    | paul.hisse@test.com    | 5678toto | true    | true     |

  Scenario: Get an existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to get the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should return the following user entities
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

  Scenario: Get an existing User by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to get the user with email paul.ochon@test.com

    Then I should return the following user entities
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

  Scenario: Get a non-existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to get the user with id 3218335e-a03f-4fbc-9875-497fc19ea4ca

    Then I should throw a NotFoundException

  Scenario: Get a non-existing User by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to get the user with email paul.itesse@test.com

    Then I should throw a NotFoundException

  Scenario: Update an existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

  Scenario: Update a non-existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to update the user with id 4132952c-1d52-44c3-973d-f8f214e3d4df with the following UserUpdateRequest
      | name          |
      | Jean DUJARDIN |
    Then I should throw a NotFoundException

  Scenario: Delete an existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to delete the user with id 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d

    Then I should have no user entries in DB

  Scenario: Delete an existing User by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to delete the user with email paul.ochon@test.com

    Then I should have no user entries in DB

  Scenario: Delete a non-existing User
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to delete the user with id fba76d12-9bb8-4af9-afb3-ec0f066fcdca

    Then I should throw a NotFoundException

  Scenario: Delete a non-existing User by email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to delete the user with email paul.itesse@test.com

    Then I should throw a NotFoundException

  Scenario: Update a User password with invalid new password
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to update the password of the user with email paul.ochon@test.com from toto1234 to toto

    Then I should throw a IllegalArgumentException

  Scenario: Update a User password with invalid old password
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to update the password of the user with email paul.ochon@test.com from toto5678 to 1234toto

    Then I should throw a IllegalArgumentException

  Scenario: Update a User password with invalid id
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to update the user with id cb15f4b3-0b96-4e03-8de0-19cd98d71c56 with the following UserUpdateRequest
      | name         |
      | Jean BONNEAU |

    Then I should throw a NotFoundException

  Scenario: Validate User password with invalid email
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to validate the user password with email paul.itesse@test.com and password toto1234

    Then I should throw a NotFoundException

  Scenario: Validate User password with invalid password
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to validate the user password with email paul.ochon@test.com and password toto5678

    Then I should return false

  Scenario: Validate User password with valid email and password
    Given the following users
      | id                                   | name       | email               | password | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     |

    When we want to validate the user password with email paul.ochon@test.com and password toto1234

    Then I should return true