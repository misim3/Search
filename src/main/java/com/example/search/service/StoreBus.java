package com.example.search.service;

import com.example.search.entity.BusRoute;
import com.example.search.entity.BusStop;
import com.example.search.repository.BusRouteRepository;
import com.example.search.repository.BusStopRepository;
import com.example.search.repository.CityRepository;
import com.example.search.entity.City;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreBus {

    private final CityRepository cityRepository;
    private final BusStopRepository busStopRepository;
    private final BusRouteRepository busRouteRepository;

    public void storeData() throws Exception {

        storeCity();

        storeBusStop();

        storeBusRoute();

        storeBusRouteDetail();
    }

    private void storeCity() throws Exception {
        String urlBuilder = "http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getCtyCodeList" + "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=snY%2FE%2Fh1clc%2FQvfB6XfZVOMyJfyzGVBzOy%2Bs4F0UCeVuXqvBB1zu8Spjz2%2FF%2F%2BBSa8oxXfpYQ%2BQYvyDX1jwZ0w%3D%3D" +
                "&" + URLEncoder.encode("_type", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("xml", StandardCharsets.UTF_8);

        InputStream xmlStream = getXMLStream(urlBuilder);

        parseXMLCityCode(xmlStream);
    }

    private InputStream getXMLStream(String string) throws Exception {
        URL url = new URL(string);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }

    private void parseXMLCityCode(InputStream xmlStream) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {
                String cityCode = item.getElementsByTagName("citycode").item(0).getTextContent();

                int cityCodeInt = Integer.parseInt(cityCode);

                findOrCreateCity(cityCodeInt);
            }
        }
    }

    private void findOrCreateCity(int cityCode) {

        if (!cityRepository.existsByCityCode(cityCode)) {
            City city = City.builder()
                    .cityCode(cityCode)
                    .build();

            cityRepository.save(city);
        }
    }

    private void storeBusStop() throws Exception {

        List<City> cities = cityRepository.findAll();

        for (City city : cities) {
            StringBuilder builder = new StringBuilder("http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList"); /*URL*/
            builder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=snY%2FE%2Fh1clc%2FQvfB6XfZVOMyJfyzGVBzOy%2Bs4F0UCeVuXqvBB1zu8Spjz2%2FF%2F%2BBSa8oxXfpYQ%2BQYvyDX1jwZ0w%3D%3D"); /*Service Key*/
            builder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지번호*/
            builder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("10000", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            builder.append("&").append(URLEncoder.encode("_type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("xml", StandardCharsets.UTF_8)); /*데이터 타입(xml, json)*/
            builder.append("&").append(URLEncoder.encode("cityCode", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(String.valueOf(city.getCityCode()), StandardCharsets.UTF_8)); /*도시코드*/

            InputStream xmlStream = getXMLStream(builder.toString());

            parseXMLBusStop(xmlStream, city.getCityCode());
        }
    }

    private void parseXMLBusStop(InputStream xmlStream, int cityCode) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        // 'item' 태그 아래의 정보를 추출
        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {
                // 필요한 정보 추출
                String nodeId = item.getElementsByTagName("nodeid").item(0).getTextContent();
                //String gpslati = item.getElementsByTagName("gpslati").item(0).getTextContent();
                //String gpslong = item.getElementsByTagName("gpslong").item(0).getTextContent();

                findOrCreateBusStop(nodeId, cityCode);
            }
        }
    }

    private void findOrCreateBusStop(String nodeId, int cityCode) {

        if (!busStopRepository.existsByCityCodeAndNodeId(cityCode, nodeId)) {
            BusStop busStop = BusStop.builder()
                    .nodeId(nodeId)
                    .cityCode(cityCode)
                    .build();

            busStopRepository.save(busStop);
        }
    }

    private void storeBusRoute() throws Exception {

        List<City> cities = cityRepository.findAll();

        for (City city : cities) {
            StringBuilder builder = new StringBuilder("http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList"); /*URL*/
            builder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=snY%2FE%2Fh1clc%2FQvfB6XfZVOMyJfyzGVBzOy%2Bs4F0UCeVuXqvBB1zu8Spjz2%2FF%2F%2BBSa8oxXfpYQ%2BQYvyDX1jwZ0w%3D%3D"); /*Service Key*/
            builder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지번호*/
            builder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("10000", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            builder.append("&").append(URLEncoder.encode("_type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("xml", StandardCharsets.UTF_8)); /*데이터 타입(xml, json)*/
            builder.append("&").append(URLEncoder.encode("cityCode", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(String.valueOf(city.getCityCode()), StandardCharsets.UTF_8)); /*도시코드*/

            InputStream xmlStream = getXMLStream(builder.toString());

            parseXMLBusRoute(xmlStream, city.getCityCode());
        }
    }

    private void parseXMLBusRoute(InputStream xmlStream, Integer cityCode) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {

                String routeId = item.getElementsByTagName("routeid").item(0).getTextContent();
                //String routeno = item.getElementsByTagName("routeno").item(0).getTextContent();

                findOrCreateBusRoute(cityCode, routeId);
            }
        }
    }

    private void findOrCreateBusRoute(Integer cityCode, String routeId) {

        if (!busRouteRepository.existsByRouteIdAndCityCode(routeId, cityCode)) {

            BusRoute busRoute = BusRoute.builder()
                    .routeId(routeId)
                    .cityCode(cityCode)
                    .build();

            busRouteRepository.save(busRoute);
        }
    }

    private void storeBusRouteDetail() throws Exception {

        List<BusRoute> busRoutes = busRouteRepository.findAllByCityCodeAndNodeIdIsNullAndNodeOrdIsNull(31050);

        for (BusRoute busRoute : busRoutes) {

            StringBuilder builder = new StringBuilder("http://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteAcctoThrghSttnList"); /*URL*/
            builder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=snY%2FE%2Fh1clc%2FQvfB6XfZVOMyJfyzGVBzOy%2Bs4F0UCeVuXqvBB1zu8Spjz2%2FF%2F%2BBSa8oxXfpYQ%2BQYvyDX1jwZ0w%3D%3D"); /*Service Key*/
            builder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지번호*/
            builder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("10000", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            builder.append("&").append(URLEncoder.encode("_type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("xml", StandardCharsets.UTF_8)); /*데이터 타입(xml, json)*/
            builder.append("&").append(URLEncoder.encode("cityCode", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(String.valueOf(busRoute.getCityCode()), StandardCharsets.UTF_8)); /*도시코드 [상세기능4. 도시코드 목록 조회]에서 조회 가능*/
            builder.append("&").append(URLEncoder.encode("routeId", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(busRoute.getRouteId(), StandardCharsets.UTF_8)); /*노선ID [상세기능1. 노선번호목록 조회]에서 조회 가능*/

            InputStream xmlStream = getXMLStream(builder.toString());

            parseXMLBusRouteStop(xmlStream, busRoute);
        }

        busRouteRepository.deleteAll(busRoutes);
    }

    private void parseXMLBusRouteStop(InputStream xmlStream, BusRoute busRoute) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlStream);

        NodeList itemList = doc.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);

            if (item != null) {
                String nodeId = item.getElementsByTagName("nodeid").item(0).getTextContent();
                String nodeOrd = item.getElementsByTagName("nodeord").item(0).getTextContent();

                findOrCreateBusRoute(busRoute, nodeId, nodeOrd);
            }
        }
    }

    private void findOrCreateBusRoute(BusRoute busRoute, String nodeId, String nodeOrd) {

        if (!busRouteRepository.existsByRouteIdAndNodeIdAndCityCode(busRoute.getRouteId(), nodeId, busRoute.getCityCode())) {

            BusRoute route = BusRoute.builder()
                    .cityCode(busRoute.getCityCode())
                    .routeId(busRoute.getRouteId())
                    .nodeId(nodeId)
                    .nodeOrd(nodeOrd)
                    .build();

            busRouteRepository.save(route);
        }
    }
}
