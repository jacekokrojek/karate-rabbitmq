Feature: sample karate test script for integration with RabbitMQ

  Background:

    * def RabbitMQ = Java.type('examples.rabbitmq.RabbitMQ')
    * def rabbitMQ = new RabbitMQ()
    * rabbitMQ.sendMessage("Hello World!")
    * def handler = function(msg){ karate.signal(msg) }
    * rabbitMQ.listen(handler)

  Scenario: check if response is equal to "Hello World!"
    Then def response = karate.listen(5000)
    And match response == "Hello World!"

  Scenario: fail if response is equal to "Hello World!"
    * response = karate.listen(5000)
    * print '### received', response
    * match response != "Hello World!"