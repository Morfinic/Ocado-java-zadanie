import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Main {
    public static class Store {
        public List<String> pickers = new ArrayList<String>();
        public String pickingStartTime;
        public String pickingEndTime;

        public Store(String dir) throws IOException, ParseException {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader("./self-test-data/" + dir + "/store.json"));
            JSONObject jsonObj = (JSONObject) obj;

            this.pickingStartTime = (String) jsonObj.get("pickingStartTime");
            this.pickingEndTime = (String) jsonObj.get("pickingEndTime");
            JSONArray arr = (JSONArray) jsonObj.get("pickers");
            for(Object tmp: arr.toArray()){
                this.pickers.add((String) tmp);
            }
        }
    }

    public static class Order{
        public String orderId;
        public String pickingTime;
        public String orderValue;
        public String completeBy;

        public Order(JSONObject obj) {
            this.orderId = (String) obj.get("orderId");
            this.pickingTime = (String) obj.get("pickingTime");
            this.orderValue = (String) obj.get("orderValue");
            this.completeBy = (String) obj.get("completeBy");
        }
    }

    public static class Orders {
        List<Order> orderList = new ArrayList<>();

        public Orders(String dir) throws IOException, ParseException {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader("./self-test-data/" + dir + "/orders.json"));
            JSONArray jsonArr = (JSONArray) obj;

            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject tmp = (JSONObject) jsonArr.get(i);
                this.orderList.add(new Order(tmp));
            }
        }
    }

    public static JSONObject readJSON(String folder) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(new FileReader("./self-test-data/" + folder + "/store.json"));
//        Object objOrders = jsonParser.parse(new FileReader("./self-test-data/" + folder + "/orders.json"));
//        JSONObject jsonObj = (JSONObject) objStore;
//        JSONArray jsonArr = (JSONArray) objOrders;

//        Store store = new Store(jsonObj, jsonArr);

        return (JSONObject) obj;
    }

    public static void main(String[] args) throws IOException, ParseException {
        String workingDir = "complete-by";
        Store store = new Store(workingDir);
        Orders orders = new Orders(workingDir);

//        Store store = readJSON("advanced-allocation");
//        System.out.println(store.orders.get(0).orderId);
//        System.out.println(store.pickers);
//        System.out.println(store.pickingStartTime);
//        System.out.println(store.pickingEndTime);
    }
}