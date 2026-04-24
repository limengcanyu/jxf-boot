package org.akuma.aliyun;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
class AkumaAliyunApplicationTests {

    private String regionId = "cn-shenzhen";

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    void describeRegions() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, AliyunConst.accessKeyId, AliyunConst.accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        DescribeRegionsRequest request = new DescribeRegionsRequest();
        request.setSysRegionId(regionId);

        try {
            // 发起请求并处理应答或异常。
            DescribeRegionsResponse response = client.getAcsResponse(request);
            for (DescribeRegionsResponse.Region region : response.getRegions()) {
                System.out.println(region.getRegionId());
                System.out.println(region.getRegionEndpoint());
                System.out.println(region.getLocalName());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }

    @Test
    void describeInstances() {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, AliyunConst.accessKeyId, AliyunConst.accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setSysRegionId(regionId);
        JSONArray instanceIds = new JSONArray();
        instanceIds.add("i-wz9i9c3qiv1stshxx8bp");
        request.setInstanceIds(instanceIds.toJSONString());

        try {
            // 发起请求并处理应答或异常。
            DescribeInstancesResponse response = client.getAcsResponse(request);
            for (DescribeInstancesResponse.Instance instance : response.getInstances()) {
                System.out.println(instance.getImageId());
                System.out.println(instance.getInstanceId());
                System.out.println(instance.getPublicIpAddress());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }

    @Test
    void testDescribeRegions() {

        Map<String, String> parameters = new HashMap<>();
        // 输入请求参数
        parameters.put("Action", "DescribeRegions");
        parameters.put("Version", "2014-05-26");
        parameters.put("AccessKeyId", AliyunConst.accessKeyId);
        parameters.put("Timestamp", SignUtils.formatIso8601Date(new Date()));
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureNonce", UUID.randomUUID().toString());
        parameters.put("Format", "JSON");
        System.out.println("请求参数: " + parameters);

        String signature = SignUtils.sign(parameters);
        if (signature == null) {
            return;
        }
        parameters.put("Signature", signature);

        StringBuilder urlBuilder = new StringBuilder("http://ecs.aliyuncs.com/?");
        parameters.forEach((s, s2) -> urlBuilder.append(s).append("=").append(s2).append("&"));
        urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        System.out.println("请求URL: " + urlBuilder.toString());

        JSONObject result = restTemplate.getForObject(urlBuilder.toString(), JSONObject.class);
        System.out.println("result: " + result);
    }

    @Test
    void testDescribeInstances() {
        Map<String, String> parameters = new HashMap<>();
        // 公共请求参数
        parameters.put("Action", "DescribeInstances");
        parameters.put("Version", "2014-05-26");
        parameters.put("AccessKeyId", AliyunConst.accessKeyId);
        parameters.put("Timestamp", SignUtils.formatIso8601Date(new Date()));
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureNonce", UUID.randomUUID().toString());
        parameters.put("Format", "JSON");

        // 接口自定义参数
        parameters.put("RegionId", "cn-shenzhen");

        System.out.println("请求参数: " + parameters);

        String signature = SignUtils.sign(parameters);
        if (signature == null) {
            return;
        }
        parameters.put("Signature", signature);

        String baseUrl = "http://ecs.aliyuncs.com/?";
        String url = SignUtils.generateUrl(baseUrl, parameters);

        JSONObject result = restTemplate.getForObject(url, JSONObject.class);
        System.out.println("result: " + result);
    }
}
