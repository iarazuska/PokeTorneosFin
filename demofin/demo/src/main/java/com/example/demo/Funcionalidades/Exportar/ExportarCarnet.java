package com.example.demo.Funcionalidades.Exportar;

import com.example.demo.Entidades.Carnet;
import com.example.demo.Entidades.Combate;
import com.example.demo.Entidades.Entrenador;
import com.example.demo.Entidades.Torneo;
import com.example.demo.SpreenBot.service.CarnetService;
import com.example.demo.SpreenBot.service.CombateService;
import com.example.demo.SpreenBot.service.EntrenadorService;
import com.example.demo.SpreenBot.service.TorneoService;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExportarCarnet {

    EntrenadorService entrenadorService;
    CarnetService carnetService;
    TorneoService torneoService;
    CombateService combateService;

    public ExportarCarnet(EntrenadorService entrenadorService, CarnetService carnetService, TorneoService torneoService, CombateService combateService) {
        this.carnetService = carnetService;
        this.entrenadorService = entrenadorService;
        this.combateService = combateService;
        this.torneoService = torneoService;
    }

    public void exportarCarnet(String id) {
        long idEntrenador = Long.parseLong(id);

        Carnet carnet = carnetService.buscarCarnetPorId(idEntrenador);
        Entrenador entrenador = entrenadorService.buscarEntrenadorPorId(idEntrenador);

        List<Torneo> torneo0 = new ArrayList<>(entrenador.getTorneos());

        if (torneo0.isEmpty()) {
            System.out.println("el entrenador no esta en ningun torneo");
        } else {
            Torneo torneo1 = torneo0.get(0);
            Torneo torneo = torneoService.buscarTorneoPorId(torneo1.getId());

            List<Combate> combatesFiltrados = torneo.getCombates().stream()
                    .filter(combate ->
                            (combate.getEntrenador1() != null && combate.getEntrenador1().getId().equals(idEntrenador)) ||
                                    (combate.getEntrenador2() != null && combate.getEntrenador2().getId().equals(idEntrenador))
                    )
                    .toList();

            crearXMLCarnet(carnet, "C:\\Users\\User\\Desktop\\demofin\\demo\\src\\main\\java\\com\\example\\demo\\Carnets" + entrenador.getNombre().toUpperCase() + ".xml", combatesFiltrados, torneo);
            System.out.println("carnet exportado");
        }
    }

    public void crearXMLCarnet(Carnet carnet, String archivoSalida, List<Combate> listaCombates, Torneo torneo) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element carnetElement = doc.createElement("carnet");
            doc.appendChild(carnetElement);

            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(String.valueOf(carnet.getIdEntrenador())));
            carnetElement.appendChild(idElement);

            Element fechaexpElement = doc.createElement("fechaexp");
            fechaexpElement.appendChild(doc.createTextNode(carnet.getFechaExpedicion().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            carnetElement.appendChild(fechaexpElement);

            Element entrenadorElement = doc.createElement("entrenador");
            carnetElement.appendChild(entrenadorElement);

            Element nombreEntrenadorElement = doc.createElement("nombre");
            nombreEntrenadorElement.appendChild(doc.createTextNode(carnet.getEntrenador().getNombre()));
            entrenadorElement.appendChild(nombreEntrenadorElement);

            Element nacionalidadElement = doc.createElement("nacionalidad");
            nacionalidadElement.appendChild(doc.createTextNode(carnet.getEntrenador().getNacionalidad()));
            entrenadorElement.appendChild(nacionalidadElement);

            Element hoyElement = doc.createElement("hoy");
            hoyElement.appendChild(doc.createTextNode(carnet.getFechaExpedicion().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            carnetElement.appendChild(hoyElement);

            Element puntosElement = doc.createElement("puntos");
            puntosElement.appendChild(doc.createTextNode(String.valueOf(carnet.getPuntos())));
            carnetElement.appendChild(puntosElement);

            Element torneosElement = doc.createElement("torneos");
            carnetElement.appendChild(torneosElement);

            Element torneoElement = doc.createElement("torneo");
            torneosElement.appendChild(torneoElement);

            Element nombreElement = doc.createElement("nombre");
            nombreElement.appendChild(doc.createTextNode(torneo.getNombre()));
            torneoElement.appendChild(nombreElement);

            Element regionElement = doc.createElement("region");
            regionElement.appendChild(doc.createTextNode(String.valueOf(torneo.getCodRegion())));
            torneoElement.appendChild(regionElement);

            Element combatesElement = doc.createElement("combates");
            torneoElement.appendChild(combatesElement);

            if (listaCombates != null) {
                for (Combate combate : listaCombates) {
                    Element combateElement = doc.createElement("combate");

                    Element idCombateElement = doc.createElement("id");
                    idCombateElement.appendChild(doc.createTextNode(String.valueOf(combate.getId())));
                    combateElement.appendChild(idCombateElement);

                    Element fechaCombateElement = doc.createElement("fecha");
                    fechaCombateElement.appendChild(doc.createTextNode(combate.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                    combateElement.appendChild(fechaCombateElement);

                    Element victoriaCombateElement = doc.createElement("victoria");
                    Long idGanadorCombate = combate.getGanador();
                    boolean esGanador = idGanadorCombate != null && idGanadorCombate.equals(carnet.getIdEntrenador());
                    victoriaCombateElement.appendChild(doc.createTextNode(String.valueOf(esGanador)));
                    combateElement.appendChild(victoriaCombateElement);

                    combatesElement.appendChild(combateElement);
                }
            } else {
                Element combateElement = doc.createElement("combate");

                Element idCombateElement = doc.createElement("id");
                combateElement.appendChild(idCombateElement);

                Element fechaCombateElement = doc.createElement("fecha");
                combateElement.appendChild(fechaCombateElement);

                Element victoriaCombateElement = doc.createElement("victoria");
                combateElement.appendChild(victoriaCombateElement);

                combatesElement.appendChild(combateElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivoSalida));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException | DOMException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("verifica que esten todos los datos ");
            e.printStackTrace();
        }
    }
}
