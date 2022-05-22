package servidorXat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "xat")
@XmlType(propOrder= {"paraula"})

public class Nomina {
	
	private String paraula;
	
	public Nomina() {
		super();
	}

	@XmlElement(name="paraula")
	public String getParaula() {
		return paraula;
	}

	public void setParaula(String paraula) {
		this.paraula = paraula;
	}
	
}