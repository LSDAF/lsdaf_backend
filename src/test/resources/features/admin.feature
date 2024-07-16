Feature: Admin Features

  Background:
    Given the BDD engine is ready
    And a clean database

  Scenario: Get all users
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | c9a7a074-532b-48d0-9b5a-fc5ccf8d7d6b | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
      | 0d4d1857-5890-42c5-bc74-0f336ab2b545 | c9a7a074-532b-48d0-9b5a-fc5ccf8d7d6b | 88      | 1249989      | 727896 |

    When an admin gets all the users with no sorting

    Then I should return the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | c9a7a074-532b-48d0-9b5a-fc5ccf8d7d6b | Paul ITESSE | paul.itesse@test.com |

  Scenario: Get all game saves
    Given the following users
      | id                                   | name        | email                |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  |
      | c9a7a074-532b-48d0-9b5a-fc5ccf8d7d6b | Paul ITESSE | paul.itesse@test.com |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 124          | 1072   |
      | 0d4d1857-5890-42c5-bc74-0f336ab2b545 | c9a7a074-532b-48d0-9b5a-fc5ccf8d7d6b | 88      | 1249989      | 727896 |

    When an admin gets all the game saves with no sorting

    Then I should return the following game save