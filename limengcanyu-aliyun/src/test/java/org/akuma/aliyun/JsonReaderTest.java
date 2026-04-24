package org.akuma.aliyun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsonReaderTest {
    @Test
    void read() throws ClientException {
        String result = "{\n" +
                "\t\"Instances\": {\n" +
                "\t\t\"Instance\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"ResourceGroupId\": \"rg-bp67acfmxazb4p****\",\n" +
                "\t\t\t\t\"Memory\": 16384,\n" +
                "\t\t\t\t\"InstanceChargeType\": \"PostPaid\",\n" +
                "\t\t\t\t\"Cpu\": 8,\n" +
                "\t\t\t\t\"OSName\": \"CentOS  7.4 64 位\",\n" +
                "\t\t\t\t\"InstanceNetworkType\": \"vpc\",\n" +
                "\t\t\t\t\"InnerIpAddress\": {\n" +
                "\t\t\t\t\t\"IpAddress\": []\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"ExpiredTime\": \"2017-12-10T04:04Z\",\n" +
                "\t\t\t\t\"ImageId\": \"m-bp67acfmxazb4p****\",\n" +
                "\t\t\t\t\"EipAddress\": {\n" +
                "\t\t\t\t\t\"AllocationId\": \"eip-2ze88m67qx5z****\",\n" +
                "\t\t\t\t\t\"IpAddress\": \"42.112.**.**\",\n" +
                "\t\t\t\t\t\"InternetChargeType\": \"PayByTraffic\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"HostName\": \"testHostName\",\n" +
                "\t\t\t\t\"Tags\": {\n" +
                "\t\t\t\t\t\"Tag\": [\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"TagKey\": \"TestKey\",\n" +
                "\t\t\t\t\t\t\t\"TagValue\": \"TestValue\"\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"VlanId\": \"\",\n" +
                "\t\t\t\t\"Status\": \"Running\",\n" +
                "\t\t\t\t\"MetadataOptions\": {\n" +
                "\t\t\t\t\t\"HttpTokens\": \"optional\",\n" +
                "\t\t\t\t\t\"HttpEndpoint\": \"enabled\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"InstanceId\": \"i-bp67acfmxazb4p****\",\n" +
                "\t\t\t\t\"StoppedMode\": \"KeepCharging\",\n" +
                "\t\t\t\t\"CpuOptions\": {\n" +
                "\t\t\t\t\t\"ThreadsPerCore\": 2,\n" +
                "\t\t\t\t\t\"Numa\": \"2\",\n" +
                "\t\t\t\t\t\"CoreCount\": 4\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"StartTime\": \"2017-12-10T04:04Z\",\n" +
                "\t\t\t\t\"DeletionProtection\": false,\n" +
                "\t\t\t\t\"SecurityGroupIds\": {\n" +
                "\t\t\t\t\t\"SecurityGroupId\": [\n" +
                "\t\t\t\t\t\t\"sg-bp67acfmxazb4p****\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"VpcAttributes\": {\n" +
                "\t\t\t\t\t\"PrivateIpAddress\": {\n" +
                "\t\t\t\t\t\t\"IpAddress\": [\n" +
                "\t\t\t\t\t\t\t\"172.17.**.**\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"VpcId\": \"vpc-2zeuphj08tt7q3brd****\",\n" +
                "\t\t\t\t\t\"VSwitchId\": \"vsw-2zeh0r1pabwtg6wcs****\",\n" +
                "\t\t\t\t\t\"NatIpAddress\": \"172.17.**.**\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"InternetChargeType\": \"PayByTraffic\",\n" +
                "\t\t\t\t\"InstanceName\": \"InstanceNameTest\",\n" +
                "\t\t\t\t\"DeploymentSetId\": \"\",\n" +
                "\t\t\t\t\"InternetMaxBandwidthOut\": 5,\n" +
                "\t\t\t\t\"SerialNumber\": \"51d1353b-22bf-4567-a176-8b3e12e4****\",\n" +
                "\t\t\t\t\"OSType\": \"linux\",\n" +
                "\t\t\t\t\"CreationTime\": \"2017-12-10T04:04Z\",\n" +
                "\t\t\t\t\"AutoReleaseTime\": \"2017-12-10T04:04Z\",\n" +
                "\t\t\t\t\"Description\": \"testDescription\",\n" +
                "\t\t\t\t\"InstanceTypeFamily\": \"ecs.g5\",\n" +
                "\t\t\t\t\"DedicatedInstanceAttribute\": {\n" +
                "\t\t\t\t\t\"Tenancy\": \"default\",\n" +
                "\t\t\t\t\t\"Affinity\": \"default\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"PublicIpAddress\": {\n" +
                "\t\t\t\t\t\"IpAddress\": [\n" +
                "\t\t\t\t\t\t\"121.40.**.**\"\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"GPUSpec\": \"\",\n" +
                "\t\t\t\t\"NetworkInterfaces\": {\n" +
                "\t\t\t\t\t\"NetworkInterface\": [\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"Type\": \"Primary\",\n" +
                "\t\t\t\t\t\t\t\"PrimaryIpAddress\": \"172.17.**.***\",\n" +
                "\t\t\t\t\t\t\t\"NetworkInterfaceId\": \"eni-2zeh9atclduxvf1z****\",\n" +
                "\t\t\t\t\t\t\t\"MacAddress\": \"00:16:3e:32:b4:**\",\n" +
                "\t\t\t\t\t\t\t\"PrivateIpSets\": {\n" +
                "\t\t\t\t\t\t\t\t\"PrivateIpSet\": [\n" +
                "\t\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\t\"PrivateIpAddress\": \"172.17.**.**\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"Primary\": true\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"SpotPriceLimit\": 0.98,\n" +
                "\t\t\t\t\"DeviceAvailable\": true,\n" +
                "\t\t\t\t\"SaleCycle\": \"month\",\n" +
                "\t\t\t\t\"InstanceType\": \"ecs.g5.large\",\n" +
                "\t\t\t\t\"OSNameEn\": \"CentOS  7.4 64 bit\",\n" +
                "\t\t\t\t\"SpotStrategy\": \"NoSpot\",\n" +
                "\t\t\t\t\"IoOptimized\": true,\n" +
                "\t\t\t\t\"ZoneId\": \"cn-hangzhou-g\",\n" +
                "\t\t\t\t\"ClusterId\": \"c-bp67acfmxazb4p****\",\n" +
                "\t\t\t\t\"EcsCapacityReservationAttr\": {\n" +
                "\t\t\t\t\t\"CapacityReservationPreference\": \"\",\n" +
                "\t\t\t\t\t\"CapacityReservationId\": \"\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"DedicatedHostAttribute\": {\n" +
                "\t\t\t\t\t\"DedicatedHostId\": \"dh-bp67acfmxazb4p****\",\n" +
                "\t\t\t\t\t\"DedicatedHostName\": \"testDedicatedHostName\",\n" +
                "\t\t\t\t\t\"DedicatedHostClusterId\": \"dc-bp67acfmxazb4h****\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"GPUAmount\": 4,\n" +
                "\t\t\t\t\"OperationLocks\": {\n" +
                "\t\t\t\t\t\"LockReason\": []\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"InternetMaxBandwidthIn\": 50,\n" +
                "\t\t\t\t\"Recyclable\": false,\n" +
                "\t\t\t\t\"RegionId\": \"cn-hangzhou\",\n" +
                "\t\t\t\t\"CreditSpecification\": \"\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"TotalCount\": 1,\n" +
                "\t\"RequestId\": \"473469C7-AA6F-4DC5-B3DB-A3DC0DE3C83E\",\n" +
                "\t\"PageSize\": 10,\n" +
                "\t\"PageNumber\": 1,\n" +
                "    \"NextToken\": \"caeba0bbb2be03f84eb48b699f0a4883\"\n" +
                "}";

//        Reader reader = new JsonReader();
//        Map<String, String> tag = reader.read(result, "Tag");
//        for (Map.Entry<String, String> stringStringEntry : tag.entrySet()) {
//            System.out.println(stringStringEntry.getKey() + ": " + stringStringEntry.getValue());
//        }
//
//        System.out.println("=========================================================");
//
//        Map<String, Object> parse = JsonUtils.parse(JSONObject.parseObject(result));
//        for (Map.Entry<String, Object> stringObjectEntry : parse.entrySet()) {
//            System.out.println(stringObjectEntry.getKey() + ": " + stringObjectEntry.getValue());
//        }

        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject retObject = parse(jsonObject);
        for (Map.Entry<String, Object> stringObjectEntry : retObject.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            System.out.println(key + ": " + value);
        }

    }

    private JSONObject parse(Object object) {
        JSONObject retObject = new JSONObject();

        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
                String key = stringObjectEntry.getKey();
                Object value = stringObjectEntry.getValue();

                if (value instanceof JSONObject) {
                    JSONObject jsonObject1 = (JSONObject) value;

                    JSONObject jsonObject2 = joinJSONObject(key, jsonObject1);
                    JSONObject parse = parse(jsonObject2);

                    retObject.putAll(parse);
                } else if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    if (jsonArray.toString().contains(":")) {

                        JSONArray jsonArray1 = joinJSONArray(key, jsonArray);
                        JSONObject parse = parse(jsonArray1);

                        retObject.putAll(parse);
                    } else {
                        String s = joinJSONArray(jsonArray);
                        retObject.put(key, s);
                    }
                } else {
                    retObject.put(key, value);
                }
            }
        } else if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;

            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    JSONObject parse = parse(o);
                    retObject.putAll(parse);
                }
            }
        } else {
            System.out.println("object is not JSONObject or JSONArray!");
        }

        return retObject;
    }

    private JSONObject joinJSONObject(String key, JSONObject paramObject) {
        JSONObject retObject = new JSONObject();

        for (Map.Entry<String, Object> stringObjectEntry : paramObject.entrySet()) {
            retObject.put(key + "." + stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        return retObject;
    }

    private JSONObject joinJSONObject(String key, int index, JSONObject paramObject) {
        JSONObject retObject = new JSONObject();

        for (Map.Entry<String, Object> stringObjectEntry : paramObject.entrySet()) {
            retObject.put(key + "[" + index + "]" + "." + stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        return retObject;
    }

    private JSONArray joinJSONArray(String key, JSONArray paramArray) {
        JSONArray retArray = new JSONArray();

        for (int i = 0; i < paramArray.size(); i++) {
            Object o = paramArray.get(i);

            if (o instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) o;
                JSONObject joinObject = joinJSONObject(key, i, jsonObject);
                retArray.add(joinObject);
            } else {
                System.out.println("paramArray is not JSONObject array!");
            }
        }

        return retArray;
    }

    private String joinJSONArray(JSONArray paramArray) {
        StringBuilder sb = new StringBuilder();

        for (Object o : paramArray) {
            sb.append(o.toString()).append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
