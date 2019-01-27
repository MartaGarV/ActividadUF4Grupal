import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.xqj.basex.BaseXXQDataSource;

public class Principal {

	public static void main(String[] args) {
		/*Declaramos una referencia a un objeto de tipo XQDataSource, que es una interfaz genérica
		implentada por varias clases. BaseXXQDataSource es una clase directamente relacionada con BaseX*/
		XQDataSource xds = new BaseXXQDataSource();
		// Objeto XQConnection para establecer la conexión
		XQConnection con;
		/*La clase XQExpresion nos permite ejecutar sentencias XQuery por medio de su método
		executeQuery()*/
		XQExpression expr;
		/*XQResultSequence, que nos permite acceso secuencial a los elementos
		resultado*/
		XQResultSequence result;
		//Variable para guardar la sentencia XQuery
		String sentencia;
		
		try {
			/*Con los métodos setProperty() estamos designando los valores para establecer la conexión con
			XBase*/
			xds.setProperty("serverName", "localhost");
			xds.setProperty("port", "1984");
			//establecemos la conexión con el método getConnection()
			con = xds.getConnection("admin", "admin");
		} catch (XQException e) {
			System.out.println("Error al establecer la conexión con BaseX");
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Establecida la conexión con BaseX");
		
		//listado de detalles de venta ordenadas por el elemento codigo
		sentencia = "for $esc in fn:collection('reciboBD')//detalle order by $esc/codigo return $esc";
		
		try {
			//Usamos el método createExpression()
			expr = con.createExpression();
			/*La clase XQExpresion nos permite ejecutar sentencias XQuery por medio de su método
			executeQuery(), que recibe como argumento una cadena con la sentencia a ejecutar y
			devuelve un objeto XQResultSequence, que nos permite acceso secuencial a los elementos
			resultado.*/
			result = expr.executeQuery(sentencia);
		} catch (XQException e) {
			System.out.println("Error al ejecutar la sentencia XQuery");
			System.out.println(e.getMessage());
			return;
		}
		
		int contador = 0;
		try {
			/*Nuestro objeto result, de tipo XQResultSequence dispone del método next() que nos permite
			ir avanzando al siguiente elemento o nodo dentro de la estructura XML resultado de la ejecución
			de la sentencia XQuery*/
			while (result.next()) {
				contador++;
				/*Obtenemos los nodos que nos interesan y los guardamos en "nodoDetalle", que pasaremos
				como argumento al método mostrarListado*/
				Node nodoDetalle = result.getNode();
				mostrarListado(nodoDetalle);
			}
		} catch (XQException e) {
			System.out.println("Error al recorrer los elementos obtenidos");
			System.out.println(e.getMessage());
		}
		//Cerramos la conexión con BaseX
		try {
			con.close();
		} catch (XQException e) {
			System.out.println("Error al cerrar la conexión con BaseX");
			System.out.println(e.getMessage());
		}
	}

	//Creamos el método mostrarListado
	private static void mostrarListado(Node nodoDetalle) {
		//Accedemos a los nodos hijos de "Detalle"
		NodeList nodos = nodoDetalle.getChildNodes();
		//Creamos las variables donde los iremos guardando según tomen valor
		String codigo = null;
		String descripcion= null;
		String unidades = null;
		String precio = null;
		
		//Recorremos y obtenemos los hijos
		for (int i=0; i<nodos.getLength();i++) {
			Node nodoHijo = nodos.item(i);
			
			if (nodoHijo.getNodeName().equals("codigo")) {
				codigo = nodoHijo.getTextContent();			
			}
			if (nodoHijo.getNodeName().equals("descripcion") ) {
				descripcion = nodoHijo.getTextContent();
				
			}
			if (nodoHijo.getNodeName().equals("unidades") ) {
				unidades = nodoHijo.getTextContent();
				
			}
			if (nodoHijo.getNodeName().equals("precio") ) {
				 precio = nodoHijo.getTextContent();
				
			}

		}//Sacamos la información por pantalla
			System.out.println(codigo + " " + descripcion +" - "+ unidades +" unidades a " + precio);
	}



}


