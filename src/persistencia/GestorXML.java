package persistencia;

import components.Avio;
import components.Ruta;
import components.Tripulant;
import java.io.FileWriter;
import java.util.Iterator;
import nu.xom.*;
import principal.Companyia;
import principal.Component;
import principal.GestioVolsExcepcio;
import principal.Vol;
import components.Classe;
import components.RutaIntercontinental;
import components.RutaInternacional;
import components.RutaNacional;
import components.RutaTransoceanica;
import components.TCP;
import components.TripulantCabina;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author cesca
 */
public class GestorXML implements ProveedorPersistencia {

    private Document doc;
    private Companyia companyia;

    public Companyia getCompanyia() {
        return companyia;
    }

    public void setCompanyia(Companyia pCompanyia) {
        companyia = pCompanyia;
    }

    public void desarDades(String nomFitxer, Companyia pCompanyia) throws GestioVolsExcepcio {
        construirModel(pCompanyia);
        desarModel(nomFitxer);
    }

    public Companyia carregarDades(String nomFitxer) throws GestioVolsExcepcio {
        carregarFitxer(nomFitxer);
        obtenirDades();
        return companyia;
    }

    /*Paràmetres: Companyia a partir de la qual volem construir el model
     *
     *Acció: 
     *Llegir els atributs de l'objecte Companyia passat per paràmetre per construir
     *un model (document XML) sobre el Document doc (atribut de GestorXML).
     *L'arrel del document XML és "companyia" i heu d'afegir-ne els valors de 
     *codi i nom com atributs. Aquesta arrel, l'heu d'afegir a doc.
     *
     *Un cop fet això, heu de recórrer l'ArrayList elements de Companyia i per 
     *a cada element, afegir un fill a doc. Cada fill tindrà com atributs els 
     *atributs de l'objecte (codi, nom, fabricant, …)
     *
     *Si es tracta d'un avio, a més, heu d'afegir fills addicionals amb els 
     *valors de les classes d'aquest avio. 
     *
     *Si es tracta d'un vol, a més, heu d'afegir fills addicionals amb els 
     *valors dels tripulants d'aquest vol. En el cas de l'atribut avio, heu d'assignar-li
     *el codi de l'avio del vol, i en el cas del cap dels TCP, el passport del cap.
     *
     *Retorn: cap
     */
    private void construirModel(Companyia pCompanyia) {
        Element arrel = new Element("companyia");
        for (Component c : pCompanyia.getComponents()) {
            Element fill = null;
            if (c instanceof Avio) {
                fill = new Element("avio");
                fill.addAttribute(new Attribute("codi", ((Avio) c).getCodi()));
                fill.addAttribute(new Attribute("fabricant", ((Avio) c).getFabricant()));
                fill.addAttribute(new Attribute("model", ((Avio) c).getModel()));
                fill.addAttribute(new Attribute("capacitat", String.valueOf(((Avio) c).getCapacitat())));
                Element classes = null;
                ArrayList<Classe> clas = ((Avio) c).getClasses();
                for (Classe classe : clas) {
                    classes = new Element("classe");
                    classes.addAttribute(new Attribute("nom", classe.getNom()));
                    classes.addAttribute(new Attribute("capacitat", String.valueOf(classe.getCapacitat())));
                    fill.appendChild(classes);
                }
            } else if (c instanceof Ruta) {
                if (c instanceof RutaNacional) {
                    fill = new Element("rutaNacional");
                    fill.addAttribute(new Attribute("codi", ((RutaNacional) c).getCodi()));
                    fill.addAttribute(new Attribute("aeroportOri", ((RutaNacional) c).getAeroportOri()));
                    fill.addAttribute(new Attribute("aeroportDes", ((RutaNacional) c).getAeroportDes()));
                    fill.addAttribute(new Attribute("distancia", String.valueOf(((RutaNacional) c).getDistancia())));
                    fill.addAttribute(new Attribute("pais", ((RutaNacional) c).getPais()));
                } else if (c instanceof RutaInternacional) {
                    fill = new Element("rutaInternacional");
                    fill.addAttribute(new Attribute("codi", ((RutaInternacional) c).getCodi()));
                    fill.addAttribute(new Attribute("aeroportOri", ((RutaInternacional) c).getAeroportOri()));
                    fill.addAttribute(new Attribute("aeroportDes", ((RutaInternacional) c).getAeroportDes()));
                    fill.addAttribute(new Attribute("distancia", String.valueOf(((RutaInternacional) c).getDistancia())));
                    fill.addAttribute(new Attribute("paisOri", ((RutaInternacional) c).getPaisOri()));
                    fill.addAttribute(new Attribute("paisDes", ((RutaInternacional) c).getPaisDes()));
                } else if (c instanceof RutaIntercontinental) {
                    fill = new Element("rutaIntercontinental");
                    fill.addAttribute(new Attribute("codi", ((RutaIntercontinental) c).getCodi()));
                    fill.addAttribute(new Attribute("aeroportOri", ((RutaIntercontinental) c).getAeroportOri()));
                    fill.addAttribute(new Attribute("aeroportDes", ((RutaIntercontinental) c).getAeroportDes()));
                    fill.addAttribute(new Attribute("distancia", String.valueOf(((RutaIntercontinental) c).getDistancia())));
                    fill.addAttribute(new Attribute("paisOri", ((RutaIntercontinental) c).getPaisOri()));
                    fill.addAttribute(new Attribute("paisDes", ((RutaIntercontinental) c).getPaisDes()));
                    fill.addAttribute(new Attribute("continentOri", ((RutaIntercontinental) c).getContinentOri()));
                    fill.addAttribute(new Attribute("continentDes", ((RutaIntercontinental) c).getContinentDes()));
                } else if (c instanceof RutaTransoceanica) {
                    fill = new Element("rutaTransoceanica");
                    fill.addAttribute(new Attribute("codi", ((RutaTransoceanica) c).getCodi()));
                    fill.addAttribute(new Attribute("aeroportOri", ((RutaTransoceanica) c).getAeroportOri()));
                    fill.addAttribute(new Attribute("aeroportDes", ((RutaTransoceanica) c).getAeroportDes()));
                    fill.addAttribute(new Attribute("distancia", String.valueOf(((RutaTransoceanica) c).getDistancia())));
                    fill.addAttribute(new Attribute("paisOri", ((RutaTransoceanica) c).getPaisOri()));
                    fill.addAttribute(new Attribute("paisDes", ((RutaTransoceanica) c).getPaisDes()));
                    fill.addAttribute(new Attribute("continentOri", ((RutaTransoceanica) c).getContinentOri()));
                    fill.addAttribute(new Attribute("continentDes", ((RutaTransoceanica) c).getContinentDes()));
                    fill.addAttribute(new Attribute("ocea", ((RutaTransoceanica) c).getOcea()));
                }
            } else if (c instanceof Vol) {
                fill = new Element("vol");
                fill.addAttribute(new Attribute("codi", ((Vol) c).getCodi()));
                fill.addAttribute(new Attribute("ruta", ((Vol) c).getRuta().getCodi()));
                fill.addAttribute(new Attribute("avio", ((Vol) c).getAvio().getCodi()));
                fill.addAttribute(new Attribute("dataSortida", String.valueOf(((Vol) c).getDataSortida())));
                fill.addAttribute(new Attribute("dataArribada", String.valueOf(((Vol) c).getDataArribada())));
                fill.addAttribute(new Attribute("horaSortida", String.valueOf(((Vol) c).getHoraSortida())));
                fill.addAttribute(new Attribute("horaArribada", String.valueOf(((Vol) c).getHoraArribada())));
                fill.addAttribute(new Attribute("durada", ((Vol) c).getDurada()));
                HashMap<String, Tripulant> tripu = ((Vol) c).getTripulacio();
                Set<String> passaportes = tripu.keySet();
                Iterator iter = passaportes.iterator();
                Element fill2 = null;
                while (iter.hasNext()) {
                    Tripulant tripi = (Tripulant) iter.next();
                    if (tripi instanceof TCP) {
                        fill2 = new Element("TCP");
                        fill2.addAttribute(new Attribute("passaport", ((TCP) c).getPassaport()));
                        fill2.addAttribute(new Attribute("nom", ((TCP) c).getNom()));
                        fill2.addAttribute(new Attribute("edad", String.valueOf(((TCP) c).getEdat())));
                        fill2.addAttribute(new Attribute("dataAlta", String.valueOf(((TCP) c).getDataAlta())));
                        fill2.addAttribute(new Attribute("horesVol", String.valueOf(((TCP) c).getHoresVol())));
                        fill2.addAttribute(new Attribute("rang", ((TCP) c).getRang()));
                    } else {
                        fill2 = new Element("tripulantCabina");
                        fill2.addAttribute(new Attribute("passaport", ((TripulantCabina) c).getPassaport()));
                        fill2.addAttribute(new Attribute("nom", ((TripulantCabina) c).getNom()));
                        fill2.addAttribute(new Attribute("edad", String.valueOf(((TripulantCabina) c).getEdat())));
                        fill2.addAttribute(new Attribute("dataAlta", String.valueOf(((TripulantCabina) c).getDataAlta())));
                        fill2.addAttribute(new Attribute("horesVol", String.valueOf(((TripulantCabina) c).getHoresVol())));
                        fill2.addAttribute(new Attribute("rang", ((TripulantCabina) c).getRang()));
                        fill2.addAttribute(new Attribute("barres", String.valueOf(((TripulantCabina) c).getBarres())));
                    }
                    fill.appendChild(fill2);
                }
            } else if (c instanceof Tripulant) {
                if (c instanceof TCP) {
                    fill = new Element("TCP");
                    fill.addAttribute(new Attribute("passaport", ((TCP) c).getPassaport()));
                    fill.addAttribute(new Attribute("nom", ((TCP) c).getNom()));
                    fill.addAttribute(new Attribute("edad", String.valueOf(((TCP) c).getEdat())));
                    fill.addAttribute(new Attribute("dataAlta", String.valueOf(((TCP) c).getDataAlta())));
                    fill.addAttribute(new Attribute("horesVol", String.valueOf(((TCP) c).getHoresVol())));
                    fill.addAttribute(new Attribute("rang", ((TCP) c).getRang()));
                } else if (c instanceof TripulantCabina) {
                    fill = new Element("tripulantCabina");
                    fill.addAttribute(new Attribute("passaport", ((TripulantCabina) c).getPassaport()));
                    fill.addAttribute(new Attribute("nom", ((TripulantCabina) c).getNom()));
                    fill.addAttribute(new Attribute("edad", String.valueOf(((TripulantCabina) c).getEdat())));
                    fill.addAttribute(new Attribute("dataAlta", String.valueOf(((TripulantCabina) c).getDataAlta())));
                    fill.addAttribute(new Attribute("horesVol", String.valueOf(((TripulantCabina) c).getHoresVol())));
                    fill.addAttribute(new Attribute("rang", ((TripulantCabina) c).getRang()));
                    fill.addAttribute(new Attribute("barres", String.valueOf(((TripulantCabina) c).getBarres())));
                }
            }
            arrel.appendChild(fill);
        }
    }

