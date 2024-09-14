Feature: GoldController tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database

  Scenario: A user gets the gold of one of his game saves with cache
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    And the following gold entries in cache
      | gameSaveId                           | gold     |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 56302802 |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Gold
      | id                                   | amount   |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 56302802 |

  Scenario: A user gets the gold of one of his game saves without cache
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Gold
      | id                                   | amount  |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 5630280 |

  Scenario: A user gets the gold of a game save that does not exist
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the gold of the game save with id 9d96507e-56e8-447d-aa0f-b2248ae59454

    Then the response status code should be 404

  Scenario: A user gets the gold of a non-owned game save
    Given the following users
      | id                                   | name        | email                | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 |
      | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | Paul ITESSE | paul.itesse@test.com | tutu5678 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 403

  Scenario: A user sets the gold of one of his game saves with cache
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |
    And the following gold entries in cache
      | gameSaveId                           | gold |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 666  |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1 to 1000

    Then the response status code should be 200

  Scenario: A user sets the gold of one of his game saves without cache
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1 to 1000

    Then the response status code should be 200

  Scenario: A user sets the gold of a game save that does not exist
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the gold of the game save with id 26b3ffe5-d1b5-4047-bf14-5c70c5e49f17 to 1000

    Then the response status code should be 404

  Scenario: A user sets the gold of a non-owned game save
    Given the following users
      | id                                   | name        | email                | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 |
      | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | Paul ITESSE | paul.itesse@test.com | tutu5678 |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack |
      | f81b710d-3e02-4871-a86f-390377798dd1 | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | 5630280 | 500          | 1072   |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the gold of the game save with id f81b710d-3e02-4871-a86f-390377798dd1 to 1000

    Then the response status code should be 403