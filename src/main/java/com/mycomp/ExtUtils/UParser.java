package com.mycomp.ExtUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

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

	public UParser(String xml, boolean isFile) {
		super();
		try {
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
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public UParser(URL url) throws Exception {
		super();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setCoalescing(true); // Convert CDATA to Text nodes
		factory.setNamespaceAware(false); // No namespaces: this is default
		factory.setValidating(false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);

		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = parser.parse(url.openStream());
		rootel = doc.getDocumentElement(); // корневой элемент документа//
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
		String result = null;
		try{
			if (node != null) {
				NamedNodeMap n = node.getAttributes();
				if (n.getLength() > 0)
					result = n.getNamedItem(paramName).getNodeValue();
				else
					result = "";
			}
		}catch (Exception e){
			System.out.println("Param not found");
		}

		return result;
	}

	/**
	 * Выстраивает xml лесенкой
	 * @param xml - строка xml
	 * @return возвращает отформатированную xml
	 */
	public static String formatXml(String xml){
		try{
			Transformer serializer= SAXTransformerFactory.newInstance().newTransformer();
			serializer.setOutputProperty("encoding", "utf-8");
			serializer.setOutputProperty("indent", "yes");
			serializer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount",
					"4");

			Source xmlSource=new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
			StreamResult res =  new StreamResult(new ByteArrayOutputStream());
			serializer.transform(xmlSource, res);
			return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
		}catch(Exception e){
			//TODO log error
			return xml;
		}
	}
	
}
