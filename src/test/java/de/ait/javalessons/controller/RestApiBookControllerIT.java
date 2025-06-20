package de.ait.javalessons.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.javalessons.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(RestApiBookController.class)
class RestApiBookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooksShouldReturnListOfBooks() throws Exception {
        //Отправляем HTTP запрос на /books
        var result = mockMvc.perform(get("/books"))
                .andReturn();

        //Проверяем что статус 200 OK
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        List<Book> books = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                }
        );
        assertThat(books).hasSize(7);
        assertThat(books.getFirst().getTitle()).isEqualTo("Clean Code");
        assertThat(books.getLast().getTitle()).isEqualTo("The Pragmatic Programmer");

    }

    @Test
    void getBookByIdShouldReturnCurrentBook() throws Exception {
        //Отправляем HTTP запрос на /books/1
        var result = mockMvc.perform(get("/books/1"))
                .andReturn();

        //Проверяем что статус 200 OK
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        //Читаем JSON и превращаем в обьект класса Book
        Book book = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);

        //Проверяем поля полученной книги
        assertThat(book.getId()).isEqualTo("1");
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getPublishYear()).isEqualTo(2008);
    }

    @Test
    void getBookByIdShouldReturnEmpty() throws Exception {

        //Запрашиваем книгу с несуществующим Id
        var result = mockMvc.perform(get(("/books/9999")))
                .andReturn();

        //Проверяем что статус 200 OK
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

        //Тело ответа пустое
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void postBookShouldAddNewBook() throws Exception {
        //Создаем новую книгу
        Book book = new Book("20", "Test Book", "Test Author", 2025);

        //Post запрос на /books c книгой в теле метода
        var result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)//Тип содержимого
                        .content(objectMapper.writeValueAsString(book))) //Преобразуем Java обьект в JSON
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());

        Book bookFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);

        assertThat(bookFromResponse.getId()).isEqualTo(book.getId());
        assertThat(bookFromResponse.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookFromResponse.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookFromResponse.getPublishYear()).isEqualTo(book.getPublishYear());
    }

    @Test
    void deleteBookShouldRemoveBook() throws Exception {
        //удаляем существующую книгу
        mockMvc.perform(delete("/books/2"))
                .andReturn();

        //Проверяем удалилась ли книга
        var result = mockMvc.perform(get("/books/2"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void postBookShouldReturnBadRequestForInvalidInput() throws Exception {

        // Создаем json с некорректными данными (например, пустой заголовок

        String wrongJson = "{ \"id\": \"21\", \"title\": ";

        var result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongJson))
                .andReturn();

        // Проверяем что статус 400
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void postBookShouldReturnBadRequestForNegativeJear() throws Exception {

        // Создаем книгу с некорректными данными значение year = -1
        String wrongJson = "{ \"id\": \"21\"," +
                " \"title\": \"Test title" +
                " \"year\": \"-2020" +
                "\"author\" : \"Test author";

        var result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongJson))
                .andReturn();

        // Проверяем что статус 400
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    void putBookShouldUpdateExistingBook() throws Exception {
        Book updatedBook = new Book("1", "Updated Title", "Updated Author", 2000);

        var result = mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        Book bookFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
        assertThat(bookFromResponse).isNotNull();
        assertThat(bookFromResponse.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(bookFromResponse.getAuthor()).isEqualTo(updatedBook.getAuthor());
    }

    @Test
    void putBookShouldCreateNewBookIfNotExists() throws Exception {
        Book newBook = new Book("999", "New Book", "New Author", 2025);

        var result = mockMvc.perform(put("/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var resultGetBook = mockMvc.perform(get("/books/999"))
                .andReturn();

        //Проверяем что статус 200 OK
        assertThat(resultGetBook.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        //Читаем JSON и превращаем в обьект класса Book
        Book book = objectMapper.readValue(resultGetBook.getResponse().getContentAsString(), Book.class);

        assertThat(book.getId()).isEqualTo(newBook.getId());
        assertThat(book.getTitle()).isEqualTo(newBook.getTitle());
    }

    @ParameterizedTest
    @ValueSource(strings = {"4", "5", "6", "1", "2", "3"})
    void deleteExitingBookShouldSucceed(String id) throws Exception{
        var deleteResult = mockMvc.perform(delete(("/books/"+id))).andReturn();
        assertThat(deleteResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        var getBook = mockMvc.perform(get("/books/"+id)).andReturn();
        assertThat(getBook.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @ParameterizedTest(name = "GET /books/{0} -> should find title: {1}")
    @CsvSource({
            "1, Clean Code",
            "2, 1984",
            "3, Effective Java",
            "4, The Great Gatsby",
            "5, Refactoring"
    })
    void getBookByIdShouldReturnCorrectBook(String id, String expectedTitle) throws Exception{
        var result = mockMvc.perform(get("/books/" + id)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        var book = objectMapper.readValue(result.getResponse().getContentAsString(), Book.class);
        assertThat(book.getTitle()).isEqualTo(expectedTitle);
    }



}
