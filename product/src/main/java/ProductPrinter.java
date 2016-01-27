import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by dzmirtyviarzhbitski on 1/26/16.
 */
public class ProductPrinter {


    public static void main(String ...s){

        //1) get data
        Collection<ProductData> skus =  LoaderFactory.getProductLoader(Store.A).load(5l);
        //2)
        List<StoreSkus> data = new LinkedList<StoreSkus>();
        for (Store store : Store.values()){
            Collection<ProductData> products =  LoaderFactory.getProductLoader(Store.A).load(5l);
           if (products != null && !products.isEmpty()){
               data.add(new StoreSkus(store, skus));
           }
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<StoreSkus>>() {
        }.getType();

        System.out.println(gson.toJson(data, type));

    }

    public static class ProductData {

        private final Map<Long, List<AttributeValue>> skus = new HashMap<Long, List<AttributeValue>>();
        private final LinkedList<AttributeValue> productAttribures = new LinkedList<AttributeValue>();
        private final String title;
        private final Long productId;

        public ProductData(long productId, String title, LinkedList<AttributeValue> productAttributes, HashMap<Long, LinkedList<AttributeValue>> skus) {
            this.productId = productId;
            this.title = title;
            if (productAttributes!=null){
                productAttributes.addAll(productAttributes);
            }
            if (skus != null){
                this.skus.putAll(skus);
            }
        }

        public Map<Long, List<AttributeValue>> getSkus() {
            return skus;
        }

        public LinkedList<AttributeValue> getProductAttribures() {
            return productAttribures;
        }

        public String getTitle() {
            return title;
        }

        public Long getProductId() {
            return productId;
        }

//        //is available one of the sku
//        public boolean isAvailaible(){
//            return skus.values().stream().flatMap(v-> v.stream()).filter(attributeValue -> attributeValue.getAttributes() == Attributes.INSTOCK && attributeValue.getValue() != null && Boolean.valueOf(attributeValue.getValue()));
//        }

    };


    enum Store{
        A("http://macys.com"),
        B("http://amazon.com"),
        C("http://sears.com");

        private final String url;
        private String dipslayName;

        Store(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public String getDipslayName() {
            return dipslayName;
        }
    }

    static class StoreSkus {
        private final String homepage;
        private final String url;

        private final List<ProductData> skus = new LinkedList<>();

        StoreSkus(Store store, Collection<ProductData> skus) {
            this.homepage = store.getDipslayName();
            this.url = store.getUrl();
            this.skus.addAll(skus);
        }

        public String getHomepage() {
            return homepage;
        }

        public String getUrl() {
            return url;
        }


    }

    private static LinkedList<AttributeValue> newSkuPemutation(String color, String value) {
        LinkedList<AttributeValue> skuValues = new LinkedList<AttributeValue>();

        skuValues.add(new AttributeValue(Attributes.COLOR, color));
        skuValues.add(new AttributeValue(Attributes.SIZE, value));
        skuValues.add(new AttributeValue(Attributes.INSTOCK, "true"));
        skuValues.add(new AttributeValue(Attributes.SKU, "nice_shoe"));
        return skuValues;
    }

    enum Attributes {
        COLOR,
        SIZE,
        SKU,
        INSTOCK,
        BASE_PRICE,
        FINAL_PRICE,
        CURRENCY;
    }

    static class AttributeValue{
        private final Attributes attributes;
        private final String value;

        AttributeValue(Attributes attributes, String value) {
            this.attributes = attributes;
            this.value = value;
        }

        public Attributes getAttributes() {
            return attributes;
        }


        public String getValue() {
            return value;
        }
    }


    interface ProductLoader {
        Collection<ProductData> load(Long id);
    }

    public static class LoaderFactory {
        public static Map<Store, ProductLoader>  loaders = new HashMap<>();

        static {
            loaders.put(Store.A, new StoreALoader());
            loaders.put(Store.B, new StoreBLoader());
            loaders.put(Store.C, new StoreCLoader());
        }

        public static ProductLoader getProductLoader(Store store){
            return loaders.get(store);
        }
        static class StoreALoader implements ProductLoader {
            @Override
            public Collection<ProductData> load(Long id) {
                LinkedList<ProductData> products = new LinkedList<ProductData>();
                HashMap<Long, LinkedList<AttributeValue>> skus = new HashMap<Long, LinkedList<AttributeValue>>();
                LinkedList<AttributeValue> skuValues = new LinkedList<AttributeValue>();
                skuValues.add(new AttributeValue(Attributes.COLOR, "red"));
                skuValues.add(new AttributeValue(Attributes.SIZE, "bic"));
                skuValues.add(new AttributeValue(Attributes.INSTOCK, "true"));
                skuValues.add(new AttributeValue(Attributes.SKU, "flufy_shirt"));
                //TODO use internal id here, as store C doesn't jave sku
                skus.put(1L, skuValues);

                LinkedList<AttributeValue> productValues = new LinkedList<AttributeValue>();
                productValues.add(new AttributeValue(Attributes.BASE_PRICE, "Nice Shoes"));

                ProductData product = new ProductData(id, "title", productValues, skus);
                Collection<ProductData>list = new LinkedList<ProductData>();
                list.add(product);
                return list;
            }
        }

        private static class StoreBLoader implements ProductLoader {
            @Override
            public Collection<ProductData> load(Long id) {
                LinkedList<ProductData> products = new LinkedList<ProductData>();
                HashMap<Long, LinkedList<AttributeValue>> skus = new HashMap<Long, LinkedList<AttributeValue>>();
                LinkedList<AttributeValue> skuValues = new LinkedList<AttributeValue>();
                skuValues.add(new AttributeValue(Attributes.COLOR, "red"));
                skuValues.add(new AttributeValue(Attributes.SIZE, "bic"));
                skuValues.add(new AttributeValue(Attributes.INSTOCK, "true"));
                skuValues.add(new AttributeValue(Attributes.SKU, "flufy_shirt"));
                //TODO use internal id here, as store C doesn't jave sku
                skus.put(555L, skuValues);

                LinkedList<AttributeValue> productValues = new LinkedList<AttributeValue>();
                productValues.add(new AttributeValue(Attributes.BASE_PRICE, "12.0"));
                productValues.add(new AttributeValue(Attributes.FINAL_PRICE, "13.0"));
                productValues.add(new AttributeValue(Attributes.CURRENCY, "13.0"));

                ProductData product = new ProductData(id, "title", productValues, skus);
                Collection<ProductData>list = new LinkedList<ProductData>();
                list.add(product);
                return list;
            }
        }

        private static class StoreCLoader implements ProductLoader {
            @Override
            public Collection<ProductData> load(Long id) {
                //denormalize store C and store 3 skus;
                //store all permutations of sizes as sku entry
//              //"blue", "red";
                //6,7,8
                //will produce 6  records.
                //put to records f

                LinkedList<ProductData> products = new LinkedList<ProductData>();
                HashMap<Long, LinkedList<AttributeValue>> skus = new HashMap<Long, LinkedList<AttributeValue>>();

                String[] sizes = new String[]{"6", "7", "8"};
                String[] colors = new String[]{"red", "blue", "black"};

                for (int i=0; i< sizes.length; i++){
                    for (int j=0 ; j < colors.length ; j++){
                        LinkedList<AttributeValue> skuValues = newSkuPemutation(sizes[i], sizes[j]);
                        //TODO use internal id here, as store C doesn't jave sku
                        skus.put(new Random().nextLong(),  skuValues);
                    }
                }

                LinkedList<AttributeValue> productValues = new LinkedList<AttributeValue>();
                productValues.add(new AttributeValue(Attributes.BASE_PRICE, "12.0"));
                productValues.add(new AttributeValue(Attributes.FINAL_PRICE, "13.0"));
                productValues.add(new AttributeValue(Attributes.CURRENCY, "13.0"));


                ProductData product = new ProductData(id, "title", productValues, skus);
                Collection<ProductData>list = new LinkedList<ProductData>();
                list.add(product);
                return products;
            }
        }
    }

}
