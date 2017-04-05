# Extra/plus
1) Cuando no hay ningun empleado libre se rechaza la llamada con un mensaje al usuario de que lo llamaremos mas tarde.
2) Cuando entran mas de 10 llamadas concurrentes una opcion podria ser modificar el rejection handler para que 
elimine de la work queue las tasks mas viejas y reintente con las mas nuevas.
3) La mayoria de las clases son simples e immutables por lo que podrian ser autogeneradas por tools como auto o immutables
de esta forma se evitaria realziar una gran cantidad de test unitarios ya que lo importante es testear que el generador 
este bien testeado
4) La codigo deberia ser facil de leer y la documentacion agregada donde sea sumamente necesaria 
(e.g.: deciciones tecnicas, posibles problemas etc.)
