package com.autumn.gateway;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.*;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2023-08-10  09:47
 **/
public class LargeJsonGenerator {

    private static final String filename = "/Users/qiushi/Downloads/启明工作相关/large_data.json";
    private static final String erpv4_filename = "/Users/qiushi/Downloads/启明工作相关/ERPV4.txt";


    public static void main(String[] args) {
        String str = readFileToString("/Users/qiushi/Downloads/启明工作相关/large_data2.json");

        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(str);
        String name = jsonObject.getJSONArray("paraValues").getString(0);

        System.out.println(name);


        Gson gson = new Gson();


        com.google.gson.JsonObject returnData = new com.google.gson.JsonParser().parse(str).getAsJsonObject();

        System.out.println(returnData.getAsJsonArray("paraValues").get(0).getAsString());
    }

    public static void main3(String[] args) {

        String root = "{\n" +
                "    \"subUrl\":\"/HQ/ErpV4/PPSService/APS_req_Plan\",\n" +
                "    \"methodName\":\"receiveOTDApsPlanSerivce\",\n" +
                "    \"iDMID\":\"u2020014669\",\n" +
                "    \"serviceName\":\"PPSService\",\n" +
                "    \"paraTypes\":[\n" +
                "        \"java.lang.String\"\n" +
                "    ],\n" +
                "    \"paraValues\":[\n" +
                "    ]\n" +
                "}";


        JsonObject rootObject = new JsonObject(root);


        JsonArray jsonArray = new JsonArray(readFileToString(filename));


        rootObject.getJsonArray("paraValues").add(jsonArray.toString());


        String str = rootObject.toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/qiushi/Downloads/启明工作相关/large_data2.json"))) {
            writer.write(str);
            System.out.println("字符串成功写入文件。");
        } catch (IOException e) {
            System.out.println("写入文件时出错：" + e.getMessage());
        }


