package org.exsel.example.table.FINAM;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RootFinam {

        public Data data;
        public Error error;
        public boolean status;


    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Analytics{
        public int growthOrFall;
        public int potentialPercent;
        public double priceTarget;
        public double price;
        public int priceChangeSign;
        public int id;
        public int quoteId;
        public String quoteTitle;
        public String quoteUrl;
        public String prognosisUrl;
        public String isin;
        public String currency;
        public String prognosisStatusTitle;
        public int prognosisStatusId;
        public String publishDate;
        public Date prognosisDate;
        public boolean favoriteStatus;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ask{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bid{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body{
        public Instrument instrument;
        public String last;
        public PriceChange priceChange;
        public String min;
        public String bid;
        public String ask;
        public String max;
        @JsonProperty("open")
        public String myopen;
        public String close;
        public String volume;
        public VolumeChange volumeChange;
        public int favoriteStatus;
        public String refreshTime;
        public String market;
        public int exchange;
        public String mic;
        public int decp;
        public boolean topfavorite;
        public Analytics analytics;
        public ArrayList<Path> path;
        public String measureUnit;
        public String measureSymbol;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChangeVolume{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Checkbox{
        public String id;
        public Object text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Close{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{
        public ArrayList<Body> body;
        @JsonProperty("NeedCfdNote")
        public boolean needCfdNote;
        @JsonProperty("NeedMoexNote")
        public boolean needMoexNote;
        @JsonProperty("NeedTradegateNote")
        public boolean needTradegateNote;
        @JsonProperty("NeedBatsNote")
        public boolean needBatsNote;
        @JsonProperty("NeedNasdaqNote")
        public boolean needNasdaqNote;
        @JsonProperty("NeedOtcNote")
        public boolean needOtcNote;
        public Data data;
        public String type;
        public Head head;
        public ArrayList<String> disclamers;
        public Paging paging;
        public String title;
        public String seoTitle;
        public Seo seo;
        public ArrayList<Object> path;
        public String dataApiUrl;
        public String favApiUrl;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error{
        public String msg;
        public String code;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Favorite{
        public String id;
        public Object text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Head{
        public Favorite favorite;
        public Checkbox checkbox;
        public Title title;
        public Last last;
        public Pchange pchange;
        public Bid bid;
        public Ask ask;
        @JsonProperty("open")
        public Open myopen;
        public Max max;
        public Min min;
        public Close close;
        public Volume volume;
        public ChangeVolume changeVolume;
        public RefreshTime refreshTime;
        public MeasureUnit measureUnit;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Instrument{
        public int id;
        public String name;
        public String url;
        public String quoteUrl;
        @JsonProperty("CanBuy")
        public int canBuy;
        @JsonProperty("PurchaseCartQuoteID")
        public int purchaseCartQuoteID;
        public Purchasecart purchasecart;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Last{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Max{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MeasureUnit{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Min{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Open{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Paging{
        public int currentPage;
        public int totalRecords;
        public int pageSize;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Path{
        public String title;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pchange{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceChange{
        public String value;
        public int direction;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Purchasecart{
        public int type;
        public boolean enabled;
        public int id;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefreshTime{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Seo{
        public Object title;
        public Object description;
        public Object keywords;
        public Object canonicalUrl;
        public Object seoText1;
        public Object seoText2;
        public boolean noindexing;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Volume{
        public String id;
        public String text;
        public boolean sortable;
        public boolean sorted;
        public Object sortedBy;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeChange{
        public String value;
        public int direction;
    }


}
