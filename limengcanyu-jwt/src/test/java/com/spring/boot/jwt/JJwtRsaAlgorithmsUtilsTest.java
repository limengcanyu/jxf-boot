package com.spring.boot.jwt;

import com.spring.boot.jwt.utils.JJwtRsaUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

public class JJwtRsaAlgorithmsUtilsTest {

    @Test
    public void creatJWS() {
        Claims claims = Jwts.claims();
        claims.put("hello", "world");

        String jwsString = JJwtRsaUtils.creatJWS(claims);
        System.out.println("jwsString: " + jwsString);
    }

    @Test
    public void readJWS() {
        String jwsString = "eyJhbGciOiJSUzUxMiJ9.eyJoZWxsbyI6IndvcmxkIn0.MaRlzCbzhVNWQhpZjxK3-C6lvyaA70bwZpz587HbJP8UN0-9ulPWT4RCqPgkQmxMEP39W56gSjMAr6GZtslXVATwAEGA-rfqycj_M-3gEg04nUuPITAQKq9vq77qhlN5q6OOt7Fzwff_pipwNjQKNbvXHL5JitqcsgZlkCcGcoA4I-vQkbkCVw97ZeMNeUidPVZCITIwgGQhCVWxDNlw8bGew6yMB2xbRNm1dkxmN7XSEHZE79O_KTpW4IK73v-efXGvZU5VAtjlK6YtT9_Zf-GWsmntQoaAXd1jZvEloCsJ6MxyOcTe2cK1STz0-pu9ZQ_tXF7Zt-BCIm95NQTbkWXsSorBmbC3zHhpEU2OKx3KWVB_xY-rz0gsOcaXVJeTTSrTrcANuU2wpGfTXgklUaS73jYQRaXu_tbqVLzoUobHGIywMh6Dczwzw_i4UzC1DBvFtOhAu4QsKcCX1rTbvdFo97jOmRHauexJgyOgSZKuX186_cPUejYwJu3g-vsbcnkEl_Yb4nrst_opzX35hx9L6LckiVdLxrj3bpiEBV_DuhsEclQINtybHqhpISzHM-qgmt5W6G62aqJXO6k3N71_DKoS4mtRsewBxgaV-BcLG8pNbjfCK7fED_yXVmRFv8YObFpFbNAFgIipH-AOtJ11N_7qxtPQA8xV85Ae-rQ";
        Claims claims = JJwtRsaUtils.readJWS(jwsString);
        System.out.println("claims: " + claims);
    }
}
