
package introsde.finalproject.soap.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listMeasureDefinitionWrapper complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listMeasureDefinitionWrapper">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="measureType" type="{http://ws.soap.finalproject.introsde/}measureDefinition" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listMeasureDefinitionWrapper", propOrder = {
    "measureType"
})
public class ListMeasureDefinitionWrapper {

    protected List<MeasureDefinition> measureType;

    /**
     * Gets the value of the measureType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the measureType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasureType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MeasureDefinition }
     * 
     * 
     */
    public List<MeasureDefinition> getMeasureType() {
        if (measureType == null) {
            measureType = new ArrayList<MeasureDefinition>();
        }
        return this.measureType;
    }

}
