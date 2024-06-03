package ugis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {

	private Document doc;
	
	private XPath xpath;
	
	public XmlParser(String path) throws Exception{
		File f = new File(path);
		if(f != null && f.exists()) {
			init(new FileInputStream(f));
		} else {
			init(this.getClass().getClassLoader().getResourceAsStream(path));
		}
		
	}
	
	public XmlParser(File file) throws Exception{
		init(new FileInputStream(file));
	}
	
	public void init(InputStream fis) throws ParserConfigurationException, SAXException, IOException{
		if(fis != null && fis.available() > 0) {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			this.doc = builder.parse(fis);
			this.xpath = XPathFactory.newInstance().newXPath();
			fis.close();
		}
	}
	
	public String getString(String path) {
		String result;
		try {
			result = xpath.evaluate(path, doc, XPathConstants.STRING).toString();
		} catch (XPathExpressionException e) {
			result = null;
		}
		return result;
	}
	
	public Map<String, String> getMap(String path) {
		Map<String, String> map;
		try {
			Node node = (Node)xpath.evaluate(path, doc, XPathConstants.NODE);
			map = getMap(node);
		} catch(Exception e) {
			map = null;
		}
		return map;
	}
	
	public Map<String, String> getMap(Node node) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node c = node.getChildNodes().item(i);
				if(c.getAttributes() == null) {
					continue;
				}
				map.put(c.getNodeName(), c.getFirstChild().getNodeValue());
			}
		} catch (Exception e) {
		}
		return map;
	} 
	
	public List<Map<String, String>> getList(String path) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			NodeList node = (NodeList)xpath.evaluate(path, doc, XPathConstants.NODESET);

			for(int i = 0; i < node.getLength(); i++) {
				list.add(getMap(node.item(i)));
			}
		} catch(Exception e) {}
		return list;
	}


}