    private void desarModel(String rutaFitxer) throws GestioVolsExcepcio {
        try {
            FileWriter fitxer = new FileWriter(rutaFitxer, false); //Obrim fitxer per sobreescriure
            fitxer.write(doc.toXML());
            fitxer.close();
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.desar");
        }
    }

    private void carregarFitxer(String rutaFitxer) throws GestioVolsExcepcio {
        Builder builder = new Builder();
        try {
            doc = builder.build("C:\\Users\\Usuario\\Documents\\NetBeansProjects\\GestiodeVolsv3\\" + rutaFitxer);
            System.out.println(doc.toXML());
        } catch (Exception e) {
            throw new GestioVolsExcepcio("GestorXML.carregar");
        }
    }

    /*Paràmetres: cap
     *
     *Acció: 
     *El mètode obtenirDades llegeix el fitxer del disc i el carrega sobre l'atribut 
     *doc de GestorXML.
     *
     *L'objectiu és llegir el document per assignar valors als atributs de Companyia
     *(i la resta d'objectes). Per llegir els valors dels atributs del document 
     *XML, heu de fer servir el mètode getAtributeValue(). 
     *Penseu que l'arrel conté els atributs de la companyia, per tant, al accedir 
     *a l'arrel del document ja podeu crear l'objecte Companyia amb el mètode constructor 
     *escaient de la classe companyia (fixeu-vos que s’ha afgeit un de nou).
     *
     *Un cop fet això, heu de recòrrer el document i per cada fill, haureu d'afegir un
     *element a l'ArrayList components de Companyia (nouXXX(.....)). Penseu que 
     *els mètodes de la classe companyia per afegir components, els hem modificat
     *perquè es pugui afegir un component passat er paràmetre.
     *
     *Si el fill (del document) que s'ha llegit és un avió o un vol, recordeu que a més
     *d'afegir-los a la companyia, també haureu d'afegir en el l'avió les seves classes
     *i en el vol la seva tripulació.
     *
     *Retorn: cap
     */
    private void obtenirDades() {
        
    }
}
