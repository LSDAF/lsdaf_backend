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
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd is set to empty

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

    And the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd should be empty

  Scenario: A user requests its inventory when it is not empty
    Given the following game saves
      | id                                   | userEmail           | gold | diamond | emerald | amethyst | currentStage | maxStage | nickname | attack | critChance | critDamage | health | resistance |
      | 0530e1fe-3428-4edd-bb32-cb563419d0bd | paul.ochon@test.com | 1000 | 1000    | 1000    | 1000     | 1000         | 1000     | test-1   | 1100   | 1200       | 1300       | 1400   | 1500       |

    And the following items to the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd
      | id                                   | itemType   |
      | 5cc47612-03fa-4622-89e1-d77d847155e3 | BOOTS      |
      | f8c27189-a51f-405a-9ec1-bf800345352b | CHESTPLATE |
      | d139d558-ae47-48b2-b7dc-857c1f18118f | GLOVES     |
      | 0d87867b-5e35-4578-b96e-b6a8af900298 | HELMET     |
      | 6a3e8cde-c176-4935-baa0-8eec1bf69037 | SHIELD     |
      | 5e275485-57ef-430d-8624-031ab23e98d4 | SWORD      |

    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user requests the endpoint to get the inventory of the game save with id 0530e1fe-3428-4edd-bb32-cb563419d0bd

    Then the response status code should be 200

    And the response should have the following items in the inventory
      | id                                   | itemType   |
      | 5cc47612-03fa-4622-89e1-d77d847155e3 | BOOTS      |
      | f8c27189-a51f-405a-9ec1-bf800345352b | CHESTPLATE |
      | d139d558-ae47-48b2-b7dc-857c1f18118f | GLOVES     |
      | 0d87867b-5e35-4578-b96e-b6a8af900298 | HELMET     |
      | 6a3e8cde-c176-4935-baa0-8eec1bf69037 | SHIELD     |
      | 5e275485-57ef-430d-8624-031ab23e98d4 | SWORD      |

