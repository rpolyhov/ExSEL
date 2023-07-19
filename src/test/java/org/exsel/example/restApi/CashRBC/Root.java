package org.exsel.example.restApi.CashRBC;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root{
    public ArrayList<Bank> banks;
    public ArrayList<City> cities;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bank{
        public Object reserve_url;
        public String name;
      public ArrayList<ArrayList<Object>> metro;
        public ArrayList<String> phones;
        public Object premium_type;
        public ArrayList<Double> coordinates;
        public String mark;
        public String dt_last_published;
        public Rate rate;
        public int id;

        public static int compareSell (Bank p1, Bank p2){
            if(p1.rate.sell > p2.rate.sell)
                return 1;
            return -1;
        }
        public static int compareBuy (Bank p1, Bank p2){
            if(p1.rate.buy > p2.rate.buy)
                return 1;
            return -1;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City{
        public int city_id;
        public String city_name;
        public boolean has_active_packages;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rate{
        public Double sell;
        public Double buy;
        public double summa;
        public int volume;
        public int currency;
        public int volume_id;
        public boolean commiss;
    }
}
