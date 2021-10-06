package org.example.api.store;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Order;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class StoreApiTest {

    @BeforeClass
    public void prepare() throws IOException {

        // Читаем конфигурационный файл в System.properties -- простейшее HashMap хранилище
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));

        // Здесь мы задаём глобальные преднастройки для каждого запроса. Аналогично можно задавать их
        // перед каждым запросом отдельно, либо создать поле RequestSpecification и задавать весь пакет настроек
        // в конкретных запросах. Подробнее тут: https://habr.com/ru/post/421005/
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/") // задаём базовый адрес каждого ресурса
                .addHeader("api_key", System.getProperty("api.key")) // задаём заголовок с токеном для авторизации
                // обязательно учитывайте, что любые приватные данные необходимо хранить в отдельных файлах, которые НЕ публикуютя
                // в открытых репозиториях (в закрытых тоже лучше не публиковать)
                .setAccept(ContentType.JSON) // задаём заголовок accept
                .setContentType(ContentType.JSON) // задаём заголовок content-type
                .log(LogDetail.ALL) // дополнительная инструкция полного логгирования для RestAssured
                .build(); // после этой команды происходит формирование стандартной "шапки" запроса.
        // Подробнее о билдерах можно почитать https://refactoring.guru/ru/design-patterns/builder
        // но интереснее в книжке Effective Java

        //Здесь задаётся фильтр, позволяющий выводить содержание ответа,
        // также к нему можно задать условия в параметрах конструктора, которм должен удовлетворять ответ (например код ответа)
        RestAssured.filters(new ResponseLoggingFilter());

    }
    @Test
    public void placeOrderTest() {
        // todo: оформить заказ на питомца
        int id = new Random().nextInt(10);
        Order order = new Order(id,0,0,"2021-10-06T08:31:38.506Z","placed",true);
        given()
                .body(order)
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
        Order actual =
                given()
                        .pathParam("orderId",id)
                        .when()
                        .get("/store/order/{orderId}")
                        .then()
                        .statusCode(200)
                        .extract().body().as(Order.class);
        Assert.assertEquals(actual.getStatus(), order.getStatus());
    }

    @Test
    public void deleteOrderTest() throws IOException{
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        // todo: удалить заказ
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200);
        // todo: проверить удаление заказа
        given()
                .pathParam("orderId", System.getProperty("orderId"))
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(404);
    }
}
