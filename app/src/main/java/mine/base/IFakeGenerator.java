package mine.base;

import java.util.List;

/**
 * Created by jacky on 15/10/8.
 */
public interface IFakeGenerator <T>{

    public List<T> generatorList(int dataCount);
}
