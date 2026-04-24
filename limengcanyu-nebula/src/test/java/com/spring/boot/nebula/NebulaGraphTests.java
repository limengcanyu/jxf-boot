package com.spring.boot.nebula;

import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import org.junit.jupiter.api.Test;
import sun.security.krb5.internal.KrbApErrException;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * @date 2021/4/27 14:53
 */
public class NebulaGraphTests {

    @Test
    void test() {
        try {
            NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
            nebulaPoolConfig.setMaxConnSize(10);
            List<HostAddress> addresses = Arrays.asList(new HostAddress("192.168.203.132", 9669));
            NebulaPool pool = new NebulaPool();
            pool.init(addresses, nebulaPoolConfig);
            Session session = pool.getSession("root", "nebula", false);
            session.execute("SHOW HOSTS;");
            session.release();
            pool.close();
        } catch (UnknownHostException | NotValidConnectionException | IOErrorException | AuthFailedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
