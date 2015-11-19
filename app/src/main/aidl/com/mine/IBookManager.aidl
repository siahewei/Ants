// IBookManager.aidl
package com.mine;

// Declare any non-default types here with import statements
import com.mine.modle.Book;

interface IBookManager {
  void addBook(in Book book);
  List<Book> getBooks();
}
