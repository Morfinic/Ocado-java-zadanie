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
        public List<String> pickers = new ArrayList<>();
        public List<Integer> pickingTime = new ArrayList<>();
//        public Integer pickingStartTime;
//        public Integer pickingEndTime;

        public Store(String dir) throws IOException, ParseException {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader("./self-test-data/" + dir + "/store.json"));
            JSONObject jsonObj = (JSONObject) obj;

            int pickingStartTime = convertToInt((String) jsonObj.get("pickingStartTime"));
            int pickingEndTime = convertToInt((String) jsonObj.get("pickingEndTime"));
//            this.pickingTime = pickingEndTime - pickingStartTime;
            JSONArray arr = (JSONArray) jsonObj.get("pickers");
            for(Object tmp: arr.toArray()){
                this.pickers.add((String) tmp);
                this.pickingTime.add(pickingEndTime - pickingStartTime);
            }
        }
    }

    public static class Order{
        public String orderId;
        public Integer pickingTime;
        public Double orderValue;
        public Integer completeBy;

        public Order(JSONObject obj) {
            this.orderId = (String) obj.get("orderId");
            this.pickingTime = Integer.parseInt(((String) obj.get("pickingTime")).replaceAll("[a-zA-Z]", ""));
            this.orderValue = Double.parseDouble((String) obj.get("orderValue"));
            this.completeBy = convertToInt((String) obj.get("completeBy"));
        }
    }

    public static class Orders {
        List<Order> orderList = new ArrayList<>();

        public Orders(String dir) throws IOException, ParseException {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(new FileReader("./self-test-data/" + dir + "/orders.json"));
            JSONArray jsonArr = (JSONArray) obj;

            for (Object o : jsonArr) {
                JSONObject tmp = (JSONObject) o;
                this.orderList.add(new Order(tmp));
            }

            for(int i = 0; i < orderList.size() - 1; i++)
                for(int j = 0; j < orderList.size() - i - 1; j++)
                    if(orderList.get(j).completeBy > orderList.get(j + 1).completeBy){
                        Order tmp = orderList.get(j);
                        orderList.set(j, orderList.get(j + 1));
                        orderList.set(j + 1, tmp);
                    }
        }

    }

    public static Integer convertToInt(String time){
        return Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3, 5));
    }

    public static void main(String[] args) throws IOException, ParseException {
        String workingDir = "advanced-allocation";
        Store store = new Store(workingDir);
        Orders orders = new Orders(workingDir);

//        System.out.println("Store:");
//        System.out.println("\t" + store.pickers + "\n\t" + store.pickingTime);
//        System.out.println("Orders:");
//        for (Order o: orders.orderList) {
//            System.out.println("\t" + o.orderId);
//            System.out.println("\t" + o.completeBy);
//            System.out.println("\t" + o.orderValue);
//            System.out.println("\t" + o.pickingTime + "\n");

        int orderIndex = 0;
        while(!store.pickers.isEmpty() && orderIndex < orders.orderList.size()){
            for(int i = 0; i < store.pickers.size(); i++){
                int pic = store.pickingTime.get(i);
                if(pic > orders.orderList.get(orderIndex).pickingTime){
                    System.out.println(store.pickers.get(i) + " " + orders.orderList.get(orderIndex).orderId + " " + orders.orderList.get(orderIndex).pickingTime);
                    orderIndex++;
                    store.pickingTime.set(i, store.pickingTime.get(i) - orders.orderList.get(orderIndex).pickingTime);
                } else{
                    store.pickers.remove(i);
                    store.pickingTime.remove(i);
                }
            }
        }

    }
}
