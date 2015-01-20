package com.mycomp.ExtUtils;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Набор нескольких процедур облегчающих работу с xml
 * @author kondakov
 *
 */
public class UParser {
	
private Element rootel = null;
private static String errorMessge;
private static String errorTrace;
	
	public UParser()
	{
		super();
	}
	
	public UParser(String xml, boolean isFile) throws Exception {
		super();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setCoalescing(true); // Convert CDATA to Text nodes
		factory.setNamespaceAware(false); // No namespaces: this is default
		factory.setValidating(false);
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = null;
		if(isFile){
			File fileXml = new File(xml);
			if(!fileXml.exists()){
				errorMessge = "Ошибка: Файл "+xml+" не существует!";
				System.out.println(errorMessge);
				return;
			}
			doc = parser.parse(fileXml);
			rootel = doc.getDocumentElement(); // корневой элемент документа//
		}else{
			StringReader streamXml = new StringReader(xml); // создаем поток
			InputSource fileXml = new InputSource();
			fileXml.setCharacterStream(streamXml);
			fileXml.setEncoding("UTF-8");
			DocumentBuilder xmldoc;
			xmldoc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = xmldoc.parse(fileXml);
			rootel = doc.getDocumentElement(); // корневой элемент документа//
		}
	}
	
	/**
	 * Метод возвращает рутовы элемент xml
	 * @return
	 */	
	public Element getRootel() {
		return rootel;
	}
	/**
	 * возвращает текст ошибки
	 * @return
	 */
	public String getErrorMessge() {
		return errorMessge;
	}
	/**
	 * возвращает стек ошибки
	 * @return
	 */
	public String getErrorTrace() {
		return errorTrace;
	}

	/**
	 * Метод производит рекурсивный поиск нода 
	 * @param sNode - родительский нод
	 * @param name - имя искомого нода
	 * @return возвращает нод
	 */
	public static Node searchNode(Node sNode, String name) 
	{
		Node result = null;
		NodeList nodelist = sNode.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node n = nodelist.item(i);
			if (n.getNodeName().equals(name)) {
				return n;
			} else {
				result = searchNode(n, name);
				if (result != null)
					if (result.getNodeName().equals(name)) {
						return result;
					}
			}
		}
		return result;
	}

	/**
	 * Метод читает атрибуты нода
	 * @param node - нод
	 * @param paramName - наименование атрибута
	 * @return возвращает значение атрибута
	 */
	public static String paramValue(Node node, String paramName) 
	{
		String result = "";
		if (node != null) {
			NamedNodeMap n = node.getAttributes();
			if (n.getLength() > 0)
				result = n.getNamedItem(paramName).getNodeValue();
			else
				result = "";
		}
		return result;
	}
	
}
