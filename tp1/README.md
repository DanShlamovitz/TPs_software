# TP1

Para interactuar con los modulos, se puede ejecutar lo siguiente:

`ghci` (asumiendo que usted lo tiene instalado)
`:l truck`

A partir de aquí, puede usar cualquiera de las funciones que se encuentran en el archivo `truck.hs`

ACLARACIÓN ACERCA DE LOS TESTS:

La función `loadT` sí falla cuando se intenta agregar un palet a un stack del camión cuyo último palet tiene un destino posterior al del destinio del nuevo palet. Sin embargo, la función `testF` no detecta esta excepción, ya que ocurre dentro de otra función que es llamada por `loadT`, no en `loadT` misma. Lo mismo ocurre cuando se quiere agregar al camión un palet que, al agregarse, excedería el límite de peso o de 