        System.out.println(str.length());


        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(str);
        String name = jsonObject.get("paraValues").toString();


    }


    private static String readFileToString(String filename) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public static void largeJsonGenerator() {
        int numItems = 250000;

        try (FileWriter fileWriter = new FileWriter(filename);
             JsonWriter jsonWriter = new JsonWriter(fileWriter)) {

            // 创建一个 Gson 实例，用于将对象序列化为 JSON
            Gson gson = new GsonBuilder().create();

            // 开始写入 JSON 对象
            jsonWriter.beginObject();
            // 写入键值对
            jsonWriter.name("name").value("John Doe");
            jsonWriter.name("age").value(30);
            jsonWriter.name("city").value("New York");
            jsonWriter.name("value").value("[[\"2AAL001\",\"2023-08-14\",\"1\",\"HTPMCTCC2W2JFN0004\",\"MSHT23072700002272\",\"1\",\"M\",\"P2\",\"HQ5050\",\"202308\",\"1\",\"2\",\"HTPMCTCC\",\"1A4&9A1&5Z0&1R0&BC1&7N0&8P1&5J1&C50&9B0&YA0&BD1&LH1&9S0&0A0&9C0&1BX&8A6&8R1&2EK&6N2&5L2&3H1&49A&BV1&0B0&1D1&KX0&9D1&HR0&8B6&BF1&CH1&KH0&8S1&5M1&TJ1&1E0&BW1&C001P3&2G4&9E0&94B&DK1&7A9&HC2&5N1&3J0&GA2&6P5&KZ0&9F1&7B1&LL0&8U1&SJ0&0DM&7C1&96B&AG0&6R0&4N1&BZ1&1H1&AH0&8W1&2K3&NB1&7E0&CM1&HG0&8X1&NC0&HX0&8Y1&GF2&9K1&FU1&6E4&4A8&8Z1&NE0&1L9&7H1&41A&GH1&DB1&6G0&5E0&TB0&3A4&DC0&1N0&1MS&KA1&9N1&7J1&5F1&3B4&4U1&BQ0&20A&HM1&4V0&1P0&BA1&LE0&KC1&8N1&UG0&21A&4W1&NJ1&7M0&9Q4&2W2J&GNL34&C001P3&SZT\",\"10\",null],[\"2AAL001\",\"2023-08-14\",\"2\",\"HTPMBLCC1K1KFQ0002\",\"MSHT23072700001323\",\"1\",\"M\",\"P2\",\"HQ5050\",\"202308\",\"1\",\"2\",\"HTPMBLCC\",\"9A1&5Z0&1R0&1AA&BC1&7N0&8P1&5J1&C50&9B0&YA0&BD1&LH1&9S0&1BJ&0A0&9C0&8A6&8R1&6N2&5L2&3H1&49A&BV1&2EP&0B0&1D1&KX0&9D1&HR0&8B6&BF1&CH1&KH0&8S1&5M1&TJ0&1E0&BW1&C001P3&2G4&9E0&94B&DK1&7A9&HC2&5N1&3J0&GA2&6P4&KZ0&9F1&7B1&LL0&8U1&SJ0&0DM&7C1&96B&AG0&6R0&4N1&BZ1&1H1&AH0&8W1&2K3&NB1&7E0&CM1&HG0&8X1&NC0&HX0&8Y1&GF1&9K1&FU1&6E4&4A7&8Z1&NE0&1L8&7H1&41A&GH1&DB1&6G0&5E0&TB0&1MA&3A4&DC0&1N0&KA1&9N1&7J1&5F1&3B4&4U1&BQ0&20A&HM1&4V0&1P0&BA1&LE0&KC1&8N1&UG0&21A&4W1&NJ1&7M0&9Q4&BLL34&1K&C001P3&QZT\",\"00\",null],[\"2AAL001\",\"2023-08-14\",\"3\",\"HTPMBLCC1K1KFQ0002\",\"MSHT23072700000687\",\"1\",\"M\",\"P2\",\"HQ5050\",\"202308\",\"1\",\"2\",\"HTPMBLCC\",\"9A1&5Z0&1R0&1AA&BC1&7N0&8P1&5J1&C50&9B0&YA0&BD1&LH1&9S0&1BJ&0A0&9C0&8A6&8R1&6N2&5L2&3H1&49A&BV1&2EP&0B0&1D1&KX0&9D1&HR0&8B6&BF1&CH1&KH0&8S1&5M1&TJ0&1E0&BW1&C001P3&2G4&9E0&94B&DK1&7A9&HC2&5N1&3J0&GA2&6P4&KZ0&9F1&7B1&LL0&8U1&SJ0&0DM&7C1&96B&AG0&6R0&4N1&BZ1&1H1&AH0&8W1&2K3&NB1&7E0&CM1&HG0&8X1&NC0&HX0&8Y1&GF1&9K1&FU1&6E4&4A7&8Z1&NE0&1L8&7H1&41A&GH1&DB1&6G0&5E0&TB0&1MA&3A4&DC0&1N0&KA1&9N1&7J1&5F1&3B4&4U1&BQ0&20A&HM1&4V0&1P0&BA1&LE0&KC1&8N1&UG0&21A&4W1&NJ1&7M0&9Q4&BLL34&1K&C001P3&QZT\",\"00\",null],[\"2AAL001\",\"2023-08-14\",\"4\",\"HTPMBLCC1K1KFQ0002\",\"MSHT23072700002084\",\"1\",\"M\",\"P2\",\"HQ5050\",\"202308\",\"1\",\"2\",\"HTPMBLCC\",\"9A1&5Z0&1R0&1AA&BC1&7N0&8P1&5J1&C50&9B0&YA0&BD1&LH1&9S0&1BJ&0A0&9C0&8A6&8R1&6N2&5L2&3H1&49A&BV1&2EP&0B0&1D1&KX0&9D1&HR0&8B6&BF1&CH1&KH0&8S1&5M1&TJ0&1E0&BW1&C001P3&2G4&9E0&94B&DK1&7A9&HC2&5N1&3J0&GA2&6P4&KZ0&9F1&7B1&LL0&8U1&SJ0&0DM&7C1&96B&AG0&6R0&4N1&BZ1&1H1&AH0&8W1&2K3&NB1&7E0&CM1&HG0&8X1&NC0&HX0&8Y1&GF1&9K1&FU1&6E4&4A7&8Z1&NE0&1L8&7H1&41A&GH1&DB1&6G0&5E0&TB0&1MA&3A4&DC0&1N0&KA1&9N1&7J1&5F1&3B4&4U1&BQ0&20A&HM1&4V0&1P0&BA1&LE0&KC1&8N1&UG0&21A&4W1&NJ1&7M0&9Q4&BLL34&1K&C001P3&QZT\",\"00\",null]");

            for (int i = 0; i < 2500000; i++) {
                jsonWriter.name("i" + i).value(i);
            }


            // 结束 JSON 对象
            jsonWriter.endObject();

            // 刷新并关闭 JsonWriter
            jsonWriter.flush();
            jsonWriter.close();

            System.out.println("JSON 写入完成。");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("生成的 JSON 文件 " + filename + " 已保存");
    }
}