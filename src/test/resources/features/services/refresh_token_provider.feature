Feature: Refresh Token Provider tests

  Background:
    Given the BDD engine is ready
    And a clean database


  Scenario: Save a refresh token in database
    Given the following users
      | id                                   | name       | email               | password | roles      |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN |

    And the following refresh tokens
      | refreshToken | status | userEmail | expirationDate |

    When we save the following refresh token XXX for the user with email paul.ochon@test.com

    Then I should have an unexpired and ACTIVE refresh token in DB for the user with email paul.ochon@test.com


  Scenario: Invalidate a refresh token for a non-existing user
    Given the following users
      | id                                   | name       | email               | password | roles      |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN |
    And the following refresh tokens
      | refreshToken | status | userEmail            | expirationDate          |
      | XXX          | ACTIVE | paul.ochon@test.com | 2070-12-12 00:00:00.000 |
    When we want to invalidate the token of the user with email paul.itesse@test.com

    Then I should throw a NotFoundException

  Scenario: Invalidate a refresh token for a user
    Given the following users
      | id                                   | name       | email               | password | roles      |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | USER,ADMIN |

    And the following refresh tokens
      | refreshToken | status | userEmail           | expirationDate          |
      | XXX          | ACTIVE | paul.ochon@test.com | 2070-12-12 00:00:00.000 |
    When we want to invalidate the token of the user with email paul.ochon@test.com

    Then I should have an unexpired and INACTIVE refresh token in DB for the user with email paul.ochon@test.com

  Scenario: Delete expired tokens
    Given the following users
      | id                                   | name        | email                | password | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | USER  |
      | 79bf4708-a93b-4e76-83c0-c528d06b87b5 | Paul ITESSE | paul.itesse@test.com | toto1234 | USER  |
      | af36b882-9186-4098-9b80-360bfa786f43 | Paul EMPLOI | paul.emploi@test.com | toto1234 | USER  |
      | 74e825da-d443-40f9-bf40-95a74ba951bf | Paul AIRE   | paul.aire@test.com   | toto1234 | USER  |
      | 8aeb8c07-95f2-48d4-a407-2c57b9bde55e | Paul ITIQUE | paul.itique@test.com | toto1234 | USER  |
      | 454c8f96-2f9c-4362-8a73-3017131a55a4 | Paul ISSE   | paul.isse@test.com   | toto1234 | USER  |

    And the following refresh tokens
      | refreshToken | status   | userEmail            | expirationDate          |
      | XXX          | ACTIVE   | paul.ochon@test.com  | 2070-12-12 00:00:00.000 |
      | YYY          | INACTIVE | paul.itesse@test.com | 2022-12-12 00:00:00.000 |
      | ZZZ          | ACTIVE   | paul.emploi@test.com | 2070-12-12 00:00:00.000 |
      | AAA          | INACTIVE | paul.aire@test.com   | 2015-12-12 00:00:00.000 |
      | BBB          | ACTIVE   | paul.itique@test.com | 2070-12-12 00:00:00.000 |
      | CCC          | ACTIVE   | paul.isse@test.com   | 2070-12-12 00:00:00.000 |

    When we want to delete the expired tokens

    Then I should have the following refresh tokens in DB
      | refreshToken | status | userEmail            | expirationDate          |
      | XXX          | ACTIVE | paul.ochon@test.com  | 2070-12-12 00:00:00.000 |
      | ZZZ          | ACTIVE | paul.emploi@test.com | 2070-12-12 00:00:00.000 |
      | BBB          | ACTIVE | paul.itique@test.com | 2070-12-12 00:00:00.000 |
      | CCC          | ACTIVE | paul.isse@test.com   | 2070-12-12 00:00:00.000 |