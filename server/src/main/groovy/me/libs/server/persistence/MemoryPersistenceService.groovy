package me.libs.server.persistence

import me.libs.server.domain.Book

/**
 * @author Noam Y. Tenne
 */
class MemoryPersistenceService implements PersistenceService {

    private Map<String, String> subjects = [:]
    private Map<String, String> apiKeys = [:]
    private Map<String, Book> books = [:]

    @Override
    String sayHello() {
        'Hello Libs :)'
    }

    @Override
    boolean usernameAvailable(String username) {
        subjects.keySet().any { it == username }
    }

    @Override
    void signUp(String username, String email, String hashedPassword) {
        subjects.put(username, hashedPassword)
    }

    @Override
    String getApiKey(String username) {
        apiKeys.get(username)
    }

    @Override
    void setApiKey(String username, String apiKey) {
        apiKeys.put(username, apiKey)
    }

    @Override
    boolean loginApiKey(String username, String apiKey) {
        apiKeys.get(username) == apiKey
    }

    @Override
    boolean login(String username, String hashedPassword) {
        subjects.get(username) == hashedPassword
    }

    @Override
    Book getBook(String id) {
        books.get(id)
    }

    @Override
    Book createBook(Book book) {
        books.put(book.id, book)
    }

    @Override
    void deleteBook(String id) {
        books.remove(id)
    }

    @Override
    void updateBook(Book book) {
        books.put(book.id, book)
    }
}
