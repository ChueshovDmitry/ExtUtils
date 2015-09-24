package com.mycomp.ExtUtils;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с соап сообщениями
 */
public class SoapUtils {
    /**
     * Добавляет параметр guid в тег body
     * @param message
     * @param guid
     * @throws Exception
     */
	public static void setGuidFromTag(SOAPMessage message, String guid) throws Exception
    {
			//Iterator elemIter =  message.getSOAPBody().getChildElements();
			//Element elme = (Element) elemIter.next();
    		SOAPBody elem =  message.getSOAPBody();
    		elem.setAttribute("guid",guid);
    }

    /**
     * Преобразует строку в soap-message
      * @param Smessage
     * @return
     * @throws Exception
     */
	public static SOAPMessage getSOAPMessageFromString(String Smessage) throws Exception {
        MessageFactory msgFactory = MessageFactory.newInstance();
        SOAPMessage message = msgFactory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();
        // Load the SOAP text into a stream source
        byte[] buffer = Smessage.getBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        StreamSource source = new StreamSource(stream);
        // Set contents of message
        soapPart.setContent(source);
        stream.close();
        return message;
    }

    /**
     * Преобрвзует soap-message в строку
     * @param Smessage
     * @return
     * @throws Exception
     */
	public static String getStringFromSOAPMessage(SOAPMessage Smessage) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Smessage.writeTo(out);
        String rez = new String(out.toByteArray());
        out.close();
        return rez;
    }

    /**
     * меняет значение параметра
     * @param message
     * @param nameParam
     * @param idCall
     * @throws DOMException
     * @throws SOAPException
     * @throws IOException
     */
	public static void setParam(SOAPMessage message,String nameParam,String idCall) throws DOMException, SOAPException, IOException {
        if (message == null) return;
        Node nd = message.getSOAPBody().getChildNodes().item(0);
        NodeList nodelist = message.getSOAPBody().getElementsByTagName(nameParam);
        if (nodelist.getLength() == 0){
            Element el = message.getSOAPBody().getOwnerDocument().createElement(nameParam);
            nd.appendChild(el);
        }
        NodeList nl = nd.getChildNodes();
        Node nd2 = null;
        for (int i = 0; i < nl.getLength(); i++) {
            nd2 = nl.item(i);
            if (nd2.getNodeName().equals(nameParam)) {
                nd2.setTextContent(idCall);
                break;
            }
        }
    }

    /**
     * Получает значение тега
     * @param message
     * @param nodeName
     * @return
     * @throws Exception
     */
	public static String getNodeVal(SOAPMessage message, String nodeName) throws Exception{
        String rez = "";
        NodeList nl = message.getSOAPBody().getElementsByTagName(nodeName);
        if (nl.getLength() > 0) {
            rez = nl.item(0).getTextContent();
        }
        return rez;
    }

    /**
     * устанавливает значение тега
     * @param message
     * @param nodeName
     * @param value
     * @throws Exception
     */
	public static void setNodeVal(SOAPMessage message, String nodeName, String value) throws Exception{
        NodeList nl = message.getSOAPBody().getElementsByTagName(nodeName);
        if (nl.getLength() > 0) {
            nl.item(0).setTextContent(value);
        }
	}

    /**
     * ???
     * @param message
     * @param nodeName
     * @return
     * @throws Exception
     */
	public static List<String> getNodeValList(SOAPMessage message, String nodeName) throws Exception{
        List<String> rez = new ArrayList<String>();
        NodeList nl = message.getSOAPHeader().getElementsByTagName(nodeName);
        if (nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                rez.add(nl.item(i).getTextContent());
            }
        }
        return rez;
    }

    /**
     * Получает наименование метода соап запроса
     * @param message
     * @return
     * @throws Exception
     */
	public static String getMethodName(SOAPMessage message) throws Exception{
        String rez = "";
        rez = message.getSOAPBody().getFirstChild().getLocalName();
        return rez;
    }

    /**
     * получает кодировку soap-message
     * @param msg
     * @return
     * @throws SOAPException
     */
    public static String getMessageEncoding(SOAPMessage msg) throws SOAPException {
        String encoding = "utf-8";
        if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {
            encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
        }
        return encoding;
    }
	
}
