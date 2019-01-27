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
		/*Declaramos una referencia a un objeto de tipo XQDataSource, que es una interfaz gen�rica
		implentada por varias clases. BaseXXQDataSource es una clase directamente relacionada con BaseX*/
		XQDataSource xds = new BaseXXQDataSource();
		// Objeto XQConnection para establecer la conexi�n
		XQConnection con;
		/*La clase XQExpresion nos permite ejecutar sentencias XQuery por medio de su m�todo
		executeQuery()*/
		XQExpression expr;
		/*XQResultSequence, que nos permite acceso secuencial a los elementos
		resultado*/
		XQResultSequence result;
		//Variable para guardar la sentencia XQuery
		String sentencia;
		
		try {
			/*Con los m�todos setProperty() estamos designando los valores para establecer la conexi�n con
			XBase*/
			xds.setProperty("serverName", "localhost");
			xds.setProperty("port", "1984");
			//establecemos la conexi�n con el m�todo getConnection()
			con = xds.getConnection("admin", "admin");
		} catch (XQException e) {
			System.out.println("Error al establecer la conexi�n con BaseX");
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Establecida la conexi�n con BaseX");
		
		//listado de detalles de venta ordenadas por el elemento codigo
		sentencia = "for $esc in fn:collection('reciboBD')//detalle order by $esc/codigo return $esc";
		
		try {
			//Usamos el m�todo createExpression()
			expr = con.createExpression();
			/*La clase XQExpresion nos permite ejecutar sentencias XQuery por medio de su m�todo
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
			/*Nuestro objeto result, de tipo XQResultSequence dispone del m�todo next() que nos permite
			ir avanzando al siguiente elemento o nodo dentro de la estructura XML resultado de la ejecuci�n
			de la sentencia XQuery*/
			while (result.next()) {
				contador++;
				/*Obtenemos los nodos que nos interesan y los guardamos en "nodoDetalle", que pasaremos
				como argumento al m�todo mostrarListado*/
				Node nodoDetalle = result.getNode();
				mostrarListado(nodoDetalle);
			}
		} catch (XQException e) {
			System.out.println("Error al recorrer los elementos obtenidos");
			System.out.println(e.getMessage());
		}
		//Cerramos la conexi�n con BaseX
		try {
			con.close();
		} catch (XQException e) {
			System.out.println("Error al cerrar la conexi�n con BaseX");
			System.out.println(e.getMessage());
		}
	}

	//Creamos el m�todo mostrarListado
	private static void mostrarListado(Node nodoDetalle) {
		//Accedemos a los nodos hijos de "Detalle"
		NodeList nodos = nodoDetalle.getChildNodes();
		//Creamos las variables donde los iremos guardando seg�n tomen valor
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

		}//Sacamos la informaci�n por pantalla
			System.out.println(codigo + " " + descripcion +" - "+ unidades +" unidades a " + precio);
	}



}


