import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GEDCOMToXML {

	
	//Constructor
	public GEDCOMToXML(){

		writeToXML();
	}
	

	//Write to file XML
	public void writeToXML(){
		
		 String s1 = "";
		 String s2 = "";
		 String s3 = "";
		 Element lastElement= null;
		
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbElement;
		BufferedReader brRead=null;

		try{
			dbElement = dbfactory.newDocumentBuilder();
			
			//Create the root
			Document docRoot = dbElement.newDocument();
			Element rootElement = docRoot.createElement("ROYAL");
			docRoot.appendChild(rootElement);
			
			//Create elements
			
			//Element fam = docRoot.createElement("FAM");
			Element e= null;
			brRead = new BufferedReader(new FileReader("complet.ged"));
			String line="";
			while((line = brRead.readLine()) != null){
				String lineTrim = line.trim();
				String str[] = lineTrim.split(" ");
				//System.out.println("length = "+str.length);

				if(str.length == 2){
					s1 = str[0];
					s2 = str[1];
					s3 = "";
				}else if(str.length ==3){
					s1 = str[0];
					s2="";

					s2 = str[1];
//					System.out.println("s2="+s2);
					s3="";
					s3 = str[2];
//					s3 = s[0];
					
				}else if(str.length >3){
					s1 = str[0];
					s2 = str[1];
					s3="";
					for(int i =2; i<str.length; i++){
						s3 = s3 + str[i]+ " ";
					}
				}
				//System.out.println(s1+"!"+s2+"!"+s3+"!");
				//Write to file xml
				//writeToXML(s1, s2, s3);
			//Element indi = docRoot.createElement("INDI");	
			//System.out.println("Check0 :" + s1);
			if( Integer.parseInt(s1)==0){
				
				//System.out.println("Check1");
				if(s3.equalsIgnoreCase("INDI")){
					//System.out.println("Check2");
					System.out.println("This is a famille Individual!");
					e = docRoot.createElement("INDI");
					rootElement.appendChild(e);
					
					//Set attribute to INDI
					Attr indiAttr = docRoot.createAttribute("id");
					s2 = s2.substring(1, s2.length()-1);
					indiAttr.setValue(s2);
					e.setAttributeNode(indiAttr);
					lastElement = e;
					
				}if(s3.equalsIgnoreCase("FAM")){
					//System.out.println("Check3");
					System.out.println("This is a famille!");
					e = docRoot.createElement("FAM");
					rootElement.appendChild(e);
					//Set attribute to FAM
					Attr famAttr = docRoot.createAttribute("id");
					s2 = s2.substring(1, s2.length()-1);
					famAttr.setValue(s2);
					e.setAttributeNode(famAttr);
					lastElement = e;
				}if(s2.equalsIgnoreCase("HEAD")){
					System.out.println("This is a head!");
					e = docRoot.createElement("HEAD");
					rootElement.appendChild(e);
					lastElement = e;
					
				}if(s3.equalsIgnoreCase("SUBM")){

					System.out.println("This is a subm!");
					e = docRoot.createElement("SUBM");
					rootElement.appendChild(e);
					//Set attribute to FAM
					Attr famAttr = docRoot.createAttribute("id");
					s2 = s2.substring(1, s2.length()-1);
					famAttr.setValue(s2);
					e.setAttributeNode(famAttr);
					lastElement = e;
				}
			}
			if(Integer.parseInt(s1)==1){

				String child = s2;
				if(child.equalsIgnoreCase("SOUR")||child.equalsIgnoreCase("DEST")||child.equalsIgnoreCase("DATE")
						||child.equalsIgnoreCase("FILE")||child.equalsIgnoreCase("CHAR")
						||child.equalsIgnoreCase("NAME")||child.equalsIgnoreCase("TITL")||child.equalsIgnoreCase("SEX")||child.equalsIgnoreCase("REFN")
						||child.equalsIgnoreCase("PHON")||child.equalsIgnoreCase("DIV")){
					String name = lastElement.getNodeName();
					if(name.equalsIgnoreCase("INDI")||name.equalsIgnoreCase("HEAD")||name.equalsIgnoreCase("FAM")||name.equalsIgnoreCase("SUBM")){	
						e= docRoot.createElement(child);
						e.setTextContent(s3);
						
						lastElement.appendChild(e);
						}else{
							Node p = e.getParentNode();
							Element x= docRoot.createElement(child);
							x.setTextContent(s3);
							p.appendChild(x);
						}
				}
				if(child.equalsIgnoreCase("BIRT")||child.equalsIgnoreCase("DEAT")||child.equalsIgnoreCase("COMM")
						||child.equalsIgnoreCase("BURI")||child.equalsIgnoreCase("ADDR")||child.equalsIgnoreCase("CHR")){
					
					String name = lastElement.getNodeName();	
					if(name.equalsIgnoreCase("INDI")||name.equalsIgnoreCase("FAM")||name.equalsIgnoreCase("SUBM")){	
					e= docRoot.createElement(child);
					e.setTextContent(s3);
					
					lastElement.appendChild(e);
					lastElement = e;
					}else{
						Node p = e.getParentNode();
						Element x= docRoot.createElement(child);
						x.setTextContent(s3);
						p.appendChild(x);
						lastElement = x;
					}
				}
				if(child.equalsIgnoreCase("FAMS")||child.equalsIgnoreCase("FAMC")){
					String name = lastElement.getNodeName();
					if(name.equalsIgnoreCase("INDI")||name.equalsIgnoreCase("FAM")){	
						e= docRoot.createElement(child);
						s3 = s3.substring(1, s3.length()-1);
						e.setAttribute("id",s3);
						lastElement.appendChild(e);
						lastElement = e;
						}else{
							Node p = e.getParentNode();
							Element x= docRoot.createElement(child);
							s3 = s3.substring(1, s3.length()-1);
							x.setAttribute("id",s3);
							p.appendChild(x);
							lastElement = x;
						}
					
				}
				if(child.equalsIgnoreCase("HUSB")||child.equalsIgnoreCase("WIFE")||child.equalsIgnoreCase("CHIL")){
					e= docRoot.createElement(child);
					s3 = s3.substring(1, s3.length()-1);
					e.setAttribute("id",s3);
					lastElement.appendChild(e);
				}
				if(child.equalsIgnoreCase("MARR")){
					e= docRoot.createElement(child);
					lastElement.appendChild(e);
					lastElement = e;
				}
			}
			if(Integer.parseInt(s1)==2){
				String lastName = lastElement.getNodeName();
				if((lastName.equalsIgnoreCase("BIRT"))||(lastName.equalsIgnoreCase("DEAT"))||(lastName.equalsIgnoreCase("BURI"))
						||(lastName.equalsIgnoreCase("MARR"))||(lastName.equalsIgnoreCase("CHR"))){
					//Add child nodes to birt, deat or marr
					if(s2.equalsIgnoreCase("DATE")){
						Element date = docRoot.createElement("DATE");
						date.setTextContent(s3);
						lastElement.appendChild(date);
					}
					if(s2.equalsIgnoreCase("PLAC")){
						Element plac = docRoot.createElement("PLAC");
						plac.setTextContent(s3);
						lastElement.appendChild(plac);
					}

				}
				if(lastName.equalsIgnoreCase("COMM")||lastName.equalsIgnoreCase("ADDR")){
					if(s2.equalsIgnoreCase("CONT")){
						Element plac = docRoot.createElement("CONT");
						plac.setTextContent(s3);
						lastElement.appendChild(plac);
					}
				}
			}
			//lastElement = e;
			}

			
			
			//Saved this element for the next step
			
			//Write to file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Royal.dtd");
			DOMSource source = new DOMSource(docRoot);
			StreamResult result = new StreamResult(new File("complet.xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);	 
			transformer.transform(source, result);
	 
			System.out.println("\nXML DOM Created Successfully.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		new GEDCOMToXML();
	}
}
