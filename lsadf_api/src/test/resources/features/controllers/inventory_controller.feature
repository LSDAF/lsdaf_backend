Feature: Inventory Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user requests its inventory when it is empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | id                                   | itemType   |

    And the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa should be empty

  Scenario: A user requests its inventory when it is not empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | BOOTS      |
      | 22222222-2222-2222-2222-222222222222 | CHESTPLATE |
      | 33333333-3333-3333-3333-333333333333 | GLOVES     |
      | 44444444-4444-4444-4444-444444444444 | HELMET     |
      | 55555555-5555-5555-5555-555555555555 | SHIELD     |
      | 66666666-6666-6666-6666-666666666666 | SWORD      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | BOOTS      |
      | 22222222-2222-2222-2222-222222222222 | CHESTPLATE |
      | 33333333-3333-3333-3333-333333333333 | GLOVES     |
      | 44444444-4444-4444-4444-444444444444 | HELMET     |
      | 55555555-5555-5555-5555-555555555555 | SHIELD     |
      | 66666666-6666-6666-6666-666666666666 | SWORD      |

  Scenario: A user requests to update an item in its inventory
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | BOOTS      |
      | 22222222-2222-2222-2222-222222222222 | CHESTPLATE |
      | 33333333-3333-3333-3333-333333333333 | GLOVES     |
      | 44444444-4444-4444-4444-444444444444 | HELMET     |
      | 55555555-5555-5555-5555-555555555555 | SHIELD     |
      | 66666666-6666-6666-6666-666666666666 | SWORD      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | itemId                               | itemType   |
      | 11111111-1111-1111-1111-111111111111 | SWORD      |

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | SWORD      |

  Scenario: A user requests to update an item in its inventory with invalid item id
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | BOOTS      |
      | 22222222-2222-2222-2222-222222222222 | CHESTPLATE |
      | 33333333-3333-3333-3333-333333333333 | GLOVES     |
      | 44444444-4444-4444-4444-444444444444 | HELMET     |
      | 55555555-5555-5555-5555-555555555555 | SHIELD     |
      | 66666666-6666-6666-6666-666666666666 | SWORD      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item in the inventory of the game save with id aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa with the following ItemUpdateRequest
      | itemId                           | itemType   |
      | invalid_item_id                  | BOOTS      |

    Then the response status code should be 400

  Scenario: A user requests to update an item from the inventory of another user
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |
      | bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb | paul.itesse@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-2   | 600    | 700        | 800        | 900    | 1000       |

    And the following items to the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
      | id                                   | itemType   |
      | 11111111-1111-1111-1111-111111111111 | BOOTS      |
      | 22222222-2222-2222-2222-222222222222 | CHESTPLATE |
      | 33333333-3333-3333-3333-333333333333 | GLOVES     |
      | 44444444-4444-4444-4444-444444444444 | HELMET     |
      | 55555555-5555-5555-5555-555555555555 | SHIELD     |
      | 66666666-6666-6666-6666-666666666666 | SWORD      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to update an item in the inventory of the game save with id bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb with the following ItemUpdateRequest
      | itemId                               | itemType   |
      | 11111111-1111-1111-1111-111111111111 | CHESTPLATE |

    Then the response status code should be 401