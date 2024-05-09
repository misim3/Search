package com.example.search.service;

import com.example.search.entity.Subway;
import com.example.search.repository.SubwayRepository;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class StoreSubway {

    private final SubwayRepository subwayRepository;

    public void storeData() throws Exception {

        storeSubway();
    }

    private void storeSubway() throws Exception {

        String urlBuilder = "http://apis.data.go.kr/1613000/SubwayInfoService/getKwrdFndSubwaySttnList" + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=snY%2FE%2Fh1clc%2FQvfB6XfZVOMyJfyzGVBzOy%2Bs4F0UCeVuXqvBB1zu8Spjz2%2FF%2F%2BBSa8oxXfpYQ%2BQYvyDX1jwZ0w%3D%3D" + "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1100", StandardCharsets.UTF_8) +
        "&" + URLEncoder.encode("_type", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("xml", StandardCharsets.UTF_8);

        InputStream xmlStream = getXMLStream(urlBuilder);

        parseXML(xmlStream);
    }

    private InputStream getXMLStream(String string) throws Exception {
        URL url = new URL(string);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    private void parseXML(InputStream xmlStream) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {

                String subwayStationId = item.getElementsByTagName("subwayStationId").item(0).getTextContent();
                String subwayStationName = item.getElementsByTagName("subwayStationName").item(0).getTextContent();
                String subwayRouteName = item.getElementsByTagName("subwayRouteName").item(0).getTextContent();

                findOrCreateSubway(subwayStationId, subwayStationName, subwayRouteName);
            }
        }
    }

    private void findOrCreateSubway(String subwayStationId, String subwayStationName, String subwayRouteName) {

        if (!subwayRepository.existsBySubwayId(subwayStationId)) {
            subwayRepository.save(Subway.builder()
                    .subwayId(subwayStationId)
                    .name(subwayStationName)
                    .routeName(subwayRouteName)
                    .build());
        }
    }
}
