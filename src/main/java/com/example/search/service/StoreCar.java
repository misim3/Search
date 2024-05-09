package com.example.search.service;

import com.example.search.entity.Car;
import com.example.search.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class StoreCar {

    private final CarRepository carRepository;

    public void storeData() throws Exception {

        storeCar();
    }

    private void storeCar() throws Exception {
        String urlBuilder = "https://openapi.its.go.kr:9443/trafficInfo?apiKey=dbe5ff4cb3ba4847aa120e9d12cd7dcb&type=all&drcType=all&minX=120&maxX=140&minY=30&maxY=40&getType=xml";

        InputStream xmlStream = getXMLStream(urlBuilder);

        parseXMLCar(xmlStream);
    }

    private InputStream getXMLStream(String string) throws Exception {
        URL url = new URL(string);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    private void parseXMLCar(InputStream xmlStream) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 407600; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {
                String startNodeId = item.getElementsByTagName("startNodeId").item(0).getTextContent();
                String endNodeId = item.getElementsByTagName("endNodeId").item(0).getTextContent();
                String travelTime = item.getElementsByTagName("travelTime").item(0).getTextContent();

                findOrCreateCar(startNodeId, endNodeId, travelTime);
            }
        }
    }

    private void findOrCreateCar(String startNodeId, String endNodeId, String travelTime) {

        if (!carRepository.existsByStartNodeIdAndEndNodeId(startNodeId, endNodeId)) {
            carRepository.save(Car.builder()
                    .startNodeId(startNodeId)
                    .endNodeId(endNodeId)
                    .time(Double.parseDouble(travelTime))
                    .build());
        }
    }
}
