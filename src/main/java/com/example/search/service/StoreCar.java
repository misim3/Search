package com.example.search.service;

import com.example.search.entity.Car;
import com.example.search.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreCar {

    private final CarRepository carRepository;

    public void storeData() throws Exception {

        //storeCarAPI();
        storeCarFILE();
        System.out.println("Done");
    }

    private void storeCarAPI() throws Exception {
        String urlBuilder = "https://openapi.its.go.kr:9443/trafficInfo?apiKey=dbe5ff4cb3ba4847aa120e9d12cd7dcb&type=all&drcType=all&minX=120&maxX=140&minY=30&maxY=40&getType=xml";

        InputStream xmlStream = getXMLStream(urlBuilder);

        parseXMLCar(xmlStream);
    }

    private void storeCarFILE() throws Exception {
        try (FileInputStream fis = new FileInputStream("C:/Users/sim00/MOCT_NODE.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            // 91484으로 시작하다가 2~3시간 실행 후 종료
            // 130000으로 시작했는 데, update가 된 걸 보니 나중에 91484에서 130000사이를 한번 확인해주자.
            // 140084부터 시작해서 끝까지 실행한 뒤에, 갯수 비교해서 필요하면, 위에서 언급한 사이 실행하자.
            for (int rowIndex = 130000; rowIndex < rows; rowIndex++) { // Assuming first row is the header
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    String id = getCellValueAsString(row.getCell(0));
                    String longitude = getCellValueAsString(row.getCell(8));
                    String latitude = getCellValueAsString(row.getCell(9));

                    if (carRepository.existsByStartNodeId(id)) {
                        List<Car> cars = carRepository.findAllByStartNodeId(id);

                        cars.forEach(car -> car.setLongitude(Double.valueOf(longitude)));
                        cars.forEach(car -> car.setLatitude(Double.valueOf(latitude)));

                        carRepository.saveAll(cars);
                        System.out.println(rowIndex);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
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
