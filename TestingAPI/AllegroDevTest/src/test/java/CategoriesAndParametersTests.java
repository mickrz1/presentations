import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

public class CategoriesAndParametersTests {

    public static String oauth_token;
    public static List<Long> categoriesIdList;
    public static final String CLIENT_ID = "REMOVED";
    public static final String CLIENT_SECRET = "REMOVED";
    public static final String DEFAULT_PARENT_ID = "954b95b6-43cf-4104-8354-dea4d9b10ddf";
    public static final String ERROR_STRING = "not3x1ST1N6";

    public String base64Input = CLIENT_ID+":"+CLIENT_SECRET;
    public String base64AuthHeader = Base64.getEncoder().encodeToString(base64Input.getBytes());

    @BeforeTest
    public void getToken() {
        Response response = RestAssured.given()
                .formParam("grant_type", "client_credentials")
                .header("Authorization", "Basic "+ base64AuthHeader)
                //"MmM1ODU2YWJhZDVmNGRhZmEwYmZjODIyZjQxZDVmOWI6UnJCejZXQXNvZDlNcE45MDJyTk16UE5YRlNlbnZMSTk0Tng5dHgxQVduaWdHVUw0M0RXcFZLRWZNeTNFbTZaNg=="
                .formParam("redirect_uri", "https://api.allegro.pl/sale/categories")
                .post("https://allegro.pl/auth/oauth/token");
                String token = response.body().path("access_token").toString();
                oauth_token = "Bearer "+token;
    }

    @Test
    public void GetCategories(){
        RestAssured.baseURI = "https://api.allegro.pl/sale";
        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                .headers(headerMap)
                .queryParam("parent.id", DEFAULT_PARENT_ID)
             .when()
                .get("/categories")
             .then()
                .assertThat().body(matchesJsonSchemaInClasspath("categories_schema.json"))
                .statusCode(200);
    }

    @Test
    public void GetCategoriesError(){
        RestAssured.baseURI = "https://api.allegro.pl/sale";
        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                    .headers(headerMap)
                    .queryParam("parent.id", ERROR_STRING)
                .when()
                    .get("/categories")
                .then()
                    .assertThat().body(matchesJsonSchemaInClasspath("error_schema.json"))
                    .statusCode(404);

    }

    @Test
    public void GetCategoryByErrorId(){
        RestAssured.baseURI = "https://api.allegro.pl/sale";

        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                    .headers(headerMap)
                    .queryParam("parent.id", DEFAULT_PARENT_ID)
                .when()
                    .get("/categories/"+ERROR_STRING)
                .then()
                    .assertThat().body(matchesJsonSchemaInClasspath("error_schema.json"))
                    .statusCode(404);
    }

    @Test(dataProvider = "data-provider", dataProviderClass = DataProviderClass.class)
    public void GetCategoryById(String Id){
        RestAssured.baseURI = "https://api.allegro.pl/sale";

        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                    .headers(headerMap)
                .when()
                    .get("/categories/"+Id)
                .then()
                    .assertThat().body(matchesJsonSchemaInClasspath("category_schema.json"))
                    .statusCode(200);
    }

    @Test
    public void GetCategoryParametersError (){
        RestAssured.baseURI = "https://api.allegro.pl/sale";

        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                    .headers(headerMap)
                .when()
                    .get("/categories/"+ERROR_STRING+"/parameters")
                .then()
                    .assertThat().body(matchesJsonSchemaInClasspath("error_schema.json"))
                    .statusCode(404);
     }

    @Test(dataProvider = "data-provider", dataProviderClass = DataProviderClass.class)
    public void GetCategoryParameters(String Id){
        RestAssured.baseURI = "https://api.allegro.pl/sale";

        Map<String,Object> headerMap = new HashMap<String,Object>();
        headerMap.put("Content-Type", "application/vnd.allegro.public.v1+json");
        headerMap.put("Accept", "application/vnd.allegro.public.v1+json");
        headerMap.put("Authorization", oauth_token);

        RestAssured.given()
                    .headers(headerMap)
                .when()
                    .get("/categories/"+Id+"/parameters")
                .then()
                    .assertThat().body(matchesJsonSchemaInClasspath("parameters_schema.json"))
                    .statusCode(200);
    }
}
