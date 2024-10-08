Feature: Currency Controller tests

  Background:
    Given the BDD engine is ready
    And the cache is enabled
    And a clean database
    And the time clock set to the present

  Scenario: A user gets the currencies of one of his game saves with cache
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 100     | 100     | 100      | 500          | 1072   | 10       | 10           |
    And the following currency entries in cache
      | gameSaveId                           | gold     | diamond  | emerald  | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 56302802 | 56302802 | 56302802 | 56302802 |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the currencies of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Currency
      | gold     | diamond  | emerald  | amethyst |
      | 56302802 | 56302802 | 56302802 | 56302802 |

  Scenario: A user gets the currencies of one of his game saves without cache
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack | diamond | emerald | amethyst | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 100  | 500          | 1072   | 200     | 300     | 400      | 10       | 10           |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the currencies of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 200

    And the response should have the following Currency
      | gold | diamond | emerald | amethyst |
      | 100  | 200     | 300     | 400      |

  Scenario: A user gets the currencies of a game save that does not exist
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the currencies of the game save with id 9d96507e-56e8-447d-aa0f-b2248ae59454

    Then the response status code should be 404

  Scenario: A user gets the currencies of a non-owned game save
    Given the following users
      | id                                   | name        | email                | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | true    | true     | USER  |
      | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | Paul ITESSE | paul.itesse@test.com | tutu5678 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the currencies of the game save with id f81b710d-3e02-4871-a86f-390377798dd1

    Then the response status code should be 403

  Scenario: A user sets the currencies of one of his game saves with cache
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | diamond | emerald | amethyst | healthPoints | attack | maxStage | currentStage |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 5630280 | 5630280 | 5630280  | 500          | 1072   | 10       | 10           |
    And the following currency entries in cache
      | gameSaveId                           | gold |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 666  |
    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | gold | diamond | emerald | amethyst |
      | 1000 | 200     | 300     | 400      |

    Then the response status code should be 200

    And the following currency entries in cache
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1000 | 200     | 300     | 400      |


  Scenario: A user sets the currencies of one of his game saves without cache
    Given the following users
      | id                                   | name       | email               | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    And the cache is disabled

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 100  | 200     | 300     | 400      |

    Then the response status code should be 200

  Scenario: A user sets the currencies of a non-owned game save
    Given the following users
      | id                                   | name        | email                | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | true    | true     | USER  |
      | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | Paul ITESSE | paul.itesse@test.com | tutu5678 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id f81b710d-3e02-4871-a86f-390377798dd1
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | 1    | 2       | 3       | 4        |

    Then the response status code should be 403

  Scenario: A user sets the currencies of a non-existing game save
    Given the following users
      | id                                   | name        | email                | password | enabled | verified | roles |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON  | paul.ochon@test.com  | toto1234 | true    | true     | USER  |
      | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | Paul ITESSE | paul.itesse@test.com | toto1234 | true    | true     | USER  |
    And the following game saves
      | id                                   | userId                               | gold    | healthPoints | attack | maxStage | currentStage | diamond | emerald | amethyst |
      | f81b710d-3e02-4871-a86f-390377798dd1 | acaaf7b2-6ecb-4516-bdec-7c27f2fc55cd | 5630280 | 500          | 1072   | 10       | 10           | 10      | 10      | 10       |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to set the currencies with the following CurrencyRequest for the game save with id 7545eed0-237c-4182-849f-f9d4e1d112b5
      | gameSaveId                           | gold | diamond | emerald | amethyst |
      | 7545eed0-237c-4182-849f-f9d4e1d112b5 | 1    | 2       | 3       | 4        |

    Then the response status code should be 404