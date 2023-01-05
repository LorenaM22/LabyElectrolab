# ELECTROLAB

En este repositorio los usuarios del maletín ElectroLab encontraran lo necesario para empezar a conocer de una manera interactiva el funcionamiento
y los componentes de este. Para ello hemos creado una aplicación en Android Studio que permite  enfocar con la cámara el maletín 
de forma que una vez detectado este se superpone un mapa de los componentes del maletín (permitiendo seleccionar estos). Si uno de ellos es seleccionado, 
se mostrará un pequeño cuadro de diálogo donde se explicará el componente y se dará la posibilidad de ejecutar un ejemplo asociado a este. 
En esta última acción se ve involucadro el maletín, por lo que el usuario deberá instalar un script que aportamos en este repositorio.


## Instalación

1. En la carpeta ElectroLab/app/src encontramos todo lo necesario a copiar en un proyecto en blanco (empty) en la aplicación de escritorio Android Studio, en la misma ruta que en este repositorio (NewProyect/app/src). De forma que para instalar la app en el teléfono solo deberá conectar el teléfono al ordenador y seguir los pasos indicados por Android Studio para subir la aplicación a su móvil.
2. En la carpeta Arduino encontramos el script  necesario a instalar el maletín.

## Detalles técnicos

Nuestro proyecto está formado por dos elementos, los cuales se comunican entre sí. El primero es la aplicación instalada en el teléfono móvil del usuario y el segundo es el propio maletín Electrolab.
La aplicación móvil ha sido implementada en Android Studio y está formada por 3 actividades que se ejecutan de manera secuencial. La primera de ellas la hemos denominado Monólogo, ya que se basa en un monólogo realizado por el personaje principal de la aplicación, Laby, el cual explica al usuario el modo de empleo de esta. Aunque este monólogo puede ser saltado por el usuario presionando el botón inferior de la pantalla para pasar directamente a la siguiente tarea si no desea oír la explicación.
 
La segunda actividad está relacionada con la comunicación entre la aplicación y el maletín. Como se ha comentado con anterioridad estos se comunican entre sí para ejecutar diversos ejemplos desde la aplicación en el maletín para ampliar el conocimiento del usuario. Antes se indicaba que se emplearía tanto el Bluetooth como los infrarrojos, pero finalmente solo se ha implementado la comunicación vía Bluetooth ya que esta va a ser una comunicación unidireccional (de la aplicación al maletín). Como es necesario, antes de empezar la comunicación ambos elementos deben vincularse el uno con el otro, por eso se encuentra esta segunda actividad en la aplicación. En ella se muestra la lista de dispositivos con Bluetooth activado para que el usuario escoja el Bluetooth del maletín. 

 
Una vez que estén los vinculados, la aplicación ejecutará la tercera actividad de la aplicación que realmente es la actividad principal, ya que en ella se desarrolla la mayor parte de las acciones. Lo primero que realiza esta actividad es activar la cámara del móvil y a continuación superpone a ella un rectángulo negro dentro del cual se debe situar el maletín.
 
De esta forma, cuando el usuario situé el maletín con las pegatinas verdes en sus esquinas, la aplicación las detecte y superponga la plantilla con las figuras geométricas correspondientes a los diferentes elementos del maletín a explicar. 
 
Una vez haya aparecido esta plantilla el usuario puede interaccionar con ella pulsando los diversos elementos, así Laby (que se encuentra en la parte superior de la pantalla) modificará su dialogo para aportar información del elemento seleccionado. Además, el usuario podrá pulsar el botón “Ir a ejemplo” (que aparecerá en la pantalla junto con el botón “Atrás” a la vez que lo haga la plantilla) para ejecutar un ejemplo de uso de ese elemento en el maletín.
 	  
Como se acaba de mencionar, en la pantalla se encuentra el botón “Atrás” que permite al usuario deseleccionar los elementos de la plantilla. Finalmente, se debe saber que si se deja de enfocar al maletín con la cámara la plantilla dejará de aparecer y por tanto no se podrá interaccionar más con ella hasta que se vuelva a enfocar al maletín y se detecten las esquinas verdes de este.
Por otro lado, en el maletín ElectroLab se ha activado el módulo Bluetooth para poder recibir los comandos correspondientes enviados por la aplicación de Android Studio una vez se selecciona un elemento y se pulsa el botón “Ir al ejemplo” de la aplicación móvil. Los comandos enviados están formados por una letra en mayúsculas, las cuales en el maletín ElectroLab se reciben como el carácter ASCII correspondiente. Por lo tanto, una vez recibida, se realizará la ejecución correspondiente. En la siguiente tabla se muestra la letra, junto con el carácter ASCII asociado y la tarea que realiza en el maletín.
LETRA | ASCII	TAREA
“L”, 76	Parpadeo de dos LEDs
“P”, 80	Muestra de mensajes por pantalla, activación de sensor de temperatura y humedad y muestra de lo obtenido por el sensor en la pantalla
“B”, 66	Reproducción del sonido de la sintonía de Mario Bros
“S”, 83	Movimiento del servo a distintos ángulos 
“M”, 77	Muestra de diversas figuras en la matriz de LEDs


