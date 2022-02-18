package viewer;

import java.util.ArrayList;
import java.util.List;

public class table {
    Object[] colnames;
    List<Object[]> data;

    table(int k) {
        colnames = new Object[k];
        data = new ArrayList<>();
    }
}
