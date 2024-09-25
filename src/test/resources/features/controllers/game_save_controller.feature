Feature: GameSave Controller tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

  Scenario: A non-logged in user tries to generate a game save
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |

    When the user requests the endpoint to generate a game save with no token

    Then the response status code should be 401

  Scenario: A User creates a new GameSave
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to generate a GameSave

    Then the response status code should be 200
    And the response should have the following GameSave
      | userId                               | userEmail           | gold | diamond | emerald | amethyst | healthPoints | attack | nickname |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | paul.ochon@test.com | 0    | 0       | 0       | 0        | 10           | 1      | Player   |

  Scenario: A non-logged in user tries to update a GameSave
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 100          | 10     |

    When the user requests the endpoint to update a GameSave with no token

    Then the response status code should be 401

  Scenario: A user tries to update a GameSave with invalid id
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 100          | 10     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id d06664b0-5c4c-4d0b-a253-4f742b470bfd with the following GameSaveUpdateRequest
      | healthPoints | attack |
      | 11289        | 5000   |

    Then the response status code should be 404

  Scenario: A user tries to update a non-owned GameSave
    Given the following users
      | id                                   | name          | email                  | password        |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON    | paul.ochon@test.com    | toto1234        |
      | 91d07c02-1119-4791-bc38-8fca9c7e447c | Jean DUJARDIN | jean.dujardin@test.com | jeandujardin666 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 91d07c02-1119-4791-bc38-8fca9c7e447c | 1000 | 100          | 10     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | healthPoints | attack |
      | 11289        | 5000   |

    Then the response status code should be 403

  Scenario: A user tries to update an owned GameSave with invalid data -> invalid healthPoints
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 100          | 10     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | healthPoints | attack |
      | -11289       | 5000   |

    Then the response status code should be 400

  Scenario: A user tries to update an owned GameSave with invalid data -> invalid attack
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 100          | 10     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | healthPoints | attack |
      | 11289        | -5000  |

    Then the response status code should be 400

  Scenario: A user updates an owned GameSave with valid data
    Given the following users
      | id                                   | name       | email               | password |
      | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | Paul OCHON | paul.ochon@test.com | toto1234 |
    And the following game saves
      | id                                   | userId                               | gold | healthPoints | attack |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | 9b274f67-d8fd-4e1a-a08c-8ed9a41e1f1d | 1000 | 100          | 10     |

    When the user logs in with the following credentials
      | email               | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update a GameSave with id 0530e1fe-3428-4edd-bb32-cb563419d0bd with the following GameSaveUpdateRequest
      | healthPoints | attack |
      | 11289        | 5000   |

    Then the response status code should be 200