@DEMO
Feature: Prueba de concepto SpringBoot + Cucumber - DEMO API - 1

  @DEMO
  Scenario: caso1- Obtener posts
    Given que configuro las cabeceras del servicio
      | HEADER       | VALUE            |
      | Content-Type | application/json |
    And que configuro los pathParams
      | PARAMETER | VALUE |
      | endPoint  | posts |
    When ejecuto el servicio "GET::https://jsonplaceholder.typicode.com/{endPoint}"
    Then valido que el codigo de respuesta sea 200