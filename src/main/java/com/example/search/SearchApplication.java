package com.example.search;

import com.example.search.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(SearchApplication.class, args);
//        StoreBus storeBus = context.getBean(StoreBus.class);
//        storeBus.storeData();
//        System.out.println("Store Bus Data complete!");
//        SearchBus searchBus = context.getBean(SearchBus.class);
//        searchBus.search();
//        System.out.println("Search Bus complete!");
//        StoreCar storeCar = (StoreCar) context.getBean("storeCar");
//        storeCar.storeData();
//        System.out.println("Store Car Data complete!");
//        System.exit(0);
//        SearchCar searchCar = context.getBean(SearchCar.class);
//        searchCar.search();
//        System.out.println("Search Car complete!");
//        StoreSubway storeSubway = context.getBean(StoreSubway.class);
//        storeSubway.storeData();
//        System.out.println("Store Subway Data complete!");
//        SearchSubway searchSubway = context.getBean(SearchSubway.class);
//        searchSubway.search();
//        System.out.println("Search Subway complete!");
    }
}
