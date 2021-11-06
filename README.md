# TP2SOA

1)Nunca muestran las metricas. ya que notamos que el metodo leerMetricas(), esta comentado.Entonces no sabemos si se estan guardando datos en un archivo. Por lo que no se puede ver si en verdad realizan la persistencia de datos.

2)Los eventos no se estan registrando en la base de datos del servidor porque estan enviandolos en el ambiente TEST y deben hacerlo en PROD.Además les esta devlviendo un código de respuesta 401 y debe ser 201. Esto ultimo error, es porque en la linea 71 del archivo Servicehttp_post ponen la palabra "Evento" con mayuscula y ustedes lo envian en miniscula.

3)No estan cumpliendo con el patron MVP, porque no estan separando bien las capas. Hay código en la clases de las activities que es de procesamiento, que deberian estar en otro clase. Ya que el procesamiento de datos deberian forma parte de la capa presentador.

TEST -> PROD
