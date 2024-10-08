Feature: Auth Controller tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: A User registers its account with valid data
    Given the following user verification tokens
      | validationToken | status | expirationDate | userEmail |
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | totototo |
    Then the response status code should be 200
    And the response should have the following UserInfo
      | email               | name      | roles | verified |
      | paul.ochon@test.com | Toto Toto | USER  | false    |
    And a new validation token should have been created for the user with email paul.ochon@test.com
    And an email should have been sent to paul.ochon@test.com

  Scenario: A User registers its account with already used email
    Given the following users
      | id                                   | name       | email               | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | true     | USER  |
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | totototo |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid password
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name      | password |
      | paul.ochon@test.com | Toto Toto | 1234     |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid name
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email               | name | password |
      | paul.ochon@test.com |      | totototo |
    Then the response status code should be 400


  Scenario: A User registers its account with invalid email format
    When the user requests the endpoint to register a user with the following UserCreationRequest
      | email      | name      | password |
      | paul.ochon | Toto Toto | totototo |
    Then the response status code should be 400

  Scenario: A User logs in with valid credentials
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    Then the response status code should be 200
    And the token from the response should not be null
    And the refresh token from the response should not be null
    And the JwtAuthentication should contain the following UserInfo
      | email               | name       | roles      | verified |
      | paul.ochon@test.com | Paul OCHON | USER,ADMIN | true     |

    And I should have an unexpired and ACTIVE refresh token in DB for the user with email paul.ochon@test.com

  Scenario: A User logs in with unregistered email
    Given the following users
      | id                                   | name       | email               | password | roles      | verified | enabled |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true     | true    |

    When the user logs in with the following credentials
      | email                | password |
      | paul.itesse@test.com | toto1234 |

    Then the response status code should be 401

  Scenario: A User logs in with invalid password
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | tutu5678 |

    Then the response status code should be 401

  Scenario: A user logs out
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user logs out
    Then the response status code should be 200

    And the used token should be invalidated

  Scenario: A User logs in with unregistered email and a refresh token
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |

    When the user logs in with the following refresh token credentials
      | email                | refreshToken |
      | paul.itesse@test.com | XXX          |

    Then the response status code should be 401

  Scenario: A user logs in with a valid refresh token
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |
    And the following refresh tokens
      | refreshToken | status | userEmail           | expirationDate          | invalidationDate |
      | XXX          | ACTIVE | paul.ochon@test.com | 2070-01-01 00:00:00.000 |                  |

    When the user logs in with the following refresh token credentials
      | email               | refreshToken |
      | paul.ochon@test.com | XXX          |

  Scenario: A user logs in with an non-existing refresh token
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |
    And the following refresh tokens
      | refreshToken | status | userEmail           | expirationDate          | invalidationDate |
      | XXX          | ACTIVE | paul.ochon@test.com | 2070-01-01 00:00:00.000 |                  |

    When the user logs in with the following refresh token credentials
      | email               | refreshToken |
      | paul.ochon@test.com | YYY          |

    Then the response status code should be 404

  Scenario: A user logs in with an invalidated refresh token
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |
    And the following refresh tokens
      | refreshToken | status      | userEmail           | expirationDate          | invalidationDate        |
      | XXX          | INVALIDATED | paul.ochon@test.com | 2070-01-01 00:00:00.000 | 2022-01-01 00:00:00.000 |

    When the user logs in with the following refresh token credentials
      | email               | refreshToken |
      | paul.ochon@test.com | XXX          |

    Then the response status code should be 401

  Scenario: A user logs in with an expired refresh token
    Given the following users
      | id                                   | name       | email               | password | roles      | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN | true    | true     |
    And the following refresh tokens
      | refreshToken | status | userEmail           | expirationDate          | invalidationDate |
      | XXX          | ACTIVE | paul.ochon@test.com | 2020-01-01 00:00:00.000 |                  |

    When the user logs in with the following refresh token credentials
      | email               | refreshToken |
      | paul.ochon@test.com | XXX          |

    Then the response status code should be 401

  Scenario: A user logs in without verifying its account
    Given the following users
      | id                                   | name       | email               | password | roles | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER  | false   | false    |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    Then the response status code should be 401

  Scenario: A disabled user tries to log in
    Given the following users
      | id                                   | name       | email               | password | roles | enabled | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER  | false   | true     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    Then the response status code should be 401

  Scenario: A User wants to validate its account using an invalid verification token
    Given the following users
      | id                                   | name       | email               | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | true    | false    | USER  |
    And the following user verification tokens
      | token | status | expirationDate          | userEmail           |
      | YYY   | ACTIVE | 2024-01-02 00:00:00.000 | paul.ochon@test.com |
    And the time clock set to the following value 2024-01-01T00:00:00Z

    When the user requests the endpoint to validate the account with the following verification token XXX

    Then the response status code should be 404

  Scenario: A User wants to validate its account using a valid verification token
    Given the following users
      | id                                   | name        | email                | roles      | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | ADMIN,USER | true     |
      | 25657be8-61e4-428e-9295-81be05f322d6 | Paul ITESSE | paul.itesse@test.com | USER       | true     |
    And the following user verification tokens
      | token | status | expirationDate          | userEmail           |
      | XXX   | ACTIVE | 2024-01-02 00:00:00.000 | paul.ochon@test.com |
    And the time clock set to the following value 2024-01-01T00:00:00Z

    When the user requests the endpoint to validate the account with the following verification token XXX

    Then the response status code should be 200

    And the response should have the following UserInfo
      | id                                   | email               | name       | roles      | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | Paul OCHON | ADMIN,USER | true     |

  Scenario: A User wants to validate its account using an expired verification token
    Given the following users
      | id                                   | name        | email                | roles      | verified |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | ADMIN,USER | true     |
      | 25657be8-61e4-428e-9295-81be05f322d6 | Paul ITESSE | paul.itesse@test.com | USER       | true     |

    And the following user verification tokens
      | token | status | expirationDate          | userEmail           |
      | XXX   | ACTIVE | 2024-01-02 00:00:00.000 | paul.ochon@test.com |

    And the time clock set to the following value 2025-01-01T00:00:00Z

    When the user requests the endpoint to validate the account with the following verification token XXX

    Then the response status code should be 400