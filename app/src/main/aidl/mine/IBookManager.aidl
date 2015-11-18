// IBookManager.aidl
package mine;

// Declare any non-default types here with import statements
import mine.modle.Book;

interface IBookManager {
  void addBook(in Book book);
  List<Book> getBooks();
}
