Feature: Auth Controller BDD tests

  Background:
    Given the BDD engine is ready
    And a clean database
    And the time clock set to the present

    # We assume we have the two following users in keycloak
    # paul.ochon@test.com: ADMIN,USER: toto1234
    # paul.itesse@test.com: USER: toto5678

  Scenario: A user logs in with valid credentials
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    Then the response status code should be 200

  Scenario: A user logs in with valid refresh token
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user uses the previously generated refresh token to log in

    Then the response status code should be 200

  Scenario: A user logs in with invalid refresh token
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto1234 |

    And the user logs in with the following refresh token invalid_token

    Then the response status code should be 400

  Scenario: A user logs in with invalid credentials -> invalid username/password
    When the user logs in with the following credentials
      | username            | password |
      | paul.ochon@test.com | toto5678 |

    Then the response status code should be 401