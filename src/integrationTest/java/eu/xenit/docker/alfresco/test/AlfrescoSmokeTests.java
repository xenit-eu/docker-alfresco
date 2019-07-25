package eu.xenit.docker.alfresco.test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.parsing.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlfrescoSmokeTests {

    @BeforeClass
    public static void setup() {
        String basePath = "/alfresco/s/";
        String alfrescoPortInternal = System.getProperty("alfresco.port.internal");
        String alfrescoHost = System.getProperty("alfresco.host");
        String alfrescoPort = System.getProperty("alfresco.tcp." + alfrescoPortInternal);

        int port = 8080;
        try {
            port = Integer.parseInt(alfrescoPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestAssured.baseURI = "http://" + alfrescoHost;
        RestAssured.port = port;
        RestAssured.basePath = basePath;

        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("admin");
        RestAssured.authentication = authScheme;
        RestAssured.defaultParser = Parser.JSON;
    }


    @Test
    public void testUp(){
        String versionRegex = "^" + System.getProperty("version") + ".+$";
        String flavor = System.getProperty("flavor");
        String version = given()
                .when()
                .get("api/server")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body("data.version", RegexMatcher.matchesRegex(versionRegex))
                .extract().path("data.version");
        String edition = given()
                .when()
                .get("api/server")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract().path("data.edition");
        assertThat(edition.trim(), equalToIgnoringCase(flavor));
    }

    @Test
    // Note: due to eventual consistency, we can't test the expected number of results
    // This test only works if alfresco-share-services amp is installed
    public void testSearch(){
        given()
                .when()
                .get("slingshot/search?term=pdf*")
                .then()
                .statusCode(200);
    }
}
